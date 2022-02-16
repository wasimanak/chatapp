package com.waseemshahzad.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String RecieverImage,RecieverUID,RecieverName,SenderUID;
    CircleImageView profileimage;
    TextView recievername;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String sImage;
    public static String rImage;

    CardView sendbtn;
    EditText et_msg;

    String senderRoom,recieverRoom;

    RecyclerView msgadapter;
    ArrayList<Messeges> messegesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        RecieverName=getIntent().getStringExtra("name");
        RecieverImage=getIntent().getStringExtra("RecieverImage");
        RecieverUID=getIntent().getStringExtra("uid");
        messegesArrayList=new ArrayList<>();


        profileimage=findViewById(R.id.profile_image);
        recievername=findViewById(R.id.recievername);
        msgadapter=findViewById(R.id.msgadapter);

        sendbtn=findViewById(R.id.sendbtn);
        et_msg=findViewById(R.id.et_msg);

        Picasso.get().load(RecieverImage).into(profileimage);
        recievername.setText(""+RecieverName);

        SenderUID=firebaseAuth.getUid();

        senderRoom=SenderUID+RecieverUID;
        senderRoom=RecieverUID+SenderUID;



        DatabaseReference reference=database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatreference=database.getReference().child("chats").child(senderRoom).child("messeges");

        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Messeges messeges=dataSnapshot.getValue(Messeges.class);
                    messegesArrayList.add(messeges);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sImage=snapshot.child("imageUri").getValue().toString();
                rImage=RecieverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messege=et_msg.getText().toString();
                if (messege.isEmpty())
                {
                    Toast.makeText(ChatActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
                    return;
                }
                et_msg.setText("");

                Date date=new Date();
                Messeges messeges=new Messeges(messege,SenderUID,date.getTime());

                database=FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messeges")
                        .push()
                        .setValue(messeges).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
//                                .child("recieverRoom")
                                .child("messeges")
                                .push().setValue(messeges).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });
    }
}