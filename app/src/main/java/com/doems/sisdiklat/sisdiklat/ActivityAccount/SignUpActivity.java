package com.doems.sisdiklat.sisdiklat.ActivityAccount;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.et_email) EditText et_email;
    @BindView(R.id.et_password1) EditText et_password1;
    @BindView(R.id.et_password2) EditText et_password2;
    @BindView(R.id.et_phone) EditText et_phone;

    private String email;
    private String password1;
    private String password2;
    private String phone;
    private ProgressDialog pDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        pDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
    }

    private void setValue(){
        email = et_email.getText().toString();
        password1 = et_password1.getText().toString();
        password2 = et_password2.getText().toString();
        phone = et_phone.getText().toString();
    }

    @OnClick(R.id.btn_signUp) public void signUp(){
        pDialog.setMessage("signing up...");
        pDialog.show();
        setValue();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
        }
        else{
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);

            if(!matcher.matches()){
                Toast.makeText(this,"email format invalid", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(TextUtils.isEmpty(password1)){
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        }
        else if(password1.length()<6){
            Toast.makeText(this, "Password minimum charcter: 6", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password2)){
            Toast.makeText(this, "Reenter your password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Enter your phone", Toast.LENGTH_SHORT).show();
        }
        else if(!password1.equals(password2)){
            Toast.makeText(this, "your password didn't match", Toast.LENGTH_SHORT).show();
        }
        else{
            try{
                mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final FirebaseUser user = mAuth.getCurrentUser();
                            String uID = user.getUid();
                            new FireDataAccount(uID).writeAccount(uID, email, password1, phone, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError == null){
                                        new FireDataEmail().writeEmail(email, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                pDialog.hide();

                                                Toast.makeText(SignUpActivity.this,"Account Successfully Registered", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(SignUpActivity.this, "1. Registration error, try again later", Toast.LENGTH_SHORT).show();
                                        Log.d("tsss", databaseError.getDetails());
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "2. Registration error, try again later", Toast.LENGTH_SHORT).show();
                            Log.d("tsss", String.valueOf(task.getException()));
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tsss", e.getMessage());
                    }
                });
            }
            catch(Throwable e){
                e.printStackTrace();
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Ërror");
                alertDialog.setMessage("Aplication running on Android 4.4 and above");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
