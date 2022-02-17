package com.waseemshahzad.chatapp.Adapter;

import static com.waseemshahzad.chatapp.Activity.ChatActivity.sImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.waseemshahzad.chatapp.ModelClass.Messeges;
import com.waseemshahzad.chatapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessegesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messeges>messegesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;

    public MessegesAdapter(Context context, ArrayList<Messeges> messegesArrayList) {
        this.context = context;
        this.messegesArrayList = messegesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ITEM_SEND)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new SenderViewHolder(view);

        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_layout_item,parent,false);
            return new RecieverViewHolder(view);


        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messeges messeges=messegesArrayList.get(position);
        if (holder.getClass()==SenderViewHolder.class)
        {
            SenderViewHolder viewHolder=(SenderViewHolder) holder;

            viewHolder.txtmessege.setText(messeges.getMessege());

            Picasso.get().load(sImage).into(viewHolder.circleImageView);

        }else{

            RecieverViewHolder viewHolder=(RecieverViewHolder) holder;
            viewHolder.txtmessege.setText(messeges.getMessege());
            Picasso.get().load(sImage).into(viewHolder.circleImageView);

        }

    }

    @Override
    public int getItemCount() {
        return messegesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
     Messeges messeges=messegesArrayList.get(position);

     if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messeges.getSenderid()))
     {
         return ITEM_SEND;
     }else {
         return ITEM_RECIEVE;
     }

    }

    class SenderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txtmessege;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.profile_image);
            txtmessege=itemView.findViewById(R.id.txtmesseges);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txtmessege;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);


            circleImageView=itemView.findViewById(R.id.profile_image);
            txtmessege=itemView.findViewById(R.id.txtmesseges);

        }
    }
}
