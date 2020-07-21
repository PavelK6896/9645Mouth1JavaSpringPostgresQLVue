package app.web.pavelk.mouth1.controller;

import app.web.pavelk.mouth1.domain.Comment;
import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.Views;
import app.web.pavelk.mouth1.service.CommentService;
import app.web.pavelk.mouth1.service.ProfileService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController // возвращает тело тоесть сиреализованый объект
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;
    private final ProfileService profileService;

    @Autowired
    public CommentController(CommentService commentService, ProfileService profileService) {
        this.commentService = commentService;
        this.profileService = profileService;

    }

    @PostMapping
    @JsonView(Views.FullComment.class) // только те что помечены
    public Comment create(
            @RequestBody Comment comment,
            Principal principal
    ) {
        User userFromDb = profileService.getUserU();
        if (principal.getName().equals(userFromDb.getId())) {
            System.out.println("UserU oK!");
        } else System.out.println("Error UserU!");


        return commentService.create(comment, userFromDb);
    }
}
