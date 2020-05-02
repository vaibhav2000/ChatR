package com.vabpack.talkv;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<MessageData> {

    ArrayList<MessageData> datahere;

    public CustomListAdapter(Context context, int resource, ArrayList<MessageData> holder)
    {
        super(context, resource, holder);
        this.datahere= holder;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        return datahere.get(position).type;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder = null;
        MessageData listViewItem = datahere.get(position);
        int listViewItemType = getItemViewType(position);


        if (convertView == null) {

            if (listViewItemType == 1) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listcustomlayout1, null);

            } else if (listViewItemType == 2) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listcustomlayout2, null);

            }

            TextView textView = (TextView) convertView.findViewById(R.id.listtextHere);
            textView.setText(listViewItem.message);

            viewHolder = new ViewHolder(textView);
            convertView.setTag(viewHolder);


        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.getText().setText(listViewItem.message);
        return convertView;
    }

}

