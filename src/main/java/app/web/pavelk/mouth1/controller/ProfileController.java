package app.web.pavelk.mouth1.controller;

import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.UserSubscription;
import app.web.pavelk.mouth1.domain.Views;
import app.web.pavelk.mouth1.service.ProfileService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController // отдает json
@RequestMapping("profile")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("{id}")
    @JsonView(Views.FullProfile.class)
    public Optional<User> get(@PathVariable("id") String id) {
        return profileService.getOne(id);
    }

    @PostMapping("change-subscription/{channelId}")
    @JsonView(Views.FullProfile.class)
    public User changeSubscription(
            Principal principal,
            @PathVariable("channelId") String channelId
    ) {

        User channel = profileService.getOne(channelId).get();
        User userFromDb = profileService.getUserU();
        if (principal.getName().equals(userFromDb.getId())) {
            System.out.println("UserU oK!");
        } else System.out.println("Error UserU!");

        if (principal.equals(channel)) { // если сам на себя
            return channel;
        } else {
            return profileService.changeSubscription(channel, userFromDb);
        }
    }

    @GetMapping("get-subscribers/{channelId}")
    @JsonView(Views.IdName.class)
    public List<UserSubscription> subscribers( // все подписки текущего пользователя
                                               @PathVariable("channelId") String id
    ) {
        return profileService.getSubscribers(id);
    }

    @PostMapping("change-status/{subscriberId}")
    @JsonView(Views.IdName.class)
    public UserSubscription changeSubscriptionStatus(
            Principal principal,
            @PathVariable("subscriberId") String stringId
    ) {
        User channel = profileService.getOne(principal.getName()).get();
        return profileService.changeSubscriptionStatus(channel, stringId);
    }
}
