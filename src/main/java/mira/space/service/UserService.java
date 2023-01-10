package mira.space.service;

import java.util.HashMap;

public interface UserService {
    HashMap<String, Boolean> auth(String name);
    HashMap<String, Boolean> init();
}
