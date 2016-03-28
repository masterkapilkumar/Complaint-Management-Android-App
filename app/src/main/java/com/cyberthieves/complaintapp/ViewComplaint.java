package com.cyberthieves.complaintapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
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
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ViewComplaint extends AppCompatActivity {

    static private final String TAG = "Complaints-App";
    private int complaint_id;
    private ProgressDialog pDialog;
    private Toolbar toolbar;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_complaint);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Complaint Details");

        complaint_id = getIntent().getExtras().getInt("complaint_id");
        pDialog = new ProgressDialog(this);

        final TextView no_comment = (TextView) findViewById(R.id.no_comments);
        no_comment.setVisibility(View.GONE);

        final View view = findViewById(android.R.id.content);
        final ArrayList<JSONObject> comments_list = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getString(R.string.domain)+"/complaint/default/view_complaint.json/"+complaint_id;
        Log.d(TAG, "url:  " + url);
        StringRequest myReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //parsing the json response from the request
                            JSONObject response1 = new JSONObject(response);

                            JSONObject complaint = response1.getJSONObject("complaint");
                            JSONArray comments = response1.getJSONArray("comments");
                            JSONArray comm_makers = response1.getJSONArray("makers");
                            JSONArray comms = new JSONArray();

                            TextView title = (TextView) findViewById(R.id.comp_title);
                            title.setText("Title: "+complaint.getString("title"));
                            TextView date = (TextView) findViewById(R.id.comp_date);
                            date.setText("Posted On: "+complaint.getString("created_on"));
                            TextView details = (TextView) findViewById(R.id.comp_details);
                            details.setText("Details:\n"+complaint.getString("details"));

                            Log.d(TAG, "comments:  "+comments+"     "+comments.length());
                            for (int i=0;i<comments.length();i++){
                                JSONObject comment=comments.getJSONObject(i);
                                JSONObject temp = new JSONObject();
                                String comment_details = comment.getString("details");
                                boolean resolve_status = complaint.getBoolean("resolve_status");

                                String posted_on = comment.getString("created_on");
                                String posted_by = comm_makers.getString(i);

                                temp.put("title", comment_details);
                                temp.put("author", "Posted by "+posted_by+" On "+posted_on);

                                comms.put(temp);
                            }

                            if(comms.length()==0)
                                no_comment.setVisibility(View.VISIBLE);
                            else
                                no_comment.setVisibility(View.GONE);

                            for (int j = 0; j < comms.length(); j++)
                                comments_list.add(comms.getJSONObject(j));

                            //exception handling
                        } catch (JSONException ex) {
                            Log.e(TAG, "Cannot parse response!!");
                        }

                        ListView listV = (ListView) findViewById(R.id.notifiList);
                        NotiListAdapter adapter = new NotiListAdapter(getApplicationContext(), R.layout.fragment_notifications, R.id.serialno, comments_list);
                        listV.setAdapter(adapter);

                        Log.d(TAG, "response in response listener: "+response);

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

        Button submit = (Button) findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                EditText post_comment = (EditText) findViewById(R.id.comment_edt);
                String post_cmnt;
                try {
                    post_cmnt = URLEncoder.encode(post_comment.getText().toString(), "UTF-8");
                }catch(UnsupportedEncodingException ex)
                {
                    post_cmnt="";
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewComplaint.this);
                if(post_cmnt.equals("")) {
                    alert.setTitle("Error");
                    final String MessageToSubmit = "Please enter something...";
                    alert.setMessage(MessageToSubmit);

                    alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
                else {
                    showpDialog();
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    final String url = getString(R.string.domain) + "/complaint/default/add_comment.json?complaint_id=" + complaint_id+"&details="+post_cmnt;
                    Log.d(TAG, url);
                    StringRequest myReq = new StringRequest(Request.Method.GET,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    cancelPDialog();
                                    Log.d(TAG, "Response: " + response);
                                    try {
                                        JSONObject response1 = new JSONObject(response);

                                        AlertDialog.Builder alert = new AlertDialog.Builder(ViewComplaint.this);
                                        alert.setTitle("Success");

                                        final String MessageToSubmit = "Comment Posted.";
                                        alert.setMessage(MessageToSubmit);

                                        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.cancel();
                                                onBackPressed();
                                            }
                                        });
                                        alert.show();

                                    } catch (JSONException ex) {
                                        Log.d(TAG, "Cannot Parse Response: " + response);
                                    }

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cancelPDialog();
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            // hide the progress dialog
                            cancelPDialog();
                            Snackbar.make(findViewById(android.R.id.content), "Unable to Access Server!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                    queue.add(myReq);
                }


            }
        });




    }
    //shows the progress dialog
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    //hides the progress dialog
    private void cancelPDialog() {
        if (pDialog.isShowing())
            pDialog.cancel();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}