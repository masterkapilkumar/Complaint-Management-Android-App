package com.cyberthieves.complaintapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    static private final String TAG = "Complaint-App";
    private Toolbar toolbar;
    private static int current_id=R.id.notifications;;
    private TextView mNotifications;
    private ListView listVw;
    private RelativeLayout NewComplaint;
    private Menu menu;
    private Menu menu2;
    private int a=1;
    private Toast toast =null;
    private ProgressDialog pDialog;
    private int pos=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Complaint App");
        pDialog = new ProgressDialog(this);

        mNotifications = (TextView) findViewById(R.id.no_notifi);
        listVw = (ListView) findViewById(R.id.notifiList);
        NewComplaint = (RelativeLayout) findViewById(R.id.post_new_complaint);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu=navigationView.getMenu().getItem(2).getSubMenu();
        menu2=navigationView.getMenu();

        MenuItem new_user = menu2.findItem(R.id.add_user);

        final View view1 = findViewById(android.R.id.content);

        //get the nevigation menu object and set the name of the user
        View en1 = navigationView.getHeaderView(0);
        TextView text = (TextView) en1.findViewById(R.id.currentUser);
        String name = getIntent().getExtras().getString("firstname") + " " + getIntent().getExtras().getString("lastname");
        text.setText(name);
        onNavigationItemSelected(menu2.findItem(current_id));

        int type=getIntent().getExtras().getInt("type");
        String hostel=getIntent().getExtras().getString("hostel");

        //set the items in the navigation menu according to the user type
        if(type==1)
        {
            new_user.setVisible(false);
            Log.d(TAG,type+"   "+hostel+"    Added 3 items\n");
            MenuItem item1 = menu.add(1, 1, Menu.NONE, "Individual Level");
            MenuItem item2 = menu.add(1, 2, Menu.NONE, "Hostel Level");
            MenuItem item3 = menu.add(1, 3, Menu.NONE, "Institute Level");
            item1.setIcon(getResources().getDrawable(R.drawable.ic_menu_send,getApplicationContext().getTheme()));
            item2.setIcon(getResources().getDrawable(R.drawable.ic_menu_send,getApplicationContext().getTheme()));
            item3.setIcon(getResources().getDrawable(R.drawable.ic_menu_send,getApplicationContext().getTheme()));

        }
        else if(type==2 && (!hostel.equals("null"))) {
            new_user.setVisible(true);
            Log.d(TAG, type + "   " + hostel + "    Added 3 items\n");
            MenuItem item1 = menu.add(1, 1, Menu.NONE, "Individual Level");
            MenuItem item2 = menu.add(1, 2, Menu.NONE, "Hostel Level");
            MenuItem item3 = menu.add(1, 3, Menu.NONE, "Institute Level");
            item1.setIcon(getResources().getDrawable(R.drawable.ic_menu_send,getApplicationContext().getTheme()));
            item2.setIcon(getResources().getDrawable(R.drawable.ic_menu_send,getApplicationContext().getTheme()));
            item3.setIcon(getResources().getDrawable(R.drawable.ic_menu_send,getApplicationContext().getTheme()));
        }
        else
        {
            new_user.setVisible(false);
            MenuItem item1 = menu.add(1, 1, Menu.NONE, "Individual Level");
            MenuItem item3 = menu.add(1, 3, Menu.NONE, "Institute Level");
            item1.setIcon(getResources().getDrawable(R.drawable.ic_menu_send,getApplicationContext().getTheme()));
            item3.setIcon(getResources().getDrawable(R.drawable.ic_menu_send, getApplicationContext().getTheme()));
        }

    }

    //action to be performed when the user presses back button
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //this function decides what to do when the user selects an item from the navigation menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //if the user selects the "Notifications" item the display notifications
        if (id == R.id.notifications) {
            showpDialog();
            current_id=id;
            toolbar.setTitle("Notifications");

            NewComplaint.setVisibility(View.GONE);
            mNotifications.setVisibility(View.VISIBLE);
            listVw.setVisibility(View.VISIBLE);

            final View view = findViewById(android.R.id.content);
            final ArrayList<JSONObject> notifications_list = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getString(R.string.domain)+"/complaint/default/view_notifications.json?user_id="+getIntent().getExtras().getInt("person_id")+"&hostel="+getIntent().getExtras().getString("hostel");
            Log.d(TAG, "url:  " + url);
            StringRequest myReq = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cancelPDialog();
                            try {
                                //parsing the json response from the request
                                JSONObject response1 = new JSONObject(response);

                                JSONArray indi_notis = response1.getJSONArray("indi_noti");
                                JSONArray hostel_notis = response1.getJSONArray("hostel_noti");
                                JSONArray insti_notis = response1.getJSONArray("insti_noti");

                                JSONArray notifis = new JSONArray();

                                if (indi_notis.length()==0 && hostel_notis.length()==0 && insti_notis.length()==0){
                                    mNotifications.setText("No Notification to show");
                                }
                                else {
                                    for (int i=0;i<insti_notis.length();i++){
                                        Log.d(TAG,"insti notis: ");
                                        JSONObject insti_noti=insti_notis.getJSONObject(i);
                                        String ins_not_details = insti_noti.getString("details");
                                        String ins_not_posted_on = insti_noti.getString("created_on");
                                        JSONObject temp = new JSONObject();
                                        temp.put("title", ins_not_details);
                                        temp.put("author", "Posted On "+ins_not_posted_on);
                                        notifis.put(temp);
                                    }

                                    for (int i=0;i<hostel_notis.length();i++){
                                        Log.d(TAG,"hostel notis: ");
                                        JSONObject hostel_noti=hostel_notis.getJSONObject(i);
                                        String hos_not_details = hostel_noti.getString("details");
                                        String hos_not_posted_on = hostel_noti.getString("created_on");
                                        JSONObject temp = new JSONObject();
                                        temp.put("title", hos_not_details);
                                        temp.put("author", "Posted On "+hos_not_posted_on);
                                        notifis.put(temp);
                                    }

                                    for (int i=0;i<indi_notis.length();i++){
                                        Log.d(TAG,"indi notis: ");
                                        JSONObject indi_noti=indi_notis.getJSONObject(i);
                                        String ind_not_details = indi_noti.getString("details");
                                        String ind_not_posted_on = indi_noti.getString("created_on");
                                        JSONObject temp = new JSONObject();
                                        temp.put("title", ind_not_details);
                                        temp.put("author", "Posted On "+ind_not_posted_on);
                                        notifis.put(temp);
                                    }
                                }

                                for (int j = 0; j < notifis.length(); j++)
                                    notifications_list.add(notifis.getJSONObject(j));

                                //exception handling
                            } catch (JSONException ex) {
                                Log.e(TAG, "Cannot parse response!!    "+ex.toString());
                            }

                            ListView listV = (ListView) findViewById(R.id.notifiList);
                            NotiListAdapter adapter = new NotiListAdapter(getApplicationContext(), R.layout.fragment_notifications, R.id.serialno, notifications_list);
                            listV.setAdapter(adapter);

                            Log.d(TAG, "response in response listener: "+response);

                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    cancelPDialog();
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    // hide the progress dialog
                    Snackbar.make(view, "Unable to Access Server!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            Log.d(TAG, "before queue");
            queue.add(myReq);
            Log.d(TAG, "after queue");



        } else if (id == R.id.logout) {   //logout and redirect to login screen
            showpDialog();
            RequestQueue queue = Volley.newRequestQueue(this);
            //Logout API
            String url = getString(R.string.domain)+"/complaint/default/logout.json";
            StringRequest myReq = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            cancelPDialog();
                            Intent login = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(login);
                            finish();
                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    cancelPDialog();
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Unable to Access Server!");
                    alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            });
            queue.add(myReq);
        } else if (id == R.id.new_complaint) {       //display the new complain page which is user to post a new complaint
            current_id=id;
            toolbar.setTitle("New Complaint");

            NewComplaint.setVisibility(View.VISIBLE);
            mNotifications.setVisibility(View.GONE);
            listVw.setVisibility(View.GONE);

            Spinner dropdown = (Spinner)findViewById(R.id.select_category);

            dropdown.setOnItemSelectedListener(this);

            List<String> categories = new ArrayList<>();
            categories.add(0, "Select Complaint Type");
            categories.add(1, "Individual Level");
            categories.add(2, "Hostel Level");
            categories.add(3, "Institute Level");

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            dropdown.setAdapter(dataAdapter);

            Button submit = (Button) findViewById(R.id.submit_complaint);

            //encode the details entered by the user in the url form and send it using GET request
            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    EditText titl = (EditText) findViewById(R.id.title);
                    EditText desc = (EditText) findViewById(R.id.description);
                    String title;
                    String hostel;
                    String description;
                    hostel = getIntent().getExtras().getString("hostel");
                    try {
                        title = URLEncoder.encode(titl.getText().toString(), "UTF-8");
                        description = URLEncoder.encode(desc.getText().toString(), "UTF-8");
                    }catch(UnsupportedEncodingException ex)
                    {
                        title="";
                        description="";
                    }

                    String type="";
                    if(pos==3) type="null";
                    if(pos==4) type="individual";
                    if(pos==5) type="hostel";
                    if(pos==6) type="institute";

                    final View view1 = findViewById(android.R.id.content);
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                    Log.d(TAG, "complaint type:  "+pos+"     "+pos );
                    if(pos==3) {
                        alert.setTitle("Error");
                        final String MessageToSubmit="Please Select Category...";
                        alert.setMessage(MessageToSubmit);

                        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                        alert.show();
                    }
                    else if(title.equals("")) {
                        alert.setTitle("Error");
                        final String MessageToSubmit="Please enter a title...";
                        alert.setMessage(MessageToSubmit);

                        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                        alert.show();
                    }
                    else if(description.equals("")) {
                        alert.setTitle("Error");
                        final String MessageToSubmit="Please include some description...";
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
                        final String url = getString(R.string.domain) + "/complaint/default/create_complaint.json?complaint_type=" + pos + "&title=" + title + "&description=" + description + "&hostel=" + hostel + "&category_name=" + type;
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
                                            Log.d(TAG,"Cannot Parse Response: "+response);
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
                        queue.add(myReq);
                    }


                }
            });

        } else if (id == R.id.add_user) {       //display the new complain page which is user to post a new complaint

            mNotifications.setVisibility(View.GONE);
            listVw.setVisibility(View.GONE);

            String url = getString(R.string.domain) + "/complaint/default/register";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        //switch to complaint details activity if it is selected from the navigation menu
        if (id==1 || id==2 || id==3) {

            mNotifications.setVisibility(View.GONE);
            listVw.setVisibility(View.GONE);

            Intent intent = new Intent(getApplicationContext(),ComplaintDetails.class);
            String hostel = getIntent().getExtras().getString("hostel");
            int idd = getIntent().getExtras().getInt("person_id");
            int type = getIntent().getExtras().getInt("type");
            intent.putExtra("person_id",idd);
            intent.putExtra("item_id",id);
            intent.putExtra("type",type);
            intent.putExtra("hostel",hostel);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //action to be performed when user selects an option from the dropdown menu
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        pos=position+3;


    }
    public void onNothingSelected(AdapterView<?> arg0) {


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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("id", current_id);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        current_id = savedInstanceState.getInt("id");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, current_id + "      ****       " + menu2.findItem(current_id).toString());
        onNavigationItemSelected(menu2.findItem(current_id));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, current_id + "      ****       " + menu2.findItem(current_id).toString());
        onNavigationItemSelected(menu2.findItem(current_id));
    }
}