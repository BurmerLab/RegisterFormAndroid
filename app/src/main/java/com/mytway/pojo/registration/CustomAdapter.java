package com.mytway.pojo.registration;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mytway.activity.R;

public class CustomAdapter extends ArrayAdapter<CheckboxModel>{
    CheckboxModel[] checkboxModelItems = null;
    Context context;

    public CustomAdapter(Context context, CheckboxModel[] resource) {
        super(context, R.layout.row,resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.checkboxModelItems = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.row, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.textView1);

        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
        name.setText(checkboxModelItems[position].getName());

        if(checkboxModelItems[position].getValue() == 1)
            cb.setChecked(true);
        else
            cb.setChecked(false);
            return convertView;
        }
    }
