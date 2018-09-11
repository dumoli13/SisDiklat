package com.doems.sisdiklat.sisdiklat.ActivityHome;

import android.graphics.Color;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.doems.sisdiklat.sisdiklat.ActivityAccount.ProfilActivity;
import com.doems.sisdiklat.sisdiklat.ActivityAccount.SignInActivity;
import com.doems.sisdiklat.sisdiklat.ActivitySchedule.NewScheduleActivity;
import com.doems.sisdiklat.sisdiklat.Misc.ViewPagerAdapter;
import com.doems.sisdiklat.sisdiklat.R;
import com.doems.sisdiklat.sisdiklat.Room.AvailableRoomActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.schedule_tabLayout) TabLayout schedule_tabLayout;
    @BindView(R.id.viewPagerSchedule) ViewPager viewPagerSchedule;
    @BindView(R.id.rl_menu) RelativeLayout rl_menu;

    private ViewPagerAdapter adapter1;
    View menu;

    public static FirebaseUser user;
    public static String uID;

    public HomeActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        rl_menu.setVisibility(View.GONE);

        user = FirebaseAuth.getInstance().getCurrentUser();

        adapter1  = new ViewPagerAdapter(getSupportFragmentManager());
        adapter1.addFragment(new FutureActivity(), ">>");
        adapter1.addFragment(new PastActivity(), "<<");
        viewPagerSchedule.setAdapter(adapter1);
        schedule_tabLayout.setupWithViewPager(viewPagerSchedule);
        viewPagerSchedule.setOffscreenPageLimit(3);
    }

    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    @OnClick(R.id.btn_addNew) public void addNew(){
        startActivity(new Intent(HomeActivity.this, NewScheduleActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_runningText:
                startActivity(new Intent(HomeActivity.this, RunningTextActivity.class));
                return true;
            case R.id.menu_account:
                startActivity(new Intent(HomeActivity.this, ProfilActivity.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                return true;
            case R.id.menu_logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.btn_more) public void more(){
        if(rl_menu.getVisibility()==View.VISIBLE){
            rl_menu.removeView(menu);
            rl_menu.setVisibility(View.GONE);
        }
        else{
            LayoutInflater _menu = LayoutInflater.from(getApplicationContext());
            menu = _menu.inflate(R.layout.menu, null, false);

            TextView menu_runningText = menu.findViewById(R.id.menu_runningText);
            TextView menu_room = menu.findViewById(R.id.menu_room);
            TextView menu_account = menu.findViewById(R.id.menu_account);
            TextView menu_about = menu.findViewById(R.id.menu_about);
            TextView menu_logOut = menu.findViewById(R.id.menu_logOut);
            RelativeLayout rl_background = menu.findViewById(R.id.rl_background);

            rl_background.setBackgroundColor(getColorWithAlpha(Color.BLACK, 0.2f));
            rl_background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rl_menu.removeView(menu);
                    rl_menu.setVisibility(View.GONE);
                }
            });

            menu_runningText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this, RunningTextActivity.class));
                    rl_menu.removeView(menu);
                    rl_menu.setVisibility(View.GONE);
                }
            });

            menu_room.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this, AvailableRoomActivity.class));
                    rl_menu.removeView(menu);
                    rl_menu.setVisibility(View.GONE);
                }
            });

            menu_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this, ProfilActivity.class));
                    rl_menu.removeView(menu);
                    rl_menu.setVisibility(View.GONE);
                }
            });

            menu_about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                    rl_menu.removeView(menu);
                    rl_menu.setVisibility(View.GONE);
                }
            });

            menu_logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                    finish();
                }
            });

            rl_menu.addView(menu);
            rl_menu.requestLayout();
            rl_menu.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.rl_home) public void hideInflater(){
        rl_menu.removeView(menu);
        rl_menu.setVisibility(View.GONE);
    }


    @Override public void onStart(){
        super.onStart();
    }

}
