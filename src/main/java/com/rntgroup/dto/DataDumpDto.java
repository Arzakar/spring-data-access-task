package com.rntgroup.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataDumpDto {

    EventDtoList eventDtoList;
    UserDtoList userDtoList;
    UserAccountDtoList userAccountDtoList;
    TicketDtoList ticketDtoList;

}
