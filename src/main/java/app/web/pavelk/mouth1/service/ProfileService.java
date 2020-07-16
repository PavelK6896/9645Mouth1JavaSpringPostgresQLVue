package app.web.pavelk.mouth1.service;

import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.UserSubscription;
import app.web.pavelk.mouth1.repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private UserDetailsRepo userDetailsRepo;

    @Autowired
    public void setUserDetailsRepo(UserDetailsRepo userDetailsRepo) {
        this.userDetailsRepo = userDetailsRepo;
    }

    public User changeSubscription(User channel, User subscriber) {
        List<UserSubscription> subcriptions = channel.getSubscribers()//получили подписщиков
                .stream()
                .filter(subscription ->
                        subscription.getSubscriber().equals(subscriber) // проверяем есть ли
                )
                .collect(Collectors.toList()); // собираем в лист



        if (subcriptions.isEmpty()) {
            UserSubscription subscription = new UserSubscription(channel, subscriber);
            channel.getSubscribers().add(subscription); // если нету то добавляем
        } else {
            channel.getSubscribers().removeAll(subcriptions); // если есть то удаляем
        }

        return userDetailsRepo.save(channel);
    }

    public Optional<User> getOne(String id) {
        return userDetailsRepo.findById(id);
    }


}
