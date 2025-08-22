package dev.satyam.msecombp.controller;

import dev.satyam.msecombp.model.User;
import dev.satyam.msecombp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    //we bring in the UserRepository to use its methods
    @Autowired
    private UserRepository userRepository;

    // POST: http://localhost:8081/api/users
    //The endpoint will save a new user to the database
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // GET: http://localhost:8081/api/users/{id}
    //The endpoint will find and return a user by their ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
