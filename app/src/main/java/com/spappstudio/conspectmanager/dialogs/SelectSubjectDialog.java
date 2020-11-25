package com.spappstudio.conspectmanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.spappstudio.conspectmanager.R;
import com.spappstudio.conspectmanager.interfaces.SelectSubjectInterface;

public class SelectSubjectDialog extends DialogFragment {

    String[] subject_array;
    SelectSubjectInterface subjectInterface;

    public SelectSubjectDialog(String[] subject_array) {
        this.subject_array = subject_array;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        subjectInterface = (SelectSubjectInterface) getActivity();

        return builder.setTitle(getString(R.string.chosen_subject)).setItems(subject_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                subjectInterface.selectSubject(subject_array[which]);
            }
        }).create();
    }
}
