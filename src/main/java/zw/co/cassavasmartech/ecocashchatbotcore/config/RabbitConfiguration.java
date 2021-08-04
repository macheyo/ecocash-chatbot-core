package zw.co.cassavasmartech.ecocashchatbotcore.config;


import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.SmsProperties;

@Configuration
@RequiredArgsConstructor
public class RabbitConfiguration {

    private final ConnectionFactory connectionFactory;
    private final SmsProperties smsProperties;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        final RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setChannelTransacted(true);
        return template;
    }

    @Bean
    public Queue smsQueue() {
        return new Queue(smsProperties.getSmsQueueName(), true);
    }

}
