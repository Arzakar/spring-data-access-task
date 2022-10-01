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
public class UserXmlDtoList {

    @XmlElement(name = "userXmlDto", type = UserXmlDto.class)
    List<UserXmlDto> users = new ArrayList<>();

}
