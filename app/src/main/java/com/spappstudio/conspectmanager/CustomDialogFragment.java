package com.spappstudio.conspectmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.spappstudio.conspectmanager.dialogs.DeleteFromListDialog;

public class CustomDialogFragment extends DialogFragment {

    CustomAdapter context;
    int index;

    public CustomDialogFragment(CustomAdapter customAdapter, int index) {
        this.context = customAdapter;
        this.index = index;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int id = getArguments().getInt("id");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setItems(R.array.all_notes_dialog_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(getContext(), EditNoteActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        break;
                    case 1:
                        DeleteFromListDialog deleteFromListDialog = new DeleteFromListDialog(context);
                        Bundle args = new Bundle();
                        args.putInt("id", id);
                        args.putInt("index", index);
                        deleteFromListDialog.setArguments(args);
                        deleteFromListDialog.show(getFragmentManager(), "Delete");
                        break;
                    default:
                        break;
                }
            }
        }).create();
    }

}
