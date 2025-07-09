package br.edu.ifrs.riogrande.tads.ppa.dto;

import br.edu.ifrs.riogrande.tads.ppa.model.Ticket;
import java.time.Instant;

public record TicketResponse(
    Long id,
    String action,
    String item,
    String details,
    Ticket.Status status,
    Integer userId,
    String userName,
    Instant createdAt
) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
            ticket.getId(),
            ticket.getAction(),
            ticket.getItem(),
            ticket.getDetails(),
            ticket.getStatus(),
            ticket.getUser().getId(),
            ticket.getUser().getProfile().getName(),
            ticket.getCreatedAt()
        );
    }
}