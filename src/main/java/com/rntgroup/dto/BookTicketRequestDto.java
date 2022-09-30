package com.rntgroup.dto;

import com.rntgroup.model.Ticket;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookTicketRequestDto {

    long userId;
    long eventId;
    int place;
    Ticket.Category category;

}
