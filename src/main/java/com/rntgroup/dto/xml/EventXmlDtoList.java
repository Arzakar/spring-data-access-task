package com.rntgroup.dto.xml;

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
public class EventXmlDtoList {

    @XmlElement(name = "eventXmlDto", type = EventXmlDto.class)
    List<EventXmlDto> events = new ArrayList<>();

}
