package com.RTU.gourmetgamble.controllers;

import com.RTU.gourmetgamble.requests.AuthenticationRequest;
import com.RTU.gourmetgamble.requests.AuthenticationResponse;
import com.RTU.gourmetgamble.requests.RegisterRequest;
import com.RTU.gourmetgamble.services.AuthenticationService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public String register( @RequestBody RegisterRequest request) {
        ResponseEntity.ok(authenticationService.register(request));
//        return ResponseEntity.ok(authenticationService.register(request));
        return "registration";
    }

    @GetMapping("/authenticate")
    public String showLoginForm() {
        return "login"; // Assuming you have a login.html template
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "registration"; // Assuming you have a registration.html template
    }

    @PostMapping("/authenticate")
    public String authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        System.out.println(ResponseEntity.ok(authenticationService.authenticate(request)));
        return "login";
    }
}
