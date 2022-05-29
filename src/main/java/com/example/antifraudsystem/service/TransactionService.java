package com.example.antifraudsystem.service;

import com.example.antifraudsystem.model.entity.Transaction;
import com.example.antifraudsystem.model.response.TransactionResponse;
import com.example.antifraudsystem.persistence.CardRepository;
import com.example.antifraudsystem.persistence.IpAddressRepository;
import com.example.antifraudsystem.persistence.TransactionRepository;
import com.example.antifraudsystem.util.enums.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
    private final IpAddressRepository ipAddressRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(IpAddressRepository ipAddressRepository, CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.ipAddressRepository = ipAddressRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse transaction(Transaction transaction) {
        TreeSet<String> infoSet = new TreeSet<>();
        TransactionResponse transactionResponse = new TransactionResponse();

        transactionRepository.save(transaction);

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

        return transactionResponse;
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
        return Arrays.stream(TransactionStatus.values())
                .filter(val -> Math.max(val.getMin(), amount) == Math.min(amount, val.getMax()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
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
}

