package com.spappstudio.conspectmanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.spappstudio.conspectmanager.AddTaskActivity;
import com.spappstudio.conspectmanager.R;
import com.spappstudio.conspectmanager.TasksActivity;
import com.spappstudio.conspectmanager.adapters.TasksRecyclerAdapter;

public class TasksLongClickDialog extends DialogFragment {

    DeleteTaskDialog.NotifyListener notifyListener;

    public TasksLongClickDialog(DeleteTaskDialog.NotifyListener notifyListener) {
        this.notifyListener = notifyListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final int id = getArguments().getInt("id");
        final int position = getArguments().getInt("position");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setItems(R.array.tasks_dialog_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(getContext(), AddTaskActivity.class);
                        intent.putExtra("type", "edit");
                        intent.putExtra("id", id);
                        startActivity(intent);
                        break;
                    case 1:
                        DeleteTaskDialog deleteTaskDialog = new DeleteTaskDialog();
                        Bundle args = new Bundle();
                        args.putInt("id", id);
                        args.putInt("position", position);
                        deleteTaskDialog.setArguments(args);
                        deleteTaskDialog.setNotifyListener(notifyListener);
                        deleteTaskDialog.show(getFragmentManager(), "Delete");
                        break;
                    default:
                        break;
                }
            }
        }).create();
    }
}
