package com.example.history4fun;

import java.util.ArrayList;

public class Visitare {
    private ArrayList<Utente> users = null;
    private ArrayList<Artefatto> artifacts = null;

    /* CONSTRUCTOR */
    public Visitare() {
        // Fai qualcosa ...
    }

    /* GETTERS AND SETTERS */
    public ArrayList<Utente> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Utente> users) {
        this.users = users;
    }

    public ArrayList<Artefatto> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(ArrayList<Artefatto> artifacts) {
        this.artifacts = artifacts;
    }

    /* METHODS */
}
