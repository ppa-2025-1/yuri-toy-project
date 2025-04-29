package br.edu.ifrs.riogrande.tads.ppa.model;

import java.util.List;

public record NewUserEvent(
        String name,
        String handle,
        String email,
        String password,
        String company,
        List<String> roles
)  {

}
