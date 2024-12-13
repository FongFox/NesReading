package com.nesreading.service;

import java.util.List;

import com.nesreading.model.dto.RegisterDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nesreading.model.User;
import com.nesreading.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UploadService uploadService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UploadService uploadService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.uploadService = uploadService;
    }

    public boolean handleCheckExistedUser(int id) {
        return !userRepository.existsById(id);
    }

    public void handleCreateUser(User newUser) {
        User tempUser = new User(newUser.getRole(), newUser.getFirstName(), newUser.getLastName(), newUser.getEmail(), newUser.getPhoneNumber());
        tempUser.setPassword(handleConvertHashPassword(newUser.getPassword()));

        this.userRepository.save(tempUser);
    }

    public void handleCreateUser(User newUser, MultipartFile file) {
        String avatar = uploadService.handleSaveUploadFile(file, "avatar");
        User user = new User(newUser.getRole(), newUser.getFirstName(), newUser.getLastName(), newUser.getEmail(), newUser.getPhoneNumber());
        user.setPassword(handleConvertHashPassword(newUser.getPassword()));
        user.setAvatar(avatar);

        userRepository.save(user);
    }

    public List<User> handleFetchAllUsers() {
        return userRepository.findAll();
    }

    public User handleFetchUserById(int id) {
        return userRepository.findById(id).get();
    }

    public void handleDeleteUserById(int id) {
        User tempUser = userRepository.getReferenceById(id);

        if(tempUser.getAvatar() != null && !tempUser.getAvatar().isEmpty()) {
            uploadService.handleDeleteFile(tempUser.getAvatar(), "avatar");
        }

        userRepository.deleteById(id);
    }

    public void handleUpdateUser(User tempUser) {
        User dbUser = handleFetchUserById(tempUser.getId());

        dbUser.setFirstName(tempUser.getFirstName());
        dbUser.setLastName(tempUser.getLastName());
        dbUser.setPhoneNumber(tempUser.getPhoneNumber());
        dbUser.setRole(tempUser.getRole());
        dbUser.setAddress(tempUser.getAddress());

        if(!tempUser.getPassword().isEmpty() && !tempUser.getPassword().isBlank()) {
            dbUser.setPassword(handleConvertHashPassword(tempUser.getPassword()));
        }

        userRepository.save(dbUser);
    }

    public void handleUpdateClientProfile(User tempUser) {
        User dbUser = handleFetchUserById(tempUser.getId());

        dbUser.setFirstName(tempUser.getFirstName());
        dbUser.setLastName(tempUser.getLastName());
        dbUser.setPhoneNumber(tempUser.getPhoneNumber());
        dbUser.setAddress(tempUser.getAddress());

        userRepository.save(dbUser);
    }

    public void handleUpdateUser(User tempUser, MultipartFile file) {
        User dbUser = handleFetchUserById(tempUser.getId());

        dbUser.setFirstName(tempUser.getFirstName());
        dbUser.setLastName(tempUser.getLastName());
        dbUser.setPhoneNumber(tempUser.getPhoneNumber());
        dbUser.setRole(tempUser.getRole());

        if(!tempUser.getPassword().isEmpty() && !tempUser.getPassword().isBlank()) {
            dbUser.setPassword(handleConvertHashPassword(tempUser.getPassword()));
        }

        if(!file.isEmpty()) {
            String avatar = uploadService.handleSaveUploadFile(file, "avatar");
            dbUser.setAvatar(avatar);
        }

        userRepository.save(dbUser);
    }

    public boolean handleCheckEmailExisted(String email) {
        return userRepository.existsByEmail(email);
    }

    public User handleFetchUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ======================= Security Service ============================
    public  String handleConvertHashPassword(String userPassword) {
        return this.passwordEncoder.encode(userPassword);
    }

    // ======================= User Mapper ============================
    public User registerDtoToUser(RegisterDTO registerDTO) {
        User user = new User(
                "USER", registerDTO.getFirstName(),
                registerDTO.getLastName(), registerDTO.getEmail(), ""
        );
        user.setPassword(registerDTO.getPassword());

        return user;
    }
}
