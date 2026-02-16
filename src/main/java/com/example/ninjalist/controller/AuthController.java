package com.example.ninjalist.controller;

import com.example.ninjalist.config.JWTUserData;
import com.example.ninjalist.config.TokenConfig;
import com.example.ninjalist.dto.request.LoginRequest;
import com.example.ninjalist.dto.request.ProfileRequest;
import com.example.ninjalist.dto.request.RegisterRequest;
import com.example.ninjalist.dto.response.LoginResponse;
import com.example.ninjalist.dto.response.ProfileResponse;
import com.example.ninjalist.dto.response.RegisterResponse;
import com.example.ninjalist.model.UserModel;
import com.example.ninjalist.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenConfig tokenConfig;

    public AuthController(UserRepository userRepository,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder,
                          TokenConfig tokenConfig) {


        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);
        UserModel user = (UserModel) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token, user.getName(), user.getEmail()));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserModel newUser = new UserModel();
        newUser.setEmail(registerRequest.email());
        newUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        newUser.setName(registerRequest.name());

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(newUser.getName(), newUser.getEmail()));
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(
            @AuthenticationPrincipal JWTUserData userData) {

        UserModel user = userRepository.findById(userData.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return ResponseEntity.ok(
                new ProfileResponse(
                        user.getName(),
                        user.getEmail(),
                        user.getPhotoUrl()
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @Valid @RequestBody ProfileRequest profileRequest,
            @AuthenticationPrincipal JWTUserData userData) {

        UserModel user = userRepository.findById(userData.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setName(profileRequest.name());
        user.setEmail(profileRequest.email());

        userRepository.save(user);

        return ResponseEntity.ok(
                new ProfileResponse(user.getName(), user.getEmail(), user.getPhotoUrl())
        );
    }

    @PutMapping("/profile/photo")
    public ResponseEntity<ProfileResponse> uploadProfilePhoto(
            @RequestParam("image") MultipartFile file,
            @AuthenticationPrincipal JWTUserData userData) throws IOException {

        UserModel user = userRepository.findById(userData.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String folder = System.getProperty("user.dir") + "/uploads/";
        File folderPath = new File(folder);
        if (!folderPath.exists()) {
            folderPath.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        String filename = "user_" + user.getId() + "_" + System.currentTimeMillis() + extension;

        File dest = new File(folder + filename);
        file.transferTo(dest);

        user.setPhotoUrl("/uploads/" + filename);
        userRepository.save(user);

        return ResponseEntity.ok(
                new ProfileResponse(user.getName(), user.getEmail(), user.getPhotoUrl())
        );
    }

}



