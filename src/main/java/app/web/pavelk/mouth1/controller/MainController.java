package app.web.pavelk.mouth1.controller;


import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.domain.Views;
import app.web.pavelk.mouth1.dto.MessagePageDto;
import app.web.pavelk.mouth1.repo.MessageRepo;
import app.web.pavelk.mouth1.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.bouncycastle.jcajce.provider.symmetric.DES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.View;
import java.awt.print.Pageable;
import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {
    private final MessageService messageService;


    @Value("${spring.profiles.active}")
    private String profile;
    private final ObjectWriter writer;

    @Autowired
    public MainController(MessageService messageService, ObjectMapper mapper) {
        this.messageService = messageService;

        this.writer = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(Views.FullMessage.class);
    }

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();

        if (user != null){ // проверка на авторизацию типо
            data.put("profile", user); // получаем авторизованого пользователя

            Sort sort = Sort.by(Sort.Direction.DESC, "id"); // сортировка


            PageRequest pageRequest = PageRequest.of(0, MessageController.MESSAGES_PER_PAGE, sort);



            MessagePageDto messagePageDto = messageService.findAll(pageRequest);


            String messages = writer.writeValueAsString(messagePageDto.getMessages());

            model.addAttribute("messages", messages);    // пробрасываем в пользователя сообщения из базы

            data.put("currentPage", messagePageDto.getCurrentPage());
            data.put("totalPages", messagePageDto.getTotalPages());

        }else{
            model.addAttribute("messages", "[]");
        }


        model.addAttribute("frontendData", data);
        // в зависимости от файла с настройками
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }
}
