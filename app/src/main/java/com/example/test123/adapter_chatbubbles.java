package com.example.test123;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapter_chatbubbles extends RecyclerView.Adapter<adapter_chatbubbles.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_Right = 1;
    private Context mcontext;
    private List<ref_chats> mref_chats;
    private String imageurl;

    FirebaseUser firebaseUser;

    public adapter_chatbubbles(Context mcontext, List<ref_chats> mref_chats) {
        this.mcontext = mcontext;
        this.mref_chats = mref_chats;
        this.imageurl = imageurl;
    }
    @NonNull
    @Override
    public adapter_chatbubbles.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype){
        if(viewtype == MSG_TYPE_Right){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.bubble_right, parent, false);
            return new adapter_chatbubbles.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mcontext).inflate(R.layout.bubble_left, parent, false);
            return new adapter_chatbubbles.ViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull adapter_chatbubbles.ViewHolder holder, int position) {

        ref_chats ref_chats = mref_chats.get(position);

        holder.show_message.setText(ref_chats.getMessage());

//        if(imageurl.equals("default")){
//
//            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
//        }
//        else {
//            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
////            Glide.with(mcontext).load(imageurl).into(holder.profile_image);
//        }

    }

    @Override
    public int getItemCount() {

        return mref_chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
//        public CircleImageView profile_image;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.message_body);
//            profile_image = itemView.findViewById(R.id.avatar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mref_chats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_Right;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
