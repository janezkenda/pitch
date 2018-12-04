package com.example.janez.datadisplay;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MatchDetailActivity extends AppCompatActivity {

    private JSONObject matchJSON;
    private String matchString;
    private TextView mMatchJSONDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);

        mMatchJSONDisplay = (TextView) findViewById(R.id.tv_match_json);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra("matchJSON")) {
                try {
                    matchJSON = new JSONObject(intent.getStringExtra("matchJSON"));
                    matchString = matchJSON.getString("homeTeamName") + " vs " + matchJSON.getString("awayTeamName")
                            + "\n" + matchJSON.getJSONObject("result").getString("goalsHomeTeam")
                            + " : " + matchJSON.getJSONObject("result").getString("goalsAwayTeam");
                    mMatchJSONDisplay.setText(matchString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Intent shareIntent() {
        return ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(matchString)
                .getIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.match_detail, menu);
        MenuItem item = menu.findItem(R.id.action_share_match);
        item.setIntent(shareIntent());
        return true;
    }
}
