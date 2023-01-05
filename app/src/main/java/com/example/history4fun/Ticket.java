package com.example.history4fun;

import java.util.ArrayList;

public class Ticket {
    private String ticket_id = null;
    private ArrayList<Utente> users = null;

    // TODO: MODIFICARE IL TIPO QUESTO ATTRIBUTO CON UNA CLASSE "DATA"
    private String date = null;

    private int followers = 0;
    private float cost = 0.0f;
    private TicketType type = TicketType.GUEST;
    private String license = null;
    private MuseumArea area = MuseumArea.FULL;
    private boolean payed = false;

    /* CONSTRUCTOR */
    public Ticket() {
        // Fai qualcosa ...
    }

    /* GETTERS AND SETTERS */
    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public ArrayList<Utente> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Utente> users) {
        this.users = users;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public MuseumArea getArea() {
        return area;
    }

    public void setArea(MuseumArea area) {
        this.area = area;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    /* METHODS */
}