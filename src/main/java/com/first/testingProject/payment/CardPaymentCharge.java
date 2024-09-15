package com.first.testingProject.payment;

public class CardPaymentCharge {

    private  final boolean isCardDebited;

    @Override
    public String toString() {
        return "CardPaymentCharge{" +
                "isCardDebited=" + isCardDebited +
                '}';
    }



    public boolean isCardDebited() {
        return isCardDebited;
    }


    public CardPaymentCharge(boolean isCardDebited) {
        this.isCardDebited = isCardDebited;
    }




}
