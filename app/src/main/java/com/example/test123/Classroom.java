package com.example.test123;

public class Classroom {
    private String classroom, owneremail, ownerid, allowjoin;

    public Classroom(){

    }

    public Classroom(String classroom, String owneremail, String ownerid, String allowjoin) {
        this.classroom = classroom;
        this.owneremail = owneremail;
        this.ownerid = ownerid;
        this.allowjoin = allowjoin;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getOwneremail() {
        return owneremail;
    }

    public void setOwneremail(String owneremail) {
        this.owneremail = owneremail;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getAllowjoin() {
        return allowjoin;
    }

    public void setAllowjoin(String allowjoin) {
        this.allowjoin = allowjoin;
    }
}
