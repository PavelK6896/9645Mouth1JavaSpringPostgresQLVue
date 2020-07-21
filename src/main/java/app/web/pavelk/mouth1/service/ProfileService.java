package app.web.pavelk.mouth1.service;

import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.UserSubscription;
import app.web.pavelk.mouth1.repo.UserDetailsRepo;
import app.web.pavelk.mouth1.repo.UserSubscriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private UserDetailsRepo userDetailsRepo;
    private UserSubscriptionRepo userSubscriptionRepo;
    private User userU;

    @Autowired
    public void setUserSubscriptionRepo(UserSubscriptionRepo userSubscriptionRepo) {
        this.userSubscriptionRepo = userSubscriptionRepo;
    }

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


    public List<UserSubscription> getSubscribers(String id) {
        System.out.println("getSubscribers ");
        return userSubscriptionRepo.findByChannel(userDetailsRepo.findById(id).get());
    }

    public UserSubscription changeSubscriptionStatus(User channel, String stringId) {

        UserSubscription subscription = userSubscriptionRepo
                .findByChannelAndSubscriber(channel, userDetailsRepo.findById(stringId).get());
        subscription.setActive(!subscription.isActive()); // преключаем статус
        System.out.println("changeSubscriptionStatus save " + subscription);
        return userSubscriptionRepo.save(subscription);
    }

    // создает нового или возврощает найденного
    public OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        Map<String, Object> attributes = oidcUser.getAttributes();
        String id = (String) attributes.get("sub");
        // String id = "888888888899999";

        Optional<User> byId = userDetailsRepo.findById(id);

        if (!byId.isPresent()) {
            System.out.println("create new user");
            userU = new User();
            userU.setId(id);
            userU.setName((String) attributes.get("name"));
            userU.setEmail((String) attributes.get("email"));
            userU.setGender((String) attributes.get("gender"));
            userU.setLocale((String) attributes.get("locale"));
            userU.setUserpic((String) attributes.get("picture"));
            userU.setLastVisit(LocalDateTime.now());
            userDetailsRepo.save(userU);
            return oidcUser;
        }

        userU = byId.get();
        System.out.println("old user");
        return oidcUser;

    }

    public User getUserU() {
        return userU;
    }
}
