package com.example.antifraudsystem.service;

import com.example.antifraudsystem.model.entity.Transaction;
import com.example.antifraudsystem.model.request.TransactionFeedbackRequest;
import com.example.antifraudsystem.model.response.TransactionResponse;
import com.example.antifraudsystem.repository.CardRepository;
import com.example.antifraudsystem.repository.IpAddressRepository;
import com.example.antifraudsystem.repository.TransactionRepository;
import com.example.antifraudsystem.repository.TransactionStatusRepository;
import com.example.antifraudsystem.util.enums.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static com.example.antifraudsystem.util.constants.Numbers.ONE;
import static com.example.antifraudsystem.util.constants.Numbers.TWO;
import static com.example.antifraudsystem.util.constants.Numbers.ZERO;


@Service
public class TransactionService {
    public static final String ALLOWED_TRANSACTION = "none";
    public static final String INCORRECT_AMOUNT = "amount";
    public static final String BLOCKED_IP = "ip";
    public static final String BLOCKED_CARD_NUMBER = "card-number";
    public static final String REGION_CORRELATION = "region-correlation";
    public static final String IP_CORRELATION = "ip-correlation";
    public static final String ALLOWED = TransactionStatus.ALLOWED.toString();
    public static final String MANUAL_PROCESSING = TransactionStatus.MANUAL_PROCESSING.toString();
    public static final String PROHIBITED = TransactionStatus.PROHIBITED.toString();
    private final IpAddressRepository ipAddressRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionStatusRepository transactionStatusRepository;

    @Autowired
    public TransactionService(IpAddressRepository ipAddressRepository, CardRepository cardRepository, TransactionRepository transactionRepository, TransactionStatusRepository transactionStatusRepository) {
        this.ipAddressRepository = ipAddressRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.transactionStatusRepository = transactionStatusRepository;
    }

    public TransactionResponse transaction(Transaction transaction) {
        TreeSet<String> infoSet = new TreeSet<>();
        TransactionResponse transactionResponse = new TransactionResponse();

        TransactionStatus transactionStatus = getTransactionStatus(transaction.getAmount());

        LocalDateTime currentTransactionDateTime = transaction.getDate();

        List<Transaction> lastHourTransactions = transactionRepository.findAllByNumberAndDateBetween(
                transaction.getNumber(),
                currentTransactionDateTime.minusHours(ONE),
                currentTransactionDateTime);

        long uniqueRegionCount = countDistinctRegion(lastHourTransactions, transaction.getRegion());
        long uniqueIpAddressCount = countDistinctIpAddress(lastHourTransactions, transaction.getIp());

        boolean isIpAddressOrCardNumberBlocked = isIpAddressOrCardNumberBlocked(transaction, infoSet);

        if (!transactionStatus.equals(TransactionStatus.ALLOWED)) {
            infoSet.add(INCORRECT_AMOUNT);
        }

        if (infoSet.contains(INCORRECT_AMOUNT) && isIpAddressOrCardNumberBlocked && transactionStatus == TransactionStatus.MANUAL_PROCESSING) {
            infoSet.remove(INCORRECT_AMOUNT);
        }

        if (isIpAddressOrCardNumberBlocked) transactionStatus = TransactionStatus.PROHIBITED;

        if (uniqueRegionCount >= TWO) {
            infoSet.add(REGION_CORRELATION);

            transactionStatus = getTransactionStatusByRegionOrIpAddressCorrelation(transactionStatus, uniqueRegionCount);
        }

        if (uniqueIpAddressCount >= TWO) {
            infoSet.add(IP_CORRELATION);

            transactionStatus = getTransactionStatusByRegionOrIpAddressCorrelation(transactionStatus, uniqueIpAddressCount);
        }

        if (infoSet.size() == ZERO && transactionStatus == TransactionStatus.ALLOWED) infoSet.add(ALLOWED_TRANSACTION);

        transactionResponse.setResult(transactionStatus);
        transactionResponse.setInfo(String.join(", ", infoSet));

        transaction.setResult(transactionStatus.toString());
        transactionRepository.save(transaction);

        return transactionResponse;
    }

