package com.geko.ecommerce.Controller;

import com.geko.ecommerce.DTO.Product.ProductDTO;
import com.geko.ecommerce.DTO.User.UserDTO;
import com.geko.ecommerce.Entity.User;
import com.geko.ecommerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    } // http://localhost:8080/api/user/getAll

    @GetMapping("/getById")
    public ResponseEntity<UserDTO> getUserById(@RequestParam Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    } // http://localhost:8080/api/user/getById?id=1

    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        if (userService.saveUser(user)) {
            return ResponseEntity.ok("User saved successfully");
        } else {
            return ResponseEntity.badRequest().body("User not saved");
        }
    } // http://localhost:8080/api/user/save

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserByUsername(@RequestParam String username) {
        if (userService.deleteUserByUsername(username)) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("User not deleted");
        }
    } // http://localhost:8080/api/user/delete?username=geko

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAll() {
        if (userService.deleteAll()) {
            return ResponseEntity.ok("All users deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Users not deleted");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestParam String username, @RequestParam String newPassword) {
        if (userService.updateUser(username, newPassword)) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.badRequest().body("User not updated");
        }
    } // http://localhost:8080/api/user/update?username=geko&newPassword=geko123

    @GetMapping("/getBoughtProducts")
    public ResponseEntity<?> getBoughtProducts(@RequestParam String username) throws InterruptedException {
        List<ProductDTO> products = userService.getAllBoughtProductsByUser(username);
        return ResponseEntity.ok(products);
    } // http://localhost:8080/api/user/getBoughtProducts?username=geko
}
