package com.example.android.bitcoinnews;

import java.io.Serializable;

public class Article implements Serializable {
    private String header;
    private String body;
    private String section;
    private String datePublished;


    private String author;

    public Article(String header, String body, String section, String datePublished, String author) {
        setHeader(header);
        setBody(body);
        setSection(section);
        setDatePublished(datePublished);
        setAuthor(author);
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDatePublished() {
        return datePublished;
    }

    private void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getSection() {
        return section;
    }

    private void setSection(String section) {
        this.section = section;
    }

    public String getHeader() {
        return header;
    }

    private void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    private void setBody(String body) {
        this.body = body;
    }
}

