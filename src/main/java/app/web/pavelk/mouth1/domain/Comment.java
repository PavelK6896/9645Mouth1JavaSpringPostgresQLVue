package app.web.pavelk.mouth1.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })// хаш код сщитаеться от поля ид
public class Comment {
    @Id
    @GeneratedValue
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String text;

    @ManyToOne
    @JoinColumn(name = "message_id") // название к кому относиться
    private Message message;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false) // неможет быть пустым
    @JsonView(Views.FullMessage.class)
    private User author;

}
