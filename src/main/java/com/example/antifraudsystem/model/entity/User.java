package com.example.antifraudsystem.model.entity;

import com.example.antifraudsystem.model.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(View.UserView.class)
    private long id;

    @NotEmpty
    @JsonView(View.UserView.class)
    private String name;

    @NotEmpty
    @Column(unique = true)
    @JsonView(View.UserView.class)
    private String username;

    @NotEmpty
    private String password;
}

