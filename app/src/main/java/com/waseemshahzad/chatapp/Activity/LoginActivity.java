package com.waseemshahzad.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.waseemshahzad.chatapp.R;

public class LoginActivity extends AppCompatActivity {
    EditText login_email, login_password;
    Button btn_signin;
    TextView tv_signup;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();


        tv_signup = findViewById(R.id.tv_signup);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        btn_signin = findViewById(R.id.btn_signin);

       btn_signin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               progressDialog.show();

               String email = login_email.getText().toString();
               String password = login_password.getText().toString();
               if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                   progressDialog.dismiss();

                   Toast.makeText(LoginActivity.this, "Enter Valid data", Toast.LENGTH_SHORT).show();

               } else if (!email.matches(emailPattern)) {
                   progressDialog.dismiss();

                   login_email.setError("Invalid Email");
                   Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
               } else if (password.length() < 6) {
                   progressDialog.dismiss();

                   login_password.setError("Invalid Password");
         Toast.makeText(LoginActivity.this, "Minimum 6 chractors Password", Toast.LENGTH_SHORT).show();
               }
               auth.signInWithEmailAndPassword(email, password)
                       .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                   @Override
                   public void onSuccess(AuthResult authResult) {
                       FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                       if (user.isEmailVerified()){
                           startActivity(new Intent(LoginActivity.this, MainActivity.class));
                           finish();
                           Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                       }else {
                           user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {

                               @Override
                               public void onSuccess(Void unused) {
                                   progressDialog.dismiss();
               Toast.makeText(LoginActivity.this, "Verify Your Email", Toast.LENGTH_SHORT).show();
                               }
                           });
                       }
                   }
               });

               auth.signInWithEmailAndPassword(email, password)
                       .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           progressDialog.dismiss();
                           startActivity(new Intent(LoginActivity.this, MainActivity.class));
                           finish();

                       } else {
                           progressDialog.dismiss();
           Toast.makeText(LoginActivity.this, "Eroor in Login", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
           }
       });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });

    }
}