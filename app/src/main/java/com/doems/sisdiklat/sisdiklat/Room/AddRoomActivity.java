package com.doems.sisdiklat.sisdiklat.Room;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doems.sisdiklat.sisdiklat.Firebase.FireDataRoom;
import com.doems.sisdiklat.sisdiklat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddRoomActivity extends AppCompatActivity {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_roomName) EditText et_roomName;
    @BindView(R.id.et_roomCapacity) EditText et_roomCapacity;

    private String uID;
    private String roomName;
    private String roomCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        ButterKnife.bind(this);
        tv_title.setText("ADD NEW ROOM");

        uID = FirebaseAuth.getInstance().getUid();
    }

    private void getValue(){
        roomName = et_roomName.getText().toString();
        roomCapacity = et_roomCapacity.getText().toString();
    }

    @OnClick(R.id.btn_addRoom) public void addRoom(){
        getValue();
        if(TextUtils.isEmpty(roomName)){
            Toast.makeText(getApplicationContext(), "Enter room name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(roomCapacity)){
            Toast.makeText(getApplicationContext(), "Enter room capacity", Toast.LENGTH_SHORT).show();
        }
        else{
            new FireDataRoom(uID).writeRoom(roomName, roomCapacity, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Toast.makeText(getApplicationContext(), "new room added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    @OnClick(R.id.btn_back) public void back(){
        finish();
    }

    @Override public void onStart() {
        super.onStart();
    }

}
