package com.example.android.booklisting;

import static android.R.attr.author;

/**
 * Created by DELL on 16-02-2017.
 */

public class Books {

    private String mTitle;
    private String mSubtitle;
    private String mAuthor;

    Books(String title, String subtitle,String author){
        mTitle = title;
        mSubtitle = subtitle;
        mAuthor = author;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSubtitle() {
        return mSubtitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }
}
