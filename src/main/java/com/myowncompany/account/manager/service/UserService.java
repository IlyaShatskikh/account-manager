package com.myowncompany.account.manager.service;

import com.myowncompany.account.manager.domain.Account;
import com.myowncompany.account.manager.domain.User;
import com.myowncompany.account.manager.exception.UserAlreadyExistsException;
import com.myowncompany.account.manager.exception.UserDoesntExistException;
import com.myowncompany.account.manager.repository.AccountRepository;
import com.myowncompany.account.manager.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public User addUser(final User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> { throw new UserAlreadyExistsException(u.getUsername()); });

        log.debug("Save user: {}", user);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(final Long userId) {
        userRepository.findById(userId).ifPresentOrElse(
                user -> {
                    accountService.getAllUserAccounts(userId)
                            .forEach(account -> {
                                accountService.getTransactions(userId, account.getId()).forEach(accountService::deleteOperation);
                                accountService.removeAccount(userId, account.getId());
                            });
                    log.debug("Delete user: {}", user);
                    userRepository.delete(user);
                },
                () -> {
                    log.warn("User doesn't exist. User id: {}", userId);
                    throw new UserDoesntExistException();
                }
        );
    }

    public List<User> getAll() {
        log.debug("Get all users");
        return userRepository.findAll();
    }
}
