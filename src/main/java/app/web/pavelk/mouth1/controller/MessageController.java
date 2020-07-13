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


    @GetMapping // json
    @JsonView(Views.FullMessage.class) // интерфейс для сортировки вывода
    public MessagePageDto list(
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return messageService.findAll(pageable);
    }

    @GetMapping("{id}")// json one
    @JsonView(Views.FullMessage.class) // фильрует вывод
    public Message getOne(@PathVariable("id") Long id) { ///// error PathVariable преобразует урл
        return messageService.getOne(id);
    }


    @PostMapping
    public Message create(
            @RequestBody Message message,
            @AuthenticationPrincipal User user // в сообщение устанавливаем автора
    ) throws IOException {
        return messageService.create(message, user);
    }

    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Long id, // вытягивает из урла текущий
            @RequestBody Message message) throws IOException {//RequestBody полученые даные в пост
        return messageService.update(id, message);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        messageService.delete(id);
    }


    /////////////////////////////////////////////
//    @MessageMapping("/changeMessage") // для веб сокета
//    @SendTo("/topic/activity")// месачь брокер (возможно ребит мку, актив мку)
//    public Message message(Message message) {
//        return messageRepo.save(message); // презапишет и положит на очередь
//    }

}
