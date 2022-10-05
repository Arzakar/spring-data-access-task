package com.rntgroup;

import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import lombok.experimental.UtilityClass;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

import java.util.Collections;
import java.util.UUID;

@UtilityClass
public class TestDataUtil {

    public Event getRandomEvent() {
        var parameters = new EasyRandomParameters();
        parameters.excludeField(FieldPredicates.named("id")
                .and(FieldPredicates.inClass(Event.class)));

        return new EasyRandom(parameters).nextObject(Event.class).setTickets(Collections.emptySet());
    }

    public Event getRandomEvent(UUID id) {
        return getRandomEvent().setId(id);
    }

    public User getRandomUser() {
        var parameters = new EasyRandomParameters();
        parameters.excludeField(FieldPredicates.named("id")
                .and(FieldPredicates.inClass(Event.class)));

        return new EasyRandom(parameters).nextObject(User.class).setTickets(Collections.emptySet());
    }

    public User getRandomUser(UUID id) {
        return getRandomUser().setId(id);
    }

    public Ticket getRandomTicket() {
        var parameters = new EasyRandomParameters();
        parameters.excludeField(FieldPredicates.named("userId")
                .and(FieldPredicates.named("eventId"))
                .and(FieldPredicates.inClass(Ticket.class)));

        return new EasyRandom(parameters).nextObject(Ticket.class);
    }

    public Ticket getRandomBookedTicket(Event event, User user) {
        return getRandomTicket()
                .setEvent(event)
                .setUser(user);
    }

}
