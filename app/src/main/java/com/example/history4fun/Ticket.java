package com.example.history4fun;

public class Ticket {
    private String ticket_id = null;
    private Utente user      = null; // Intestatario del biglietto
    private int followers    = 0;
    private String date      = null;
    private TicketType type  = TicketType.guest;
    private float cost       = 0.0f;
    private MuseumArea area = MuseumArea.full;

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

    public Utente getUser() {
        return user;
    }

    public void setUser(Utente user) {
        this.user = user;
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

    public MuseumArea getArea() {
        return area;
    }

    public void setArea(MuseumArea area) {
        this.area = area;
    }

    /* METHODS */
}