package com.spappstudio.conspectmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spappstudio.conspectmanager.objects.Conspect;
import com.spappstudio.conspectmanager.picassotransform.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    public  ArrayList<Conspect> conspects;
    boolean isEmpty = false;

    private static LayoutInflater inflater = null;

    public CustomAdapter(AllNotesActivity allNotesActivity, ArrayList<Conspect> conspects) {
        this.conspects = conspects;
        if (conspects.size() == 0) {
            isEmpty = true;
            conspects.add(new Conspect(-1, -1, allNotesActivity.getString(R.string.click_to_add_conspect), "", "", "", ""));
        }
        context = allNotesActivity;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return conspects.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView textViewNames;
        TextView textViewSubjectDate;
        ImageView imageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.list_item_all_notes, null);
        holder.textViewNames = (TextView)rowView.findViewById(R.id.textViewName);
        holder.textViewSubjectDate = (TextView)rowView.findViewById(R.id.textViewSubjectDate);
        holder.imageView = (ImageView)rowView.findViewById(R.id.imageView);

        Picasso.get().load("file:" + conspects.get(position).first_image_path).transform(new RoundedCornersTransform()).resize(128, 128).centerCrop().into(holder.imageView);

        holder.textViewNames.setText(conspects.get(position).name);
        if (isEmpty) {
            holder.textViewNames.setGravity(Gravity.CENTER);
            holder.textViewNames.setTextSize(22);
            holder.textViewSubjectDate.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
        }
        if (!conspects.get(position).subject.equals("") && !conspects.get(position).date.equals("")) {
            holder.textViewSubjectDate.setVisibility(View.VISIBLE);
        }
        if (!isEmpty) {
            if (conspects.get(position).subject.equals("")) {
                holder.textViewSubjectDate.setText(conspects.get(position).date);
            } else if (conspects.get(position).date.equals("")) {
                holder.textViewSubjectDate.setText(conspects.get(position).subject);
            } else {
                holder.textViewSubjectDate.setText(conspects.get(position).subject + " - " + conspects.get(position).date);
            }
        }
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty) {
                    Intent intent = new Intent(context, AddNotesActivity.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, OneNoteActivity.class);
                    intent.putExtra("id", conspects.get(position).id);
                    intent.putExtra("name", conspects.get(position).name);
                    intent.putExtra("subject", conspects.get(position).subject);
                    intent.putExtra("date", conspects.get(position).date);
                    intent.putExtra("about", conspects.get(position).about);
                    intent.putExtra("n_photos", conspects.get(position).n_photos);
                    context.startActivity(intent);

                }
            }
        });

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isEmpty) {
                    CustomDialogFragment dialogFragment = new CustomDialogFragment(CustomAdapter.this, position);
                    Bundle args = new Bundle();
                    args.putInt("id", conspects.get(position).id);
                    dialogFragment.setArguments(args);
                    dialogFragment.show(((AllNotesActivity)context).getSupportFragmentManager(), "custom");
                }
                return false;
            }
        });

        return rowView;
    }

    public void updateDataset(ArrayList<Conspect> data) {
        this.conspects = data;
    }
}
