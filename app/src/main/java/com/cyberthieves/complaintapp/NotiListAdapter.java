package com.cyberthieves.complaintapp;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class NotiListAdapter extends ArrayAdapter<JSONObject> {

    int num;
    ArrayList<JSONObject> complaints;
    Context context;

    public NotiListAdapter(Context context, int num, int id, ArrayList<JSONObject> complaints){
        super(context, num, id, complaints);
        this.num = num;
        this.context = context;
        this.complaints = complaints;
    }

    public View getView(int position, View converView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(num,parent,false);
        TextView title = (TextView)itemView.findViewById(R.id.title);
        TextView author = (TextView)itemView.findViewById(R.id.author);

        try {

            title.setText(complaints.get(position).getString("title"));

            author.setText(complaints.get(position).getString("author"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return itemView;
    }

}
