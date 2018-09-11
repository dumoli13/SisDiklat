package com.doems.sisdiklat.sisdiklat.ActivityAccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doems.sisdiklat.sisdiklat.ActivityHome.HomeActivity;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataAccount;
import com.doems.sisdiklat.sisdiklat.Model.ModelAccount;
import com.doems.sisdiklat.sisdiklat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfilActivity extends AppCompatActivity {

    @BindView(R.id.tv_email) TextView tv_email;
    @BindView(R.id.tv_phone) TextView tv_phone;
    @BindView(R.id.et_oldPassword) EditText et_oldPassword;
    @BindView(R.id.et_password1) EditText et_password1;
    @BindView(R.id.et_password2) EditText et_password2;
    @BindView(R.id.tv_title) TextView tv_title;

    private ValueEventListener valueEventListener;
    private FirebaseUser user;
    private String uID;
    private String email;
    private String phone;
    private String oldPassword;
    private String password1;
    private String password2;
    private String savedOldPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();
        tv_title.setText("ACCOUNT");
    }

    private void getAccoutData(){
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ModelAccount account = dataSnapshot.getValue(ModelAccount.class);

                tv_email.setText(account.getEmail());
                tv_phone.setText(account.getPhone());
                savedOldPassword = account.getPassword();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        new FireDataAccount(uID).ref.addListenerForSingleValueEvent(valueEventListener);
    }

    private void updateAuthPassword(){
        user.updatePassword(password1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    updateDatabasePassword();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error. Please log out the log in again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateDatabasePassword(){
        new FireDataAccount(uID).writePassword(password1, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError==null){
                    Toast.makeText(getApplicationContext(),"Update success", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error. Please log out the log in again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @OnClick(R.id.btn_editPhone) public void editPhone(){

        startActivity(new Intent(ProfilActivity.this, EditPhoneActivity.class));
    }

    @OnClick(R.id.btn_changePassword) public void changePassword(){
        oldPassword = et_oldPassword.getText().toString();
        password1 = et_password1.getText().toString();
        password2 = et_password2.getText().toString();

        if(TextUtils.isEmpty(oldPassword)){
            Toast.makeText(this, "enter your old password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password1)){
            Toast.makeText(this, "enter your new password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password2)){
            Toast.makeText(this, "reenter your new password", Toast.LENGTH_SHORT).show();
        }
        else if(!password1.equals(password2)){
            Toast.makeText(this, "new password didn't match", Toast.LENGTH_SHORT).show();
        }
        else if(password1.length()<6){
            Toast.makeText(this, "password minimum character: 6", Toast.LENGTH_LONG).show();
        }
        else{
            updateAuthPassword();
            et_oldPassword.setText("");
            et_password1.setText("");
            et_password2.setText("");
        }
    }

    @OnClick(R.id.btn_back) public void back(){
        onBackPressed();
    }

    @OnClick(R.id.btn_logOut) public void logOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ProfilActivity.this, SignInActivity.class));
        finish();
    }

    @Override public void onStart() {
        super.onStart();
        getAccoutData();
    }
}
