package app.web.pavelk.mouth1.dto;

import app.web.pavelk.mouth1.domain.Views;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
@JsonView(Views.Id.class) // serializes
public class WsEventDto {
    private ObjectType objectType;
    private EventType eventType;
    @JsonRawValue // add ... destruct
    private String body;


    public WsEventDto(ObjectType objectType, EventType eventType, String body) {
        this.objectType = objectType;
        this.eventType = eventType;
        this.body = body;
    }
}
