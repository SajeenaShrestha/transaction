package com.demo.induction.tp.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {
    private String type;
    private BigDecimal amount;
    private String narration;
}
