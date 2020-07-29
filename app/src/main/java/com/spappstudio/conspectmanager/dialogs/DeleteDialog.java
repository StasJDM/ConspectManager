package com.spappstudio.conspectmanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.spappstudio.conspectmanager.AllNotesActivity;
import com.spappstudio.conspectmanager.DBHelper;

public class DeleteDialog extends DialogFragment {

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int id = getArguments().getInt("id");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setMessage("Конспект будет удалён").setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DBHelper(getContext()).deleteConspect(id);
                Intent intent = new Intent(getContext(), AllNotesActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
    }
}
