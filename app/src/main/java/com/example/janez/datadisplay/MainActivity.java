package com.example.janez.datadisplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MatchAdapter.MatchAdapterClickHandler, LoaderManager.LoaderCallbacks<JSONArray> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 37;
    private RecyclerView mRecyclerView;
    private MatchAdapter mMatchAdapter;
    private ProgressBar mProgressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMatchAdapter = new MatchAdapter(this);
        mRecyclerView.setAdapter(mMatchAdapter);
        mProgressBar = findViewById(R.id.pb_progressbar);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onClick(JSONObject matchJSON) {
        Intent detailIntent = new Intent(this, MatchDetailActivity.class);
        detailIntent.putExtra("matchJSON", matchJSON.toString());
        startActivity(detailIntent);
    }

    @Override
    public Loader<JSONArray> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<JSONArray>(this) {
            JSONArray matchData = null;

            @Override
            protected void onStartLoading() {
                if (matchData != null) {
                    deliverResult(matchData);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
                /*mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();*/
            }

            @Override
            public void deliverResult(JSONArray data) {
                matchData = data;
                super.deliverResult(data);
            }

            @Override
            public JSONArray loadInBackground() {
                URL requestURL = NetworkUtils.buildUrl();
                try {
                    String results = NetworkUtils.getData(requestURL);
                    JSONArray jsonResults = JSONUtils.parseData(results);
                    return jsonResults;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMatchAdapter.setMatchData(data);
    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            mMatchAdapter.setMatchData(null);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
