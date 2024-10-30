package com.geko.ecommerce.Consumer.neo4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geko.ecommerce.Entity.User;
import com.geko.ecommerce.Entity.UserNode;
import com.geko.ecommerce.Repository.neo4j.UserNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class Neo4jUserConsumer {
    private final UserNodeRepository userNodeRepository;

    @Autowired
    public Neo4jUserConsumer(UserNodeRepository userNodeRepository) {
        this.userNodeRepository = userNodeRepository;
    }

    @KafkaListener(topics = "user-node-save-topic", groupId = "neo4j-consumer-group")
    public void create(String username) {
        try {
            UserNode userNode = new UserNode();
            userNode.setUsername(username);
            userNode.setBoughtProducts(new ArrayList<>());
            userNodeRepository.save(userNode);
            System.out.println("User node saved to Neo4j: " + userNode);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "user-node-delete-topic", groupId = "neo4j-consumer-group")
    public void delete(String username) {
        try {
            userNodeRepository.deleteUserAndRelationships(username);
            System.out.println("User node deleted from Neo4j: " + username);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
