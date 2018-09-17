package com.doems.sisdiklat.sisdiklat.ActivitySchedule;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doems.sisdiklat.sisdiklat.Firebase.FireDataRoom;
import com.doems.sisdiklat.sisdiklat.Model.ModelRoom;
import com.doems.sisdiklat.sisdiklat.R;
import com.doems.sisdiklat.sisdiklat.Room.AddRoomActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class setRoomActivity extends AppCompatActivity {

    @BindView(R.id.ll_room) LinearLayout ll_room;
    @BindView(R.id.tv_noRoom) TextView tv_noRoom;

    private String uID;

    private List<ModelRoom> roomList = new ArrayList<>();
    private ValueEventListener roomEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_room);
        ButterKnife.bind(this);
        uID = FirebaseAuth.getInstance().getUid();
        checkEmpty();
    }

    private void checkEmpty(){
        if(roomList.size()>0){
            tv_noRoom.setVisibility(View.GONE);
            ll_room.setVisibility(View.VISIBLE);
        }
        else{
            tv_noRoom.setVisibility(View.VISIBLE);
            ll_room.setVisibility(View.GONE);
        }
    }

    private void initList(){
        ll_room.removeAllViews();
        ll_room.removeAllViewsInLayout();
        roomEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    final ModelRoom _modelRoom = postSnapshot.getValue(ModelRoom.class);
                    final String key = postSnapshot.getKey();
                    roomList.add(_modelRoom);

                    LayoutInflater _canvasRoom = LayoutInflater.from(getApplicationContext());
                    View canvasRoom = _canvasRoom.inflate(R.layout.card_setup_room, null, false);
                    LinearLayout layout_cardSetRoom = (LinearLayout) canvasRoom.findViewById(R.id.layout_cardSetRoom);
                    TextView tv_roomName = (TextView) canvasRoom.findViewById(R.id.tv_roomName);
                    TextView tv_roomCapacity = (TextView) canvasRoom.findViewById(R.id.tv_roomCapacity);

                    tv_roomName.setText(_modelRoom.getName());
                    tv_roomCapacity.setText("capacity: " + _modelRoom.getCapacity());
                    layout_cardSetRoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("roomID", key);
                            intent.putExtra("roomCap", _modelRoom.getCapacity());
                            intent.putExtra("roomName", _modelRoom.getName());
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    });
                    ll_room.addView(canvasRoom);
                    ll_room.requestLayout();
                }
                checkEmpty();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        new FireDataRoom(uID).ref.addValueEventListener(roomEventListener);
    }

    @OnClick(R.id.tv_addRoom) public void addNewRoom(){
        Intent intent = new Intent(this, AddRoomActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_close) public void close(){
        onBackPressed();
    }

    @Override public void onStart() {
        super.onStart();
        initList();
    }
}
