package com.company;

import java.text.DecimalFormat;

public class Personal extends LoanP implements Generate {

    // decalring variable
    double interestRate = 0.06;


    // constructor method
    // constructor method
    public Personal
    (String clientno, String clientName, double loanAmt, int noOfYears, String loanType) {
        super(clientno, clientName, loanAmt, noOfYears, loanType);
    }

    @Override
    public double[][] generateTable() {
        // varaiables
        double startVal = getLoanAmt();
        double endingValue = 0;
        double balance= startVal;
        double[][] monthlyPaymentArr = new double[getNoOfYears()*12][6];
        double monthlyRate= getNoOfYears() / 12;
        DecimalFormat df = new DecimalFormat("0.00");
        //calculation
        for (int i = 1; i <= getNoOfYears() * 12; i++) {
            double monthlyPayment = (startVal* monthlyRate)/(1-Math.pow(1+monthlyRate, -getNoOfYears()));
            monthlyPaymentArr[i - 1][0] = i;

            monthlyPaymentArr[i - 1][1] = Double.parseDouble(df.format(monthlyPayment-interestRate));
            ;
            monthlyPaymentArr[i - 1][2] = Double.parseDouble(df.format(balance*interestRate));
            monthlyPaymentArr[i - 1][3]  = Double.parseDouble(df.format(monthlyPayment));
            ;
            monthlyPaymentArr[i - 1][4] = Double.parseDouble(df.format(balance));
        }
        return monthlyPaymentArr;
    }
}
