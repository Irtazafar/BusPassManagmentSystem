package com.example.finalproject;

import android.graphics.Bitmap;

public class PassClass {
    public String getRollNum() {
        return rollNum;
    }

    public void setRollNum(String rollNum) {
        this.rollNum = rollNum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public PassClass(String rollNum, String reason, String parentNo, String dateTime, Boolean status) {
        this.rollNum = rollNum;
        this.reason = reason;
        this.parentNo = parentNo;
        this.dateTime = dateTime;
        this.status = status;
    }

    String rollNum,reason,parentNo,dateTime;
    Boolean status;

    public PassClass(String rollNum, String reason, String parentNo, String dateTime, Boolean status, Bitmap qrCodeImage) {
        this.rollNum = rollNum;
        this.reason = reason;
        this.parentNo = parentNo;
        this.dateTime = dateTime;
        this.status = status;
        this.qrCodeImage = qrCodeImage;
    }

    Bitmap qrCodeImage;

    public Bitmap getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(Bitmap qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

}
