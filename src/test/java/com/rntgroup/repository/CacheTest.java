package com.rntgroup.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.rntgroup.TestDataUtil;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class CacheTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        userAccountRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void eventCacheTest() {
        List<Event> events = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            events.add(eventRepository.save(TestDataUtil.getRandomEvent()));
        }

        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();

        entityManagerFactory.getCache().evictAll();
        statistics.clear();

        events.stream().map(Event::getId).forEach(eventRepository::findById);
        events.stream().map(Event::getId).forEach(eventRepository::findById);

        EntityStatistics firstEntityStatistics = statistics.getEntityStatistics(Event.class.getName());
        assertEquals(5, firstEntityStatistics.getCacheHitCount());
        assertEquals(5, firstEntityStatistics.getCacheMissCount());

        statistics.clear();
        events.stream().map(Event::getId).forEach(eventRepository::findById);
        events.stream().map(Event::getId).forEach(eventRepository::findById);

        EntityStatistics secondEntityStatistics = statistics.getEntityStatistics(Event.class.getName());

        assertEquals(10, secondEntityStatistics.getCacheHitCount());
        assertEquals(0, secondEntityStatistics.getCacheMissCount());
    }

    @Test
    void ticketCacheTest() {
        Event event = eventRepository.save(TestDataUtil.getRandomEvent());
        User user = userRepository.save(TestDataUtil.getRandomUser());
        List<Ticket> tickets = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            tickets.add(ticketRepository.save(TestDataUtil.getRandomBookedTicket(event, user)));
        }

        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();

        entityManagerFactory.getCache().evictAll();
        statistics.clear();

        tickets.stream().map(Ticket::getId).forEach(ticketRepository::findById);
        tickets.stream().map(Ticket::getId).forEach(ticketRepository::findById);

        EntityStatistics firstEntityStatistics = statistics.getEntityStatistics(Ticket.class.getName());
        assertEquals(5, firstEntityStatistics.getCacheHitCount());
        assertEquals(5, firstEntityStatistics.getCacheMissCount());

        statistics.clear();
        tickets.stream().map(Ticket::getId).forEach(ticketRepository::findById);
        tickets.stream().map(Ticket::getId).forEach(ticketRepository::findById);

        EntityStatistics secondEntityStatistics = statistics.getEntityStatistics(Ticket.class.getName());

        assertEquals(10, secondEntityStatistics.getCacheHitCount());
        assertEquals(0, secondEntityStatistics.getCacheMissCount());
    }

    @Test
    void userCacheTest() {
        List<User> users = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            users.add(userRepository.save(TestDataUtil.getRandomUser()));
        }

        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();

        entityManagerFactory.getCache().evictAll();
        statistics.clear();

        users.stream().map(User::getId).forEach(userRepository::findById);
        users.stream().map(User::getId).forEach(userRepository::findById);

        EntityStatistics firstEntityStatistics = statistics.getEntityStatistics(User.class.getName());
        assertEquals(5, firstEntityStatistics.getCacheHitCount());
        assertEquals(5, firstEntityStatistics.getCacheMissCount());

        statistics.clear();
        users.stream().map(User::getId).forEach(userRepository::findById);
        users.stream().map(User::getId).forEach(userRepository::findById);

        EntityStatistics secondEntityStatistics = statistics.getEntityStatistics(User.class.getName());

        assertEquals(10, secondEntityStatistics.getCacheHitCount());
        assertEquals(0, secondEntityStatistics.getCacheMissCount());
    }
}
