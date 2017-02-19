package com.example.android.booklisting;

import static android.R.attr.author;

/**
 * Created by DELL on 16-02-2017.
 */

public class Books {

    private String mTitle;
    private String mAuthor;
    private String mUrl;

    Books(String title,String author,String url){
        mTitle = title;
        mAuthor = author;
        mUrl = url;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmUrl() {
        return mUrl;
    }

}
