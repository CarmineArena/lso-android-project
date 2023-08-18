package com.example.history4fun;

public class Artefatto {
    private String artifact_id = null;
    private String n_description = null;
    private String y_description = null;
    private String e_description = null;
    private MuseumArea area = MuseumArea.full;

    /* CONSTRUCTOR */
    public Artefatto() {
        // Fai qualcosa ...
    }

    /* GETTERS AND SETTERS */
    public String getArtifact_id() {
        return artifact_id;
    }

    public void setArtifact_id(String artifact_id) {
        this.artifact_id = artifact_id;
    }

    public String getN_description() {
        return n_description;
    }

    public void setN_description(String n_description) {
        this.n_description = n_description;
    }

    public String getY_description() {
        return y_description;
    }

    public void setY_description(String y_description) {
        this.y_description = y_description;
    }

    public String getE_description() {
        return e_description;
    }

    public void setE_description(String e_description) {
        this.e_description = e_description;
    }

    public MuseumArea getArea() {
        return area;
    }

    public void setArea(MuseumArea area) {
        this.area = area;
    }

    /* METHODS */
}