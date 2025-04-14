package br.edu.ifrs.riogrande.tads.ppa.model;

import java.util.List;

import br.edu.ifrs.riogrande.tads.ppa.model.Profile.AccountType;

public record NewUser(
    String name,
    String handle,
    String email,
    String password,
    String company,
    AccountType type,
    List<String> roles
) {}