package soltani.code.user_service.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soltani.code.user_service.dto.UserResponse;
import soltani.code.user_service.entity.User;
import soltani.code.user_service.exception.ResourceNotFoundException;
import soltani.code.user_service.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long id)
    {
        User selectedUser = userRepository.findById(id)
                .orElseThrow( () -> new
                        ResourceNotFoundException("User not found with id: " + id));

        UserResponse userResponse = new UserResponse(
                selectedUser.getId(),
                selectedUser.getUsername(),
                selectedUser.getRole());

        return ResponseEntity.ok(userResponse);

    }

}
