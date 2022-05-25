package com.example.antifraudsystem.controller;

import com.example.antifraudsystem.model.entity.Card;
import com.example.antifraudsystem.model.request.CardNumberRequest;
import com.example.antifraudsystem.model.response.DeleteCardResponse;
import com.example.antifraudsystem.service.CardService;
import com.example.antifraudsystem.util.validators.CardNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/antifraud/stolencard")
@Validated
public class CardController {
    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public Card saveStolenCard(@Valid @RequestBody CardNumberRequest cardNumberRequest) {
        return cardService.saveStolenCard(cardNumberRequest);
    }

    @DeleteMapping("/{number}")
    public DeleteCardResponse deleteCard(@CardNumber @PathVariable String number) {
        return cardService.deleteCard(number);
    }

    @GetMapping
    public List<Card> listCards() {
        return cardService.listCards();
    }
}