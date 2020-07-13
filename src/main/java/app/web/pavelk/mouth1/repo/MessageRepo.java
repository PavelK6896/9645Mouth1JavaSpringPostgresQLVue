package app.web.pavelk.mouth1.repo;


import app.web.pavelk.mouth1.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;



public interface MessageRepo extends JpaRepository<Message, Long> { // наследуем методы работы с базой

    @EntityGraph(attributePaths = {"comments"})
        // возвращает превязанные сущьности одним запросом
    Page<Message> findAll(Pageable pageable);
}
