package com.doems.sisdiklat.sisdiklat.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doems.sisdiklat.sisdiklat.Model.ModelRoom;
import com.doems.sisdiklat.sisdiklat.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetRoomAdapter extends RecyclerView.Adapter<SetRoomAdapter.MyViewHolder> {

    private Map<String, ModelRoom> roomMap = new LinkedHashMap<>();
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private Activity activity;
    private Intent intent;

    public interface OnItemClickListener{
        void onItemClick(View view, ModelRoom obj, int position, String key);
    }

    public void setmOnItemClickListener(AdapterView.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.layout_cardSetRoom) LinearLayout layout_cardSetRoom;
        @BindView(R.id.tv_roomName) TextView tv_roomName;
        @BindView(R.id.tv_roomCapacity)  TextView tv_roomCapacity;

        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public SetRoomAdapter(Map<String, ModelRoom> roomMap, Activity activity, Intent intent){
        this.roomMap = roomMap;
        this.activity = activity;
        this.intent = intent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_setup_room, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final List<String> keyList = new ArrayList<>(roomMap.keySet());
        final List<ModelRoom> roomList = new ArrayList<>(roomMap.values());
        final String key = keyList.get(i);
        final ModelRoom room = roomList.get(i);

        myViewHolder.tv_roomName.setText(room.getName());
        myViewHolder.tv_roomCapacity.setText("capacity: " + room.getCapacity());

        myViewHolder.layout_cardSetRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("roomID", key);
                intent.putExtra("roomCap", room.getCapacity());
                intent.putExtra("roomName", room.getName());
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomMap.size();
    }
}