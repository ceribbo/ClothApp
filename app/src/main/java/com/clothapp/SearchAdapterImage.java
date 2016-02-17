package com.clothapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clothapp.resources.User;

import java.util.ArrayList;
import java.util.List;
import com.clothapp.resources.Image;

/**
 * Created by nc94 on 2/17/16.
 */
public class SearchAdapterImage extends BaseAdapter {
    private final Context context;
    private List< Image > image=new ArrayList<>();

    public SearchAdapterImage(Context context, List<Image> image) {
        this.context = context;
        this.image = image;
    }
    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public Object getItem(int position) {
        return image.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row==null) {
            //se la convertView di quest'immagine è nulla la inizializzo
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.search_item_image, parent, false);

        }

        TextView t=(TextView)row.findViewById(R.id.hashtag);
        t.setText(getItem(position).hashCode());
        return row;
    }




}
