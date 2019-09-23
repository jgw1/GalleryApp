package com.example.galleryapp.Gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.DB.DatabaseAccess;
import com.example.galleryapp.GalleryDay.DayMotherAdapter;
import com.example.galleryapp.Map.MapMarkerItem;
import com.example.galleryapp.Map.MapRecyclerViewAdapter;
import com.example.galleryapp.Map.MapRecyclerViewModel;
import com.example.galleryapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlbumMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    private Marker selectedMarker;
    private View marker_root_view;
    private TextView tv_marker,position;
    private Context mContext;
    private DatabaseAccess databaseAccess;
    private ArrayList<MapRecyclerViewModel> mapRecyclerViewModels;
    private LinearLayout linearLayout;
    private RecyclerView mapRecyclerView;
    private Activity activity;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context  instanceof Activity){
            activity = (Activity) context;
        }

    }

    public AlbumMap()
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
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_albummap,

                container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return layout;
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){
        linearLayout = view.findViewById(R.id.menu);
        mapRecyclerView = view.findViewById(R.id.mapRecyclerView);
        position = view.findViewById(R.id.address);
        linearLayout.setVisibility(View.INVISIBLE);
        mapRecyclerViewModels = new ArrayList<>();

        this.databaseAccess = DatabaseAccess.getInstance(mContext);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ClusterManager<MapMarkerItem> mClusterManager = new ClusterManager<>(activity,mMap);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.537523, 126.96558), 14));
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnMapClickListener(this);

        //위치값 받아 오는데 필요한 허가 확인
        final LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        // 현재 위치 정보 저장 - 차후에 DB시 필요 / 여기에선 필요 없음
        Location location = lm.getLastKnownLocation(locationProvider); //현재 위치 정보
        double latitude = location.getLatitude(); //현재 위치 - 위도 값
        double longitude = location.getLongitude(); //현재 위치 - 경도 값



        // Custom 마커를 위해 Render setting
//        ClusterRenderer renderer = new ClusterRenderer(mContext,mMap,mClusterManager);
//        mClusterManager.setRenderer(renderer);

        //선택 클러스터를 화면 중앙에 배치
        mMap.setOnCameraChangeListener(mClusterManager);


        //결과확인을 위한 테스트용 세팅
        for(int i = 0; i<50;i++){
            double lat = 37.537523 + (i/200d);
            double lng = 126.96558 + (i/200d);
            mClusterManager.addItem(new MapMarkerItem((new LatLng(lat, lng)),"1"));
        }

        //전체 다 하나의 마커를 하나의 클러스터로 묶을 경우 사용
        mClusterManager.cluster();

        //클러스터 선택시 해당 주소와 그 이하의 섬네일 표시
        mClusterManager.setOnClusterClickListener(cluster -> {
            Geocoder mGeoCoder = new Geocoder(activity);
            //섬네일 리스트 상단에 선택 클러스터 주소 표시
            try {
                List<Address> mResultList = mGeoCoder.getFromLocation(cluster.getPosition().latitude,cluster.getPosition().longitude,1);
                position.setText(String.valueOf(mResultList.get(0).getAddressLine(0)));
                position.setTextColor(Color.WHITE);
            }
            catch (IOException e) {
                e.printStackTrace();
            }


            databaseAccess.open();
            //선택 클러스터에 포함된 마커들 섬네일 표시
            for(ClusterItem clusterItem : cluster.getItems()){
                double a = clusterItem.getPosition().latitude;
                double b = clusterItem.getPosition().longitude;

                linearLayout.setVisibility(View.VISIBLE);

                //DB제작 이후 해당부분 업데이트 필요 - 현재는 테스트용으로 코딩함
                // 위도,경도 받은 이후 DB 서치 -> 해당 섬네일 또는 경로ArrayList에 저장
//                if((a!=0)&(b!=0)){
//                    mapRecyclerViewModels.add(new MapRecyclerViewModel("SUCCESS"));
//                }
                mapRecyclerViewModels.add(databaseAccess.getDataForMap(String.valueOf(b),String.valueOf(a)));

            }

            databaseAccess.close();
            MapRecyclerViewAdapter itemListDataAdapter = new MapRecyclerViewAdapter(mContext, mapRecyclerViewModels);
            mapRecyclerView.setHasFixedSize(true);
            mapRecyclerView.setAdapter(itemListDataAdapter);
            mapRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mapRecyclerViewModels = new ArrayList<>();
            return false;
        });
    }
}
