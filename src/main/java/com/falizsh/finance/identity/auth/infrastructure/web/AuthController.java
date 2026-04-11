package com.falizsh.finance.identity.auth.infrastructure.web;

import com.falizsh.finance.identity.auth.application.dto.request.LoginRequest;
import com.falizsh.finance.identity.auth.application.dto.request.RefreshTokenRequest;
import com.falizsh.finance.identity.auth.application.dto.response.AuthResponse;
import com.falizsh.finance.identity.auth.infrastructure.security.model.UserDetailsImpl;
import com.falizsh.finance.identity.auth.application.usecase.AuthSessionUseCase;
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

    private final AuthSessionUseCase authSessionUseCase;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Authentication authentication = authenticationManager.authenticate(token);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        AuthResponse response = authSessionUseCase.createSession(userDetails.getUser());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        AuthResponse response = authSessionUseCase.refreshSession(request.refreshToken());

        return ResponseEntity.ok(response);
    }

}