    public Transaction feedback(TransactionFeedbackRequest transactionFeedbackRequest) {
        Transaction transaction = findTransaction(transactionFeedbackRequest.getTransactionId());

        boolean isTransactionResultEqualsRequestFeedback = transaction.getResult()
                .equals(transactionFeedbackRequest.getFeedback());

        boolean isFeedbackAlreadyInDatabase = !"".equals(transaction.getFeedback());

        if (isFeedbackAlreadyInDatabase) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (isTransactionResultEqualsRequestFeedback) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        transaction.setFeedback(transactionFeedbackRequest.getFeedback());

        transactionStatusLimitChange(transaction);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> findAllTransactionsByCardNumber(String number) {
        if (transactionRepository.countAllByNumber(number) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return transactionRepository.findAllByNumber(number);
    }

    private TransactionStatus getTransactionStatusByRegionOrIpAddressCorrelation(
            TransactionStatus transactionStatus, long uniqueElementCount) {

        if (!transactionStatus.equals(TransactionStatus.PROHIBITED)) {
            transactionStatus = (uniqueElementCount > TWO)
                    ? TransactionStatus.PROHIBITED
                    : TransactionStatus.MANUAL_PROCESSING;
        }
        return transactionStatus;
    }

    private long countDistinctRegion(List<Transaction> lastHourTransactions, String currentTransactionRegion) {
        return lastHourTransactions.stream()
                .map(Transaction::getRegion)
                .filter(transactionRegion -> !transactionRegion.equals(currentTransactionRegion))
                .distinct()
                .count();
    }

    private long countDistinctIpAddress(List<Transaction> lastHourTransactions, String currentTransactionIpAddress) {
        return lastHourTransactions.stream()
                .map(Transaction::getIp)
                .filter(transactionIpAddress -> !transactionIpAddress.equals(currentTransactionIpAddress))
                .distinct()
                .count();
    }

    private TransactionStatus getTransactionStatus(long amount) {
        if (amount <= Transaction.getAllowedLimit()) {
            return TransactionStatus.ALLOWED;
        } else if (amount <= Transaction.getManualLimit()) {
            return TransactionStatus.MANUAL_PROCESSING;
        } else {
            return TransactionStatus.PROHIBITED;
        }
    }

    private Boolean isIpAddressOrCardNumberBlocked(
            Transaction transaction,
            TreeSet<String> infoSet) {

        String ip = transaction.getIp();
        String number = transaction.getNumber();
        Boolean isIpAddressBlocked = isIpAddressBlocked(ip, infoSet);
        Boolean isCardNumberBlocked = isCardNumberBlocked(number, infoSet);
        return isIpAddressBlocked || isCardNumberBlocked;
    }

    private Boolean isIpAddressBlocked(String ipAddress, TreeSet<String> infoSet) {
        Boolean isIpAddressBlocked = ipAddressRepository.existsByIp(ipAddress);
        if (isIpAddressBlocked) infoSet.add(BLOCKED_IP);
        return isIpAddressBlocked;
    }

    private Boolean isCardNumberBlocked(String cardNumber, TreeSet<String> infoSet) {
        Boolean isCardNumberBlocked = cardRepository.existsByNumber(cardNumber);
        if (isCardNumberBlocked) infoSet.add(BLOCKED_CARD_NUMBER);
        return isCardNumberBlocked;
    }

    private Transaction findTransaction(long id) {
        return Optional
                .ofNullable(transactionRepository.findByTransactionId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void transactionStatusLimitChange(Transaction transaction) {
        String result = transaction.getResult();
        String feedback = transaction.getFeedback();

        if (feedback.equals(ALLOWED)) {

            if (result.equals(MANUAL_PROCESSING)) {
                changeLimit(transaction, ALLOWED, true);
            }

            if (result.equals(PROHIBITED)) {
                changeLimit(transaction, ALLOWED, true);
                changeLimit(transaction, MANUAL_PROCESSING, true);
            }
        }

        if (feedback.equals(MANUAL_PROCESSING)) {

            if (result.equals(ALLOWED)) {
                changeLimit(transaction, ALLOWED, false);
            }

            if (result.equals(PROHIBITED)) {
                changeLimit(transaction, MANUAL_PROCESSING, true);
            }
        }

        if (feedback.equals(PROHIBITED)) {

            if (result.equals(ALLOWED)) {
                changeLimit(transaction, ALLOWED, false);
                changeLimit(transaction, MANUAL_PROCESSING, false);
            }

            if (result.equals(MANUAL_PROCESSING)) {
                changeLimit(transaction, MANUAL_PROCESSING, false);
            }
        }

        Transaction.setAllowedLimit(
                transactionStatusRepository
                        .findByName(ALLOWED).getMax());

        Transaction.setManualLimit(
                transactionStatusRepository
                        .findByName(MANUAL_PROCESSING).getMax());
    }

    private void changeLimit(Transaction transaction, String name, Boolean isIncreasing) {
        com.example.antifraudsystem.model.entity.TransactionStatus transactionStatus = transactionStatusRepository.findByName(name);

        double operationType = (isIncreasing) ? 1 : -1;

        double firstHalf = 0.8 * transactionStatus.getMax();

        double secondHalf = operationType * 0.2 * transaction.getAmount();

        long newLimit = (long) Math.ceil(firstHalf + secondHalf);

        transactionStatus.setMax(newLimit);

        transactionStatusRepository.save(transactionStatus);
    }
}

