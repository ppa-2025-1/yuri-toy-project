package br.edu.ifrs.riogrande.tads.ppa.model;

import java.util.List;

public record NewUserEvent(
    Integer userId,
    String name,
    String handle,
    String email,
    String company,
    List<String> roles
) {}