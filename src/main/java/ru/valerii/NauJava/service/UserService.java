package ru.valerii.NauJava.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.valerii.NauJava.dto.UserRegistrationDto;
import ru.valerii.NauJava.entity.Role;
import ru.valerii.NauJava.entity.User;
import ru.valerii.NauJava.exception.UserAlreadyExistsException;
import ru.valerii.NauJava.mapper.EntityMapper;
import ru.valerii.NauJava.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EntityMapper entityMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityMapper = entityMapper;
    }

    public void registerUser(UserRegistrationDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с именем '" + userDto.getUsername() + "' уже существует");
        }

        User user = entityMapper.toEntity(userDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }
}