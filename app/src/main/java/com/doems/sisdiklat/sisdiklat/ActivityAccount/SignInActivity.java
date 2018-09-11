package com.doems.sisdiklat.sisdiklat.ActivityAccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doems.sisdiklat.sisdiklat.ActivityHome.HomeActivity;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataAccount;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataEmail;
import com.doems.sisdiklat.sisdiklat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.et_username) EditText et_username;
    @BindView(R.id.et_password) EditText et_password;

    String email;
    String password;
    String uID;

    List<String> emailList = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private ValueEventListener emailEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user != null){
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
            Log.d("tsss", "current user: "+user.getEmail());
        }
        getEmail();
    }

    private void getEmail(){
        emailEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String tempEmail = postSnapshot.getValue().toString();
                    emailList.add(tempEmail);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        new FireDataEmail().ref.addValueEventListener(emailEventListener);
        Log.d("tsss", "email in database" + String.valueOf(emailList.size()));
    }

    private void setValue(){
        email = et_username.getText().toString();
        password = et_password.getText().toString();
    }

    @OnClick(R.id.btn_signIn) public void signIn(){
        setValue();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter your email", Toast.LENGTH_SHORT).show();
        }
        else if(!TextUtils.isEmpty(email)){
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);

            if(!matcher.matches()){
                Toast.makeText(this,"email format invalid", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        }
        else{
            if(emailList.size()==0){
                Toast.makeText(this, "Email not registered", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                for(int i=0;i<emailList.size(); i++){
                    if(email.equals(emailList.get(i))){
                        try{
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        uID = mAuth.getCurrentUser().getUid();
                                        email = mAuth.getCurrentUser().getEmail();
                                        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(SignInActivity.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        catch (Throwable e){

                        }
                    }
                    if(i==emailList.size()-1){
                        if(!email.equals(emailList.get(i))){
                            if(FirebaseAuth.getInstance().getCurrentUser()==null){
                                Toast.makeText(this, "Email not registered", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @OnClick(R.id.btn_signUp) public void signUp(){
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    @OnClick(R.id.reset_hint) public void resetPassword(){
        email = et_username.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Type your email first", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "check your email", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onStart() {
        super.onStart();

        if(user != null){
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
        }
    }
}
