package com.rntgroup.dto.xml;

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
public class UserXmlDto {

    UUID id;
    String name;
    String email;

}
