package com.doems.sisdiklat.sisdiklat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.doems.sisdiklat.sisdiklat.ActivityAccount.SignInActivity;
import com.doems.sisdiklat.sisdiklat.ActivityHome.HomeActivity;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

public class FlashActivity extends AppCompatActivity {

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        setContentView(R.layout.activity_flash);
        user = FirebaseAuth.getInstance().getCurrentUser();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(user != null){
                    startActivity(new Intent(FlashActivity.this, HomeActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(FlashActivity.this, SignInActivity.class));
                    finish();
                }
            }
        },1000);
    }
}
