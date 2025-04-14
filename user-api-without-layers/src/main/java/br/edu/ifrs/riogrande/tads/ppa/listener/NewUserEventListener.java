package br.edu.ifrs.riogrande.tads.ppa.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.edu.ifrs.riogrande.tads.ppa.config.RabbitMQConfig;
import br.edu.ifrs.riogrande.tads.ppa.model.NewUserEvent;

@Component
public class NewUserEventListener {

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void newUser(NewUserEvent event) {
        
    }
}