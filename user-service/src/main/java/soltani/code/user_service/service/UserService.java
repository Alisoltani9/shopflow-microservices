package soltani.code.user_service.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import soltani.code.user_service.dto.LoginRequest;
import soltani.code.user_service.dto.RegisterRequest;
import soltani.code.user_service.entity.User;
import soltani.code.user_service.repository.UserRepository;
import soltani.code.user_service.security.JwtUtil;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Long findUserIdByUsername(String username)
    {
        return userRepository.findByUsername(username).orElseThrow().getId();
    }


    public ResponseEntity<Map<String, String>> login(LoginRequest request) {
        Optional<User> selectedUser = userRepository.findByUsername(request.getUsername());
        if (selectedUser.isEmpty())
        {
            return ResponseEntity.status(401).body(Map.of("error", "the username doesn't exist"));

        }
        boolean matchedPassword = passwordEncoder.matches(request.getPassword(),
                selectedUser.get().getPassword());

        if (!matchedPassword)
        {
            return ResponseEntity.status(401).body(Map.of("error", "wrong password"));

        }

        String token = jwtUtil.generateToken(
                selectedUser.get().getUsername()
                ,selectedUser.get().getRole());
        return ResponseEntity.ok(Map.of("token", token));
    }

    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        return userRepository.save(user);
    }
}
