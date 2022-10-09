package com.rntgroup.mapper;

import com.rntgroup.dto.EventDto;
import com.rntgroup.model.Event;
import org.mapstruct.Mapper;

@Mapper
public abstract class EventMapper {

    public abstract Event toModel(EventDto eventDto);

    public abstract EventDto toDto(Event event);

}
