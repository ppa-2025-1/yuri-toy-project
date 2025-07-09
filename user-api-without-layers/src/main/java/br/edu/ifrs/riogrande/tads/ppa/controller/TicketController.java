package br.edu.ifrs.riogrande.tads.ppa.controller;

import br.edu.ifrs.riogrande.tads.ppa.dto.CreateTicketRequest;
import br.edu.ifrs.riogrande.tads.ppa.dto.TicketResponse;
import br.edu.ifrs.riogrande.tads.ppa.dto.UpdateStatusRequest;
import br.edu.ifrs.riogrande.tads.ppa.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody @Valid CreateTicketRequest request, UriComponentsBuilder uriBuilder) {
        var ticket = ticketService.create(request);
        var uri = uriBuilder.path("/api/v1/tickets/{id}").buildAndExpand(ticket.getId()).toUri();
        return ResponseEntity.created(uri).body(TicketResponse.from(ticket));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> listTickets() {
        var tickets = ticketService.findAll()
            .stream()
            .map(TicketResponse::from)
            .toList();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id) {
        var ticket = ticketService.findById(id);
        return ResponseEntity.ok(TicketResponse.from(ticket));
    }

    @PatchMapping("/{id}/status") // Path alterado para /status
    public ResponseEntity<TicketResponse> updateTicketStatus(@PathVariable Long id, @RequestBody @Valid UpdateStatusRequest request) {
        var ticket = ticketService.updateStatus(id, request);
        return ResponseEntity.ok(TicketResponse.from(ticket));
    }
}