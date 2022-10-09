package com.rntgroup.mapper;

import com.rntgroup.dto.TicketDto;
import com.rntgroup.model.Ticket;
import org.mapstruct.Mapper;

@Mapper
public abstract class TicketMapper {

    public abstract Ticket toModel(TicketDto ticketDto);

    public abstract TicketDto toDto(Ticket ticket);

}
