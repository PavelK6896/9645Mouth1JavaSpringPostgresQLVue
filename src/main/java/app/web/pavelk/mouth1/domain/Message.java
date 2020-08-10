package app.web.pavelk.mouth1.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table// исткать в таблице
@ToString(of = {"id", "text"})// ломбок то стр
@EqualsAndHashCode(of = {"id"})
@Data // актевировать
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//генерировать ид автоматически  хиб выберет стартегию сам
    @JsonView(Views.Id.class) // для фильтра
    private Long id;

    @JsonView(Views.IdName.class)
    private String text;

    @Column(updatable = false) // необновляемое
    //сериализация поумолчанею джексон меняем формат по патерну
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.FullMessage.class)
    private LocalDateTime creationDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonView(Views.FullMessage.class) // только для полного запроса
    private User author;

    @OneToMany(mappedBy = "message", orphanRemoval = true)
    // пернадлежит месажу orphanRemoval - если месадж удалить коменты тоже удаляться
    @JsonView(Views.FullMessage.class)
    private List<Comment> comments;


    @JsonView(Views.FullMessage.class)
    private String link;
    @JsonView(Views.FullMessage.class)
    private String linkTitle;
    @JsonView(Views.FullMessage.class)
    private String linkDescription;
    @JsonView(Views.FullMessage.class)
    private String linkCover;

}
