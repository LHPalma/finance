package com.falizsh.finance.auth.usecase;

import com.falizsh.finance.auth.model.UserDetailsImpl;
import com.falizsh.finance.users.user.model.User;
import com.falizsh.finance.users.user.repository.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthUseCase implements UserDetailsService {

    private final UserQuery query;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = query.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return new UserDetailsImpl(user);
    }

}
