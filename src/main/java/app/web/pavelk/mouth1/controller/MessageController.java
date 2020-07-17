package app.web.pavelk.mouth1.controller;

import app.web.pavelk.mouth1.domain.Message;
import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.Views;
import app.web.pavelk.mouth1.dto.MessagePageDto;
import app.web.pavelk.mouth1.service.MessageService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController//json controller xml
@RequestMapping("message")
public class MessageController {
    public static final int MESSAGES_PER_PAGE = 3;

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping
    @JsonView(Views.FullMessage.class) // интерфейс для сортировки вывода
    public MessagePageDto list(
            @AuthenticationPrincipal User user,
            @PageableDefault(
                    size = MESSAGES_PER_PAGE, sort = {"id"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {

        System.out.println(" message GetMapping");
        return messageService.findForUser(pageable, user);
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
            @AuthenticationPrincipal User user // в сообщение устанавливаем автора
    ) throws IOException {
        Message message1 = messageService.create(message, user);
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
