package com.example.history4fun;

import androidx.annotation.NonNull;

public enum MuseumArea {
    full, jurassic, prehistory, egypt, roman, greek;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case full:
                return "full";
            case jurassic:
                return "jurassic";
            case prehistory:
                return "prehistory";
            case egypt:
                return "egypt";
            case roman:
                return "roman";
            case greek:
                return "greek";
            default:
                throw new IllegalArgumentException();
        }
    }
}