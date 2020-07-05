package app.web.pavelk.mouth1.controller;

import app.web.pavelk.mouth1.domain.Message;
import app.web.pavelk.mouth1.domain.Views;
import app.web.pavelk.mouth1.dto.EventType;
import app.web.pavelk.mouth1.dto.ObjectType;
import app.web.pavelk.mouth1.repo.MessageRepo;
import app.web.pavelk.mouth1.util.WsSender;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController//json controller xml
@RequestMapping("message")
public class MessageController {
    // rest по урлу


    private MessageRepo messageRepo;
    private final BiConsumer<EventType, Message> wsSender;


    @Autowired
    public MessageController(MessageRepo messageRepo, WsSender wsSender) {
        this.messageRepo = messageRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class); //Views.IdName.class ограничим
    }


    @GetMapping // json
    @JsonView(Views.IdName.class) // интерфейс для сортировки вывода
    public List<Message> list() {
        return messageRepo.findAll();
    }

    @GetMapping("{id}")// json one
    @JsonView(Views.FullMessage.class) // фильрует вывод
    public Message getOne(@PathVariable("id") Long id) { ///// error PathVariable преобразует урл
        try {
            return messageRepo.getOne(id);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }


    @PostMapping
    public Message create(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());

        Message updatedMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE, updatedMessage); //web socket working
        return updatedMessage;
    }

    @PutMapping("{id}")
    public Message update(@PathVariable("id") Long id, // вытягивает из урла текущий
                          @RequestBody Message message) {//RequestBody полученые даные в пост

        Message messageFromDb = messageRepo.getOne(id);
        BeanUtils.copyProperties(message, messageFromDb, "id");// копирует из message в messageFromDb все поля кроме id

        Message updatedMessage = messageRepo.save(messageFromDb);
        wsSender.accept(EventType.UPDATE, updatedMessage);

        return updatedMessage;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        Message message = messageRepo.getOne(id);
        messageRepo.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }

//
//    @MessageMapping("/changeMessage") // для веб сокета
//    @SendTo("/topic/activity")// месачь брокер (возможно ребит мку, актив мку)
//    public Message message(Message message) {
//        return messageRepo.save(message); // презапишет и положит на очередь
//    }


}
