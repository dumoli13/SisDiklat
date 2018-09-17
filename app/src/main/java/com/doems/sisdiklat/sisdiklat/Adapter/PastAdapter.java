package com.doems.sisdiklat.sisdiklat.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doems.sisdiklat.sisdiklat.ActivityHome.FutureActivity;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataRoom;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataSchedule;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataUnavailableTime;
import com.doems.sisdiklat.sisdiklat.Model.ModelSchedule;
import com.doems.sisdiklat.sisdiklat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PastAdapter extends RecyclerView.Adapter<PastAdapter.MyViewHolder>{
        private Map<String, ModelSchedule> scheduleMap = new LinkedHashMap<>();
        private OnItemClickListener mOnItemClickListener;
        private Context context;

        public interface OnItemClickListener{
            void onItemClick(View view, ModelSchedule obj, int position, String key);
        }

        public void setOnItemClickLitener(final OnItemClickListener mItemClickListener){
            this.mOnItemClickListener = mItemClickListener;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.layout_cardPast) RelativeLayout layout_cardPast;
            @BindView(R.id.tv_date) TextView tv_date;
            @BindView(R.id.tv_month) TextView tv_month;
            @BindView(R.id.tv_room) TextView tv_room;
            @BindView(R.id.tv_event) TextView tv_event;
            @BindView(R.id.tv_participant) TextView tv_participant;
            @BindView(R.id.tv_speaker) TextView tv_speaker;
            @BindView(R.id.iv_trash) ImageView iv_trash;


            String uID = FirebaseAuth.getInstance().getUid();

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public PastAdapter(Map<String, ModelSchedule> scheduleMap, Context context){
            this.scheduleMap = scheduleMap;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_schedule_past, viewGroup, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            final List<String> keyList = new ArrayList<>(scheduleMap.keySet());
            final List<ModelSchedule> scheduleList = new ArrayList<>(scheduleMap.values());
            final String key = keyList.get(i);
            ModelSchedule schedule = scheduleList.get(i);
            SimpleDateFormat sdfDate = new SimpleDateFormat("d", Locale.US);
            SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM", Locale.US);

            final String roomID = schedule.getRoomID();
            final String scheduleID = schedule.getuID();
            String date = sdfDate.format(schedule.getStartDate());
            String month = sdfMonth.format(schedule.getStartDate());

            myViewHolder.tv_date.setText(date);
            myViewHolder.tv_month.setText(month);
            myViewHolder.tv_event.setText(schedule.getEvent().toString());
            myViewHolder.tv_room.setText(schedule.getRoomName().toString());
            myViewHolder.tv_participant.setText("participant: " + schedule.getParticipant().toString());
            myViewHolder.tv_speaker.setText("speaker: " + schedule.getSpeaker().toString());


            myViewHolder.iv_trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setMessage("Are you sure you want to delete?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new FireDataSchedule(myViewHolder.uID).ref.child(key).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            new FireDataUnavailableTime(myViewHolder.uID).ref.child(roomID).child(scheduleID).child("date").child("0").removeValue();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return scheduleMap.size();
        }

    }