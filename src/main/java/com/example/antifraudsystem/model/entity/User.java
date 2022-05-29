package com.example.antifraudsystem.model.entity;

import com.example.antifraudsystem.model.View;
import com.example.antifraudsystem.util.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User extends IdentityId {
    @NotEmpty
    @JsonView(View.UserView.class)
    private String name;

    @NotEmpty
    @Column(unique = true)
    @JsonView(View.UserView.class)
    private String username;

    @NotEmpty
    private String password;

    @JsonView(View.UserView.class)
    private UserRole role = UserRole.MERCHANT;

    private Boolean isAccountLocked = true;
}
