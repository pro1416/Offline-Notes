package com.pravesh.offlinenotes.model;

public class Note {
    private int id;
    private String title, subtitle, content;

    public Note(int id, String title, String subtitle, String content) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
    }

    public Note(String title, String subtitle, String content) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
