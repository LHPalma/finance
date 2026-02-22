package com.falizsh.finance.users.user.repository;

import com.falizsh.finance.infra.exception.DuplicatedDataException;
import com.falizsh.finance.users.user.dto.UserCreateDTO;
import com.falizsh.finance.users.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserCommand {

    private final UserRepository repository;

    public User create(UserCreateDTO data) {
        //TODO: fazer um existsBy CPF
        if (repository.existsByEmail(data.email())) {
            throw new DuplicatedDataException("business.user.email.already.exists");
        }

        String salt = "salt";
        String password = data.password(); // + salt

        return repository.save(User.to(data, password, salt));
    }

}
