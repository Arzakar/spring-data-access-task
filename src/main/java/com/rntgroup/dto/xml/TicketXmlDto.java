package com.rntgroup.dto.xml;

import com.rntgroup.enumerate.Category;
import com.rntgroup.model.Event;
import com.rntgroup.model.User;
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
public class TicketXmlDto {

    UUID id;
    Event event;
    User user;
    Category category;
    int place;

}
