package com.example.galleryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galleryapp.Camera.CameraActivity;
import com.example.galleryapp.DB.DatabaseAccess;
import com.example.galleryapp.Gallery.GalleryFragment;
import com.example.galleryapp.Gallery.OneImage;
import com.example.galleryapp.Map.Location;
import com.example.galleryapp.Util.ClearEditText;
import com.example.galleryapp.Util.CustomDialog;
import com.example.galleryapp.Util.GalleryAppCode;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView RV_HashTagList;
    private HashTagAdapter hashTagAdapter;
    private ClearEditText ET_SearchHashTag;
    private DatabaseAccess databaseAccess;
    private CustomDialog customDialog;
    private Button BT_Album;

    ArrayList<String> AL_HashTagList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ET_SearchHashTag = findViewById(R.id.edittext);
        RV_HashTagList = findViewById(R.id.hashtaglist);
        BT_Album = findViewById(R.id.button2);
        BT_Album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GalleryFragment.class));
            }
        });


        hashTagAdapter = new HashTagAdapter(getApplicationContext(),AL_HashTagList);
        RV_HashTagList.setAdapter(hashTagAdapter);

        RV_HashTagList.setLayoutManager(new GridLayoutManager(getApplicationContext(),6));

        ET_SearchHashTag.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        ET_SearchHashTag.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();
                    AL_HashTagList.add(ET_SearchHashTag.getText().toString());

                    hashTagAdapter.notifyDataSetChanged();
                    Log.d("AGAG",""+AL_HashTagList);

                    ET_SearchHashTag.setText(null);
                    return true;
                }
                return false;
            }
        });

        ET_SearchHashTag.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ET_SearchHashTag.setSelection(0,ET_SearchHashTag.length());
                return true;
            }
        });

    }


}
