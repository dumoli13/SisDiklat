package com.doems.sisdiklat.sisdiklat.ActivitySchedule;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.doems.sisdiklat.sisdiklat.Firebase.FireDataRoom;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataSchedule;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataUnavailableTime;
import com.doems.sisdiklat.sisdiklat.Model.ModelRoom;
import com.doems.sisdiklat.sisdiklat.Model.ModelUnavailableDate;
import com.doems.sisdiklat.sisdiklat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewScheduleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.btn_back) ImageView btn_back;
    @BindView(R.id.et_event) EditText et_event;
    @BindView(R.id.tv_room) TextView tv_room;
    @BindView(R.id.et_speaker) EditText et_speaker;
    @BindView(R.id.et_participant) EditText et_participant;
    @BindView(R.id.checkBox) CheckBox checkBox;
    @BindView(R.id.tv_startDate) TextView tv_startDate;
    @BindView(R.id.ll_endDate) RelativeLayout ll_endDate;
    @BindView(R.id.tv_endDate)TextView tv_endDate;

    private String uID;
    private String event;
    private String roomID;
    private String roomCap;
    private String roomName;
    private String speaker;
    private String participant;
    private Date startDate;
    private Date endDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d yyyy", Locale.US);
    private boolean checked=false;

    private int checkStartDate;
    private int checkEndDate;
    DatePickerDialog datePickerDialog1;
    DatePickerDialog datePickerDialog2;
    private ValueEventListener dateEventListener;
    List<Calendar> _listCalendar = new ArrayList<>();
    Calendar calendar1 = Calendar.getInstance();
    Calendar[] unavailableCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);
        ButterKnife.bind(this);
        ll_endDate.setVisibility(View.GONE);
        uID = FirebaseAuth.getInstance().getUid();
    }

    private void prepareDateList(){
        if(roomID!=null){
            dateEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    _listCalendar.clear();
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ModelUnavailableDate modelUnavailableDate = postSnapshot.getValue(ModelUnavailableDate.class);
                        if(modelUnavailableDate!=null){
                            List<Date> _listDate = modelUnavailableDate.getDate();
                            for(Date date : _listDate){
                                Calendar _calendar = Calendar.getInstance();
                                _calendar.setTime(date);
                                _listCalendar.add(_calendar);
                            }
                        }
                    }
                    unavailableCalendar = new Calendar[_listCalendar.size()];
                    unavailableCalendar = _listCalendar.toArray(unavailableCalendar);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            };
            new FireDataUnavailableTime(uID).ref.child(roomID).addValueEventListener(dateEventListener);
        }
    }

    @OnClick(R.id.ll_room) public void pickRoom(){
        Intent intent = new Intent(this, setRoomActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.ll_startDate) public void pickStartDate(){
        Calendar _calendar = Calendar.getInstance();
        _calendar.setTime(new Date(System.currentTimeMillis()));
        datePickerDialog1 = DatePickerDialog.newInstance(this,
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH));
        datePickerDialog1.show(getFragmentManager(), "datePickerDialog");
        datePickerDialog1.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog1.setMinDate(_calendar);
        if(unavailableCalendar!=null) datePickerDialog1.setDisabledDays(unavailableCalendar);
    }

    @OnClick(R.id.ll_endDate) public void pickEndDate(){
        if(startDate==null){
            Toast.makeText(getApplicationContext(), "Set start date first", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Calendar _calendar = Calendar.getInstance();
            _calendar.setTime(startDate);
            _calendar.add(Calendar.DATE,1);
            datePickerDialog2 = DatePickerDialog.newInstance(this,
                    calendar1.get(Calendar.YEAR),
                    calendar1.get(Calendar.MONTH),
                    calendar1.get(Calendar.DAY_OF_MONTH));
            datePickerDialog2.show(getFragmentManager(), "datePickerDialog");
            datePickerDialog1.setVersion(DatePickerDialog.Version.VERSION_1);
            datePickerDialog2.setMinDate(_calendar);
            if(unavailableCalendar!=null) datePickerDialog2.setDisabledDays(unavailableCalendar);
        }
    }

    @OnClick(R.id.btn_create) public void createNew(){
        event = et_event.getText().toString();
        speaker = et_event.getText().toString();
        participant = et_participant.getText().toString();

        if(TextUtils.isEmpty(event)){
            Toast.makeText(this, "Enter your event name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(speaker)){
            Toast.makeText(this, "Enter the speaker", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(participant)){
            Toast.makeText(this, "Enter the participant", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(startDate==null){
            Toast.makeText(this, "Enter event's date", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(checked && endDate==null){
            Toast.makeText(this, "Enter event's end date", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            if(!checked) endDate=null;
            final ArrayList<Date> setNewUnavailable = new ArrayList<>();
            Calendar _calendar = Calendar.getInstance();
            setNewUnavailable.add(startDate);

            if(endDate!=null){
                for(int i=0;i>checkStartDate-checkEndDate;i--) {
                    _calendar.setTime(new Date(endDate.getTime()));
                    _calendar.add(Calendar.DATE,i);
                    setNewUnavailable.add(new Date(_calendar.getTimeInMillis()));
                }
            }

            new FireDataSchedule(uID).writeSchedule(event, roomID, roomCap, roomName, speaker, participant, startDate, endDate, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    new FireDataUnavailableTime(uID).writeUnavailable(roomID, databaseReference.getKey(), setNewUnavailable, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            finish();
                        }
                    });
                }
            });
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1){
            if(resultCode == Activity.RESULT_OK){
                roomID = data.getStringExtra("roomID");
                roomCap = data.getStringExtra("roomCap");
                roomName = data.getStringExtra("roomName");
                tv_room.setText(roomName+" (capacity: "+roomCap+")");
                prepareDateList();

                Calendar _calendar = Calendar.getInstance();
                if (startDate != null) {
                    _calendar.setTime(startDate);
                    if(_listCalendar.contains(_calendar)) {
                        startDate=null;
                        tv_startDate.setText("choose start date");
                    }
                }
                if (endDate != null) {
                    _calendar.setTime(endDate);
                    if(_listCalendar.contains(_calendar)) {
                        endDate=null;
                        tv_endDate.setText("choose end date");
                    }
                }
            }
        }
    }

    @OnClick(R.id.btn_back) public void back(){
        onBackPressed();
    }

    @Override
    public void onStart(){
        super.onStart();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checked=true;
                    ll_endDate.setVisibility(View.VISIBLE);
                }
                else{
                    checked=false;
                    ll_endDate.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if(view==datePickerDialog1) {
            startDate = new Date(year-1900, monthOfYear, dayOfMonth);
            checkStartDate = year*10000+monthOfYear*100+dayOfMonth;
            tv_startDate.setText(sdf.format(startDate));
        }

        else if(view==datePickerDialog2) {
            endDate = new Date(year-1900, monthOfYear, dayOfMonth);
            checkEndDate = year*10000+monthOfYear*100+dayOfMonth;
            tv_endDate.setText(sdf.format(endDate));
        }
    }

}
