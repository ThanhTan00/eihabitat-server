package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.UserCreationReq;
import com.eihabitat.eihabitat_server.dto.request.UserUpdateReq;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationReq request) {
        User user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setSignupDate(request.getSignupDate());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setProfileName(request.getProfileName());

        return userRepository.save(user);
    }

    public List<User> getUsers() {
       return userRepository.findAll();
    }

    public User getUser(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(String userId, UserUpdateReq req) {
        User user = getUser(userId);

        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setProfileName(req.getProfileName());
        user.setProfileAvatar(req.getProfileAvatar());
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());
        user.setGender(req.getGender());
        user.setDateOfBirth(req.getDateOfBirth());
        user.setNationality(req.getNationality());

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
