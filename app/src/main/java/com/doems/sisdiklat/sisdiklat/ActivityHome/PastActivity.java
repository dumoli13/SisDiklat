package com.doems.sisdiklat.sisdiklat.ActivityHome;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doems.sisdiklat.sisdiklat.Adapter.PastAdapter;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataSchedule;
import com.doems.sisdiklat.sisdiklat.Model.ModelSchedule;
import com.doems.sisdiklat.sisdiklat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PastActivity extends Fragment {

    @BindView(R.id.tv_emptySchedule) TextView tv_emptySchedule;
    @BindView(R.id.recyclerview_schedule)RecyclerView recyclerview_schedule;

    private ChildEventListener childEventListener;
    private Map<String, ModelSchedule> pastListMap = new LinkedHashMap<>();
    private PastAdapter mAdapter;

    private View view;
    private String uID;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_past, container, false);
        ButterKnife.bind(this, view);
        uID = FirebaseAuth.getInstance().getUid();
        context = this.getContext();

        initRecycler();
        return view;
    }

    private void initRecycler(){
        mAdapter = new PastAdapter(pastListMap, context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview_schedule.setLayoutManager(layoutManager);
        recyclerview_schedule.setItemAnimator(new DefaultItemAnimator());
        recyclerview_schedule.setAdapter(mAdapter);

        checkEmpty();
    }

    private void checkEmpty(){
        if(pastListMap.size()>0){
            tv_emptySchedule.setVisibility(View.GONE);
            recyclerview_schedule.setVisibility(View.VISIBLE);
        }
        else{

            tv_emptySchedule.setVisibility(View.VISIBLE);
            recyclerview_schedule.setVisibility(View.GONE);
        }
    }

    private void initList(){
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("tsss",String.valueOf(System.currentTimeMillis()));
                String key = dataSnapshot.getKey();
                ModelSchedule schedule= dataSnapshot.getValue(ModelSchedule.class);
                if(schedule!=null){
                    Date startDate = schedule.getStartDate();

                    if(System.currentTimeMillis()>startDate.getTime()){
                        Date endDate=null;
                        if(schedule.getEndDate()!=null) endDate = schedule.getEndDate();
                        String roomID = schedule.getRoomID();
                        String roomName = schedule.getRoomName();
                        String event = schedule.getEvent();
                        String participant = schedule.getParticipant();
                        String speaker = schedule.getSpeaker();
                        pastListMap.put(key, new ModelSchedule(key, event, roomID, roomName, speaker,
                                participant, startDate, endDate));
                        mAdapter.notifyDataSetChanged();
                        checkEmpty();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                ModelSchedule schedule= dataSnapshot.getValue(ModelSchedule.class);
                Log.d("tsss schedule ",schedule.toString());
                if(schedule!=null){
                    Date startDate = schedule.getStartDate();

                    if(System.currentTimeMillis()>startDate.getTime()){
                        Date endDate=null;
                        if(schedule.getEndDate()!=null) endDate = schedule.getEndDate();
                        String roomID = schedule.getRoomID();
                        String roomName = schedule.getRoomName();
                        String event = schedule.getEvent();
                        String participant = schedule.getParticipant();
                        String speaker = schedule.getSpeaker();
                        pastListMap.put(key, new ModelSchedule(key, event, roomID, roomName, speaker,
                                participant, startDate, endDate));
                        mAdapter.notifyDataSetChanged();
                        checkEmpty();
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                pastListMap.remove(key);
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
        new FireDataSchedule(uID).ref.orderByChild(FireDataSchedule.STARTDATE).addChildEventListener(childEventListener);
    }

    @Override public void onStart() {
        super.onStart();
        initList();
    }

    @Override public void onResume(){
        super.onResume();
        initList();
    }
}
