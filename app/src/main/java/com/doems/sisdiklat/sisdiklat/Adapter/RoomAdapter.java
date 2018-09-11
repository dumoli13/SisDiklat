package com.doems.sisdiklat.sisdiklat.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doems.sisdiklat.sisdiklat.Firebase.FireDataRoom;
import com.doems.sisdiklat.sisdiklat.Model.ModelRoom;
import com.doems.sisdiklat.sisdiklat.R;
import com.doems.sisdiklat.sisdiklat.Room.AvailableRoomActivity;
import com.doems.sisdiklat.sisdiklat.Room.EditRoomActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {

    private Map<String, ModelRoom> roomMap = new LinkedHashMap<>();
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private Context context;

    public interface OnItemClickListener{
        void onItemClick(View view, ModelRoom obj, int position, String key);
    }

    public void setmOnItemClickListener(AdapterView.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.layout_cardRoom) LinearLayout layout_cardRoom;
        @BindView(R.id.tv_roomName) TextView tv_roomName;
        @BindView(R.id.tv_roomCapacity)  TextView tv_roomCapacity;
        @BindView(R.id.iv_trash) ImageView iv_trash;

        String uID = FirebaseAuth.getInstance().getUid();

        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public RoomAdapter(Map<String, ModelRoom> roomMap, Context context){
        this.roomMap = roomMap;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_room, viewGroup, false);
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

        myViewHolder.layout_cardRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext().getApplicationContext(), EditRoomActivity.class);
                intent.putExtra("messageKey", key);
                intent.putExtra("messageName", room.getName());
                intent.putExtra("messageCap", room.getCapacity());
                v.getContext().startActivity(intent);
            }
        });

        myViewHolder.iv_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FireDataRoom(myViewHolder.uID).ref.child(key).removeValue();

            }
        });
    }

    @Override
    public int getItemCount() {
        return roomMap.size();
    }
}
