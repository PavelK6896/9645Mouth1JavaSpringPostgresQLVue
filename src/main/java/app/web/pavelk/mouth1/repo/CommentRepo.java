package app.web.pavelk.mouth1.repo;


import app.web.pavelk.mouth1.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {
}
