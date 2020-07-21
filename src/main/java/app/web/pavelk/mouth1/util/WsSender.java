package app.web.pavelk.mouth1.util;

import app.web.pavelk.mouth1.dto.EventType;
import app.web.pavelk.mouth1.dto.ObjectType;
import app.web.pavelk.mouth1.dto.WsEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class WsSender {
    private final SimpMessagingTemplate template; // очередь сообщений
    private final ObjectMapper mapper; // сериализация


    public WsSender(SimpMessagingTemplate template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    // Consumer // ананимная функция // замыкание //cycle

    public <T> BiConsumer<EventType, T> getSender(ObjectType objectType, Class view) {
        ObjectWriter writer = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(view);

        return (EventType eventType, T payload) -> {
            String value = null;

            try {
                value = writer.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            template.convertAndSend(
                    "/topic/activity",
                    new WsEventDto(objectType, eventType, value)
            );

            //  System.out.println("WsSender = " + value);
        };
    }


}
