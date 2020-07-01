package app.web.pavelk.mouth1.repo;


import app.web.pavelk.mouth1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepo extends JpaRepository<User, String> {
}
