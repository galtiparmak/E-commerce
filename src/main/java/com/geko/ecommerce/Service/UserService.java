package com.geko.ecommerce.Service;

import com.geko.ecommerce.DTO.Product.ProductDTO;
import com.geko.ecommerce.DTO.User.UserDTO;
import com.geko.ecommerce.DTO.User.UserMapper;
import com.geko.ecommerce.Entity.User;
import com.geko.ecommerce.Producer.UserProducer;
import com.geko.ecommerce.Repository.mysql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserProducer userProducer;
    private List<ProductDTO> response = null;

    @Autowired
    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(UserMapper.toDTO(user));
        }
        return userDTOS;
    }

    public UserDTO getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with id: " + id);
        }
        return UserMapper.toDTO(optionalUser.get());
    }

    public boolean saveUser(User user) {
        try {
            userProducer.createUser(user);
            return true;
        } catch (Exception e) {
            System.err.println("User not saved: " + user);
            return false;
        }
    }

    public boolean deleteUserByUsername(String username) {
        try {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isEmpty()) {
                throw new RuntimeException("User not found with username: " + username);
            }
            User user = optionalUser.get();
            userProducer.deleteUser(user);
            return true;
        } catch (Exception e) {
            System.out.println("User not found with username: " + username);
            return false;
        }
    }

    public boolean deleteAll() {
        try {
            userRepository.deleteAll();
            return true;
        } catch (Exception e) {
            System.err.println("Users not deleted");
            return false;
        }
    }

    public boolean updateUser(String username, String newPassword) {
        try {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isEmpty()) {
                throw new RuntimeException("User not found with username: " + username);
            }
            User user = optionalUser.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            System.err.println("User not found with username: " + username);
            return false;
        }
    }

    private volatile boolean isResponseReceived = false;

    public List<ProductDTO> getAllBoughtProductsByUser(String username) {
        try {
            isResponseReceived = false;
            response = null;
            userProducer.getAllBoughtProductsByUser(username);
            for (int i = 0; i < 10; i++) {
                if (isResponseReceived) {
                    break;
                }
                Thread.sleep(1000);
            }

            return response != null ? response : new ArrayList<>();

        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted while waiting: " + e.getMessage());
            return new ArrayList<>();

        } catch (Exception e) {
            System.err.println("User not found with username: " + username);
            return null;
        }
    }

    public void handleResponse(List<ProductDTO> response) {
        this.response = response;
        this.isResponseReceived = true;
    }


}
