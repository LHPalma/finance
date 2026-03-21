package com.falizsh.finance.identity.auth.usecase;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.identity.users.user.repository.UserQuery;
import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class AuthUseCaseTest extends TestSupport {

    @Mock
    private UserQuery userQuery;

    private AuthUseCase authUseCase;

    private User mockedUser;

    @Override
    public void init() {
        mockedUser = valid(User.class);
        authUseCase = new AuthUseCase(userQuery);
    }

    @Test
    @DisplayName("Should return UserDetails when a valid email is provided")
    void shouldReturnUserDetailsWhenValidEmailIsProvided() {
        String validEmail = mockedUser.getEmail();
        when(userQuery.findByEmail(validEmail)).thenReturn(mockedUser);

        UserDetails result = authUseCase.loadUserByUsername(validEmail);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(validEmail);

        InOrder verifier = inOrder(userQuery);
        verifier.verify(userQuery).findByEmail(validEmail);
        verifier.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist")
    void shouldThrowExceptionWhenUserDoesNotExist() {
        String invalidEmail = "ghost@finance.com";
        when(userQuery.findByEmail(invalidEmail)).thenReturn(null);

        assertThatThrownBy(() -> authUseCase.loadUserByUsername(invalidEmail))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Usuário não encontrado");

        InOrder verifier = inOrder(userQuery);
        verifier.verify(userQuery).findByEmail(invalidEmail);
        verifier.verifyNoMoreInteractions();
    }
}