package com.cyberthieves.complaintapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<JSONObject> {

    static private final String TAG = "Complaints-App";
    int num;
    ArrayList<JSONObject> complaints;
    Context context;

    public ListAdapter(Context context, int num, int id, ArrayList<JSONObject> complaints){
        super(context, num, id, complaints);
        this.num = num;
        this.context = context;
        this.complaints = complaints;
    }

    public View getView(final int position, View converView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(num,parent,false);
        TextView title = (TextView)itemView.findViewById(R.id.title);
        TextView author = (TextView)itemView.findViewById(R.id.author);
        Button like = (Button) itemView.findViewById(R.id.like);
        Button dislike = (Button) itemView.findViewById(R.id.dislike);
        TextView votes = (TextView) itemView.findViewById(R.id.likes);
        Switch resolve = (Switch) itemView.findViewById(R.id.resolve);
        LinearLayout complaint_details = (LinearLayout) itemView.findViewById(R.id.complaint_details);

        boolean resolve_status;

        final int complaint_id;

        try {

            title.setText(complaints.get(position).getString("title"));

            author.setText(complaints.get(position).getString("author"));

            votes.setText(complaints.get(position).getString("votes"));

            resolve_status = complaints.get(position).getBoolean("resolve_status");

            complaint_id = complaints.get(position).getInt("complaint_id");

            complaint_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show_complaint(v, complaint_id);
                }
            });

            if(resolve_status) {
                resolve.setChecked(true);
                resolve.setClickable(false);
            }
            else {
                resolve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disable(v, complaint_id);
                    }
                });
            }


            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeclick(itemView, complaint_id);
                }
            });

            dislike.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dislikeclick(itemView, complaint_id);
                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return itemView;
    }

    public void likeclick(View view, int complaint_id) {
        Button like = (Button) view.findViewById(R.id.like);
        Button dislike = (Button) view.findViewById(R.id.dislike);
        dislike.setBackground(ResourcesCompat.getDrawable(view.getResources(), R.drawable.betaunselected, null));
        like.setBackground(ResourcesCompat.getDrawable(view.getResources(), R.drawable.alpha, null));

        final View view1 = view;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = view.getResources().getString(R.string.domain)+"/complaint/default/voting.json/"+complaint_id+"/up";
        Log.i(TAG, url);
        StringRequest myReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TextView likes = (TextView) view1.findViewById(R.id.likes);
                        likes.setText(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog
                Snackbar.make(view1, "Unable to Access Server!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        queue.add(myReq);
    }

    public void dislikeclick(View view, int complaint_id) {
        Button dislike = (Button) view.findViewById(R.id.dislike);
        Button like = (Button) view.findViewById(R.id.like);
        like.setBackground(ResourcesCompat.getDrawable(view.getResources(), R.drawable.alphaunselected, null));
        dislike.setBackground(ResourcesCompat.getDrawable(view.getResources(), R.drawable.beta, null));

        final View view1 = view;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = view.getResources().getString(R.string.domain)+"/complaint/default/voting.json/"+complaint_id+"/down";
        Log.i(TAG, url);
        StringRequest myReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TextView likes = (TextView) view1.findViewById(R.id.likes);
                        likes.setText(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog
                Snackbar.make(view1, "Unable to Access Server!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        queue.add(myReq);
    }

    public void disable(View view, int complaint_id) {
        Switch resolve = (Switch) view.findViewById(R.id.resolve);
        final View view1 = view;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = view.getResources().getString(R.string.domain)+"/complaint/default/mark_as_resolved.json?com_id="+complaint_id;
        Log.i(TAG, url);
        StringRequest myReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog
                Snackbar.make(view1, "Unable to Access Server!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        queue.add(myReq);
        resolve.setClickable(false);
    }

    public void show_complaint(View view, int complaint_id) {
        Intent intent = new Intent(getContext(),ViewComplaint.class);

        intent.putExtra("complaint_id", complaint_id);
        context.startActivity(intent);
        ((Activity) getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}
