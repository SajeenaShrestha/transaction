package com.demo.induction.tp.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionTypeConstant {

    CREDIT("C"),
    DEBIT("D");

    private final String name;

}
