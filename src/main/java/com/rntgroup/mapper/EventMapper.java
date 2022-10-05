package com.rntgroup.mapper;

import com.rntgroup.dto.xml.EventXmlDto;
import com.rntgroup.model.Event;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {

    Event toModel(EventXmlDto eventXmlDto);

}
