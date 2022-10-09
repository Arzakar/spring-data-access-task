package com.rntgroup.mapper;

import com.rntgroup.dto.TicketDto;
import com.rntgroup.model.Ticket;
import com.rntgroup.service.EventService;
import com.rntgroup.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class TicketMapper {

    @Autowired
    protected UserService userService;

    @Autowired
    protected EventService eventService;

    @Mapping(target = "event", expression = "java(eventService.findById(ticketDto.getEventId()))")
    @Mapping(target = "user", expression = "java(userService.findById(ticketDto.getUserId()))")
    public abstract Ticket toModel(TicketDto ticketDto);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "userId", source = "user.id")
    public abstract TicketDto toDto(Ticket ticket);

}
