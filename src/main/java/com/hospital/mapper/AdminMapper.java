package com.hospital.mapper;

import com.hospital.entity.Admin;
import com.hospital.requestDto.AdminRegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMapper {

//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "email", target = "email")
//    @Mapping(target = "password",ignore = true)
//    @Mapping(source = "", target = "")
//    @Mapping(source = "", target = "")
//    Admin toEntity(AdminRegisterRequest dto);
}
