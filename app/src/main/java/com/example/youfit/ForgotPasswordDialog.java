package com.example.youfit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordDialog extends DialogFragment {

    ForgotPasswordDialogListener listener;

    EditText email;

    @Override
    public void onAttach(@NonNull Context context) {
        if (context == null){
            System.out.println("Context is null");
        }
        else {
            super.onAttach(context);
            try{
                listener = (ForgotPasswordDialogListener) context;

            } catch(ClassCastException e){
                throw new ClassCastException(getActivity().toString() + " must implement ForgotPasswordDialogListener");
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        email = getDialog().findViewById(R.id.editTextEmailAddress2);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        dialogBuilder.setView(inflater.inflate(R.layout.dialog_forgot_password, null));

        // Action for "YES" button
        dialogBuilder.setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkIfEmpty(email, "Email")){
                    return;
                }
                listener.onDialogPositiveClick(ForgotPasswordDialog.this, email);

            }
        });

        // Action for "NO" button
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNegativeClick(ForgotPasswordDialog.this);
            }
        });

        return dialogBuilder.create();

    }

    private boolean checkIfEmpty(EditText editText, String requireddMessage) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError(requireddMessage + " is required");
            return true;
        }
        return false;
    }


}
