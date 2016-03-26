package com.cyberthieves.complaintapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cyberthieves.complaintapp.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//This is a fragment activity to display the details of all the unresolved complaints
public class Unresolved extends Fragment{

    static private final String TAG = "Complaints-App";
    public String hostel;
    public int item_id;
    public int person_id;
    public int type;
    private TextView no_comp;

    public Unresolved() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view;
        view = inflater.inflate(R.layout.complaints_list, container, false);

        if(item_id==1) {
            no_comp = (TextView) view.findViewById(R.id.no_complaints);
            no_comp.setVisibility(View.GONE);
            final ArrayList<JSONObject> complaints_list = new ArrayList<>();
            // sending json request to get a list of all the complaints related to the user's hostel
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = getString(R.string.domain)+"/complaint/default/prev_indi_complaints_by_date.json/individual/"+person_id;
            Log.d(TAG, "url:  " + url);
            StringRequest myReq = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //parsing the json response from the request
                                JSONObject response1 = new JSONObject(response);

                                JSONArray rows = response1.getJSONArray("rows");
                                JSONArray complaints = new JSONArray();

                                for(int i=0; i< rows.length(); i++) {
                                    JSONObject complaint = rows.getJSONObject(i);
                                    JSONObject temp = new JSONObject();
                                    boolean resolve_status = complaint.getBoolean("resolve_status");
                                    if(resolve_status)
                                        continue;
                                    String title = complaint.getString("title");
                                    temp.put("title", title);
                                    int complaint_id = complaint.getInt("id");
                                    temp.put("complaint_id", complaint_id);
                                    String created_on = complaint.getString("created_on");
                                    temp.put("author", "Posted On "+created_on);
                                    String votes = complaint.getString("votes");
                                    temp.put("votes", votes);
                                    temp.put("resolve_status", resolve_status);

                                    complaints.put(temp);

                                }

                                Log.d(TAG, "complaints:   " + complaints.toString());

                                if(complaints.length()==0)
                                    no_comp.setVisibility(View.VISIBLE);
                                else
                                    no_comp.setVisibility(View.GONE);

                                for (int j = 0; j < complaints.length(); j++)
                                    complaints_list.add(complaints.getJSONObject(j));

                                //exception handling
                            } catch (JSONException ex) {
                                Log.e(TAG, "Cannot parse response!!");
                            }

                            ListView listV = (ListView) view.findViewById(R.id.notifiList);
                            ListAdapter adapter = new ListAdapter(getContext(), R.layout.fragment_all_complaints, R.id.serialno, complaints_list);
                            listV.setAdapter(adapter);

                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    // hide the progress dialog
                    Snackbar.make(view, "Unable to Access Server!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            Log.d(TAG, "before queue");
            queue.add(myReq);
            Log.d(TAG, "after queue");
        }
        else if(item_id==2) {
            no_comp = (TextView) view.findViewById(R.id.no_complaints);
            no_comp.setVisibility(View.GONE);
            final ArrayList<JSONObject> complaints_list = new ArrayList<>();
            // sending json request to get a list of all the complaints related to the user's hostel
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = getString(R.string.domain)+"/complaint/default/prev_hostel_complaints_by_date.json/hostel/"+hostel;
            Log.d(TAG, "url:  "+url);
            StringRequest myReq = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //parsing the json response from the request
                                JSONObject response1 = new JSONObject(response);

                                JSONArray rows = response1.getJSONArray("rows");
                                JSONArray maker = response1.getJSONArray("maker");
                                JSONArray complaints = new JSONArray();

                                for(int i=0; i< rows.length(); i++) {
                                    JSONObject complaint = rows.getJSONObject(i);
                                    String  posted_by = maker.getString(i);
                                    JSONObject temp = new JSONObject();
                                    boolean resolve_status = complaint.getBoolean("resolve_status");
                                    if(resolve_status)
                                        continue;
                                    String title = complaint.getString("title");
                                    temp.put("title", title);
                                    int complaint_id = complaint.getInt("id");
                                    temp.put("complaint_id", complaint_id);
                                    String created_on = complaint.getString("created_on");
                                    temp.put("author", "Posted by "+posted_by+" On "+created_on);
                                    String votes = complaint.getString("votes");
                                    temp.put("votes", votes);
                                    temp.put("resolve_status", resolve_status);

                                    complaints.put(temp);

                                }

                                Log.d(TAG, "complaints:   " + complaints.toString());

                                if(complaints.length()==0)
                                    no_comp.setVisibility(View.VISIBLE);
                                else
                                    no_comp.setVisibility(View.GONE);

                                for (int j = 0; j < complaints.length(); j++)
                                    complaints_list.add(complaints.getJSONObject(j));

                                //exception handling
                            } catch (JSONException ex) {
                                Log.e(TAG, "Cannot parse response!!");
                            }

