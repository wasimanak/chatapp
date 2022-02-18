package com.waseemshahzad.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.waseemshahzad.chatapp.R;
import com.waseemshahzad.chatapp.ModelClass.UserAdapter;
import com.waseemshahzad.chatapp.Adapter.Users;

import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView mainuserrecyclerview;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView img_logout, setting;
    TextView btn_shareapp,btn_moreapps,btn_rateus;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        btn_rateus=findViewById(R.id.btn_rateus);
        btn_moreapps=findViewById(R.id.btn_moreapps);
        btn_shareapp=findViewById(R.id.btn_shareapp);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        usersArrayList = new ArrayList<>();

        DatabaseReference reference = database.getReference().child("user");
        Toast.makeText(this, "Fetching data Please wait...", Toast.LENGTH_SHORT).show();
        progressDialog.show();
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        img_logout = findViewById(R.id.img_logout);
        setting = findViewById(R.id.setting);

        mainuserrecyclerview = findViewById(R.id.mainuserrecyclerview);
        mainuserrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(MainActivity.this, usersArrayList);
        mainuserrecyclerview.setAdapter(adapter);

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this, R.style.Dialog);

                dialog.setContentView(R.layout.dialoge_layout);

                TextView yesbtn,nobtn;

                yesbtn = dialog.findViewById(R.id.yesbtn);
                nobtn = dialog.findViewById(R.id.nobtn);

                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });
                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Setting_Activity.class));
            }
        });


        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        btn_moreapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        btn_rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRateUs = new Intent(Intent.ACTION_VIEW);
                intentRateUs.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(intentRateUs);
            }
        });
        btn_shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                intentShare.setType("text/plain");
                startActivity(Intent.createChooser(intentShare,"settings_share"));
            }
        });

    }
    public void showDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.contactus_dialoge);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dilog_bg);
        ImageView iv_call,iv_facebook,iv_google,iv_youtube;

        dialog.show();

        Button btn_close = dialog.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        iv_call=dialog.findViewById(R.id.iv_call);
        iv_facebook=dialog.findViewById(R.id.iv_facebook);
        iv_google=dialog.findViewById(R.id.iv_google);
        iv_youtube=dialog.findViewById(R.id.iv_youtube);

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String defultapp = "WhatsApp";

                try {
                    String url = "https://api.whatsapp.com/send?phone=+923012861054" +
                            "&text=" + URLEncoder.encode("HELLO Kalma App TEAM !  ", "UTF-8");

                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setAction(Intent.ACTION_VIEW);

                    if (defultapp.equalsIgnoreCase("WhatsApp")) {
                        sendIntent.setPackage("com.whatsapp");
                        Log.e("message", "WhatsApp");
                    }
                    sendIntent.setData(Uri.parse(url));
                    startActivity(sendIntent);

                } catch (Exception e) {
                    Log.e("message", e.getMessage());
                }
            }
        });
        iv_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+"wasimanak@gmail.com"));
//                intent.putExtra(Intent.EXTRA_SUBJECT, "KSK User");
                intent.putExtra(Intent.EXTRA_SUBJECT, "KSK USER");
                intent.putExtra(Intent.EXTRA_EMAIL, "\nThis email from Kalma App USER");

                startActivity(intent);





            }
        });
        iv_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                String url="https://www.youtube.com/c/WasiManak";
                try {
                    intent =new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            }
        });
        iv_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String YourPageURL = "https://www.facebook.com/CEdiscoveries";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));

                startActivity(browserIntent);
            }
        });

    }
}