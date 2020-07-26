package com.stasyanstudio.practic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;

public class PageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static final String ARGUMENT_IMAGE_PATH = "arg_image_path";

    int pageNumber;
    String imagesPath;

    static PageFragment newInstance(int page, String path) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        arguments.putString(ARGUMENT_IMAGE_PATH, path);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = Integer.valueOf(getArguments().getInt(ARGUMENT_PAGE_NUMBER));
        imagesPath = getArguments().getString(ARGUMENT_IMAGE_PATH);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fragment_images, null);
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)view.findViewById(R.id.imageView);
        imageView.setImage(ImageSource.uri(imagesPath));
        imageView.setMinimumDpi(1);
        imageView.setDoubleTapZoomDpi(1);
        return view;
    }


}
