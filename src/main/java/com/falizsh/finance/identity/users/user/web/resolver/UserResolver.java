package com.falizsh.finance.identity.users.user.web.resolver;

import com.falizsh.finance.identity.users.user.dto.request.PageRequestInput;
import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.identity.users.user.usecase.GetUserAddressesUseCase;
import com.falizsh.finance.identity.users.user.usecase.GetUserByIdUseCase;
import com.falizsh.finance.identity.users.user.usecase.GetUserEmailsUseCase;
import com.falizsh.finance.identity.users.userAddress.model.UserAddress;
import com.falizsh.finance.identity.users.userEmail.model.EmailStatus;
import com.falizsh.finance.identity.users.userEmail.model.UserEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class UserResolver {

    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetUserEmailsUseCase getUserEmailsUseCase;
    private final GetUserAddressesUseCase getUserAddresses;

    @QueryMapping
    public User user(@Argument Long id) {
        return getUserByIdUseCase.execute(id);
    }

    @SchemaMapping(typeName = "User", field = "emails")
    public Page<UserEmail> emails(
            User user,
            @Argument Set<EmailStatus> statuses,
            @Argument PageRequestInput pageRequest
    ) {

        int pageNumber = (pageRequest != null && pageRequest.page() != null) ? pageRequest.page() : 0;
        int pageSize = (pageRequest != null && pageRequest.size() != null) ? pageRequest.size() : 10;

        return getUserEmailsUseCase.execute(user.getId(), statuses, pageNumber, pageSize);
    }

    @SchemaMapping(typeName = "User", field = "addresses")
    public Page<UserAddress> addresses(
            User user,
            @Argument Boolean isPrimary,
            @Argument PageRequestInput pageRequest
    ) {
        int pageNumber = (pageRequest != null && pageRequest.page() != null) ? pageRequest.page() : 0;
        int pageSize = (pageRequest != null && pageRequest.size() != null) ? pageRequest.size() : 10;

        return getUserAddresses.execute(user.getId(), isPrimary, pageNumber, pageSize);

    }
}