package edu.cmu.juicymeeting.exception;

/**
 * Created by chenlinquan on 11/19/15.
 */
public class JuicyException extends Exception {
    private int errorNum;
    private String errorMessage;
    private String[] errorMessages = {"Must be exactly 4 digits!"};

    public JuicyException(int errorNum) {
        this.errorNum = errorNum;
        this.errorMessage = errorMessages[errorNum];
    }
}