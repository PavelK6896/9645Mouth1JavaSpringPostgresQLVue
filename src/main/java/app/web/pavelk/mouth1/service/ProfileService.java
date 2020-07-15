package app.web.pavelk.mouth1.service;

import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ProfileService {

    private UserDetailsRepo userDetailsRepo;

    @Autowired
    public void setUserDetailsRepo(UserDetailsRepo userDetailsRepo) {
        this.userDetailsRepo = userDetailsRepo;
    }

    public User changeSubscription(User channel, User subscriber) {
        Set<User> subscribers = channel.getSubscribers();

        if (subscribers.contains(subscriber)) { // если есть таой усер то удалить
            subscribers.remove(subscriber);
        } else {
            subscribers.add(subscriber);
        }

        return userDetailsRepo.save(channel);
    }

    public Optional<User> getOne(String id) {
        return userDetailsRepo.findById(id);
    }


}
