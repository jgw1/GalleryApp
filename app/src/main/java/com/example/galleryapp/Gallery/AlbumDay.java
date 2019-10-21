package com.example.galleryapp.Gallery;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.GalleryDay.DayChildModel;
import com.example.galleryapp.GalleryDay.DayMotherAdapter;
import com.example.galleryapp.GalleryDay.DayMotherModel;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.ClearEditText;
import com.example.galleryapp.Util.GetDataFromDB;
import com.example.galleryapp.Util.HashTagAdapter;

import java.util.ArrayList;

public class AlbumDay extends Fragment {
    private RecyclerView mRecyclerView,RV_HashtagSearchList;
    private ClearEditText ET_SearchHashTag;
    SQLiteDatabase database;
    private HashTagAdapter hashTagAdapter;
    private GetDataFromDB getDataFromDB;
    private ImageButton IB_Search;
    private GalleryDBAccess galleryDBAccess;
    private Boolean search = false;
    private InputMethodManager inputMethodManager;


    ArrayList<DayMotherModel> allSampleData;
    DayMotherModel MD = new DayMotherModel();
    ArrayList<DayChildModel> childDataModels = new ArrayList<>();
    Activity activity;

    ArrayList<String> AL_HashtagList = new ArrayList<>();

    @Override
    public void onDestroy() {
        super.onDestroy();
        allSampleData = new ArrayList<>();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context  instanceof Activity){
            activity = (Activity) context;
        }

    }
    public AlbumDay()
    {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_albumday,

                container, false);
        RV_HashtagSearchList = layout.findViewById(R.id.SearchListDay);
        IB_Search = layout.findViewById(R.id.SearchDay);
        ET_SearchHashTag =layout.findViewById(R.id.HashtagSearchDay);
        allSampleData = new ArrayList<>();
        this.galleryDBAccess = GalleryDBAccess.getInstance(activity);
        galleryDBAccess.open();
        allSampleData = galleryDBAccess.getDataForGallery();
        for(int i =0 ;i<allSampleData.size() ;i++){
            if(allSampleData.get(i).getHeaderTitle() == null){
                allSampleData.remove(i);
            }
        }

        galleryDBAccess.close();

        return layout;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){



//        getDataFromDB = new GetDataFromDB(activity);
//        try {
//            allSampleData = getDataFromDB.execute().get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        allSampleData.get(0).getAllItemsInSection();
        Log.d("GWGW","SIZE : " + allSampleData.size());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardviewalbum);
        mRecyclerView.setHasFixedSize(true);
        DayMotherAdapter adapter = new DayMotherAdapter(getContext(),allSampleData);
        mRecyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("mRecyclerView","mRecyclerView ");
                return false;
            }
        });
        hashTagAdapter = new HashTagAdapter(activity,AL_HashtagList);
        RV_HashtagSearchList.setAdapter(hashTagAdapter);
        inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        IB_Search.setOnClickListener(view1 -> {
            search = !search;
            if(search){
                ET_SearchHashTag.requestFocus();

                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);}
            else{
                ET_SearchHashTag.clearFocus();
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        RV_HashtagSearchList.setLayoutManager(new GridLayoutManager(activity,3));
        ET_SearchHashTag.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        ET_SearchHashTag.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                AL_HashtagList.add(ET_SearchHashTag.getText().toString());
                hashTagAdapter.notifyDataSetChanged();
                ET_SearchHashTag.setText(null);
                return true;
            }
            return false;
        });


    }

}
