package com.myowncompany.account.manager.ws;

import com.myowncompany.account.manager.domain.Account;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface AccountManagerService {

        @WebMethod
        public List<Account> getUserAccounts(@WebParam(name = "id") Long id);
}
