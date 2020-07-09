package app.web.pavelk.mouth1.controller;

import app.web.pavelk.mouth1.domain.Message;
import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.Views;
import app.web.pavelk.mouth1.dto.EventType;
import app.web.pavelk.mouth1.dto.MetaDto;
import app.web.pavelk.mouth1.dto.ObjectType;
import app.web.pavelk.mouth1.repo.MessageRepo;
import app.web.pavelk.mouth1.util.WsSender;
import com.fasterxml.jackson.annotation.JsonView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController//json controller xml
@RequestMapping("message")
public class MessageController {
    // rest по урлу

    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+"; //regeks
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE); //CASE_INSENSITIVE не проверяет регистор
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);



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
    public Message create(@RequestBody Message message,
                          @AuthenticationPrincipal User user // в сообщение устанавливаем автора
                          ) throws IOException {
        message.setCreationDate(LocalDateTime.now());
        fillMeta(message);
        message.setAuthor(user);// устанавливаем автора
        Message updatedMessage = messageRepo.save(message);
        wsSender.accept(EventType.CREATE, updatedMessage); //web socket working
        return updatedMessage;
    }

    @PutMapping("{id}")
    public Message update(@PathVariable("id") Long id, // вытягивает из урла текущий
                          @RequestBody Message message) throws IOException {//RequestBody полученые даные в пост

        Message messageFromDb = messageRepo.getOne(id);
        BeanUtils.copyProperties(message, messageFromDb, "id");// копирует из message в messageFromDb все поля кроме id
        fillMeta(message);
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

    /////////////////////////////////////////////


    private void fillMeta(Message message) throws IOException {
        String text = message.getText();
        Matcher matcher = URL_REGEX.matcher(text);

        if (matcher.find()) { // если нашел значение
            String url = text.substring(matcher.start(), matcher.end()); // получаем урл
            message.setLink(url); // загоняем в сущьность сылку


            matcher = IMG_REGEX.matcher(url); // проверяем на картинку

            if (matcher.find()) { // если картинка
                message.setLinkCover(url); // в сущьность
            } else if (!url.contains("youtu")) { // стандартная форма ютуба
                MetaDto meta = getMeta(url); // парсит jsop  // по созданому класу

                message.setLinkCover(meta.getCover());
                message.setLinkTitle(meta.getTitle());
                message.setLinkDescription(meta.getDescription());
            }
        }
    }

    private MetaDto getMeta(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements title = doc.select("meta[name$=title],meta[property$=title]"); // находим тег

        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element) { // провека на нал
        return element == null ? "" : element.attr("content");
    }


}
