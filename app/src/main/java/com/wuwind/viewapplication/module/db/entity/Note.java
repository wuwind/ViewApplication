package com.wuwind.viewapplication.module.db.entity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "NOTE".
 */
public class Note {

    private Long id;
    /** Not-null value. */
    private String text;

    public Note() {
    }

    public Note(Long id) {
        this.id = id;
    }

    public Note(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getText() {
        return text;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setText(String text) {
        this.text = text;
    }

}
