package com.example.test123;

public class Users {
    private String name;
    private String email;
    private String phone;
    private String uid;
    private String sec;
    private String profilepic;

    public Users(){

    }

    public Users(String name, String email, String phone, String uid, String sec, String profilepic) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.uid = uid;
        this.sec = sec;
        this.profilepic = profilepic;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() {
        return uid;
    }

    public String getSec() {
        return sec;
    }

    public String getProfilepic() {
        return profilepic;
    }
}
