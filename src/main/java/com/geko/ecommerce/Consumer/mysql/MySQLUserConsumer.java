package com.geko.ecommerce.Consumer.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.User;
import com.geko.ecommerce.Repository.mysql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MySQLUserConsumer {
    private final UserRepository userRepository;

    @Autowired
    public MySQLUserConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "user-save-topic", groupId = "mysql-consumer-group")
    public void create(String userJson) {
        try {
            User user = objectMapper.readValue(userJson, User.class);
            userRepository.save(user);
            System.out.println("User saved to MySQL: " + user);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @KafkaListener(topics = "user-delete-topic", groupId = "mysql-consumer-group")
    public void delete(String userJson) {
        try {
            User user = objectMapper.readValue(userJson, User.class);
            userRepository.delete(user);
            System.out.println("User deleted from MySQL: " + user);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
