package com.javahomework.chatPusher.ui;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.javahomework.chatPusher.R;
import com.javahomework.chatPusher.adapter.FirebaseHelper;
import com.javahomework.chatPusher.adapter.UserRecyclerAdapter;
import com.javahomework.chatPusher.adapter.UserRecyclerAdapter;
import com.javahomework.chatPusher.model.User;
import com.javahomework.chatPusher.utils.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class IntroActivity extends AppCompatActivity implements UserRecyclerAdapter.OnUserListener {
    EditText edt_user_name;
    TextView logged_email,edt_room_name;
    Button enter,add,delete,update;
    RecyclerView recyclerView;
    List<User> listUser;
    String chosenName="";
    String chosenRoom="";
    String chosenKey="";
    UserRecyclerAdapter userRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        edt_user_name=(EditText) findViewById(R.id.edt_user_name) ;
        logged_email=(TextView) findViewById(R.id.textView);
        enter=(Button) findViewById(R.id.btn_enter);
        add=(Button) findViewById(R.id.btn_add);
        delete=(Button) findViewById(R.id.btn_delete);
        update=(Button) findViewById(R.id.btn_update);
        recyclerView=(RecyclerView) findViewById(R.id.user_recycler_view);

        if (getIntent()!=null) {
            logged_email.setText(getIntent().getStringExtra(Constants.USER_MAIL));

            new FirebaseHelper(getIntent().getStringExtra(Constants.USER_ID)).readUsers(new FirebaseHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<User> users, List<String> keys) {
                    userRecyclerAdapter=new UserRecyclerAdapter(users,keys,IntroActivity.this::onUserClick);
                    listUser=users;
                    recyclerView.setLayoutManager(new LinearLayoutManager(IntroActivity.this));
                    recyclerView.setAdapter(userRecyclerAdapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(IntroActivity.this,DividerItemDecoration.VERTICAL));
                }

                @Override
                public void DataIsInserted() {

                }

                @Override
                public void DataIsUpdated() {

                }

                @Override
                public void DataIsDeleted() {

                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user=new User(edt_user_name.getText().toString(),randomString(10));
                    System.out.println(user.toString());
                    new FirebaseHelper(getIntent().getStringExtra(Constants.USER_ID)).addUser(logged_email.getText().toString(),user, new FirebaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<User> users, List<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user=new User(edt_user_name.getText().toString(),chosenRoom);
                    new FirebaseHelper((getIntent().getStringExtra(Constants.USER_ID))).updateUser(chosenKey, user, new FirebaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<User> users, List<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FirebaseHelper((getIntent().getStringExtra(Constants.USER_ID))).deleteUser(chosenKey, new FirebaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<User> users, List<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
            });
        }

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name=logged_email.getText().toString();

                if (TextUtils.isEmpty(chosenName)||TextUtils.isEmpty(chosenRoom)){
                    showToastMessage("Choose a friend to start !");
                    return;
                }

                enterRoom(chosenRoom,chosenName,user_name);
            }
        });
    }

    private void enterRoom(String chat_room_name,String friend_name, String user_name) {
        Intent intent=new Intent(this,ChatRoomActivity.class);
        intent.putExtra(Constants.CHAT_ROOM_NAME_EXTRA,chat_room_name);
        intent.putExtra(Constants.FRIEND_NAME_EXTRA,friend_name);
        intent.putExtra(Constants.USER_NAME_EXTRA,user_name);
        startActivity(intent);
    }

    private void showToastMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserClick(int position,String key) {
        chosenRoom=listUser.get(position).getRoom();
        chosenName=listUser.get(position).getEmail();
        chosenKey=key;
    }

    static private String randomString(int n) {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
