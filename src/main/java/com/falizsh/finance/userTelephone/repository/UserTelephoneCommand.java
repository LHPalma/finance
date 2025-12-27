package com.falizsh.finance.userTelephone.repository;

import com.falizsh.finance.user.model.User;
import com.falizsh.finance.user.repository.UserRepository;
import com.falizsh.finance.userTelephone.dto.UserTelephoneCreateRequest;
import com.falizsh.finance.userTelephone.model.UserTelephone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserTelephoneCommand {

    private final UserRepository userRepository;

    @Transactional
    public UserTelephone create(UserTelephoneCreateRequest data, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user.not.found"));

        user.addTelephone(
                data.type(),
                data.areaCode(),
                data.telephone(),
                data.isPrimary()
        );

        userRepository.save(user);

        return user.getTelephones().stream()
                .filter(tel -> tel.getAreaCode().equals(data.areaCode()) && tel.getTelephone().equals(data.telephone()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Erro ao recuperar último telefone salvo"));

    }

}
