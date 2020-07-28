package com.spappstudio.conspectmanager.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.spappstudio.conspectmanager.ActivityViewImages;
import com.spappstudio.conspectmanager.R;

import java.util.ArrayList;

import static com.spappstudio.conspectmanager.Photo.getRoundedCornerBitmap;

public class RecyclerAdapeter extends RecyclerView.Adapter<RecyclerAdapeter.ViewHolder> {

    public ArrayList<String> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String mItem;
        private ArrayList<String> data;
        public SubsamplingScaleImageView imageView;

        public ViewHolder(SubsamplingScaleImageView iv, ArrayList<String> data) {
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
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item, parent, false);
        imageView.setMinimumDpi(1);
        ViewHolder viewHolder = new ViewHolder(imageView, dataset);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(dataset.get(position));
        holder.imageView.setImage(ImageSource.bitmap(getRoundedCornerBitmap(bitmap, 40)));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}
