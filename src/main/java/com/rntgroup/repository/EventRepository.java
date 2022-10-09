package com.rntgroup.repository;

import com.rntgroup.model.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    Page<Event> findByTitle(String title, Pageable pageable);

    Page<Event> findByDate(LocalDate date, Pageable pageable);

}
