package com.example.android.booklisting;

import static android.R.attr.author;

/**
 * Created by DELL on 16-02-2017.
 */

public class Books {

    private String mTitle;
    private String mAuthor;
    private String mUrl;
    private int mSNo;

    Books(String title,String author,String url){
        mTitle = title;
        mAuthor = author;
        mUrl = url;
    }

    Books(int sNo,String title,String author,String url){
        mSNo = sNo;
        mTitle = title;
        mAuthor = author;
        mUrl = url;
    }

    public String getSno() {
        String temp = "";
        temp += mSNo;
        return temp;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

}
