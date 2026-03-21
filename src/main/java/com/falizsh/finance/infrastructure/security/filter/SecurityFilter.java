package com.falizsh.finance.infrastructure.security.filter;

import com.falizsh.finance.identity.auth.action.GetSubjectAction;
import com.falizsh.finance.identity.auth.usecase.AuthUseCase;
import com.falizsh.finance.identity.users.user.repository.UserQuery;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final GetSubjectAction getSubject;

    private final UserQuery userQuery;
    private final AuthUseCase authUseCase;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = retrieveToken(request);

        if (token != null) {
            String subject = getSubject.from(token);
            if (subject == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
                //throw new ServletException("invalid.token");
            }

            UserDetails userDetails = authUseCase.loadUserByUsername(subject);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);
    }

    private String retrieveToken(HttpServletRequest request) throws ServletException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
