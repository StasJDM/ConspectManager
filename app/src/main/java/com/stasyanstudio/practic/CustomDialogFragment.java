package com.stasyanstudio.practic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

    CustomAdapter context;
    int index;

    public CustomDialogFragment(CustomAdapter customAdapter, int index) {
        this.context = customAdapter;
        this.index = index;
    }

    boolean is_deleted = false;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int id = getArguments().getInt("id");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setItems(R.array.all_notes_dialog_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        break;
                    case 1:
                        is_deleted = new DBHelper(getContext()).deleteConspect(id);
                        context.conspects.remove(index);
                        context.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }).create();
    }

}
