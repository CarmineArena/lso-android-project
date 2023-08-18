package com.example.history4fun;

public enum TicketType {
    guest, family, group, expert, school;

    @Override
    public String toString() {
        switch (this) {
            case guest:
                return "guest";
            case family:
                return "family";
            case group:
                return "group";
            case expert:
                return "expert";
            case school:
                return "school";
            default:
                throw new IllegalArgumentException();
        }
    }
}