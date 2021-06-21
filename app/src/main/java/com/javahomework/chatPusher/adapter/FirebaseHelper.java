package com.javahomework.chatPusher.adapter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javahomework.chatPusher.model.User;
import com.javahomework.chatPusher.ui.IntroActivity;
import com.javahomework.chatPusher.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String logged_id;

    public FirebaseHelper(String id) {
        logged_id=id;
        firebaseDatabase=FirebaseDatabase.getInstance("https://chat-pusher-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference().child(id).child("contact");
    }

    public interface  DataStatus {
        void DataIsLoaded(List<User> users,List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public void readUsers(final DataStatus dataStatus) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<User> users=new ArrayList<>();
                List<String> keys= new ArrayList<>();
                for(DataSnapshot keyNode: snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    User user=keyNode.getValue(User.class);
                    users.add(user);
                }
                dataStatus.DataIsLoaded(users,keys);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    public void addUser(String logged_email,User user,final  DataStatus dataStatus) {
        firebaseDatabase.getReference().orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {

                    String key=childSnapshot.getKey();
                    if(key.contains("chatPusher")==false) {
                        User myData=new User(logged_email, user.getRoom());
                        firebaseDatabase.getReference().child(key).child("contact").child(logged_id).setValue(myData);
                        databaseReference.child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dataStatus.DataIsInserted();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    public void updateUser(String key,User user,final DataStatus dataStatus) {
        databaseReference.child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsUpdated();
            }
        });
    }
    public void deleteUser(String key,final  DataStatus dataStatus) {
        databaseReference.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsDeleted();
            }
        });
        firebaseDatabase.getReference().child(key).child("contact").child(logged_id).setValue(null);
    }
}
