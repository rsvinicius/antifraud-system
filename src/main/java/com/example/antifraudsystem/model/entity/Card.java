package com.example.antifraudsystem.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Card extends IdentityId {
    public Card(String number) {
        this.number = number;
    }

    private String number;
}
