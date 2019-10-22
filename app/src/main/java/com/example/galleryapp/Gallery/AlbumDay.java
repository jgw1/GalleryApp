package com.example.galleryapp.Gallery;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.RecyclerView;
import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.GalleryDay.AlbumDayTitleModel;
import com.example.galleryapp.GalleryDay.GalleryDayAdapter;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.ClearEditText;
import com.example.galleryapp.Util.GetDataFromDB;
import com.example.galleryapp.Util.HashTagAdapter;
import com.example.galleryapp.GalleryDay.ItemInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
    private RecyclerView addHeaderRecyclerView;
    private static DateFormat dateFormat = new SimpleDateFormat("MM월 dd일");
    private ArrayList<ItemInterface> mUsersAndSectionList;
    private ArrayList<GalleryModel> List_GalleryTotal;

    Activity activity;

    ArrayList<String> AL_HashtagList = new ArrayList<>();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUsersAndSectionList = new ArrayList<>();
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
        List_GalleryTotal = new ArrayList<>();
        this.galleryDBAccess = GalleryDBAccess.getInstance(activity);
        galleryDBAccess.open();
        List_GalleryTotal = galleryDBAccess.getDataForMap();
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardviewalbum);
        mRecyclerView.setHasFixedSize(true);

        hashTagAdapter = new HashTagAdapter(activity,AL_HashtagList);
        RV_HashtagSearchList.setAdapter(hashTagAdapter);
        inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mUsersAndSectionList = new ArrayList<>();
        getSectionalList(List_GalleryTotal);
        GalleryDayAdapter customAdapter = new GalleryDayAdapter(mUsersAndSectionList,activity);
        GridLayoutManager manager = new GridLayoutManager(activity, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(GalleryDayAdapter.TYPE_HEADER == customAdapter.getItemViewType(position)){
                    return 3;
                }
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(customAdapter);

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
    private void getSectionalList(ArrayList<GalleryModel> usersList) {
        Collections.sort(usersList, new Comparator<GalleryModel>() {
            @Override
            public int compare(GalleryModel user1, GalleryModel user2) {
                Long date = FileNameToStringValue(user1);
                Long date1 = FileNameToStringValue(user2);

                return date.compareTo(date1) > 0 ? 1 : 0;
            }
        });

        String lastHeader = "";

        int size = usersList.size();

        for (int i = 0; i < size; i++) {

            GalleryModel user = usersList.get(i);
            Long date = FileNameToStringValue(user);
            String header = dateFormat.format(new Date(date));

            if (!TextUtils.equals(lastHeader, header)) {
                lastHeader = header;
                mUsersAndSectionList.add(new AlbumDayTitleModel(header));
            }
            mUsersAndSectionList.add(user);
        }
    }
    private Long FileNameToStringValue(GalleryModel galleryModel){
        String FileName = galleryModel.getFilename();
        FileName = FileName.replaceAll(".jpg","");
        Long date = Long.parseLong(FileName);
        return date;
    }
}
