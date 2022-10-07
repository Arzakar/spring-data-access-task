package com.rntgroup.mapper;

import com.rntgroup.dto.UserDto;
import com.rntgroup.dto.xml.UserXmlDto;
import com.rntgroup.model.User;
import org.mapstruct.Mapper;

@Mapper
public abstract class UserMapper {

    public abstract User toModel(UserDto userDto);

    public abstract UserDto toDto(User user);

}
