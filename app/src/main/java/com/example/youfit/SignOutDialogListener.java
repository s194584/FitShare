package com.example.youfit;

import androidx.fragment.app.DialogFragment;

public interface SignOutDialogListener {

    /* This interface is used to pass on the action event
       when the user click either "YES" or "NO" back to the
       host activity/fragment.
       The host activity/fragment will implement these methods.
       The SignOutDialogFragment will create an instance of this interface */

    public void onDialogPositiveClick(DialogFragment dialog);

    public void onDialogNegativeClick(DialogFragment dialog);
}
