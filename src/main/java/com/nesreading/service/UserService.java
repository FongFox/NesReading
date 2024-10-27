package com.nesreading.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nesreading.domain.User;
import com.nesreading.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User newUser) {
        return this.userRepository.save(newUser);
    }

    public List<User> handleFetchAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> handleFetchUserById(int id) {
        return userRepository.findById(id);
    }

    public void handleDeleteUserById(int id) {
        userRepository.deleteById(id);
    }

    public User handleUpdateUser(User tempUser) {
        User dbUser = userRepository.findById(tempUser.getId()).get();

        dbUser.setFirstName(tempUser.getFirstName());
        dbUser.setLastName(tempUser.getLastName());
        dbUser.setPhoneNumber(tempUser.getPhoneNumber());
        dbUser.setRole(tempUser.getRole());

        if(!tempUser.getPassword().isEmpty() || !tempUser.getPassword().isBlank()) {
            dbUser.setPassword(tempUser.getPassword());
        }

        return userRepository.save(dbUser);
    }
}
