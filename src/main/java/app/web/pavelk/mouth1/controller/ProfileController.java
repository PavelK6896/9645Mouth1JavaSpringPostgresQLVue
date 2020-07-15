package app.web.pavelk.mouth1.controller;

import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.Views;
import app.web.pavelk.mouth1.service.ProfileService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
            @AuthenticationPrincipal User subscriber,
            @PathVariable("channelId") String channelId
    ) {
        User channel = profileService.getOne(channelId).get();

        if (subscriber.equals(channel)) { // если сам на себя
            return channel;
        } else {
            return profileService.changeSubscription(channel, subscriber);
        }
    }
}
