package app.web.pavelk.mouth1.service;


import app.web.pavelk.mouth1.domain.Comment;
import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.repo.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private CommentRepo commentRepo;

    @Autowired
    public void setCommentRepo(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public Comment create(Comment comment, User user) { // создает комент
        comment.setAuthor(user);
        commentRepo.save(comment);

        return comment;
    }
}
