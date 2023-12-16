package com.mzalcmanis.transfer.db.repo;

import com.mzalcmanis.transfer.db.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    List<AccountEntity> findByClientId(UUID clientId);
}
