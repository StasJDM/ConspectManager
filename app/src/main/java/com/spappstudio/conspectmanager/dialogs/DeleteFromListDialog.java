package com.spappstudio.conspectmanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.spappstudio.conspectmanager.CustomAdapter;
import com.spappstudio.conspectmanager.DBHelper;
import com.spappstudio.conspectmanager.R;

public class DeleteFromListDialog extends DialogFragment {

    CustomAdapter context;

    public DeleteFromListDialog(CustomAdapter context) {
        this.context = context;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int id = getArguments().getInt("id");
        final int index = getArguments().getInt("index");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setMessage(getString(R.string.conspect_will_delete)).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DBHelper(getContext()).deleteConspect(id);
                context.conspects.remove(index);
                context.notifyDataSetChanged();
            }
        }).setNegativeButton(getString(R.string.cancel), null).create();
    }

}
