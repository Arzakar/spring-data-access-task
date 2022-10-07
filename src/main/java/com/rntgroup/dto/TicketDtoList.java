package com.rntgroup.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketDtoList {

    @XmlElement(name = "ticketDto", type = TicketDto.class)
    List<TicketDto> tickets = new ArrayList<>();

}
