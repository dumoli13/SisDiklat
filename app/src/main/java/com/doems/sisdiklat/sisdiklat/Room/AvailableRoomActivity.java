package com.doems.sisdiklat.sisdiklat.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.doems.sisdiklat.sisdiklat.Adapter.RoomAdapter;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataRoom;
import com.doems.sisdiklat.sisdiklat.Model.ModelRoom;
import com.doems.sisdiklat.sisdiklat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AvailableRoomActivity extends AppCompatActivity {
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_emptyRoom) TextView tv_emptyRoom;
    @BindView(R.id.rv_room) RecyclerView rv_room;

    private ChildEventListener childEventListener;
    private Map<String, ModelRoom> roomMap = new LinkedHashMap<>();
    private RoomAdapter mAdapter;

    private String uID;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_room);
        ButterKnife.bind(this);
        tv_title.setText("AVAILABLE ROOM");

        uID = FirebaseAuth.getInstance().getUid();
        context = this.getApplicationContext();
        initRecycler();
    }

    private void initRecycler(){
        mAdapter = new RoomAdapter(roomMap, context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplication());
        rv_room.setLayoutManager(layoutManager);
        rv_room.setItemAnimator(new DefaultItemAnimator());
        rv_room.setAdapter(mAdapter);

        checkEmpty();
    }

    private void checkEmpty(){
        if(roomMap.size()>0){
            rv_room.setVisibility(View.VISIBLE);
            tv_emptyRoom.setVisibility(View.GONE);
        }
        else {
            rv_room.setVisibility(View.GONE);
            tv_emptyRoom.setVisibility(View.VISIBLE);
        }
    }

    private void initRoomList(){
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String key = dataSnapshot.getKey();
                final ModelRoom modelRoom = dataSnapshot.getValue(ModelRoom.class);
                roomMap.put(key, modelRoom);
                mAdapter.notifyDataSetChanged();
                checkEmpty();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                final String key = dataSnapshot.getKey();
                final ModelRoom modelRoom = dataSnapshot.getValue(ModelRoom.class);
                roomMap.put(key, modelRoom);
                mAdapter.notifyDataSetChanged();
                checkEmpty();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final String key = dataSnapshot.getKey();
                roomMap.remove(key);
                mAdapter.notifyDataSetChanged();
                checkEmpty();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        new FireDataRoom(uID).ref.addChildEventListener(childEventListener);
    }

    @OnClick(R.id.btn_addNew) public void addNew(){
        startActivity(new Intent(AvailableRoomActivity.this, AddRoomActivity.class));
    }

    @OnClick(R.id.btn_back) public void back(){
        onBackPressed();
    }

    @Override public void onStart() {
        super.onStart();
        initRoomList();
    }

    @Override public void onResume(){
        super.onResume();
        initRoomList();
    }

}
