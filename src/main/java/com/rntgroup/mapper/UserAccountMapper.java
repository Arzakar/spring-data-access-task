package com.rntgroup.mapper;

import com.rntgroup.dto.UserAccountDto;
import com.rntgroup.model.UserAccount;
import com.rntgroup.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class UserAccountMapper {

    @Autowired
    protected UserService userService;

    @Mapping(target = "user", expression = "java(userService.findById(userDto.getUserId()))")
    public abstract UserAccount toModel(UserAccountDto userDto);

    @Mapping(target = "userId", source = "user.id")
    public abstract UserAccountDto toDto(UserAccount user);

}
