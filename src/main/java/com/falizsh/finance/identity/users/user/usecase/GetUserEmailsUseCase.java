package com.falizsh.finance.identity.users.user.usecase;

import com.falizsh.finance.identity.users.userEmail.model.EmailStatus;
import com.falizsh.finance.identity.users.userEmail.model.UserEmail;
import com.falizsh.finance.identity.users.userEmail.repository.UserEmailQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class GetUserEmailsUseCase {

    private final UserEmailQuery query;

    public Page<UserEmail> execute(Long userId, Set<EmailStatus> statuses, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        if (statuses != null && !statuses.isEmpty()) {
            return query.findEmailsByUserIdAndStatuses(userId, statuses, pageRequest);
        }

        return query.findEmailsByUserId(userId, pageRequest);
    }

}
