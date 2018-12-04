package com.example.janez.datadisplay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by janez on 26/11/2017.
 */

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchAdapterViewHolder> {
    private JSONArray mMatchData;

    private final MatchAdapterClickHandler mClickHandler;

    public interface MatchAdapterClickHandler {
        void onClick(JSONObject matchData);
    }

    public MatchAdapter(MatchAdapterClickHandler handler) {
        mClickHandler = handler;
    }

    public class MatchAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mHomeTeam;
        public final TextView mAwayTeam;
        public final TextView mHomeTeamResult;
        public final TextView mAwayTeamResult;
        public final TextView mDate;

        public MatchAdapterViewHolder(View view) {
            super(view);
            mHomeTeam = view.findViewById(R.id.tv_hometeam);
            mHomeTeamResult = view.findViewById(R.id.tv_hometeam_result);
            mAwayTeam = view.findViewById(R.id.tv_awayteam);
            mAwayTeamResult = view.findViewById(R.id.tv_awayteam_result);
            mDate = view.findViewById(R.id.tv_date);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("ARR", "CLICKED!");
            try {
                int adapterPosition = getAdapterPosition();
                JSONObject matchJSON = mMatchData.getJSONObject(adapterPosition);
                mClickHandler.onClick(matchJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MatchAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.fixture_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttach = false;

        View view = inflater.inflate(layoutId, viewGroup, shouldAttach);
        return new MatchAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchAdapterViewHolder holder, int position) {
        try {
            JSONObject matchObj = mMatchData.getJSONObject(position);
            if (matchObj.getString("status").equals("IN_PLAY"))
                holder.mDate.setText("LIVE!");
            else
                holder.mDate.setText(matchObj.getString("date"));
            //Log.e("JSONOBJECT", matchObj.toString(4));
            if (matchObj.getString("homeTeamName").equals("Chelsea FC")) {
                holder.mHomeTeam.setText(matchObj.getString("homeTeamName"));
                holder.mAwayTeam.setText(matchObj.getString("awayTeamName"));
                holder.mHomeTeamResult.setText(matchObj.getJSONObject("result").getString("goalsHomeTeam"));
                holder.mAwayTeamResult.setText(matchObj.getJSONObject("result").getString("goalsAwayTeam"));
            } else {
                holder.mHomeTeam.setText(matchObj.getString("awayTeamName"));
                holder.mAwayTeam.setText(matchObj.getString("homeTeamName"));

                holder.mHomeTeamResult.setText(matchObj.getJSONObject("result").getString("goalsAwayTeam"));
                holder.mAwayTeamResult.setText(matchObj.getJSONObject("result").getString("goalsHomeTeam"));
            }
        } catch (JSONException e) {
            holder.mHomeTeam.setText("@string/error");
            holder.mAwayTeam.setText("@string/error");
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mMatchData == null) return 0;
        return mMatchData.length();
    }

    public void setMatchData(JSONArray matchdata) {
        mMatchData = matchdata;
        notifyDataSetChanged();
    }
}
