package br.edu.ifrs.riogrande.tads.ppa.listener;

import br.edu.ifrs.riogrande.tads.ppa.config.RabbitMQConfig;
import br.edu.ifrs.riogrande.tads.ppa.dto.CreateTicketRequest;
import br.edu.ifrs.riogrande.tads.ppa.model.NewUserEvent;
import br.edu.ifrs.riogrande.tads.ppa.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NewUserEventListener {

    private static final Logger logger = LoggerFactory.getLogger(NewUserEventListener.class);
    private final TicketService ticketService;

    public NewUserEventListener(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void onNewUser(NewUserEvent event) {
        logger.info("Novo usuário recebido via RabbitMQ: {}", event.handle());

        String details = String.format(
            "Criar email %s@tads.rg.ifrs.edu.br para o novo usuário %s.",
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
            logger.info("O Chamado para criar e-mail para o usuário {} foi aberto com sucesso. ", event.handle());
        } catch (Exception e) {
            logger.error("Não foi possível abrir o Chamado automaticamente " + event.handle(), e);
        }
    }
}