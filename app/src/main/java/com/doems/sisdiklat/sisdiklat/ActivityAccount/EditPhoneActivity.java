package com.doems.sisdiklat.sisdiklat.ActivityAccount;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doems.sisdiklat.sisdiklat.Firebase.FireData;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataAccount;
import com.doems.sisdiklat.sisdiklat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPhoneActivity extends AppCompatActivity {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_phone) EditText et_phone;

    private String phone;
    private String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        ButterKnife.bind(this);

        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FireData.getRefDatabase();

        tv_title.setText("EDIT PHONE");
    }

    @OnClick(R.id.btn_back) public void back(){
        onBackPressed();
    }

    @OnClick(R.id.btn_saveChange) public void save(){
        phone = et_phone.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "phone number is empty", Toast.LENGTH_SHORT).show();
        }
        else{
            new FireDataAccount(uID).writePhone(phone, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError==null){
                        finish();
                    }
                    else{
                        Toast.makeText(EditPhoneActivity.this,"Error while updating. Try again later", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
