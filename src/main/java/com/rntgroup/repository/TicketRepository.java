package com.rntgroup.repository;

import com.rntgroup.model.Ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @QueryHints(@QueryHint(name = HINT_CACHEABLE, value = "true"))
    Optional<Ticket> findById(@NonNull UUID id);

    Page<Ticket> findByUserId(UUID userId, Pageable pageable);

    Page<Ticket> findByEventId(UUID eventId, Pageable pageable);

    Optional<Ticket> findByEventIdAndPlace(UUID eventId, int place);

}
