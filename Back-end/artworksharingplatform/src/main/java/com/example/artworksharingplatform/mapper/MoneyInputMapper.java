package com.example.artworksharingplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.artworksharingplatform.entity.MoneyInput;
import com.example.artworksharingplatform.model.MoneyInputDTO;

@Mapper(componentModel = "spring")
public interface MoneyInputMapper {
	
	@Mapping(source =  "input.money" , target = "input_money")
	MoneyInputDTO toMoneyInputDTO(MoneyInput input);
}
