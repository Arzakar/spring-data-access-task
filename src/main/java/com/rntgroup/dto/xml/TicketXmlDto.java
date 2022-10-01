package com.rntgroup.dto.xml;

import com.rntgroup.enumerate.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketXmlDto {

    Long id;
    long eventId;
    long userId;
    Category category;
    int place;

}
