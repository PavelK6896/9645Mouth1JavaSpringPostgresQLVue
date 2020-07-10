package app.web.pavelk.mouth1.service;


import app.web.pavelk.mouth1.domain.Comment;
import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.Views;
import app.web.pavelk.mouth1.dto.EventType;
import app.web.pavelk.mouth1.dto.ObjectType;
import app.web.pavelk.mouth1.repo.CommentRepo;
import app.web.pavelk.mouth1.util.WsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
public class CommentService {
    private CommentRepo commentRepo;
    private BiConsumer<EventType, Comment> wsSender;

    @Autowired
    public void setWsSender(WsSender wsSender) {
        this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
    }

    @Autowired
    public void setCommentRepo(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public Comment create(Comment comment, User user) { // создает комент
        comment.setAuthor(user);

        Comment comment2 = commentRepo.save(comment);

        wsSender.accept(EventType.CREATE, comment2);

        return comment2;
    }
}
