package com.wixossdeckbuilder.backendservice.service;

import com.wixossdeckbuilder.backendservice.model.entities.CustomRole;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.model.payloads.UserRequest;
import com.wixossdeckbuilder.backendservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public WixossUser createNewUser(UserRequest userRequest) {
        System.out.println("Create new user called");
        WixossUser dupeUser = userRepository.findByUsername(userRequest.getUsername());
        if (Objects.nonNull(dupeUser)) {
            System.out.println("there is a dupe");
            return null;
        }
        Set<CustomRole> authorities = new HashSet<>();
        authorities.add(CustomRole.REGISTERED_USER);
        WixossUser newWixossUser = new WixossUser(
                null,
                userRequest.getUsername(),
                userRequest.getUserEmail(),
                userRequest.getUserPassword(),
                true,
                authorities);
        System.out.println("new user" + newWixossUser);
        return userRepository.save(newWixossUser);
    }

    public List<WixossUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<WixossUser> getSingleUser(Long id){
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    //update user
}
