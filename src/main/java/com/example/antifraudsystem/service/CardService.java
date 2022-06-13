package com.example.antifraudsystem.service;


import com.example.antifraudsystem.model.entity.Card;
import com.example.antifraudsystem.model.request.CardNumberRequest;
import com.example.antifraudsystem.model.response.DeleteCardResponse;
import com.example.antifraudsystem.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card saveStolenCard(CardNumberRequest cardNumberRequest) {
        if (cardRepository.existsByNumber(cardNumberRequest.getNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return cardRepository.save(new Card(cardNumberRequest.getNumber()));
    }

    public DeleteCardResponse deleteCard(String cardNumber) {
        Card card = findCard(cardNumber);

        cardRepository.delete(card);

        return new DeleteCardResponse(cardNumber);
    }

    public List<Card> listCards() {
        return cardRepository.findAll();
    }

    private Card findCard(String cardNumber) {
        return Optional
                .ofNullable(cardRepository.findByNumber(cardNumber))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
