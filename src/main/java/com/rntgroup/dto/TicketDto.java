package com.rntgroup.dto;

import com.rntgroup.enumerate.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@XmlRootElement
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketDto {

    UUID id;
    UUID eventId;
    UUID userId;
    Category category;
    Integer place;

}
