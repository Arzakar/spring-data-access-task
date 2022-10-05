package com.rntgroup.util;

import com.rntgroup.dto.xml.UserXmlDto;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class MarshallerWrapperIntTest {

    @Autowired
    private MarshallerWrapper marshallerWrapper;

    @Test
    @DisplayName("Объект успешно переведён в XML")
    void shouldMarshallObject() {
        UserXmlDto user = new EasyRandom().nextObject(UserXmlDto.class);
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<userXmlDto>"
                + "<email>" + user.getEmail() + "</email>"
                + "<id>" + user.getId() + "</id>"
                + "<name>" + user.getName() + "</name>"
                + "</userXmlDto>";

        assertEquals(expectedXml, marshallerWrapper.marshall(user));
    }

    @Test
    @DisplayName("Объект успешно создан из XML")
    void shouldUnmarshallXml() {
        UserXmlDto user = new EasyRandom().nextObject(UserXmlDto.class);
        String userAsXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<userXmlDto>"
                + "<email>" + user.getEmail() + "</email>"
                + "<id>" + user.getId() + "</id>"
                + "<name>" + user.getName() + "</name>"
                + "</userXmlDto>";

        assertEquals(user, marshallerWrapper.unmarshall(new ByteArrayInputStream(userAsXml.getBytes(StandardCharsets.UTF_8)), UserXmlDto.class));
    }

}