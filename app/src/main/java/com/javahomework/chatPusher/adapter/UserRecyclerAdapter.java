package com.javahomework.chatPusher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.javahomework.chatPusher.R;
import com.javahomework.chatPusher.model.Message;
import com.javahomework.chatPusher.model.User;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {
    //Context context;
    List<User> listUser;
    List<String> listKey;
    private UserRecyclerAdapter.OnUserListener mOnUserListener;
    private int selectedPos = RecyclerView.NO_POSITION;

    public UserRecyclerAdapter(List<User> listUser,List<String> listKey,OnUserListener onUserListener) {
        //this.context = context;
        this.listUser = listUser;
        this.mOnUserListener=onUserListener;
        this.listKey=listKey;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_list, parent, false);
        return new ViewHolder(view,mOnUserListener);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, int position) {
        // Gán dữ liêuk
        System.out.println("Pos:"+position);
        String key=listKey.get(position);
        User user = listUser.get(position);

        holder.itemView.setSelected(selectedPos == position);
        holder.bind(user,key);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPos);
                selectedPos = position;
                notifyItemChanged(selectedPos);
                mOnUserListener.onUserClick(holder.getBindingAdapterPosition(),key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size(); // trả item tại vị trí postion
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtRoom,txtEmail;
        String key;
        OnUserListener onUserListener;
        public ViewHolder(@NonNull View itemView,OnUserListener onUserListener) {
            super(itemView);
            // Ánh xạ view
            txtEmail = itemView.findViewById(R.id.txt_email);
            txtRoom = itemView.findViewById(R.id.txt_room);
            this.onUserListener=onUserListener;
        }
        public void bind(User user,String key) {
            txtEmail.setText(user.getEmail());
            txtRoom.setText(user.getRoom());
            this.key=key;
        }
    }

    public interface OnUserListener{
        void onUserClick(int position,String key);
    }
}
