package com.spappstudio.conspectmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spappstudio.conspectmanager.R;
import com.spappstudio.conspectmanager.objects.Conspect;
import com.spappstudio.conspectmanager.picassotransform.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ConspectsRecyclerAdapter extends RecyclerView.Adapter<ConspectsRecyclerAdapter.ViewHolder> {

    private ArrayList<Conspect> dataset;

    private static ItemClickListener itemClickListener;

    public static class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView imageView;
        TextView textViewName;
        TextView textViewSubjectDate;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            imageView = view.findViewById(R.id.imageView);
            textViewName = view.findViewById(R.id.textViewName);
            textViewSubjectDate = view.findViewById(R.id.textViewSubjectDate);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public ConspectsRecyclerAdapter(ArrayList<Conspect> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_all_notes, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewName.setText(dataset.get(position).name);
        holder.textViewSubjectDate.setText(dataset.get(position).date);
        Picasso.get().load("file:" + dataset.get(position).first_image_path).transform(new RoundedCornersTransform()).resize(128, 128).centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        ConspectsRecyclerAdapter.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
