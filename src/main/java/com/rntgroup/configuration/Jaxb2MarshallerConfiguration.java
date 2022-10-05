package com.rntgroup.configuration;

import com.rntgroup.dto.xml.EventXmlDto;
import com.rntgroup.dto.xml.EventXmlDtoList;
import com.rntgroup.dto.xml.TicketXmlDto;
import com.rntgroup.dto.xml.TicketXmlDtoList;
import com.rntgroup.dto.xml.UserXmlDto;
import com.rntgroup.dto.xml.UserXmlDtoList;
import com.rntgroup.enumerate.Category;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Jaxb2MarshallerConfiguration {

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        final var jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(
                EventXmlDto.class,
                EventXmlDtoList.class,
                TicketXmlDto.class,
                TicketXmlDtoList.class,
                UserXmlDto.class,
                UserXmlDtoList.class,
                Category.class
        );
        return jaxb2Marshaller;
    }
}
