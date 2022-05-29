package com.example.antifraudsystem.model.entity;

import com.example.antifraudsystem.model.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class IdentityId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.UserView.class)
    private long id;
}
