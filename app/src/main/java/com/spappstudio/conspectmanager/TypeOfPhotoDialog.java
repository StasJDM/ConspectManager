package com.spappstudio.conspectmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TypeOfPhotoDialog extends DialogFragment {

    public OnFragmentInteractionListener mCallback;

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_GALLERY = 2;

    String currentPhotoPath;
    ArrayList<String> photosPathArrayList;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        try {
            mCallback = (OnFragmentInteractionListener) getActivity();
        } catch (Exception e) {
        }
        photosPathArrayList = new ArrayList<String>();
        return builder.setItems(R.array.add_photo_dialog_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        try {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            File file = new File(Environment.getExternalStorageDirectory(), timeStamp + ".png");
                            Uri photo_dir = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photo_dir);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                            currentPhotoPath = file.getAbsolutePath();
                            photosPathArrayList.add(currentPhotoPath);
                            mCallback.sendRequestCode(REQUEST_IMAGE_CAPTURE, photosPathArrayList);
                        } catch (ActivityNotFoundException e) {
                            String errorMessage = "Ошибка камеры!";
                            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(Intent.createChooser(intent, "Выберите"), REQUEST_GALLERY);
                        break;
                    default:
                        break;
                }
            }
        }).create();
    }

    public interface OnFragmentInteractionListener {
        void sendRequestCode(int code, ArrayList<String> photo_path);
    }
}
