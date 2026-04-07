package com.falizsh.finance.identity.auth.web;

import com.falizsh.finance.identity.auth.dto.request.LoginRequest;
import com.falizsh.finance.identity.auth.dto.request.RefreshTokenRequest;
import com.falizsh.finance.identity.auth.dto.response.AuthResponse;
import com.falizsh.finance.identity.auth.model.UserDetailsImpl;
import com.falizsh.finance.identity.auth.usecase.AuthSessionUseCase;
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
    private AuthSessionUseCase authSessionUseCase;

    private AuthController authController;

    private User mockedUser;
    private LoginRequest loginRequest;
    private RefreshTokenRequest refreshTokenRequest;
    private Authentication authentication;

    @Override
    public void init() {
        mockedUser = valid(User.class);
        loginRequest = valid(LoginRequest.class);
        refreshTokenRequest = new RefreshTokenRequest("refresh.token.value");
        authentication = mock(Authentication.class);

        authController = new AuthController(authenticationManager, authSessionUseCase);
    }

    @Test
    @DisplayName("Should authenticate successfully and return auth tokens")
    void shouldAuthenticateAndReturnJwtToken() {
        AuthResponse expectedResponse = AuthResponse.builder()
                .accessToken("access.token.generated")
                .refreshToken("refresh.token.generated")
                .build();
        UserDetailsImpl userDetails = new UserDetailsImpl(mockedUser);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authSessionUseCase.createSession(mockedUser)).thenReturn(expectedResponse);

        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(expectedResponse);

        InOrder verifier = inOrder(authenticationManager, authSessionUseCase);

        verifier.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifier.verify(authSessionUseCase).createSession(mockedUser);
        verifier.verifyNoMoreInteractions();
    }

    @Test
    void shouldRefreshAuthTokens() {
        AuthResponse expectedResponse = AuthResponse.builder()
                .accessToken("new.access.token")
                .refreshToken("new.refresh.token")
                .build();

        when(authSessionUseCase.refreshSession(refreshTokenRequest.refreshToken()))
                .thenReturn(expectedResponse);

        ResponseEntity<AuthResponse> response = authController.refresh(refreshTokenRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
    }
}
