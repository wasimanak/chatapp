package com.waseemshahzad.chatapp.ModelClass;

public class Messeges {
    String messege;
    String senderid;
    long timeStamp;

    public Messeges() {
    }

    public Messeges(String messege, String senderid, long timeStamp) {
        this.messege = messege;
        this.senderid = senderid;
        this.timeStamp = timeStamp;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
