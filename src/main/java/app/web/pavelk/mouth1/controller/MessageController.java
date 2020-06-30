package app.web.pavelk.mouth1.controller;

import app.web.pavelk.mouth1.domain.Message;
import app.web.pavelk.mouth1.domain.Views;
import app.web.pavelk.mouth1.repo.MessageRepo;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@RestController//json controller xml
@RequestMapping("message")
public class MessageController {
    // rest по урлу


    private MessageRepo messageRepo;

    @Autowired
    public void setMessageRepo(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping//выдача
    @JsonView(Views.IdName.class) // интерфейс для сортировки вывода
    public List<Message> list() {
        return messageRepo.findAll();
    }

    @GetMapping("{id}")//добовляеться мапин по /id
    @JsonView(Views.FullMessage.class) // фильрует вывод
    public Message getOne(@PathVariable("id") Long id) { ///// error PathVariable преобразует урл
        try {
            return messageRepo.getOne(id);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @PostMapping//создание
    public Message create(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());
        return messageRepo.save(message);
    }

    @PutMapping("{id}") //обновляем конкретную запись
    public Message update(@PathVariable("id") Long id, // вытягивает из урла текущий
                          @RequestBody Message message) {//RequestBody полученые даные в пост

        Message messageFromDb = messageRepo.getOne(id);
        // копирует из message в messageFromDb все поля кроме id
        BeanUtils.copyProperties(message, messageFromDb, "id");

        return messageRepo.save(messageFromDb); // сохраняем в базу и возвращаем обратно
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        messageRepo.delete(messageRepo.getOne(id));//удаляем запись из базы
    }

}
