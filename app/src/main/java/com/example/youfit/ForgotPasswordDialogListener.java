package com.example.youfit;

import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public interface ForgotPasswordDialogListener {

    public void onDialogPositiveClick(DialogFragment dialog, EditText email);

    public void onDialogNegativeClick(DialogFragment dialog);
}
