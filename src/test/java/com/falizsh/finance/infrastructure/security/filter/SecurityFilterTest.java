package com.falizsh.finance.infrastructure.security.filter;

import com.falizsh.finance.identity.auth.application.port.TokenDecoder;
import com.falizsh.finance.identity.auth.application.usecase.AuthUseCase;
import com.falizsh.finance.identity.auth.infrastructure.security.model.UserDetailsImpl;
import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.identity.users.user.model.UserStatus;
import com.falizsh.finance.support.TestSupport;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SecurityFilterTest extends TestSupport {

    @Mock
    private TokenDecoder tokenDecoder;

    @Mock
    private AuthUseCase authUseCase;

    @Mock
    private FilterChain filterChain;

    private SecurityFilter securityFilter;

    @Override
    public void init() {
        securityFilter = new SecurityFilter(tokenDecoder, authUseCase);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueWhenAuthorizationHeaderIsMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        securityFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(tokenDecoder, never()).from(org.mockito.ArgumentMatchers.anyString());
        verify(authUseCase, never()).loadUserByUsername(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void shouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "invalid-token-without-bearer");
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(tokenDecoder.from("invalid-token-without-bearer")).thenReturn(null);

        securityFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(401);
        verify(filterChain, never()).doFilter(request, response);
        InOrder inOrder = inOrder(tokenDecoder);
        inOrder.verify(tokenDecoder).from("invalid-token-without-bearer");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldAuthenticateWhenBearerTokenIsValid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer access.token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        User user = User.builder()
                .id(1L)
                .name("Test")
                .email("valid@email.com")
                .password("password")
                .status(UserStatus.ACTIVE)
                .build();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        when(tokenDecoder.from("access.token")).thenReturn(user.getEmail());
        when(authUseCase.loadUserByUsername(user.getEmail())).thenReturn(userDetails);

        securityFilter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo(user.getEmail());
        verify(filterChain).doFilter(request, response);

        InOrder inOrder = inOrder(tokenDecoder, authUseCase, filterChain);
        inOrder.verify(tokenDecoder).from("access.token");
        inOrder.verify(authUseCase).loadUserByUsername(user.getEmail());
        inOrder.verify(filterChain).doFilter(request, response);
        inOrder.verifyNoMoreInteractions();
    }
}
