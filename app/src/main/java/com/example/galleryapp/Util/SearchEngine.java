package com.example.galleryapp.Util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.Gallery.GalleryFragment;
import com.example.galleryapp.R;

import java.util.ArrayList;

public class SearchEngine extends AppCompatActivity {
    private RecyclerView RV_HashTagList;
    private HashTagAdapter hashTagAdapter;
    private ClearEditText ET_SearchHashTag;
    private GalleryDBAccess galleryDBAccess;
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
        BT_Album.setOnClickListener(view -> startActivity(new Intent(SearchEngine.this, GalleryFragment.class)));


        hashTagAdapter = new HashTagAdapter(getApplicationContext(),AL_HashTagList);
        RV_HashTagList.setAdapter(hashTagAdapter);

        RV_HashTagList.setLayoutManager(new GridLayoutManager(getApplicationContext(),6));

        ET_SearchHashTag.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        ET_SearchHashTag.setOnEditorActionListener((v, actionId, event) -> {
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
        });

        ET_SearchHashTag.setOnLongClickListener(view -> {
            ET_SearchHashTag.setSelection(0,ET_SearchHashTag.length());
            return true;
        });

    }


}
