package com.myowncompany.account.manager.service;

import com.myowncompany.account.manager.domain.Account;
import com.myowncompany.account.manager.domain.Action;
import com.myowncompany.account.manager.domain.Operation;
import com.myowncompany.account.manager.domain.User;
import com.myowncompany.account.manager.exception.*;
import com.myowncompany.account.manager.repository.AccountRepository;
import com.myowncompany.account.manager.repository.OperationRepository;
import com.myowncompany.account.manager.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public Account addAccount(final Long userId, final String iban) {
        final var user = userRepository.findById(userId).orElseThrow(UserDoesntExistException::new);
        accountRepository.findByIban(iban).ifPresent(account -> { throw new AccountAlreadyExistsException(iban); });

        final var account = mapToAccount(iban, user);
        account.setBalance(BigDecimal.ZERO);

        log.debug("Save account: {}", account);
        return accountRepository.save(account);
    }

    public void removeAccount(final Long userId, final Long accountId) {
        accountRepository.findById(accountId)
                .filter(account -> account.getUser().getId().equals(userId))
                .ifPresentOrElse(
                        account -> {
                            log.debug("Delete account: {}", account);
                            accountRepository.delete(account);
                        },
                        () -> {
                            log.warn("User (id: {}) doesn't have account (id: {})", userId, accountId);
                            throw new UserDoesntHaveAccountException("UserId: %s, accountId: %s".formatted(userId, accountId));
                        }
                );
    }

    public List<Account> getAllUserAccounts(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(UserDoesntExistException::new);

        return accountRepository.findByUserId(userId)
                .orElse(List.of());
    }

    @Transactional
    public void deposit(final Long userId, final Long accountId, final BigDecimal depositAmount) {
        final var user = userRepository.findById(userId).orElseThrow(UserDoesntExistException::new);

        accountRepository.findById(accountId)
                .filter(account -> account.getUser().getId().equals(userId))
                .ifPresentOrElse(
                        account -> {
                            final var balance = account.getBalance().add(depositAmount);
                            account.setBalance(balance);

                            log.debug("Save account: {}", account);
                            accountRepository.save(account);

                            final var transaction = Operation.builder()
                                    .account(account)
                                    .user(user)
                                    .amount(depositAmount)
                                    .action(Action.DEPOSIT)
                                    .timestamp(LocalDateTime.now())
                                    .build();

                            log.debug("Save transaction: {}", transaction);
                            operationRepository.save(transaction);
                        },
                        () -> {
                            log.warn("Account doesn't exist. Account id: {}", accountId);
                            throw new AccountDoesntExistException(accountId.toString());
                        }
                );
    }

    @Transactional
    public void withdraw(final Long userId, final Long accountId, final BigDecimal withdrawAmount) {
        final var user = userRepository.findById(userId).orElseThrow(UserDoesntExistException::new);

        accountRepository.findById(accountId)
                .filter(account -> account.getUser().getId().equals(userId))
                .ifPresentOrElse(
                        account -> {
                            final var balance = account.getBalance().subtract(withdrawAmount);
                            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                                throw new NotEnoughFundsException();
                            }
                            account.setBalance(balance);

                            log.debug("Save account: {}", account);
                            accountRepository.save(account);

                            final var transaction = Operation.builder()
                                    .account(account)
                                    .user(user)
                                    .amount(withdrawAmount)
                                    .action(Action.WITHDRAWAL)
                                    .timestamp(LocalDateTime.now())
                                    .build();

                            log.debug("Save transaction: {}", transaction);
                            operationRepository.save(transaction);
                        },
                        () -> {
                            log.warn("Account doesn't exist. Account id: {}", accountId);
                            throw new AccountDoesntExistException(accountId.toString());
                        }
                );
    }

    public BigDecimal getBalance(final Long userId, final Long accountId) {
        return accountRepository.findById(accountId)
                .filter(account -> account.getUser().getId().equals(userId))
                .orElseThrow(() -> new AccountDoesntExistException(accountId.toString()))
                .getBalance();
    }

    public BigDecimal getTotal(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(UserDoesntExistException::new);

        return accountRepository.totalByUserId(userId)
                .orElseThrow(AccountDoesntExistException::new);
    }

    public List<Operation> getTransactions(Long userId, Long accountId) {
        return operationRepository.findByUserIdAndAccountId(userId, accountId).orElse(List.of());
    }

    public void deleteOperation(Operation operation) {
        operationRepository.delete(operation);
    }

    public Account mapToAccount(final String iban, final User user) {
        var account = new Account();
        account.setIban(iban);
        account.setUser(user);
        return account;
    }
}
