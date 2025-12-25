package com.falizsh.finance.userAddress.repository;

import com.falizsh.finance.shared.valueObject.CEP;
import com.falizsh.finance.user.model.User;
import com.falizsh.finance.user.repository.UserRepository;
import com.falizsh.finance.userAddress.dto.UserAddressCreateDTO;
import com.falizsh.finance.userAddress.model.UserAddress;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserAddressCommand {

    private final UserRepository userRepository;

    public UserAddress create(Long userId, UserAddressCreateDTO data) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user.not.found"));

        CEP zipCode = CEP.unverified(data.zipCode());

        user.addAddress(
                data.type(),
                data.street(),
                data.number(),
                data.complement(),
                data.neighborhood(),
                data.city(),
                data.state(),
                zipCode,
                data.country(),
                data.isPrimary()
        );

        userRepository.save(user);

        return user.getAddresses().stream()
                .max(Comparator.comparing(UserAddress::getCreatedAt))
                .orElseThrow(() -> new IllegalStateException("Erro ao recuperar último endereço salvo"));

    }

}
