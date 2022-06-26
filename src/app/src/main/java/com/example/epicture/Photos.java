package com.example.epicture;

public class Photos {

    private String title;
    private String link;

    public Photos(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }
}
