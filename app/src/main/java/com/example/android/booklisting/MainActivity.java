package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {

    private static final int BOOK_LOADER_ID = 1;

    private ProgressBar mprogress;

    private static String BOOKS_URL =
            "https://www.googleapis.com/books/v1/volumes?maxResults=20&q=";

    private BookAdapter mAdapter;

    private TextView mEmptyStateTextView;
    ConnectivityManager cm;
    ListView bookListView;

    String uriText;

    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchQuery = (EditText) findViewById(R.id.search_query);
        uriText = searchQuery.getText().toString();

        searchButton = (Button) findViewById(R.id.search);

        bookListView = (ListView) findViewById(R.id.list);

            mprogress=(ProgressBar)findViewById(R.id.progress);

            mAdapter = new BookAdapter(MainActivity.this, new ArrayList<Books>());

            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            bookListView.setEmptyView(mEmptyStateTextView);

            bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Books currentBook = mAdapter.getItem(position);

                    Uri bookUri = Uri.parse(currentBook.getUrl());

                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                    startActivity(websiteIntent);
                }
            });

        mprogress.setVisibility(GONE);
        mEmptyStateTextView.setText(R.string.search_query);

        cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEmptyStateTextView.setVisibility(View.INVISIBLE);
                    if (uriText != null) {
                        mprogress.setVisibility(View.VISIBLE);
                        bookListView.setAdapter(mAdapter);
                        LoaderManager loaderManager = getLoaderManager();
                        loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);

                    } else {
                        Toast.makeText(MainActivity.this, "Entry Search Query", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
                mEmptyStateTextView.setText(R.string.no_internet_connection);
                mprogress.setVisibility(GONE);
            }

    }

    @Override
    public Loader<List<Books>> onCreateLoader(int i, Bundle bundle) {
        EditText searchQuery = (EditText) findViewById(R.id.search_query);
        String uriText = searchQuery.getText().toString();
        BOOKS_URL += uriText;
        return new BooksLoader(this, BOOKS_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> bookses) {
        mAdapter.clear();
        if (bookses != null && !bookses.isEmpty()) {
            mAdapter.addAll(bookses);
        }

        mEmptyStateTextView.setText(R.string.no_earthquakes);

        View progress = findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        mAdapter.clear();
    }
}
