package com.example.siamsot.newsapp;

import java.net.URL;


public class News {
    private String mTitle;
    private String mUrl;
    private String mDate;
    private String mAuthor;
    private String mSection;

    public News(String title, String author, String section, String url, String date) {
        mTitle = title;
        mUrl = url;
        mDate = date;
        mAuthor = author;
        mSection = section;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getDate() {
        return mDate;
    }

    public String getAuthor() { return mAuthor; }

    public String getSection(){ return mSection;}
}
