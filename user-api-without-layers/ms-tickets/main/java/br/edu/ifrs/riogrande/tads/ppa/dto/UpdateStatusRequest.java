package br.edu.ifrs.riogrande.tads.ppa.dto;

import br.edu.ifrs.riogrande.tads.ppa.model.Ticket.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
    @NotNull Status newStatus
) {}