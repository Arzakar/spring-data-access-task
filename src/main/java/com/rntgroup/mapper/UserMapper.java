package com.rntgroup.mapper;

import com.rntgroup.dto.xml.UserXmlDto;
import com.rntgroup.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User toModel(UserXmlDto userXmlDto);

}
