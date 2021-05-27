package com.myowncompany.account.manager.controller;

import com.myowncompany.account.manager.domain.Account;
import com.myowncompany.account.manager.domain.Operation;
import com.myowncompany.account.manager.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/account-manager/user")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/{user_id}/account/add")
    public Account addAccount(@PathVariable(name = "user_id") Long userId, @RequestParam(name = "iban") String iban) {
        return accountService.addAccount(userId, iban);
    }

    @DeleteMapping("/{user_id}/account/{account_id}")
    public void removeAccount(@PathVariable(name = "user_id") Long userId, @PathVariable(name = "account_id") Long accountId) {
        accountService.removeAccount(userId, accountId);
    }

    @GetMapping("/{user_id}/account/")
    public List<Account> getAllUserAccounts(@PathVariable(name = "user_id") Long userId) {
        return accountService.getAllUserAccounts(userId);
    }

    @PutMapping("/{user_id}/account/{account_id}/deposit")
    public void deposit(@PathVariable(name = "user_id") Long userId, @PathVariable(name = "account_id") Long accountId, @RequestParam(name = "depositAmount") BigDecimal depositAmount) {
        accountService.deposit(userId, accountId, depositAmount);
    }

    @PutMapping("/{user_id}/account/{account_id}/withdraw")
    public void withdraw(@PathVariable(name = "user_id") Long userId, @PathVariable(name = "account_id") Long accountId, @RequestParam(name = "withdrawAmount") BigDecimal withdrawAmount) {
        accountService.withdraw(userId, accountId, withdrawAmount);
    }

    @GetMapping("/{user_id}/account/{account_id}/balance")
    public BigDecimal getBalance(@PathVariable(name = "user_id") Long userId, @PathVariable(name = "account_id") Long accountId) {
        return accountService.getBalance(userId, accountId);
    }

    @GetMapping("/{user_id}/account/{account_id}/operations")
    public List<Operation> getOperations(@PathVariable(name = "user_id") Long userId, @PathVariable(name = "account_id") Long accountId) {
        return accountService.getTransactions(userId, accountId);
    }

    @GetMapping("/{user_id}/account/balance")
    public BigDecimal getAllBalance(@PathVariable(name = "user_id") Long userId) {
        return accountService.getTotal(userId);
    }
}
