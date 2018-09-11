package com.doems.sisdiklat.sisdiklat.ActivityHome;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doems.sisdiklat.sisdiklat.Adapter.RunningTextAdapter;
import com.doems.sisdiklat.sisdiklat.Firebase.FireData;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataRunningText;
import com.doems.sisdiklat.sisdiklat.Model.ModelRunningText;
import com.doems.sisdiklat.sisdiklat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RunningTextActivity extends AppCompatActivity {


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_message) EditText et_message;
    @BindView(R.id.btn_save) Button btn_save;
    @BindView(R.id.rv_runningText) RecyclerView rv_runningText;
    @BindView(R.id.tv_emptyText) TextView tv_emptyText;

    private ChildEventListener childEventListener;
    private Map<String, ModelRunningText> runningTextMap = new LinkedHashMap<>();
    private RunningTextAdapter mAdapter;
    private String uID;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_text);
        ButterKnife.bind(this);
        context = this.getApplicationContext();
        uID = FirebaseAuth.getInstance().getUid();

        tv_title.setText("RUNNING TEXT");
        initRecycler();
    }

    private void initRecycler(){
        mAdapter = new RunningTextAdapter(runningTextMap, context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_runningText.setLayoutManager(layoutManager);
        rv_runningText.setItemAnimator(new DefaultItemAnimator());
        rv_runningText.setAdapter(mAdapter);

        checkEmpty();
    }

    private void initList(){
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                ModelRunningText runningText = dataSnapshot.getValue(ModelRunningText.class);
                runningTextMap.put(key, runningText);
                mAdapter.notifyDataSetChanged();
                checkEmpty();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                ModelRunningText runningText = dataSnapshot.getValue(ModelRunningText.class);
                runningTextMap.put(key, runningText);
                mAdapter.notifyDataSetChanged();
                checkEmpty();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                runningTextMap.remove(key);
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
        new FireDataRunningText(uID).ref.addChildEventListener(childEventListener);
    }

    private void checkEmpty(){
        if(runningTextMap.size()>0){
            tv_emptyText.setVisibility(View.GONE);
            rv_runningText.setVisibility(View.VISIBLE);
        }
        else{

            tv_emptyText.setVisibility(View.VISIBLE);
            rv_runningText.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_save) public void saveText(){
        String text = et_message.getText().toString();
        if(TextUtils.isEmpty(text)){
            Toast.makeText(getApplicationContext(),"Enter your new message", Toast.LENGTH_SHORT).show();
        }
        else{
            new FireDataRunningText(uID).writeText(text, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError==null){
                        mAdapter.notifyDataSetChanged();
                        checkEmpty();
                        et_message.setText("");
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Error, try again later", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        }
    }

    @OnClick(R.id.btn_back) public void back(){
        onBackPressed();
    }


    @Override public void onStart() {
        super.onStart();
        initList();
    }
}
