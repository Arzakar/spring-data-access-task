package com.rntgroup;

import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import lombok.experimental.UtilityClass;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

@UtilityClass
public class TestDataUtil {

    public Event getRandomEvent() {
        var parameters = new EasyRandomParameters();
        parameters.excludeField(FieldPredicates.named("id").and(FieldPredicates.inClass(Event.class)));

        return new EasyRandom(parameters).nextObject(Event.class);
    }

    public User getRandomUser() {
        var parameters = new EasyRandomParameters();
        parameters.excludeField(FieldPredicates.named("id").and(FieldPredicates.inClass(Event.class)));

        return new EasyRandom(parameters).nextObject(User.class);
    }

    public Ticket getRandomTicket() {
        var parameters = new EasyRandomParameters();
        parameters.excludeField(FieldPredicates.named("userId")
                .and(FieldPredicates.named("eventId"))
                .and(FieldPredicates.inClass(Ticket.class)));

        return new EasyRandom(parameters).nextObject(Ticket.class);
    }

    public Ticket getRandomBookedTicket(long eventId, long userId) {
        Ticket ticket = getRandomTicket();
        ticket.setEventId(eventId);
        ticket.setUserId(userId);

        return ticket;
    }

}
