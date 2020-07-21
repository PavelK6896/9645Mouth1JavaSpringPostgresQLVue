package app.web.pavelk.mouth1.controller;

import app.web.pavelk.mouth1.domain.Message;
import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.Views;
import app.web.pavelk.mouth1.dto.MessagePageDto;
import app.web.pavelk.mouth1.service.MessageService;
import app.web.pavelk.mouth1.service.ProfileService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController//json controller xml
@RequestMapping("message")
public class MessageController {
    public static final int MESSAGES_PER_PAGE = 3;
    private final MessageService messageService;
    private final ProfileService profileService;

    @Autowired
    public MessageController(MessageService messageService, ProfileService profileService) {
        this.messageService = messageService;
        this.profileService = profileService;
    }

    @GetMapping
    @JsonView(Views.FullMessage.class) // интерфейс для сортировки вывода
    public MessagePageDto list(
            Principal principal,
            @PageableDefault(
                    size = MESSAGES_PER_PAGE, sort = {"id"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {

        System.out.println(" message GetMapping");
        //  User userFromDb = profileService.getOne(principal.getName()).get();
        User userFromDb = profileService.getUserU();
        if (principal.getName().equals(userFromDb.getId())) {
            System.out.println("UserU oK!");
        } else System.out.println("Error UserU!");

        return messageService.findForUser(pageable, userFromDb);
    }

    @GetMapping("{id}")// json one
    @JsonView(Views.FullMessage.class) // фильрует вывод
    public Message getOne(@PathVariable("id") Long id) { ///// error PathVariable преобразует урл
        return messageService.getOne(id);
    }

    @PostMapping
    @JsonView(Views.FullMessage.class)
    public Message create(
            @RequestBody Message message,
            Principal principal // в сообщение устанавливаем автора
    ) throws IOException {

        User userFromDb = profileService.getUserU();

        if (principal.getName().equals(userFromDb.getId())) {
            System.out.println("UserU oK!");
        } else System.out.println("Error UserU!");

        Message message1 = messageService.create(message, userFromDb);
        System.out.println(" message PostMapping " + message1);
        return message1;
    }

    @PutMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message update(
            @PathVariable("id") Long id, // вытягивает из урла текущий
            @RequestBody Message message) throws IOException {//RequestBody полученые даные в пост
        System.out.println(id + " message PutMapping " + message);
        Message message1 = messageService.update(id, message);
        System.out.println("message PutMapping " + message);

        return message1;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        System.out.println("message DeleteMapping");
        messageService.delete(id);
    }

}
