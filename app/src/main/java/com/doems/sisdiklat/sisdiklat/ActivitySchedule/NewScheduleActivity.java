package com.doems.sisdiklat.sisdiklat.ActivitySchedule;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewScheduleActivity extends AppCompatActivity implements OnItemSelectedListener{

    @BindView(R.id.btn_back) ImageView btn_back;
    @BindView(R.id.et_event) EditText et_event;
    @BindView(R.id.spinner_room) Spinner spinner_room;
    @BindView(R.id.et_speaker) EditText et_speaker;
    @BindView(R.id.et_participant) EditText et_participant;
    @BindView(R.id.checkBox) CheckBox checkBox;
    @BindView(R.id.tv_startDate) TextView tv_startDate;
    @BindView(R.id.ll_endDate) RelativeLayout ll_endDate;
    @BindView(R.id.tv_endDate)TextView tv_endDate;

    private String uID;
    private String event;
    private String roomID;
    private String roomName;
    private String speaker;
    private String participant;
    private Date startDate;
    private Date endDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d yyyy", Locale.US);
    private SimpleDateFormat calendarFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private boolean checked=false;
    ArrayAdapter<String> dataAdapter;

    private int checkStartDate;
    private int checkEndDate;

    private ValueEventListener roomEventListener;
    private ValueEventListener dateEventListener;
    List<ModelRoom> allRoomList = new ArrayList<>();
    List<String> allRoomList1 = new ArrayList<>();
    List<Integer> dateList = new ArrayList<>();
    Calendar calendar1 = Calendar.getInstance();
    Calendar[] unavailableCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);
        ButterKnife.bind(this);
        ll_endDate.setVisibility(View.GONE);
        calendar1.setTime(new Date(System.currentTimeMillis()));
        calendar1.add(Calendar.DATE,1);
        startDate = calendar1.getTime();
        tv_startDate.setText(sdf.format(startDate));

        uID = FirebaseAuth.getInstance().getUid();
        prepareRoomList();

    }

    private void prepareRoomList(){
        if(allRoomList.size()!=0) allRoomList.clear();
        if(allRoomList1.size()!=0) allRoomList1.clear();
        roomEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot  postSnapshot : dataSnapshot.getChildren()){
                    String roomID = postSnapshot.getKey();
                    ModelRoom _modelRoom = postSnapshot.getValue(ModelRoom.class);
                    if(_modelRoom !=null){
                        ModelRoom modelRoom = new ModelRoom(roomID, _modelRoom.getName(), _modelRoom.getCapacity());
                        allRoomList.add(modelRoom);
                        allRoomList1.add(modelRoom.getName()+" (capacity: "+modelRoom.getCapacity()+")");
                        dataAdapter = new ArrayAdapter<>(getApplicationContext(),
                                R.layout.spinner_item, allRoomList1);
                        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner_room.setAdapter(dataAdapter);
                        spinner_room.setSelection(dataAdapter.getPosition(roomName));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        new FireDataRoom(uID).ref.addValueEventListener(roomEventListener);
        spinner_room.setOnItemSelectedListener(this);
    }

    private void prepareDateList(){
        if(roomID!=null){
            dateEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ModelUnavailableDate modelUnavailableDate = dataSnapshot.getValue(ModelUnavailableDate.class);
                    if(modelUnavailableDate!=null){
                        List<Date> _listDate = modelUnavailableDate.getDate();
                        List<Calendar> _listCalendar = new ArrayList<>(_listDate.size());
                        for(Date date : _listDate){
                            Calendar _calendar = Calendar.getInstance();
                            _calendar.setTime(date);
                            _listCalendar.add(_calendar);
                        }
                        unavailableCalendar = new Calendar[_listCalendar.size()];
                        unavailableCalendar = _listCalendar.toArray(unavailableCalendar);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            };
            new FireDataUnavailableTime(uID).ref.child(roomID).addValueEventListener(dateEventListener);
        }
    }

//    private boolean checkAvailableDate (int check){
//        boolean[] result = {true};
//        if(unavailableDateList.size()>0){
//            for(int i=0;i<unavailableDateList.size();i++){
//                if(unavailableDateList.get(i)==check){
//                    result[0] = false;
//                    break;
//                }
//            }
//        }
//        return result[0];
//    }

//    DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {
//
//        @Override
//        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//            calendar1.set(Calendar.YEAR, year);
//            calendar1.set(Calendar.MONTH, monthOfYear);
//            calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//            startDate = calendar1.getTime();
//            checkStartDate = startDate.getYear()*10000+startDate.getMonth()*100+startDate.getDate();
//            if(checkAvailableDate(checkStartDate)){
//                tv_startDate.setText(sdf.format(startDate));
//            }
//            else{
//                Toast.makeText(getApplicationContext(), "Room unavailable in this time", Toast.LENGTH_SHORT).show();
//                pickStartDate();
//            }
//        }
//    };
//
//    DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
//
//        @Override
//        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//            calendar1.set(Calendar.YEAR, year);
//            calendar1.set(Calendar.MONTH, monthOfYear);
//            calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//            endDate = calendar1.getTime();
//            checkEndDate = endDate.getYear()*10000+endDate.getMonth()*100+endDate.getDate();
//
//            if(endDate.getTime()>startDate.getTime()){
//                if(checkAvailableDate(checkEndDate)){
//                    tv_endDate.setText(sdf.format(endDate));
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "Room unavailable in this time", Toast.LENGTH_SHORT).show();
//                    pickEndDate();
//                }
//            }
//            else{
//                Toast.makeText(getApplicationContext(), "choose another date", Toast.LENGTH_SHORT).show();
//                pickEndDate();
//            }
//        }
//    };
//
    @OnClick(R.id.ll_startDate) public void pickStartDate(){
        Calendar _calendar = Calendar.getInstance();
        _calendar.setTime(new Date(System.currentTimeMillis()));
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance((DatePickerDialog.OnDateSetListener) getApplication(),
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(getFragmentManager(), "datePickerDialog");
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(_calendar);
        if(unavailableCalendar!=null){
            datePickerDialog.setDisabledDays(unavailableCalendar);
        }

        startDate = calendar1.getTime();
        tv_startDate.setText(sdf.format(startDate));

//        DatePickerDialog datePickerDialog = new DatePickerDialog(NewScheduleActivity.this, startDatePicker,
//                calendar1.get(Calendar.YEAR),
//                calendar1.get(Calendar.MONTH),
//                calendar1.get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
//        datePickerDialog.show();
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
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance((DatePickerDialog.OnDateSetListener) this,
                    calendar1.get(Calendar.YEAR),
                    calendar1.get(Calendar.MONTH),
                    calendar1.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show(getFragmentManager(), "datePickerDialog");
            datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
            datePickerDialog.setMinDate(_calendar);
            if(unavailableCalendar!=null){
                datePickerDialog.setDisabledDays(unavailableCalendar);
            }

            endDate = calendar1.getTime();
            checkEndDate = endDate.getYear()*10000+endDate.getMonth()*100+endDate.getDate();

            if(endDate.getTime()>startDate.getTime()){
                tv_endDate.setText(sdf.format(endDate));
            }
            else{
                Toast.makeText(getApplicationContext(), "choose another date", Toast.LENGTH_SHORT).show();
                pickEndDate();
            }
        }
//            prepareDateList();
//            DatePickerDialog datePickerDialog2 = new DatePickerDialog(NewScheduleActivity.this, endDatePicker,
//                    calendar1.get(Calendar.YEAR),
//                    calendar1.get(Calendar.MONTH),
//                    calendar1.get(Calendar.DAY_OF_MONTH));
//            datePickerDialog2.getDatePicker().setMinDate(startDate.getTime());
//            datePickerDialog2.show();
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
            Toast.makeText(this, "Enter event's start time", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            final ArrayList<Date> setNewUnavailable = new ArrayList<>();
            Calendar _calendar = Calendar.getInstance();

            setNewUnavailable.add(startDate);
            if(endDate!=null){
                checkStartDate = (startDate.getYear()+1900)*10000+(startDate.getMonth()+1)*100+startDate.getDate();
                checkEndDate = endDate.getYear()*10000+(endDate.getMonth()+1)*100+endDate.getDate();
                for(int i=0;i>checkEndDate-checkStartDate-1;i--) {
                    _calendar.setTime(new Date(startDate.getTime()));
                    _calendar.add(Calendar.DATE,i);
                    setNewUnavailable.add(new Date(_calendar.getTimeInMillis()));
                }
            }

            new FireDataSchedule(uID).writeSchedule(event, roomID, roomName, speaker, participant, startDate, endDate, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    new FireDataUnavailableTime(uID).writeUnavailable(roomID, setNewUnavailable, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            finish();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner_room.setSelection(position);
        roomID = allRoomList.get(position).getuID();
        roomName = allRoomList.get(position).getName();
        prepareDateList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                    endDate=null;
                }
            }
        });
    }

}
