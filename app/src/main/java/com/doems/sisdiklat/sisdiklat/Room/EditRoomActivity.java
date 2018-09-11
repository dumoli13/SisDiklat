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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditRoomActivity extends AppCompatActivity {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_roomName) EditText et_roomName;
    @BindView(R.id.et_roomCapacity) EditText et_roomCapacity;

    private String uID;
    private String roomKey;
    private String roomName;
    private String roomCapacity;

    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);
        ButterKnife.bind(this);
        tv_title.setText("MODIFY ROOM");

        uID = FirebaseAuth.getInstance().getUid();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                roomKey = null;
                roomName = null;
                roomCapacity = null;
            } else {
                roomKey = extras.getString("messageKey");
                roomName = extras.getString("messageName");
                roomCapacity = extras.getString("messageCap");
            }
        }
        else {
            roomKey = (String) savedInstanceState.getSerializable("messageKey");
            roomName = (String) savedInstanceState.getSerializable("messageName");
            roomCapacity = (String) savedInstanceState.getSerializable("messageCap");
        }

        et_roomName.setText(roomName);
        et_roomCapacity.setText(roomCapacity);
    }

    private void getValue(){
        roomName = et_roomName.getText().toString();
        roomCapacity = et_roomCapacity.getText().toString();
    }

    @OnClick(R.id.btn_saveChange) public void saveChange(){
        getValue();
        if(TextUtils.isEmpty(roomName)){
            Toast.makeText(getApplicationContext(), "Enter room name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(roomCapacity)){
            Toast.makeText(getApplicationContext(), "Enter room capacity", Toast.LENGTH_SHORT).show();
        }
        else{
            new FireDataRoom(uID).modifyRoom(roomKey, roomName, roomCapacity, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
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
