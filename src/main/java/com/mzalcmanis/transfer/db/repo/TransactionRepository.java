package com.mzalcmanis.transfer.db.repo;

import com.mzalcmanis.transfer.db.entity.TransactionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    @Query("FROM TransactionEntity " +
            "WHERE senderAccountId = :accountId " +
            " OR receiverAccountId = :accountId " +
            "ORDER BY createdDate desc")
    List<TransactionEntity> findByAccountId(@Param("accountId") UUID accountId, Pageable pageable);
}
