package com.myowncompany.account.manager.ws.impl;

import com.myowncompany.account.manager.domain.Account;
import com.myowncompany.account.manager.service.AccountService;
import com.myowncompany.account.manager.ws.AccountManagerService;

import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "com.myowncompany.account.manager.ws.AccountManagerService", serviceName = "accountService")
public class AccountManagerServiceImpl implements AccountManagerService {

    private AccountService accountService;

    @Override
    public List<Account> getUserAccounts(Long id) {
        return accountService.getAllUserAccounts(id);
    }
}
