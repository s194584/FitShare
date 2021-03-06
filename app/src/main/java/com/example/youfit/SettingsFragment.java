package com.example.youfit;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youfit.domain.DatabaseListener;
import com.example.youfit.domain.Server;
import com.example.youfit.domain.User;
import com.example.youfit.domain.Workout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class SettingsFragment extends Fragment implements DatabaseListener {
    private final String TAG = "SettingsFragment";

    private boolean isChecked;
    private String curretUsername;
    private View view;
    private EditText nameEditText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.nameEditText = view.findViewById(R.id.nameEditText);

        initUIElements(view);

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
                ((MainActivity)getActivity()).notifications = b;
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

    private void showNoticeDialog() {
        DialogFragment noticeDialog = new SignOutDialogFragment();
        noticeDialog.show(getActivity().getSupportFragmentManager(), "signOut" );
    }

    private void showPasswordDialog(){
        DialogFragment passwordDialog = new ChangePasswordDialogFragment();
        passwordDialog.show(getActivity().getSupportFragmentManager(), "password_change");
    }


    @Override
    public void onComplete(DataSnapshot dataSnapshot) {
        Log.i(TAG, "onComplete entered");
        curretUsername = dataSnapshot.child("name").getValue().toString();

        initUIElements(view);
    }

    public void initUIElements(View view) {
        if (!(getActivity() ==null)) {
            ((Switch)view.findViewById(R.id.switch_notifications)).setChecked(((MainActivity) getActivity()).isNotifications());
        }
        this.nameEditText.setText(curretUsername);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Server server = ((MainActivity) getActivity()).getServer();
        server.loadCurrentUser(this);
    }
}