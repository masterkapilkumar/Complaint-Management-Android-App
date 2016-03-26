package com.cyberthieves.complaintapp;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddUser extends AppCompatActivity{

    private Toolbar toolbar;
    static private final String TAG = "Complaints-App";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);
        pDialog = new ProgressDialog(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button submit = (Button) findViewById(R.id.add_usr_btn);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText first_namee = (EditText) findViewById(R.id.first_name);
                EditText last_namee = (EditText) findViewById(R.id.last_name);
                EditText email_ide = (EditText) findViewById(R.id.email_id);
                EditText entry_noe = (EditText) findViewById(R.id.entry_num);
                EditText usernamee = (EditText) findViewById(R.id.user_name);
                EditText passworde = (EditText) findViewById(R.id.password);
                EditText confirm_passworde = (EditText) findViewById(R.id.confirm_password);

                String first_name="";
                String last_name="";
                String email_id="";
                String entry_no="";
                String username="";
                String password="";
                String confirm_password="";

                final View view1 = findViewById(android.R.id.content);
                AlertDialog.Builder alert = new AlertDialog.Builder(AddUser.this);

                if (first_name.equals("") || last_name.equals("") || email_id.equals("") || entry_no.equals("") || username.equals("") || password.equals("") || confirm_password.equals("")) {
                    alert.setTitle("Error");
                    final String MessageToSubmit = "Please Fill all the details...";
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
                    /*RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    final String url = getString(R.string.domain) + "/complaint/default/register.json?complaint_type=" + pos + "&title=" + title + "&description=" + description + "&hostel=" + hostel + "&category_name=" + type;
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
                                        boolean Success = response1.getBoolean("success");

                                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                                        if (Success) {
                                            alert.setTitle("Success");

                                            final String MessageToSubmit = "Complaint Posted.";
                                            alert.setMessage(MessageToSubmit);

                                            alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    dialog.cancel();
                                                }
                                            });
                                            alert.show();
                                        } else {
                                            //alertdialog if the credentials are invalid

                                            alert.setTitle("Faliure");

                                            final String MessageToSubmit = "Complaint Not Posted.";
                                            alert.setMessage(MessageToSubmit);

                                            alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    dialog.cancel();
                                                }
                                            });
                                            alert.show();
                                        }

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
                            Snackbar.make(view1, "Unable to Access Server!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                    queue.add(myReq);*/
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

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
