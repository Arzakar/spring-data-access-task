package com.rntgroup.dto;

import com.rntgroup.enumerate.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookTicketRequestDto {

    UUID userId;
    UUID eventId;
    int place;
    Category category;

}
