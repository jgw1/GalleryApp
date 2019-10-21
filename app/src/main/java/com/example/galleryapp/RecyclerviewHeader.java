package com.example.galleryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.Gallery.GalleryModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewHeader extends AppCompatActivity {
    private static final String TAG = RecyclerviewHeader.class.getSimpleName();
    private RecyclerView addHeaderRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_header);
        addHeaderRecyclerView = (RecyclerView)findViewById(R.id.recyclerviewheader);
        GridLayoutManager linearLayoutManager = new GridLayoutManager((RecyclerviewHeader.this),3);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (3 - position % 3);
            }
        });
        addHeaderRecyclerView.setLayoutManager(manager);
        addHeaderRecyclerView.setHasFixedSize(true);
        BoardItem customAdapter = new BoardItem(getDataSource());
        addHeaderRecyclerView.setAdapter(customAdapter);
    }
    private List<ItemObject> getDataSource(){
        List<ItemObject> data = new ArrayList<ItemObject>();
        data.add(new ItemObject("First Header"));
        data.add(new ItemObject("This is the item content in the first position"));
        data.add(new ItemObject("This is the item content in the second position"));
        data.add(new ItemObject("This is the item content in the third position"));
        data.add(new ItemObject("This is the item content in the fourth position"));
        data.add(new ItemObject("This is the item content in the fifth position"));
        data.add(new ItemObject("Second Header"));
        data.add(new ItemObject("This is the item content in the first position"));
        data.add(new ItemObject("This is the item content in the second position"));
        return data;
    }
    //http://blog.sqisland.com/2014/12/recyclerview-grid-with-header.html
}
