package com.rntgroup.mapper;

import com.rntgroup.dto.UserDto;
import com.rntgroup.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    User toModel(UserDto userDto);

    @Mapping(target = "userAccountId", source = "account.id")
    UserDto toDto(User user);

}
