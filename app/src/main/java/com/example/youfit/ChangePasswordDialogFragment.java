package com.example.youfit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ChangePasswordDialogFragment extends DialogFragment {

    ChangePasswordDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        if (context == null){
            System.out.println("Context is null");
        }
        else {
            super.onAttach(context);
            try{
                listener = (ChangePasswordDialogListener) context;

            } catch(ClassCastException e){
                throw new ClassCastException(getActivity().toString() + " must implement ChangePasswordDialogFragment");
            }
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(R.string.reset_password);

        // Action for "YES" button
        dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogPositiveClick(ChangePasswordDialogFragment.this);
            }
        });

        // Action for "NO" button
        dialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNegativeClick(ChangePasswordDialogFragment.this);
            }
        });

        return dialogBuilder.create();

    }
}
