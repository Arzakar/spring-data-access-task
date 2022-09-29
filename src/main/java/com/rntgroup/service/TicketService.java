package com.rntgroup.service;

import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;

import java.util.List;

public interface TicketService {

    Ticket create(Ticket ticket);

    List<Ticket> findByUser(User user, int pageSize, int pageNum);
    List<Ticket> findByEvent(Event event, int pageSize, int pageNum);

    Ticket deleteById(long id);
}