                            ListView listV = (ListView) view.findViewById(R.id.notifiList);
                            ListAdapter adapter = new ListAdapter(getContext(), R.layout.fragment_all_complaints, R.id.serialno, complaints_list);
                            listV.setAdapter(adapter);

                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    // hide the progress dialog
                    Snackbar.make(view, "Unable to Access Server!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            Log.d(TAG, "before queue");
            queue.add(myReq);
            Log.d(TAG, "after queue");

        }
        else if(item_id==3) {
            no_comp = (TextView) view.findViewById(R.id.no_complaints);
            no_comp.setVisibility(View.GONE);
            final ArrayList<JSONObject> complaints_list = new ArrayList<>();
            // sending json request to get a list of all the complaints related to the user's hostel
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = getString(R.string.domain)+"/complaint/default/prev_institute_complaints_by_date.json/institute";
            Log.d(TAG, "url:  "+url);
            StringRequest myReq = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //parsing the json response from the request
                                JSONObject response1 = new JSONObject(response);

                                JSONArray rows = response1.getJSONArray("rows");
                                JSONArray maker = response1.getJSONArray("maker");
                                JSONArray complaints = new JSONArray();

                                for(int i=0; i< rows.length(); i++) {
                                    JSONObject complaint = rows.getJSONObject(i);
                                    String  posted_by = maker.getString(i);
                                    JSONObject temp = new JSONObject();
                                    boolean resolve_status = complaint.getBoolean("resolve_status");
                                    if(resolve_status) {
                                        continue;
                                    }
                                    String title = complaint.getString("title");
                                    temp.put("title", title);
                                    int complaint_id = complaint.getInt("id");
                                    temp.put("complaint_id", complaint_id);
                                    String created_on = complaint.getString("created_on");
                                    temp.put("author", "Posted by "+posted_by+" On "+created_on);
                                    String votes = complaint.getString("votes");
                                    temp.put("votes", votes);
                                    temp.put("resolve_status", resolve_status);

                                    complaints.put(temp);

                                }

                                Log.d(TAG, "complaints:   " + complaints.toString());

                                if(complaints.length()==0)
                                    no_comp.setVisibility(View.VISIBLE);
                                else
                                    no_comp.setVisibility(View.GONE);

                                for (int j = 0; j < complaints.length(); j++)
                                    complaints_list.add(complaints.getJSONObject(j));

                                //exception handling
                            } catch (JSONException ex) {
                                Log.e(TAG, "Cannot parse response!!");
                            }

                            ListView listV = (ListView) view.findViewById(R.id.notifiList);
                            ListAdapter adapter = new ListAdapter(getContext(), R.layout.fragment_all_complaints, R.id.serialno, complaints_list);
                            listV.setAdapter(adapter);

                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    // hide the progress dialog
                    Snackbar.make(view, "Unable to Access Server!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            Log.d(TAG, "before queue");
            queue.add(myReq);
            Log.d(TAG, "after queue");

        }


        return  view;
    }
}
