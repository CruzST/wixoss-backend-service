package com.wixossdeckbuilder.backendservice.service;

import com.wixossdeckbuilder.backendservice.model.enums.CustomRole;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.model.payloads.UserRequest;
import com.wixossdeckbuilder.backendservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public WixossUser createNewUser(UserRequest userRequest) {
        System.out.println("Create new user called");
        WixossUser dupeUser = userRepository.findByUsername(userRequest.getUsername());
        if (Objects.nonNull(dupeUser)) {
            logger.debug("There is a dupe");
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
        logger.debug("new user" + newWixossUser);
        return userRepository.save(newWixossUser);
    }

    public List<WixossUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<WixossUser> getSingleUser(Long id) {
        return userRepository.findById(id);
    }

    public WixossUser getReferenceToUserById(Long id) {
        return userRepository.getReferenceById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    //update user
}
