package com.spappstudio.conspectmanager.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spappstudio.conspectmanager.OneTaskActivity;
import com.spappstudio.conspectmanager.R;
import com.spappstudio.conspectmanager.objects.Task;

import java.util.ArrayList;

public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.ViewHolder> {

    private static ItemClickListener itemClickListener;

    private ArrayList<Task> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textViewTitle;
        TextView textViewText;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            textViewTitle = (TextView)view.findViewById(R.id.textViewTitle);
            textViewText = (TextView)view.findViewById(R.id.textViewText);
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

    public TasksRecyclerAdapter(ArrayList<Task> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public TasksRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tasks, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewTitle.setText(dataset.get(position).title);
        holder.textViewText.setText(dataset.get(position).text);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void updateDataset(ArrayList<Task> dataset) {
        this.dataset = dataset;
    }

    public void addTask(Task task) {
        dataset.add(task);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        TasksRecyclerAdapter.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
