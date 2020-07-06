package app.web.pavelk.mouth1.controller;


import app.web.pavelk.mouth1.domain.User;
import app.web.pavelk.mouth1.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {
    private final MessageRepo messageRepo;

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) {
        HashMap<Object, Object> data = new HashMap<>();

        if (user != null){ // проверка на авторизацию типо
            data.put("profile", user); // получаем авторизованого пользователя
            data.put("messages", messageRepo.findAll()); // пробрасываем в пользователя сообщения из базы
        }


        model.addAttribute("frontendData", data);
        // в зависимости от файла с настройками
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }
}