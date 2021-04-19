package com.myowncompany.account.manager.repository;

import com.myowncompany.account.manager.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    Optional<List<Operation>> findByUserIdAndAccountId(Long userId, Long accountId);
}
