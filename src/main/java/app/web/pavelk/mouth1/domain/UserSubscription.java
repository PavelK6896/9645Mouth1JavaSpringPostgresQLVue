package app.web.pavelk.mouth1.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@NoArgsConstructor
public class UserSubscription implements Serializable {
    @EmbeddedId // сложно составной ключь для сущьности
    @JsonIgnore
    @JsonView(Views.Id.class)
    private UserSubscriptionId id;

    @MapsId("channelId")
    @ManyToOne
    @JsonView(Views.IdName.class)
    // в поле Set<User> subscriptions - будут только идентефикаторы
    @JsonIdentityReference
    // используется для настройки ссылок на объекты, которые будут сериализованы как идентификаторы объектов вместо полных объектов POJO
    @JsonIdentityInfo( // позволяет сериализовать POJO по идентификатору, когда он встречается во второй раз во время сериализации.
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private User channel;

    @MapsId("subscriberId")
    @ManyToOne
    @JsonView(Views.IdName.class)
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private User subscriber;

    @JsonView(Views.IdName.class)
    private boolean active; // связь состояния

    public UserSubscription(User channel, User subscriber) {
        this.channel = channel;
        this.subscriber = subscriber;
        this.id = new UserSubscriptionId(channel.getId(), subscriber.getId()); // заполняем сложно составной ключь
    }
}
