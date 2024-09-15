package com.first.testingProject.payment;

import java.math.BigDecimal;

public interface CardPaymentCharger {

    CardPaymentCharge chargeCard (
            String cardSource,
            BigDecimal amount,
            Currency currency,
            String description

    );

}
