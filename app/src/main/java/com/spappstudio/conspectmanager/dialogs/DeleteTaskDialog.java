package com.spappstudio.conspectmanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.spappstudio.conspectmanager.AllNotesActivity;
import com.spappstudio.conspectmanager.DBHelper;
import com.spappstudio.conspectmanager.R;

public class DeleteTaskDialog extends DialogFragment {

    public NotifyListener notifyListener;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int id = getArguments().getInt("id");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setMessage(getString(R.string.task_will_delete)).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DBHelper(getContext()).deleteTask(id);
                if (getArguments().containsKey("position")) {
                    final int position = getArguments().getInt("position");
                    notifyListener.onDelete(position);
                } else {
                    getActivity().finish();
                }
            }
        }).setNegativeButton(getString(R.string.cancel), null).create();
    }

    public void setNotifyListener(NotifyListener notifyListener) {
        this.notifyListener = notifyListener;
    }

    public interface NotifyListener {
        void onDelete(int position);
    }
}
