package com.example.artworksharingplatform.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.artworksharingplatform.entity.User;
import com.example.artworksharingplatform.model.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.emailAddress", target = "emailAddress")
    @Mapping(source = "user.telephone", target = "telephone")
    @Mapping(source = "user.imagePath", target = "imagePath")
    @Mapping(source = "user.pass", target = "password")
    @Mapping(source = "user.status", target = "status")
    @Mapping(expression = "java(user.getRole().name())", target = "roleName")
    UserDTO toUserDTO(User user);

    List<UserDTO> toList(List<User> userList);
}
