package com.cyberthieves.complaintapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A login screen that offers login via email/password.
 */
//the login page. Contains two field to provide user id and password and submit the info. If authenticated the app continues to the home
//page otherwise it gives an error message showing invalid user details.
public class LoginActivity extends Activity  {

    static private final String TAG = "Complaint-App";
    private TextInputLayout mUserIdLayout;
    private EditText mUserId;
    private TextInputLayout mPasswordLayout;
    private EditText mPassword;
    private Button mlogIn;
    private TextView mforgotPasswd;
    private ProgressDialog pDialog;
    private Toast toast;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pDialog = new ProgressDialog(this);
        //define the placeholders for user_id and password
        mUserIdLayout = (TextInputLayout) findViewById(R.id.email);
        mUserId = (EditText) findViewById(R.id.emailf);
        mPasswordLayout = (TextInputLayout) findViewById(R.id.password);
        mPassword = (EditText) findViewById(R.id.passwordf);
        mlogIn = (Button) findViewById(R.id.email_sign_in_button);
        mforgotPasswd= (TextView) findViewById(R.id.forgPass);
        mforgotPasswd.setMovementMethod(LinkMovementMethod.getInstance());

        mUserId.addTextChangedListener(new CustomTextWatcher(mUserId));
        mPassword.addTextChangedListener(new CustomTextWatcher(mPassword));

        toast = new Toast(this);
        toast = Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_LONG);
        toast.cancel();
        //focus change listener for the edit text containing the user_id
        mUserId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        //focus change listener for the edit text containing the password
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        mlogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                proceed(view);
            }
        });

        //checkbox to toggle/display the password for accessibility on clicking
        CheckBox checkbox=(CheckBox)findViewById(R.id.pass_show);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    mPassword.setInputType(129);
                    //129 is the input type set when setting android:inputType:textPassword
                }
            }
        });

    }
    //function to proceed if the authentication is a success
    private void proceed(View view) {
        Log.i(TAG, "Entered proceed()");
        if (!checkUserId()) {
            checkPasswd();
            return;
        }
        else if (!checkPasswd()) {
            checkUserId();
            return;
        }
        //log for debugging
        Log.i(TAG, "Input correct...");

        final String userId = mUserId.getText().toString();
        final String passwd = mPassword.getText().toString();
        final View view1 = view;

        showpDialog();

        // sending json request
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.domain)+"/complaint/default/login.json?userid=" + userId + "&password=" + passwd;
        Log.d(TAG, url);
        StringRequest myReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cancelPDialog();
                        try {
                            //parsing the json response from the request
                            JSONObject response1 = new JSONObject(response);
                            boolean Success = response1.getBoolean("success");
                            if (Success){
                                Intent main = new Intent(LoginActivity.this,MainActivity.class);
                                JSONObject person=response1.getJSONObject("user");
                                String last_name = person.getString("last_name");
                                int id = person.getInt("id");
                                String first_name = person.getString("first_name");
                                String entry_no = person.getString("entry_no");
                                String email = person.getString("email_id");
                                String username = person.getString("username");
                                String hostel = person.getString("hostel");
                                int type = person.getInt("type_");

                                //passing the data to next activity
                                main.putExtra("firstname", first_name);
                                main.putExtra("lastname",last_name);
                                main.putExtra("username",username);
                                main.putExtra("person_id",id);
                                main.putExtra("type",type);
                                main.putExtra("email",email);
                                main.putExtra("entry_no",entry_no);
                                main.putExtra("hostel",hostel);

                                startActivity(main);
                                finish();
                            }
                            else{
                                //alertdialog if the credentials are invalid
                                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                                alert.setTitle("Invalid UserID or Password!!");

                                final String MessageToSubmit="Please enter the details again...";
                                alert.setMessage(MessageToSubmit);

                                alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                });
                                alert.show();
                            }
                            //exception handling
                        } catch (JSONException ex) {
                            Log.e(TAG, "Cannot parse response!!");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                cancelPDialog();
                Snackbar.make(view1, "Unable to Access Server!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        queue.add(myReq);
    }


    //pattern to check user_id
    private boolean checkUserId() {
        Pattern pat = Pattern.compile("[a-zA-Z]{2}[a-zA-Z0-9]{1}[0-1]{1}[0-9]{5}");
        Matcher mat = pat.matcher(mUserId.getText().toString());
        if (mUserId.getText().toString().trim().isEmpty()) {
            mUserIdLayout.setError(getString(R.string.err_msg_name));
            return false;
        } else {
            mUserIdLayout.setErrorEnabled(false);
        }
        return true;
    }
    //check password to make sure it is not only white spaces
    private boolean checkPasswd() {
        if (mPassword.getText().toString().trim().isEmpty()) {
            mPasswordLayout.setError(getString(R.string.err_msg_passwd));
            return false;
        } else {
            mPasswordLayout.setErrorEnabled(false);
        }
        return true;
    }
    //implementing customtextwatcher for the user_id and password field to detect any changes made to these fields
    private class CustomTextWatcher implements TextWatcher {

        private View view;

        private CustomTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.emailf:
                    mUserIdLayout.setErrorEnabled(false);
                    break;
                case R.id.passwordf:
                    mPasswordLayout.setErrorEnabled(false);
                    break;
            }
        }
    }
    // hide the keyboard
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    //show progess bar
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    //hide progress bar
    private void cancelPDialog() {
        if (pDialog.isShowing())
            pDialog.cancel();
    }

    @Override
    public void onBackPressed() {
        if(toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            toast.cancel();
            super.onBackPressed();
        }
    }
}




