package com.example.youfit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SignOutDialogFragment extends DialogFragment {
    private final String TAG = "SignOutDialogFragment";

    // Uses this interface to pass action event from dialog back to host activity/fragment
    SignOutDialogListener listener;

    // Instantiates the listener interface
    @Override
    public void onAttach(@NonNull Context context) {
        if (context == null){
            Log.i(TAG, "Context is null");
        }
        else {
            super.onAttach(context);
            try{
                listener = (SignOutDialogListener) context;

            } catch(ClassCastException e){
                throw new ClassCastException(getActivity().toString() + " must implement SignOutDialogListener");
            }
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(R.string.sign_out_dialog);

        // Action for "YES" button
        dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogPositiveClick(SignOutDialogFragment.this);
            }
        });

        // Action for "NO" button
        dialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNegativeClick(SignOutDialogFragment.this);
            }
        });

        return dialogBuilder.create();
    }
}
