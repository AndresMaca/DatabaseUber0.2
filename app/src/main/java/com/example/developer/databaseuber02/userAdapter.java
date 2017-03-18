package com.example.developer.databaseuber02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Developer on 17/2/2017.
 */
public class userAdapter extends ArrayAdapter<User> {
    public userAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);

        }
        TextView numberText = (TextView) convertView.findViewById(R.id.number_item);
        TextView nameText = (TextView) convertView.findViewById(R.id.name_item_user);


        numberText.setText(user.getNumber().toString());
        nameText.setText(user.getName());


        return convertView;
    }
}
