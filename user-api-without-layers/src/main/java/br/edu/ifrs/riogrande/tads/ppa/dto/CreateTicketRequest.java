package br.edu.ifrs.riogrande.tads.ppa.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketRequest(
    @NotBlank String action,
    @NotBlank String item,
    String details,
    @NotNull Integer userId
) {}