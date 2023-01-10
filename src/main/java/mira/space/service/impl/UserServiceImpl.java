package mira.space.service.impl;

import mira.space.model.User;
import mira.space.model.repo.UserRepository;
import mira.space.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public HashMap<String, Boolean> auth(String name) {
        HashMap<String, Boolean> map = new HashMap<>();
        if (Strings.isBlank(name)) {
            map.put("result", false);
            return map;
        }

        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        User user = new User();
        user.setName(name);
        user.setSessionId(sessionId);

        userRepository.save(user);

        map.put("result", true);
        return map;
    }

    @Override
    public HashMap<String, Boolean> init() {
        HashMap<String, Boolean> map = new HashMap<>();
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<User> userOpt = userRepository.findBySessionId(sessionId);
        map.put("result", userOpt.isPresent());
        return map;
    }
}
