package com.falizsh.finance.user.web;

import com.falizsh.finance.user.model.User;
import com.falizsh.finance.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/finance/user")
public class UserController {
    private final UserRepository repository;

    @GetMapping
    public List<User> findAllUsers() {
        return repository.findAll();
    }

}
