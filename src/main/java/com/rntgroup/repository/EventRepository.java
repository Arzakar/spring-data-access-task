package com.rntgroup.repository;

import com.rntgroup.model.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @QueryHints(@QueryHint(name = HINT_CACHEABLE, value = "true"))
    Optional<Event> findById(@NonNull UUID id);

    Page<Event> findByTitle(String title, Pageable pageable);

    Page<Event> findByDate(LocalDate date, Pageable pageable);

}
