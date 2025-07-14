package br\edu\ifrs\riogrande\tads\ppa\listener;

import br.edu.ifrs.riogrande.tads.ppa.ms_ticket.config.RabbitMQConfig;
import br.edu.ifrs.riogrande.tads.ppa.ms_ticket.dto.CreateTicketRequest;
import br.edu.ifrs.riogrande.tads.ppa.ms_ticket.model.NewUserEvent;
import br.edu.ifrs.riogrande.tads.ppa.ms_ticket.model.User;
import br.edu.ifrs.riogrande.tads.ppa.ms_ticket.repository.UserRepository;
import br.edu.ifrs.riogrande.tads.ppa.ms_ticket.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NewUserEventListener {

    private static final Logger logger = LoggerFactory.getLogger(NewUserEventListener.class);
    private final TicketService ticketService;
    private final UserRepository userRepository;

    public NewUserEventListener(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    @Transactional
    public void onNewUser(NewUserEvent event) {
        logger.info("Novo usuário recebido via RabbitMQ: {}", event.handle());

        // 1. Salva uma cópia local do usuário para manter a integridade referencial.
        User localUser = new User();
        localUser.setId(event.userId());
        localUser.setHandle(event.handle());
        localUser.setName(event.name());
        userRepository.save(localUser);
        logger.info("Usuário local salvo no ms-ticket: {}", localUser.getHandle());

        // 2. Abre o ticket, como antes.
        String details = String.format(
            "Create email %s@tads.rg.ifrs.edu.br for new user %s.",
            event.handle(),
            event.name()
        );

        CreateTicketRequest request = new CreateTicketRequest(
            "CREATE",
            "EMAIL",
            details,
            event.userId()
        );

        try {
            ticketService.create(request);
            logger.info("O Chamado para criar e-mail para o usuário {} foi aberto com sucesso.", event.handle());
        } catch (Exception e) {
            logger.error("Não foi possível abrir o Chamado automaticamente " + event.handle(), e);
        }
    }
}