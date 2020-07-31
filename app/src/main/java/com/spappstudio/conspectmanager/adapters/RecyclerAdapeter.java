package com.spappstudio.conspectmanager.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.spappstudio.conspectmanager.ActivityViewImages;
import com.spappstudio.conspectmanager.R;
import com.spappstudio.conspectmanager.picassotransform.RoundedCornersTransform;
import com.spappstudio.conspectmanager.picassotransform.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerAdapeter extends RecyclerView.Adapter<RecyclerAdapeter.ViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    public ArrayList<String> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String mItem;
        private ArrayList<String> data;
        public ImageView imageView;

        public ViewHolder(ImageView iv, ArrayList<String> data) {
            super(iv);
            iv.setOnClickListener(this);
            this.data = data;
            imageView = iv;
        }

        public void SetItem(String item) {
            mItem = item;
        }

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ActivityViewImages.class);
            intent.putStringArrayListExtra("photos_path", data);
            intent.putExtra("page_now", getAdapterPosition());
            view.getContext().startActivity(intent);
        }
    }

    public RecyclerAdapeter(ArrayList<String> data) {
        dataset = data;
    }

    @Override
    public RecyclerAdapeter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(imageView, dataset);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.imageView.setImage(uri(dataset.get(position)));
        Picasso.get().load("file:" + dataset.get(position)).transform(new RoundedTransformation(20, 0)).resize(600, 900).centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dataset, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dataset, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(ViewHolder viewHolder) {
        //viewHolder.rowView
    }

    @Override
    public void onRowClear(ViewHolder viewHolder) {

    }

    public ArrayList<String> getDataset(){
        return dataset;
    }

}
