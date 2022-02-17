package com.waseemshahzad.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.waseemshahzad.chatapp.R;
import com.waseemshahzad.chatapp.Adapter.Users;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {
    TextView tv_signin,reg_name,reg_pass,reg_confirmpass,reg_email;
    CircleImageView profile_image;
    Button btn_signup;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri imageURI;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageUri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        setContentView(R.layout.activity_registration);
        tv_signin=findViewById(R.id.tv_signin);
        reg_name=findViewById(R.id.reg_name);
        reg_pass=findViewById(R.id.reg_pass);
        reg_confirmpass=findViewById(R.id.reg_confirmpass);
        reg_email=findViewById(R.id.reg_email);
        profile_image=findViewById(R.id.profile_image);
        btn_signup=findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                String name=reg_name.getText().toString();
                String email=reg_email.getText().toString();
                String pass=reg_pass.getText().toString();
                String confirmpass=reg_confirmpass.getText().toString();
                String status="Hi there I'm using Chat App";


                if (TextUtils.isEmpty(name)|| TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmpass)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Enter Valid Data", Toast.LENGTH_SHORT).show();
                }else if (!email.matches(emailPattern)) {
                    reg_email.setError("Please enter valid email");
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                }else if (!pass.equals(confirmpass)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }else if (pass.length()<6) {
                    progressDialog.dismiss();

                    Toast.makeText(RegistrationActivity.this, "Enter 6 chracter Password", Toast.LENGTH_SHORT).show();
                }else {

                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());
                                if (imageURI!=null)
                                {
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageUri=uri.toString();
                                                        Users users=new Users(auth.getUid(),name,email,imageUri,status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful())
                                                           {
                                                               startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                                                           }else {
                                                               Toast.makeText(RegistrationActivity.this, "Error in creating a new User", Toast.LENGTH_SHORT).show();
                                                           }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    String status="Hi there I'm using Chat App";
                                    imageUri="https://firebasestorage.googleapis.com/v0/b/chatapp-3c1c1.appspot.com/o/profile.png?alt=media&token=237b04a8-95ba-4319-a157-761363d96e47";
                                    Users users=new Users(auth.getUid(),name,email,imageUri,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                                            }else {
                                                Toast.makeText(RegistrationActivity.this, "Error in creating a new User", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            } }
                    });
                } }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10)
        {
            if (data!=null)
            {
                imageURI=data.getData();
                profile_image.setImageURI(imageURI);
            }
        }
    }
}