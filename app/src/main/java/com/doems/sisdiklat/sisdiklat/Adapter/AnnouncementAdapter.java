package com.doems.sisdiklat.sisdiklat.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.doems.sisdiklat.sisdiklat.Firebase.FireDataAnnouncement;
import com.doems.sisdiklat.sisdiklat.Model.ModelAnnouncement;
import com.doems.sisdiklat.sisdiklat.Model.ModelRoom;
import com.doems.sisdiklat.sisdiklat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.MyViewHolder> {

    private Map<String, ModelAnnouncement> announcementMap = new LinkedHashMap<>();
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private Context context;

    public interface OnItemClickListener{
        void onItemClick(View view, ModelRoom obj, int position, String key);
    }

    public void setmOnItemClickListener(AdapterView.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.layout_cardAnnouncement) RelativeLayout layout_cardAnnouncement;
        @BindView(R.id.iv_content) ImageView iv_content;
        @BindView(R.id.iv_trash)  ImageView iv_trash;

        String uID = FirebaseAuth.getInstance().getUid();

        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public AnnouncementAdapter(Map<String, ModelAnnouncement> announcementMap, Context context){
        this.announcementMap = announcementMap;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_annoucement, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final List<String> keyList = new ArrayList<>(announcementMap.keySet());
        final List<ModelAnnouncement> announcementList = new ArrayList<>(announcementMap.values());
        final String key = keyList.get(i);
        final ModelAnnouncement announcement = announcementList.get(i);
        final String name = announcement.getName();
        final Uri url = Uri.parse(announcement.getUrl());

        Picasso.with(context).load(url).fit().into(myViewHolder.iv_content);

        myViewHolder.iv_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference.child(myViewHolder.uID+"/announcement/"+name).delete();
                new FireDataAnnouncement(myViewHolder.uID).ref.child(key).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return announcementMap.size();
    }
}