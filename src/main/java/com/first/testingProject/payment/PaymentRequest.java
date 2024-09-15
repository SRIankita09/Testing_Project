package com.first.testingProject.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRequest {

    private  final  Payment payment;

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "payment=" + payment +
                '}';
    }



    public Payment getPayment() {
        return payment;
    }



    public PaymentRequest( @JsonProperty("payment") Payment payment) {
        this.payment = payment;
    }




}
