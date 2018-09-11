package com.doems.sisdiklat.sisdiklat.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doems.sisdiklat.sisdiklat.Firebase.FireDataRoom;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataRunningText;
import com.doems.sisdiklat.sisdiklat.Model.ModelRoom;
import com.doems.sisdiklat.sisdiklat.Model.ModelRunningText;
import com.doems.sisdiklat.sisdiklat.R;
import com.doems.sisdiklat.sisdiklat.Room.EditRoomActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RunningTextAdapter extends RecyclerView.Adapter<RunningTextAdapter.MyViewHolder> {

    private Map<String, ModelRunningText> runningTextMap = new LinkedHashMap<>();
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private Context context;

    public interface OnItemClickListener{
        void onItemClick(View view, ModelRoom obj, int position, String key);
    }

    public void setmOnItemClickListener(AdapterView.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.layout_cardRunningText) RelativeLayout layout_cardRunningText;
        @BindView(R.id.tv_runningText) TextView tv_runningText;
        @BindView(R.id.iv_delete) ImageView iv_delete;
        @BindView(R.id.wv_runningText) WebView wv_runningText;

        String uID = FirebaseAuth.getInstance().getUid();

        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public RunningTextAdapter(Map<String, ModelRunningText> runningTextMap, Context context){
        this.runningTextMap = runningTextMap;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_running_text, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final List<String> keyList = new ArrayList<>(runningTextMap.keySet());
        final List<ModelRunningText> runningTextList = new ArrayList<>(runningTextMap.values());
        final String key = keyList.get(i);
        final ModelRunningText runningText = runningTextList.get(i);
        String showText = "<html><body style=\"text-align:justify\"> "+ runningText.getText() +" </body></Html>";

        myViewHolder.wv_runningText.loadData(String.format("%s", runningText.getText()), "text/html", "utf-8");
//        myViewHolder.tv_runningText.setText(runningText.getText());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            myViewHolder.tv_runningText.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
        myViewHolder.tv_runningText.setText(Html.fromHtml(showText));
        myViewHolder.wv_runningText.setVisibility(View.GONE);
        myViewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FireDataRunningText(myViewHolder.uID).ref.child(key).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return runningTextMap.size();
    }
}