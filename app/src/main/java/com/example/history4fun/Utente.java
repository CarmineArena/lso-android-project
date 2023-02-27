package com.example.history4fun;

public class Utente {
    private String user_id = null;
    private String name = null;
    private String surname = null;
    private String email = null;
    private String password = null;
    private int age = 0;
    private String phone_number = null;
    private boolean expert = false;

    /* CONSTRUCTOR */
    public Utente() {}

    public Utente(String user_id, String name, String surname, String email,
                  int age, String phone_number, boolean expert) {
        this.user_id = user_id;
        this.name    = name;
        this.surname = surname;
        this.email   = email;

        this.age          = age;
        this.phone_number = phone_number;
        this.expert       = expert;
    }

    /* GETTERS AND SETTERS */
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isExpert() {
        return expert;
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    /* METHODS */
}
