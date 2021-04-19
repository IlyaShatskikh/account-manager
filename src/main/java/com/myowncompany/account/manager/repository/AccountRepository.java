package com.myowncompany.account.manager.repository;

import com.myowncompany.account.manager.domain.Account;
import com.myowncompany.account.manager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByIban(String iban);

    @Query(value = "SELECT sum(balance) FROM Account WHERE user_id = :userId")
    Optional<BigDecimal> totalByUserId(Long userId);

    Optional<List<Account>> findByUserId(Long userId);
}
