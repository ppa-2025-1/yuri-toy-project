package br.edu.ifrs.riogrande.tads.ppa.service;

import br.edu.ifrs.riogrande.tads.ppa.dto.CreateTicketRequest;
import br.edu.ifrs.riogrande.tads.ppa.dto.UpdateStatusRequest;
import br.edu.ifrs.riogrande.tads.ppa.model.Ticket;
import br.edu.ifrs.riogrande.tads.ppa.model.User;
import br.edu.ifrs.riogrande.tads.ppa.repository.TicketRepository;
import br.edu.ifrs.riogrande.tads.ppa.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    private static final Map<Ticket.Status, Set<Ticket.Status>> VALID_STATUS_TRANSITIONS = Map.of(
        Ticket.Status.NEW, Set.of(Ticket.Status.IN_PROGRESS, Ticket.Status.CANCELED),
        Ticket.Status.IN_PROGRESS, Set.of(Ticket.Status.RESOLVED, Ticket.Status.CANCELED)
    );

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Ticket create(CreateTicketRequest request) {
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

        Ticket ticket = new Ticket();
        ticket.setAction(request.action());
        ticket.setItem(request.item());
        ticket.setDetails(request.details());
        ticket.setUser(user);
        ticket.setStatus(Ticket.Status.NEW);

        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public Ticket findById(Long id) {
        return ticketRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado!"));
    }

    @Transactional(readOnly = true)
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Transactional
    public Ticket updateStatus(Long id, UpdateStatusRequest request) {
        Ticket ticket = findById(id);
        Ticket.Status currentStatus = ticket.getStatus();
        Ticket.Status newStatus = request.newStatus();

        if (!VALID_STATUS_TRANSITIONS.getOrDefault(currentStatus, Set.of()).contains(newStatus)) {
            throw new IllegalStateException(
                "Não é possível alterar do status do chamado de " + currentStatus + " para " + newStatus);
        }

        ticket.setStatus(newStatus);
        return ticketRepository.save(ticket);
    }
}