package com.doems.sisdiklat.sisdiklat.ActivityHome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.doems.sisdiklat.sisdiklat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.tv_title) TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        tv_title.setText("ABOUT");
    }

    @OnClick(R.id.btn_back) public void back(){
        onBackPressed();
    }
}
