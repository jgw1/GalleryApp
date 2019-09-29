package com.example.galleryapp.CompareFilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.GalleryAppCode;
import com.example.galleryapp.Util.SpacesItemDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FiltersListFragment extends Fragment implements ThumbnailAdapter.ThumbnailsAdapterListener {
    private RecyclerView recyclerView;
    private ThumbnailAdapter thumbnailAdapter;
    private List<ThumbnailItem> thumbnailItemList;
    private FiltersListFragmentListener listener;

    public void setListener(FiltersListFragmentListener listener){
        this.listener = listener;
    }

    public FiltersListFragment(){
        //필요한 공간
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_filters_list,container,false);
        recyclerView = view.findViewById(R.id.filter_recyclerView);

        thumbnailItemList = new ArrayList<>();

        thumbnailAdapter = new ThumbnailAdapter(getActivity(),thumbnailItemList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8,getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.setAdapter(thumbnailAdapter);
        File ImageFile = new File(GalleryAppCode.Path,CompareFilter.File_Name);
        Bitmap bitmap = BitmapFactory.decodeFile(ImageFile.getPath());
        prepareThumbnail(bitmap);
        return view;
    }
    public void prepareThumbnail(final Bitmap bitmap) {
        Runnable r = () -> {
            Bitmap thumbImage;

            if (bitmap == null) {
                File ImageFile = new File(GalleryAppCode.Path,CompareFilter.File_Name);
                thumbImage = BitmapUtils.getBitmapFromGallery(getActivity(), Uri.fromFile(ImageFile), 100, 100);
            } else {
                thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            }

            if (thumbImage == null)
                return;

            ThumbnailsManager.clearThumbs();
            thumbnailItemList.clear();

            // add normal bitmap first
            ThumbnailItem thumbnailItem = new ThumbnailItem();
            thumbnailItem.image = thumbImage;
            thumbnailItem.filterName = getString(R.string.filter_normal);
            ThumbnailsManager.addThumb(thumbnailItem);

            List<Filter> filters = FilterPack.getFilterPack(getActivity());

            for (Filter filter : filters) {
                ThumbnailItem tI = new ThumbnailItem();
                tI.image = thumbImage;
                tI.filter = filter;
                tI.filterName = filter.getName();
                ThumbnailsManager.addThumb(tI);
            }

            thumbnailItemList.addAll(ThumbnailsManager.processThumbs(getActivity()));

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    thumbnailAdapter.notifyDataSetChanged();
                }
            });
        };

        new Thread(r).start();
    }
    @Override
    public void onFilterSelected(Filter filter) {
        if (listener != null)
            listener.onFilterSelected(filter);
    }

    public interface FiltersListFragmentListener{
        void onFilterSelected(Filter filter);
    }
}
