package app.web.pavelk.mouth1.repo;


import app.web.pavelk.mouth1.domain.Message;
import app.web.pavelk.mouth1.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MessageRepo extends JpaRepository<Message, Long> { // наследуем методы работы с базой

//    @EntityGraph(attributePaths = {"comments"})
//        // возвращает превязанные сущьности одним запросом
//    Page<Message> findAll(Pageable pageable);

    @EntityGraph(attributePaths = { "comments" })
    Page<Message> findByAuthorIn(List<User> users, Pageable pageable); // только те из переданного списка


}
