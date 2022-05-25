package com.example.antifraudsystem.service;

import com.example.antifraudsystem.model.request.TransactionRequest;
import com.example.antifraudsystem.model.response.TransactionResponse;
import com.example.antifraudsystem.persistence.CardRepository;
import com.example.antifraudsystem.persistence.IpAddressRepository;
import com.example.antifraudsystem.util.enums.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.TreeSet;

@Service
public class TransactionService {
    public static final String ALLOWED_TRANSACTION = "none";
    public static final String INCORRECT_AMOUNT = "amount";
    public static final String BLOCKED_IP = "ip";
    public static final String BLOCKED_CARD_NUMBER = "card-number";
    private final IpAddressRepository ipAddressRepository;
    private final CardRepository cardRepository;

    @Autowired
    public TransactionService(IpAddressRepository ipAddressRepository, CardRepository cardRepository) {
        this.ipAddressRepository = ipAddressRepository;
        this.cardRepository = cardRepository;
    }

    public TransactionResponse transaction(TransactionRequest transactionRequest) {
        TransactionStatus transactionStatus = getTransactionStatus(transactionRequest.getAmount());
        TreeSet<String> infoSet = new TreeSet<>();
        TransactionResponse transactionResponse = new TransactionResponse();

        if (transactionStatus != TransactionStatus.ALLOWED) {
            infoSet.add(INCORRECT_AMOUNT);
        }

        boolean isIpAddressOrCardNumberBlocked = isIpAddressOrCardNumberBlocked(transactionRequest, infoSet);

        if (infoSet.contains(INCORRECT_AMOUNT) && isIpAddressOrCardNumberBlocked && transactionStatus == TransactionStatus.MANUAL_PROCESSING) {
            infoSet.remove(INCORRECT_AMOUNT);
        }

        if (isIpAddressOrCardNumberBlocked) transactionStatus = TransactionStatus.PROHIBITED;

        if (infoSet.size() == 0 && transactionStatus == TransactionStatus.ALLOWED) infoSet.add(ALLOWED_TRANSACTION);

        transactionResponse.setResult(transactionStatus);
        transactionResponse.setInfo(String.join(", ", infoSet));

        return transactionResponse;
    }

    private TransactionStatus getTransactionStatus(long amount) {
        return Arrays.stream(TransactionStatus.values())
                .filter(val -> Math.max(val.getMin(), amount) == Math.min(amount, val.getMax()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    private Boolean isIpAddressOrCardNumberBlocked(
            TransactionRequest transactionRequest,
            TreeSet<String> infoSet) {

        String ip = transactionRequest.getIp();
        String number = transactionRequest.getNumber();
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

