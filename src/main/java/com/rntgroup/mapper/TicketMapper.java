package com.rntgroup.mapper;

import com.rntgroup.dto.xml.TicketXmlDto;
import com.rntgroup.model.Ticket;
import org.mapstruct.Mapper;

@Mapper
public interface TicketMapper {

    Ticket toModel(TicketXmlDto ticketXmlDto);

}
