package com.geko.ecommerce.Service;

import com.geko.ecommerce.DTO.Product.ProductDTO;
import com.geko.ecommerce.DTO.Product.ProductMapper;
import com.geko.ecommerce.DTO.User.UserDTO;
import com.geko.ecommerce.DTO.User.UserMapper;
import com.geko.ecommerce.Entity.Product;
import com.geko.ecommerce.Entity.ProductNode;
import com.geko.ecommerce.Entity.User;
import com.geko.ecommerce.Entity.UserNode;
import com.geko.ecommerce.Producer.UserNodeProducer;
import com.geko.ecommerce.Producer.UserProducer;
import com.geko.ecommerce.Repository.mysql.UserRepository;
import com.geko.ecommerce.Repository.neo4j.UserNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserProducer userProducer;
    private final UserNodeProducer userNodeProducer;
    private final UserNodeRepository userNodeRepository;
    @Autowired
    @Qualifier("transactionManager")
    private PlatformTransactionManager mysqlTransactionManager;
    @Autowired
    @Qualifier("neo4jTransactionManager")
    private PlatformTransactionManager neo4jTransactionManager;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserProducer userProducer,
                       UserNodeProducer userNodeProducer,
                       UserNodeRepository userNodeRepository) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
        this.userNodeProducer = userNodeProducer;
        this.userNodeRepository = userNodeRepository;
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
            saveUserToNeo4j(user.getUsername());
            saveUserToMysql(user);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Transactional("transactionManager")
    public void saveUserToMysql(User user) {
        try {
            userProducer.createUser(user);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional("neo4jTransactionManager")
    public void saveUserToNeo4j(String username) {
        try {
            userNodeProducer.createUser(username);
        }
        catch (Exception e) {
            e.printStackTrace();
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

    public List<ProductDTO> getAllBoughtProductsForUser(String username){
        Optional<UserNode> optionalUserNode = userNodeRepository.findByUsername(username);
        if (optionalUserNode.isEmpty()) {
            throw new RuntimeException("User node not found with username " + username);
        }

        UserNode userNode = optionalUserNode.get();
        List<ProductNode> products = userNode.getBoughtProducts();
        List<ProductDTO> productDTOS = new ArrayList<>();

        for (ProductNode product : products) {
            ProductDTO productDTO = ProductMapper.toDTO(product);
            productDTOS.add(productDTO);
        }

        return productDTOS;
    }

}
