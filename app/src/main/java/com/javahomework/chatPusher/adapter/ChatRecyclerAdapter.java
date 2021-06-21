package com.javahomework.chatPusher.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.javahomework.chatPusher.R;
import com.javahomework.chatPusher.model.Message;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by kehinde on 9/24/17.
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ChatViewHolder> {

    private Context context;
    private ArrayList<Message> messageList;
    private String m_username;

    public ChatRecyclerAdapter(Context context, ArrayList<Message> messageList, String m_username) {
        this.context = context;
        this.messageList = messageList;
        this.m_username = m_username;
    }

    class  ChatViewHolder extends RecyclerView.ViewHolder {
        TextView txt_username;
        TextView txt_chat_message;

        public ChatViewHolder(View itemView) {
            super(itemView);
            txt_username=(TextView) itemView.findViewById(R.id.txt_username);
            txt_chat_message=(TextView) itemView.findViewById(R.id.txt_chat_message);
            //ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_chat_message,parent,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Message message=messageList.get(position);

        holder.txt_chat_message.setText(message.getMessage());
        if (message.getUsername().equals(m_username))
            holder.txt_username.setText("You");
        else
            holder.txt_username.setText(message.getUsername());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addMessage(Message message){
        messageList.add(message);
        notifyDataSetChanged();
    }

    public void setMessages(ArrayList<Message> list){
        if (list!=null) messageList=list;
        notifyDataSetChanged();
    }

    public ArrayList<Message> getMessageList() {
        return messageList;
    }
}
