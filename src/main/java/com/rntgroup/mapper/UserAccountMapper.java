package com.rntgroup.mapper;

import com.rntgroup.dto.UserAccountDto;
import com.rntgroup.model.UserAccount;
import org.mapstruct.Mapper;

@Mapper
public abstract class UserAccountMapper {

    public abstract UserAccount toModel(UserAccountDto userDto);

    public abstract UserAccountDto toDto(UserAccount user);

}
