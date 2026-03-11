package com.falizsh.finance.auth.web;

import com.falizsh.finance.auth.dto.request.LoginRequest;
import com.falizsh.finance.auth.dto.response.AuthResponse;
import com.falizsh.finance.auth.model.UserDetailsImpl;
import com.falizsh.finance.auth.usecase.GenerateJWTToken;
import com.falizsh.finance.auth.usecase.GenerateJWTTokenUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final GenerateJWTTokenUseCase tokenUseCase;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid LoginRequest request) {

        var token = new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Authentication authentication = authenticationManager.authenticate(token);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        AuthResponse response = new AuthResponse(tokenUseCase.generate(userDetails.getUser()));

        return ResponseEntity.ok(response);
    }

}
