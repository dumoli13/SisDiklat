package com.doems.sisdiklat.sisdiklat.ActivityHome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doems.sisdiklat.sisdiklat.Adapter.AnnouncementAdapter;
import com.doems.sisdiklat.sisdiklat.Firebase.FireDataAnnouncement;
import com.doems.sisdiklat.sisdiklat.Model.ModelAnnouncement;
import com.doems.sisdiklat.sisdiklat.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnnouncementActivity extends AppCompatActivity {

    @BindView(R.id.tv_noImage) TextView tv_noImage;
    @BindView(R.id.rv_image) RecyclerView rv_image;

    private Map<String, ModelAnnouncement> announcementMap = new LinkedHashMap<>();
    private ChildEventListener imageEventListener;
    private StorageReference mStorageRef;
    private static final int FILE_SELECT_CODE=1;
    private AnnouncementAdapter mAdapter;


    private String uID;
    private File localFile;
    private ProgressDialog pDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        ButterKnife.bind(this);
        uID = FirebaseAuth.getInstance().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        context = this.getApplicationContext();

        pDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        initRecycler();
    }

    private void initRecycler(){
        mAdapter = new AnnouncementAdapter(announcementMap, context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplication());
        rv_image.setLayoutManager(layoutManager);
        rv_image.setItemAnimator(new DefaultItemAnimator());
        rv_image.setAdapter(mAdapter);
    }

    private void checkEmpty(){
        if(announcementMap.size()!=0){
            tv_noImage.setVisibility(View.GONE);
            rv_image.setVisibility(View.VISIBLE);
        }
        else{
            tv_noImage.setVisibility(View.VISIBLE);
            rv_image.setVisibility(View.GONE);
        }
    }

    private void initList(){
        imageEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String key = dataSnapshot.getKey();
                final ModelAnnouncement _modelAnnouncement = dataSnapshot.getValue(ModelAnnouncement.class);
                final String name = _modelAnnouncement.getName();

                mStorageRef.child(uID+"/announcement/"+name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri url=uri;
                        announcementMap.put(key, new ModelAnnouncement(key, name, url.toString()));
                        mAdapter.notifyDataSetChanged();
                        checkEmpty();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String key = dataSnapshot.getKey();
                final ModelAnnouncement _modelAnnouncement = dataSnapshot.getValue(ModelAnnouncement.class);
                final String name = _modelAnnouncement.getName();

                mStorageRef.child(uID+"/announcement/"+name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri url=uri;
                        announcementMap.put(key, new ModelAnnouncement(key, name, url.toString()));
                        mAdapter.notifyDataSetChanged();
                        checkEmpty();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                announcementMap.remove(key);
                mAdapter.notifyDataSetChanged();
                checkEmpty();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        new FireDataAnnouncement(uID).ref.addChildEventListener(imageEventListener);
    }

    @OnClick(R.id.btn_back) public void back(){
        onBackPressed();
    }

    @OnClick(R.id.tv_addNew) public void addNew(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        try{
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, FILE_SELECT_CODE);
        }
        catch (android.content.ActivityNotFoundException e){
            Toast.makeText(this, "Please install file manager", Toast.LENGTH_LONG).show();
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==FILE_SELECT_CODE && resultCode==RESULT_OK){
            pDialog.setMessage("uploading file...");
            pDialog.show();

            Uri file = data.getData();
            String path = data.getDataString();
            String fileName = path.substring(path.lastIndexOf("/")+1);

            //UPLOAD BIG SIZE FILE
            final StorageReference storageReference1 = mStorageRef.child(uID+"/announcement/"+fileName);
            storageReference1.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pDialog.hide();
                            String name = taskSnapshot.getMetadata().getName();
                            String url = storageReference1.getDownloadUrl().toString();

                            new FireDataAnnouncement(uID).writeAdress(name, url, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    Toast.makeText(getApplicationContext(), "upload success", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) ((int)(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount());
                            pDialog.setProgress(progress);
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
    }
}