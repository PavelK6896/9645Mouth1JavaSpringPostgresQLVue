package app.web.pavelk.mouth1.repo;


import app.web.pavelk.mouth1.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepo extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = {"subscriptions", "subscribers"})
        // eager жадная загрузка
    Optional<User> findById(String s);

}
