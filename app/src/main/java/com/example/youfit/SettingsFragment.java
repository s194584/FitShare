package com.example.youfit;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.youfit.domain.Server;
import com.example.youfit.domain.User;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        nameEditText.setText(((MainActivity) getActivity()).getServer().getUsername());

        view.findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Server server = ((MainActivity) getActivity()).getServer();
                String newName = nameEditText.getText().toString();
                if(newName.length() < 27 && newName.length() > 3)
                {
                    server.updateCurrentUserUsername(newName);
                    Toast.makeText(getContext(),"Username updated", Toast.LENGTH_LONG).show();
                }else
                    {
                        Toast.makeText(getContext(),"Username must be between 3 and 26 characters",Toast.LENGTH_LONG).show();
                    }

            }
        });

        ((Switch)view.findViewById(R.id.switch_notifications)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setupNotifications(b);
            }
        });

        view.findViewById(R.id.logOutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoticeDialog();
            }
        });

        view.findViewById(R.id.changeEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeEmail = new Intent(getActivity().getApplicationContext(), ChangeEmailActivity.class);
                startActivity(changeEmail);
            }
        });

        view.findViewById(R.id.changePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordDialog();
            }
        });
    }

    private void setupNotifications(boolean b) {
        Notification.Builder builder = new Notification.Builder(getActivity());
        builder.setContentText("Hey, got any workouts?")
                .setContentTitle("Your favorite workout app")
                .setPriority(Notification.PRIORITY_DEFAULT);
    }

    private void showNoticeDialog() {
        DialogFragment noticeDialog = new SignOutDialogFragment();
        noticeDialog.show(getActivity().getSupportFragmentManager(), "signOut" );
    }

    private void showPasswordDialog(){
        DialogFragment passwordDialog = new ChangePasswordDialogFragment();
        passwordDialog.show(getActivity().getSupportFragmentManager(), "password_change");
    }



}