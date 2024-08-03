package com.example.artworksharingplatform.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.artworksharingplatform.entity.Transaction;
import com.example.artworksharingplatform.model.TransactionDTO;

@Mapper(componentModel = "spring", uses = MoneyInputMapper.class)
public interface TransactionMapper {

    @Mapping(source = "transaction.user.id", target = "userId")
    @Mapping(source = "transaction.totalMoney", target = "totalMoney")
    @Mapping(source = "transaction.id", target = "transactionId")
    @Mapping(source = "transaction.transactionDate", target = "transactionDate")
    @Mapping(source = "transaction.moneyInput", target = "input_money")
    @Mapping(expression = "java(transactionDescription(transaction))", target = "description")

    TransactionDTO toTransactionDTO(Transaction transaction);

    List<TransactionDTO> toList(List<Transaction> transactions);

    default String transactionDescription(Transaction transaction) {
        if (transaction.getOrders() != null) {
            return "Order";
        } else if (transaction.getPreOrders() != null) {
            return "PreOrder";
        } else if (transaction.getMoneyInput() != null) {
            return transaction.getMoneyInput().getDescription();
        }
        return "";
    }
}