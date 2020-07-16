package app.web.pavelk.mouth1.repo;


import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.UserSubscription;
import app.web.pavelk.mouth1.domain.UserSubscriptionId;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSubscriptionRepo extends JpaRepository<UserSubscription, UserSubscriptionId> {
    List<UserSubscription> findBySubscriber(User user); // ищем подписки
}
