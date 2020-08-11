package com.company;

/**
 * @author Aryan Puri
 * https://github.com/Puriaryan/finalExam.git
 */

public class LoanP {
    // decalring variables
    private String clientno;
    private String clientName;
    private double loanAmt;
    private int noOfYears;
    private String loanType;

    // constructor method
    public LoanP(String clientno, String clientName, double loanAmt, int noOfYears,
                   String loanType){
        this.clientno = clientno;
        this.clientName = clientName;
        this.loanAmt = loanAmt;
        this.noOfYears = noOfYears;
        this.loanType = loanType;
    }

    public LoanP(){

    }


    // getter setter methods
    public String getClientno() {
        return clientno;
    }

    public void setClientno(String clientno) {
        this.clientno = clientno;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public double getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(double loanAmt) {
        this.loanAmt = loanAmt;
    }

    public int getNoOfYears() {
        return noOfYears;
    }

    public void setNoOfYears(int noOfYears) {
        this.noOfYears = noOfYears;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
}
