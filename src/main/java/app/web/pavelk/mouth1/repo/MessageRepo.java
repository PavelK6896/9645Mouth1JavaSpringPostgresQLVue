package app.web.pavelk.mouth1.repo;


import app.web.pavelk.mouth1.domain.Message;
import app.web.pavelk.mouth1.domain.User;
import org.springframework.boot.convert.Delimiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.EntityListeners;
import javax.persistence.EntityResult;
import javax.persistence.QueryHint;
import java.util.List;


public interface MessageRepo extends JpaRepository<Message, Long> { // наследуем методы работы с базой

    @EntityGraph(attributePaths = {"comments"})
    Page<Message> findByAuthorIn(List<User> users, Pageable pageable); // только те из переданного списка


//    setMaxResults(5)
//.getResultList();
//
//    assertEquals(5, posts.size());
//
//    assertArrayEquals(
//            LongStream.rangeClosed(1, 5).toArray(),
//    posts.stream().mapToLong(Post::getId).toArray()
//);

}
