package com.example.galleryapp.Gallery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.example.galleryapp.GalleryTotal.GalleryTotalAdapter;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.ClearEditText;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.HashTagAdapter;

import java.io.File;
import java.util.ArrayList;

public class AlbumTotal extends Fragment {
    private RecyclerView RV_GalleryTotalView,RV_HashtagSearchList;
    private ArrayList<GalleryModel> List_GalleryTotal;
    private Activity activity;
    private HashTagAdapter hashTagAdapter;
    private ClearEditText ET_SearchHashTag;
    private GalleryDBAccess galleryDBAccess;
    private ImageButton IB_Search,IB_DELETE;
    private GalleryTotalAdapter galleryTotalAdapter;
    private Boolean search,delete = false;
    private InputMethodManager inputMethodManager;

    ArrayList<String> AL_HashtagList = new ArrayList<>();

    public AlbumTotal()
    {

    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context  instanceof Activity){
            activity = (Activity) context;
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List_GalleryTotal = new ArrayList<>();
        this.galleryDBAccess = GalleryDBAccess.getInstance(activity);
        galleryDBAccess.open();
        List_GalleryTotal = galleryDBAccess.getDataForMap();
        galleryDBAccess.close();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_albumtotal,

                container, false);

        RV_GalleryTotalView = layout.findViewById(R.id.GalleryTotalView);
        RV_HashtagSearchList = layout.findViewById(R.id.SearchListTotal);
        IB_Search = layout.findViewById(R.id.SearchTotal);
        ET_SearchHashTag =layout.findViewById(R.id.HashtagSearchTotal);
        IB_DELETE = layout.findViewById(R.id.DeleteTotal);
        IB_DELETE.setOnClickListener(this::onClick);
        galleryTotalAdapter = new GalleryTotalAdapter(getContext(),List_GalleryTotal);
        RV_GalleryTotalView.setHasFixedSize(true);
        RV_GalleryTotalView.setAdapter(galleryTotalAdapter);
        RV_GalleryTotalView.setLayoutManager(new GridLayoutManager(getContext(),3));
        return layout;
    }

    @Override
    public void onViewCreated(final View view,Bundle savedInstanceState){
        galleryTotalAdapter.notifyDataSetChanged();
        RV_GalleryTotalView.setAdapter(galleryTotalAdapter);

        hashTagAdapter = new HashTagAdapter(activity,AL_HashtagList);
        RV_HashtagSearchList.setAdapter(hashTagAdapter);
        inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.DeleteTotal:
                delete = !delete;
                if( delete) {
                    galleryTotalAdapter.setSelectable(true);
                }else{
                    int size = List_GalleryTotal.size();
                    for (int i=0;i<size;i++){
                        GalleryModel galleryModel = List_GalleryTotal.get(i);
                        if(galleryModel.getChecked())
                        {
                            File file = new File(GalleryAppCode.Path,galleryModel.getFilename());
                            file.delete();

                            galleryDBAccess.open();
                            galleryDBAccess.delete(galleryModel);
                            galleryDBAccess.close();

                            List_GalleryTotal.remove(i);
                            i--;
                            size=List_GalleryTotal.size();
                        }
                    }

                    galleryTotalAdapter.setSelectable(false);
                }
                break;
        }
    }

}
