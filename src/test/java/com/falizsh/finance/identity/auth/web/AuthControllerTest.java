package com.falizsh.finance.identity.auth.web;

import com.falizsh.finance.identity.auth.dto.request.LoginRequest;
import com.falizsh.finance.identity.auth.dto.response.AuthResponse;
import com.falizsh.finance.identity.auth.model.UserDetailsImpl;
import com.falizsh.finance.identity.auth.usecase.GenerateJWTTokenUseCase;
import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthControllerTest extends TestSupport {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private GenerateJWTTokenUseCase tokenUseCase;

    private AuthController authController;

    private User mockedUser;
    private LoginRequest loginRequest;
    private Authentication authentication;

    @Override
    public void init() {
        mockedUser = valid(User.class);
        loginRequest = valid(LoginRequest.class);
        authentication = mock(Authentication.class);

        authController = new AuthController(authenticationManager, tokenUseCase);
    }

    @Test
    @DisplayName("Should authenticate successfully and return a valid JWT token")
    void shouldAuthenticateAndReturnJwtToken() {
        String expectedToken = "jwt.token.generated.by.usecase";
        UserDetailsImpl userDetails = new UserDetailsImpl(mockedUser);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(tokenUseCase.generate(mockedUser)).thenReturn(expectedToken);

        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        InOrder verifier = inOrder(authenticationManager, tokenUseCase);

        verifier.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifier.verify(tokenUseCase).generate(mockedUser);
        verifier.verifyNoMoreInteractions();
    }
}