package com.rntgroup.mapper;

import com.rntgroup.dto.EventDto;
import com.rntgroup.model.Event;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {

    Event toModel(EventDto eventDto);

    EventDto toDto(Event event);

}
