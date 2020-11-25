package com.spappstudio.conspectmanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.spappstudio.conspectmanager.R;
import com.spappstudio.conspectmanager.interfaces.SelectConspectInterface;
import com.spappstudio.conspectmanager.objects.Conspect;

import java.util.ArrayList;

public class SelectConspectDialog extends DialogFragment {

    String[] conspect_array_string;
    ArrayList<Integer> conspectId;

    SelectConspectInterface conspectInterface;

    public SelectConspectDialog (String[] conspect_array_string, ArrayList<Integer> conspectId) {
        this.conspect_array_string = conspect_array_string;
        this.conspectId = conspectId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        conspectInterface = (SelectConspectInterface) getActivity();

        return builder.setTitle(R.string.select_conspectus).setItems(conspect_array_string, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                conspectInterface.selectConspect(conspectId.get(which).intValue());
            }
        }).create();
    }
}
