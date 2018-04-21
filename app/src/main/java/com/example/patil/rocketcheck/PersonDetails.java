package com.example.patil.rocketcheck;

/**
 * Created by patil on 11-02-2018.
 */

/**
 * Created by patil on 11-02-2018.
 */




public class PersonDetails {
    private String txnId,name,phoneNo,emailId,gender,status,checkInStatus;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNo() {

        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {

        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTxnId() {

        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }
}
