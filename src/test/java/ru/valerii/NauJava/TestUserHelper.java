package ru.valerii.NauJava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.valerii.NauJava.entity.Role;
import ru.valerii.NauJava.entity.User;
import ru.valerii.NauJava.repository.UserRepository;


@Component
public class TestUserHelper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void createTestUserIfNotExists(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User testUser = new User();
            testUser.setUsername(username);
            testUser.setPassword(passwordEncoder.encode(password));
            testUser.setRole(Role.USER);
            userRepository.save(testUser);
        }
    }

    @Transactional
    public void deleteTestUser(String username) {
        userRepository.findByUsername(username)
                .ifPresent(user -> userRepository.delete(user));
    }
}