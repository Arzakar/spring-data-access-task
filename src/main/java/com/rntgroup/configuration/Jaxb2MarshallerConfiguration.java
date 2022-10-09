package com.rntgroup.configuration;

import com.rntgroup.dto.DataDumpDto;
import com.rntgroup.dto.EventDto;
import com.rntgroup.dto.TicketDto;
import com.rntgroup.dto.UserAccountDto;
import com.rntgroup.dto.UserAccountDtoList;
import com.rntgroup.dto.UserDto;
import com.rntgroup.dto.EventDtoList;
import com.rntgroup.dto.TicketDtoList;
import com.rntgroup.dto.UserDtoList;
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
                EventDto.class,
                EventDtoList.class,
                TicketDto.class,
                TicketDtoList.class,
                UserDto.class,
                UserDtoList.class,
                UserAccountDto.class,
                UserAccountDtoList.class,
                DataDumpDto.class,
                Category.class
        );
        return jaxb2Marshaller;
    }
}
