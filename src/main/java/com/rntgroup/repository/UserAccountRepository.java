package com.rntgroup.repository;

import com.rntgroup.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    @QueryHints(@QueryHint(name = HINT_CACHEABLE, value = "true"))
    Optional<UserAccount> findById(@NonNull UUID id);

    Optional<UserAccount> findByUserId(UUID userId);

}
