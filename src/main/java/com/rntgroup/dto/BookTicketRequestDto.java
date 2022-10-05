package com.rntgroup.dto;

import com.rntgroup.enumerate.Category;
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
    Category category;

}
