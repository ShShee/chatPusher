package com.javahomework.chatPusher.ui;

import android.content.res.ColorStateList;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.javahomework.chatPusher.R;
import com.javahomework.chatPusher.adapter.ChatRecyclerAdapter;
import com.javahomework.chatPusher.model.Message;
import com.javahomework.chatPusher.model.response.ServerResponse;
import com.javahomework.chatPusher.network.APIService;
import com.javahomework.chatPusher.utils.Constants;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatRoomActivity extends AppCompatActivity {

    EditText edt_chat_message;
    FloatingActionButton fab_send_message;
    RecyclerView chat_recycler_view;
    ProgressBar progress;

    private String chat_room_name;
    private String username;
    private String friendname;
    private ChatRecyclerAdapter chatRecyclerAdapter;
    private ArrayList<Message> messageList=new ArrayList<>();
    private String LIST = "list";
    private Channel channel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        edt_chat_message=(EditText) findViewById(R.id.edt_chat_message);
        fab_send_message=(FloatingActionButton) findViewById(R.id.fab_send_message);
        chat_recycler_view=(RecyclerView) findViewById(R.id.chat_recycler_view);
        progress=(ProgressBar) findViewById(R.id.progress);

        if (getIntent()!=null) {
            chat_room_name = getIntent().getStringExtra(Constants.CHAT_ROOM_NAME_EXTRA);
            username = getIntent().getStringExtra(Constants.USER_NAME_EXTRA);
            friendname=getIntent().getStringExtra(Constants.FRIEND_NAME_EXTRA);
        }
        if (getSupportActionBar()!=null && friendname!=null) {
            getSupportActionBar().setTitle(friendname);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (savedInstanceState!=null) messageList=savedInstanceState.getParcelableArrayList(LIST);

        chatRecyclerAdapter=new ChatRecyclerAdapter(this,messageList,username);
        chat_recycler_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        chat_recycler_view.setItemAnimator(new DefaultItemAnimator());
        chat_recycler_view.setAdapter(chatRecyclerAdapter);

        edt_chat_message.addTextChangedListener(textWatcher);

        //Pusher Connection
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");
        Pusher pusher = new Pusher("c244e1ce8fbab722ca7d", options);

        channel = pusher.subscribe(chat_room_name);
        channel.bind("new_message", subscriptionEventListener);

        pusher.connect();

        fab_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=edt_chat_message.getText().toString();
                if (!TextUtils.isEmpty(message)) {

                    sendMessage(message);
                }
            }
        });
    }

    SubscriptionEventListener subscriptionEventListener=new SubscriptionEventListener() {
        @Override
        public void onEvent(String channelName, String eventName, final String data) {
            Gson gson=new Gson();
            final Message message = gson.fromJson(data,Message.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMessageAdded(message);
                }
            });
        }
    };

    private void sendMessage(final String message) {
        showProgress();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        APIService apiService= builder.build().create(APIService.class);
        Call<ServerResponse> call=apiService.sendMessage(chat_room_name,
                new Message(message,username));
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.body()!=null){
                    if (response.body().getSuccess()!=null) {
                        hideProgress();
                        edt_chat_message.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                hideProgress();
            }
        });

    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        fab_send_message.hide();
    }

    private void hideProgress() {
        progress.setVisibility(View.GONE);
        fab_send_message.show();
    }

    public void showMessageAdded(Message message){
        chatRecyclerAdapter.addMessage(message);
        chat_recycler_view.scrollToPosition((chatRecyclerAdapter.getItemCount()-1));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST,chatRecyclerAdapter.getMessageList());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().isEmpty()) {
                fab_send_message.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            }
            else {
                fab_send_message.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (channel.isSubscribed())channel.unbind("new_message",subscriptionEventListener);
    }
}
