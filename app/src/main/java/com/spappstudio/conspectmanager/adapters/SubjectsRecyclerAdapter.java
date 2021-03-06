package com.spappstudio.conspectmanager.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spappstudio.conspectmanager.R;
import com.spappstudio.conspectmanager.objects.Subject;

import java.util.ArrayList;

public class SubjectsRecyclerAdapter extends RecyclerView.Adapter<SubjectsRecyclerAdapter.ViewHolder> {

    private ArrayList<Subject> dataset;

    private static ItemClickListener itemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView textViewTitle;
        TextView textViewConspectCount;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewConspectCount = view.findViewById(R.id.textViewConspectsCount);
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

    public SubjectsRecyclerAdapter(ArrayList<Subject> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjects_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewTitle.setText(dataset.get(position).name);
        holder.textViewConspectCount.setText(String.valueOf(dataset.get(position).conspectCount));
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        SubjectsRecyclerAdapter.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
