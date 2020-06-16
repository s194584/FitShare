package com.example.youfit.domain;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Server {

    protected FirebaseAuth firebaseAuth;
    protected FirebaseDatabase rootNode;
    protected DatabaseReference databaseReference;
    protected Activity activity;

    public Server(Activity activity) {
        this.activity = activity;
    }

    public String getUsername() {
        this.firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            this.rootNode = FirebaseDatabase.getInstance();
            this.databaseReference = this.rootNode.getReference("Users/" + this.firebaseAuth.getCurrentUser().getUid());
            final String[] username = new String[1];

            // Attach a listener to read the data at our posts reference
            this.databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    username[0] = "hej";
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            return username[0];
        }
        return "could not finde username";
    }


}
