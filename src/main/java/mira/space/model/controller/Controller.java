package mira.space.model.controller;

import mira.space.model.Messages;
import mira.space.model.User;
import mira.space.model.repo.MessagesRepository;
import mira.space.model.repo.UserRepository;
import mira.space.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class Controller {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessagesRepository messagesRepository;

    @Autowired
    UserService userService;

    @GetMapping("/init")
    public HashMap<String, Boolean> init() {
        return userService.init();
    }

    @PostMapping("/auth")
    public HashMap<String, Boolean> auth(@RequestParam String name) {
        return userService.auth(name);
    }

    @GetMapping("/user")
    public String getUser() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? "ничего нет" : users.toString();
    }

    @PostMapping("/user")
    public String postUser(@RequestParam("name") String name, HttpSession session) {
        User user = new User();
        user.setName(name);
        user.setSessionId(session.getId());
        userRepository.save(user);
        return "ok";
    }

    @GetMapping("/message")
    public Map<Integer, HashMap<String, String>> getMessage() {
        //TODO: отсортировать, сделать MessagesMapper
        List<Messages> messagesList = messagesRepository.findAll();
        if (messagesList.isEmpty()) {
            return new HashMap<>();
        }

        Map<Integer, HashMap<String, String>> resultMap = new HashMap<>();
        for (int i = 0; i < messagesList.size(); i++) {
            Messages messages = messagesList.get(i);
            HashMap<String, String> messageElements = new HashMap<>();
            messageElements.put("datetime", messages.getDateTime().toString());
            messageElements.put("username", messages.getUser().getName());
            messageElements.put("text", messages.getMessage());
            resultMap.put(i, messageElements);
        }
        return resultMap;
    }

    @PostMapping("/message")
    public Map<String, Boolean> postMessage(@RequestParam("message") String message) {
        if (Strings.isBlank(message)) {
            return Map.of("result", false);
        }
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<User> user = userRepository.findBySessionId(sessionId);
        Messages messages = new Messages();
        messages.setMessage(message);
        messages.setDateTime(LocalDateTime.now());
        user.ifPresent(messages::setUser);
        messagesRepository.saveAndFlush(messages);
        return Map.of("result", true);
    }

}
