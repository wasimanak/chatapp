package com.waseemshahzad.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String RecieverImage,RecieverUID,RecieverName;
    CircleImageView profileimage;
    TextView recievername;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String sImage;
    public static String rImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        RecieverName=getIntent().getStringExtra("name");
        RecieverImage=getIntent().getStringExtra("RecieverImage");
        RecieverUID=getIntent().getStringExtra("uid");


        profileimage=findViewById(R.id.profile_image);
        recievername=findViewById(R.id.recievername);

        Picasso.get().load(RecieverImage).into(profileimage);
        recievername.setText(""+RecieverName);

        DatabaseReference reference=database.getReference().child("user").child(firebaseAuth.getUid());

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

    }
}