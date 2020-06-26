package app.web.pavelk.mouth1.controller;

import app.web.pavelk.mouth1.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController//json controller xml
@RequestMapping("message")
public class MessageController {
    // rest по урлу

    private int counter = 4;
    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
        add(new HashMap<String, String>() {{
            put("id", "1"); // json поле
            put("text", "First message");
        }});
        add(new HashMap<String, String>() {{
            put("id", "2");
            put("text", "Second message");
        }});
        add(new HashMap<String, String>() {{
            put("id", "3");
            put("text", "Third message");
        }});
    }};

    @GetMapping
    public List<Map<String, String>> list() {
        return messages;
    }

    @GetMapping("{id}")//добовляеться мапин по /id
    public Map<String, String> getOne(@PathVariable String id) {//PathVariable преобразует урл
        return getMessage(id);
    }

    private Map<String, String> getMessage(@PathVariable String id) {//поиск по id
        return messages.stream()
                .filter(message -> message.get("id").equals(id)) //фильтруем по id
                .findFirst()//первое попавшееся
                .orElseThrow(NotFoundException::new);//бросаем исключение если нечего не нашли //404
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(counter++)); //json поле

        messages.add(message);

        return message;
    }

    @PutMapping("{id}") //обновляем конкретную запись
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> message) {//RequestBody полученые даные в пост
        Map<String, String> messageFromDb = getMessage(id);

        messageFromDb.putAll(message);
        messageFromDb.put("id", id);//установим тотже id

        return messageFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Map<String, String> message = getMessage(id);

        messages.remove(message);//удаляем запись из листа
    }

}
