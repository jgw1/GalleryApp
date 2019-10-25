package com.example.galleryapp.Gallery;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.GalleryDay.AlbumDayTitleModel;
import com.example.galleryapp.GalleryDay.GalleryDayAdapter;
import com.example.galleryapp.GalleryDay.ItemInterface;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.ClearEditText;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.GetDataFromDB;
import com.example.galleryapp.Util.HashTagAdapter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class AlbumDay extends Fragment implements View.OnClickListener {
    private RecyclerView mRecyclerView, RV_HashtagSearchList;
    private ClearEditText ET_SearchHashTag;
    SQLiteDatabase database;
    private HashTagAdapter hashTagAdapter;
    private GetDataFromDB getDataFromDB;
    private ImageButton IB_Search,IB_Delete;
    private GalleryDBAccess galleryDBAccess;
    private boolean search,delete = false;
    private InputMethodManager inputMethodManager;
    private RecyclerView addHeaderRecyclerView;
    private static DateFormat dateFormat = new SimpleDateFormat("MM월 dd일");
    private ArrayList<ItemInterface> mUsersAndSectionList;
    private ArrayList<GalleryModel> List_GalleryTotal;
    private ArrayList<Integer> headerlist;
    private GalleryDayAdapter galleryDayAdapter;
    private Context context;
    private AlbumDayFragmentInteractionListener mListener;

    Activity activity;

    ArrayList<String> AL_HashtagList = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AlbumDayFragmentInteractionListener) {
            mListener = (AlbumDayFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public AlbumDay(Context context, ArrayList<GalleryModel> galleryModelArrayList) {
        this.List_GalleryTotal = galleryModelArrayList;
        this.context = context;
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
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_albumday,

                container, false);
        RV_HashtagSearchList = layout.findViewById(R.id.SearchListDay);
        IB_Search = layout.findViewById(R.id.SearchDay);
        ET_SearchHashTag = layout.findViewById(R.id.HashtagSearchDay);


        return layout;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {


        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardviewalbum);
        mRecyclerView.setHasFixedSize(true);

        hashTagAdapter = new HashTagAdapter(activity, AL_HashtagList);
        RV_HashtagSearchList.setAdapter(hashTagAdapter);
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mUsersAndSectionList = new ArrayList<>();
        headerlist = new ArrayList<>();
        getSectionalList(List_GalleryTotal);
        for (int i = 0; i < mUsersAndSectionList.size(); i++) {
            if (mUsersAndSectionList.get(i).isSection()) {
                headerlist.add(i);
            }
        }
        headerlist.add(mUsersAndSectionList.size());
        galleryDayAdapter = new GalleryDayAdapter(mUsersAndSectionList, context, headerlist);
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (GalleryDayAdapter.TYPE_HEADER == galleryDayAdapter.getItemViewType(position)) {
                    return 3;
                }
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(galleryDayAdapter);
        IB_Delete = view.findViewById(R.id.DeleteDay);
        IB_Delete.setOnClickListener(this);
        IB_Search.setOnClickListener(view1 -> {
            search = !search;
            if (search) {
                ET_SearchHashTag.requestFocus();

                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            } else {
                ET_SearchHashTag.clearFocus();
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        RV_HashtagSearchList.setLayoutManager(new GridLayoutManager(activity, 3));
        ET_SearchHashTag.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        ET_SearchHashTag.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
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

    private Long FileNameToStringValue(GalleryModel galleryModel) {
        String FileName = galleryModel.getFilename();
        FileName = FileName.replaceAll(".jpg", "");
        Long date = Long.parseLong(FileName);
        return date;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void setData(ArrayList<GalleryModel> galleryModel) {
        mUsersAndSectionList = new ArrayList<>();
        getSectionalList(galleryModel);

        galleryDayAdapter.notifyDataSetChanged();

        //Fragment화면 갱신
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.DeleteDay:
                delete = !delete;
                if (delete) {
                    galleryDayAdapter.setSelectable(true);
                } else {
                    int size = List_GalleryTotal.size();
                    for (int i = 0; i < size; i++) {
                        GalleryModel galleryModel = List_GalleryTotal.get(i);
                        if (galleryModel.getChecked()) {
                            File file = new File(GalleryAppCode.Path, galleryModel.getFilename());
                            file.delete();
                            this.galleryDBAccess = GalleryDBAccess.getInstance(context);
                            galleryDBAccess.open();
                            galleryDBAccess.delete(galleryModel);
                            galleryDBAccess.close();

                            List_GalleryTotal.remove(i);
                            i--;
                            size = List_GalleryTotal.size();
                        }
                    }
                    mUsersAndSectionList = new ArrayList<>();
                    getSectionalList(List_GalleryTotal);
                    galleryDayAdapter.notifyDataSetChanged();

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.detach(this).attach(this).commit();

                    mListener.onAlbumdayFragmentInteraction(List_GalleryTotal);
                    galleryDayAdapter.setSelectable(false);
                }
                break;
        }
    }
        public interface AlbumDayFragmentInteractionListener {
            void onAlbumdayFragmentInteraction(ArrayList<GalleryModel> galleryModel);
        }
}

