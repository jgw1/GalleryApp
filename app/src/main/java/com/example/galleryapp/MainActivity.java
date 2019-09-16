package com.example.galleryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galleryapp.Util.ClearEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private RecyclerView RV_HashTagList;
    private HashTagAdapter hashTagAdapter;
    private EditText ET_SearchHashTag;
    ArrayList<String> AL_HashTagList = new ArrayList<>();
    int a = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ET_SearchHashTag = findViewById(R.id.edittext);
        RV_HashTagList = findViewById(R.id.hashtaglist);

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
                    a++;
                    hashTagAdapter.notifyDataSetChanged();
                    Log.d("AGAG",""+AL_HashTagList);

                    ET_SearchHashTag.setText(null);
                    return true;
                }
                return false;
            }
        });



    }


    //엔터키 눌렀을때 item 생성
}
