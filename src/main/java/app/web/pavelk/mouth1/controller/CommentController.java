package app.web.pavelk.mouth1.controller;

import app.web.pavelk.mouth1.domain.Comment;
import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.Views;
import app.web.pavelk.mouth1.service.CommentService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // возвращает тело тоесть сиреализованый объект
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @JsonView(Views.FullMessage.class) // только те что помечены
    public Comment create(
            @RequestBody Comment comment,
            @AuthenticationPrincipal User user
    ) {
        return commentService.create(comment, user);
    }
}
