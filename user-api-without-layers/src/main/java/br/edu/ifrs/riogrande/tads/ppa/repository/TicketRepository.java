package br.edu.ifrs.riogrande.tads.ppa.repository;

import org.springframework.data.repository.ListCrudRepository;
import br.edu.ifrs.riogrande.tads.ppa.model.Ticket;

public interface TicketRepository extends ListCrudRepository<Ticket, Long> {

}