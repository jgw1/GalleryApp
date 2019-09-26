package com.example.galleryapp.Gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.DB.DatabaseAccess;
import com.example.galleryapp.Map.MapMarkerItem;
import com.example.galleryapp.Map.MapRecyclerViewAdapter;
import com.example.galleryapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

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
    private ArrayList<GalleryModel> mapClusterModel,mapModel;
    private LinearLayout linearLayout;
    private RecyclerView mapRecyclerView;
    private int TotalClusterItem;
    private Activity activity;
    ArrayList<GalleryModel> mapClusterItemModel;
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

        mapClusterItemModel = new ArrayList<>();

        this.databaseAccess = DatabaseAccess.getInstance(mContext);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        mMap.animateCamera(center);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ClusterManager<MapMarkerItem> mClusterManager = new ClusterManager<>(activity,mMap);


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



        //Custom 마커를 위해 Render setting

        //선택 클러스터를 화면 중앙에 배치
        mMap.setOnCameraChangeListener(mClusterManager);

        databaseAccess.open();
        mapModel = databaseAccess.getDataForMap();
        databaseAccess.close();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnMapClickListener(this);

        //결과확인을 위한 테스트용 세팅
        for(int i = 0; i<mapModel.size();i++){
            double lat = mapModel.get(i).getLatitude();
            double lng = mapModel.get(i).getLongitude();
            mClusterManager.addItem(new MapMarkerItem((new LatLng(lat, lng))));
        }
        Log.d("GQGQGQ","AAFA" + mClusterManager.getMarkerCollection());
        //전체 다 하나의 마커를 하나의 클러스터로 묶을 경우 사용
        mClusterManager.cluster();

        //클러스터 선택시 해당 주소와 그 이하의 섬네일 표시
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MapMarkerItem>() {
            @Override
            public boolean onClusterItemClick(MapMarkerItem mapMarkerItem) {


                Geocoder mGeoCoder = new Geocoder(activity);
                //섬네일 리스트 상단에 선택 클러스터 주소 표시
                try {
                    List<Address> mResultList = mGeoCoder.getFromLocation(mapMarkerItem.getPosition().latitude,mapMarkerItem.getPosition().longitude,1);
                    position.setText(String.valueOf(mResultList.get(0).getAddressLine(0)));
                    position.setTextColor(Color.WHITE);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                double a = mapMarkerItem.getPosition().latitude;
                double b = mapMarkerItem.getPosition().longitude;

                linearLayout.setVisibility(View.VISIBLE);

                for(int i = 0; i<mapModel.size();i++){
                    double lat = mapModel.get(i).getLatitude();
                    double lng = mapModel.get(i).getLongitude();
                    if((a==lat) && (b ==lng))
                        mapClusterItemModel.add(mapModel.get(i));
                }
                MapRecyclerViewAdapter itemListDataAdapter = new MapRecyclerViewAdapter(activity, mapClusterItemModel);
                TotalClusterItem = mapClusterItemModel.size();
                mapRecyclerView.setHasFixedSize(true);
                mapRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                mapRecyclerView.setAdapter(itemListDataAdapter);
                mapClusterItemModel = new ArrayList<>();
                return true;
            }
        });

        ClusterRenderer renderer = new ClusterRenderer(activity,mMap,mClusterManager);
        mClusterManager.setRenderer(renderer);




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



            //선택 클러스터에 포함된 마커들 섬네일 표시
            for(ClusterItem clusterItem : cluster.getItems()){
                double a = clusterItem.getPosition().latitude;
                double b = clusterItem.getPosition().longitude;

                linearLayout.setVisibility(View.VISIBLE);
                for(int i = 0; i<mapModel.size();i++){
                    double lat = mapModel.get(i).getLatitude();
                    double lng = mapModel.get(i).getLongitude();
                    if((a==lat) && (b ==lng))
                        mapClusterModel.add(mapModel.get(i));
                }
            }

            MapRecyclerViewAdapter itemListDataAdapter = new MapRecyclerViewAdapter(activity, mapClusterModel);
            mapRecyclerView.setHasFixedSize(true);
            mapRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            mapRecyclerView.setAdapter(itemListDataAdapter);

            mapClusterModel = new ArrayList<>();
            return false;
        });
    }
    public class ClusterRenderer extends DefaultClusterRenderer<MapMarkerItem> {
        public ClusterRenderer(Context context, GoogleMap map, ClusterManager<MapMarkerItem> clusterManager) {
            super(context, map, clusterManager);
            activity  = (Activity) context;
        }
        @Override
        //뭉치 마커 커스텀 해주는 부분
        protected void onBeforeClusterRendered(Cluster<MapMarkerItem> cluster, MarkerOptions markerOptions){
            final IconGenerator mClusterIconGenerator;
            mClusterIconGenerator = new IconGenerator(activity);

            //클러스터 디자인 설정
            LayoutInflater myInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View activityview = myInflater.inflate(R.layout.clustermarker,null,false);
            TextView textview = activityview.findViewById(R.id.textview);
            ImageView imageView = activityview.findViewById(R.id.flag);
//
//            Bitmap bitmap = Thumbnail.MakeThumbnail("FullscreenActionBarStyle");

            //해당 클러스터에 포함된 마커 개수들 표현
            textview.setText(String.valueOf(cluster.getSize()));
//            for(ClusterItem clusterItem : cluster.getItems()){
//                Log.d("HAHAHA","GETITEMS"+clusterItem.getPosition());
//            }

//            textview.setBackground(ContextCompat.getDrawable(mContext,R.drawable.background_circle));
            //해당 클러스터에 포함된 마커 개수들 표현 디자인 설정
            textview.setTextColor(Color.BLACK);

            textview.setTextSize(15f);
            textview.setGravity(View.TEXT_ALIGNMENT_CENTER);

            mClusterIconGenerator.setContentView(activityview);
            // mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext,R.drawable.background_circle));
            mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);
            final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        //낱개 마커 커스텀 해주는 부분
        @Override
        protected void  onBeforeClusterItemRendered(MapMarkerItem markerItem, MarkerOptions markerOptions){
            final IconGenerator mClusterIconGenerator;
            mClusterIconGenerator = new IconGenerator(activity);

            //클러스터 디자인 설정
            LayoutInflater myInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View activityview = myInflater.inflate(R.layout.clustermarker,null,false);
            TextView textview = activityview.findViewById(R.id.textview);
            ImageView imageView = activityview.findViewById(R.id.flag);

            //
//            Bitmap bitmap = Thumbnail.MakeThumbnail("FullscreenActionBarStyle");

            //해당 클러스터에 포함된 마커 개수들 표현

//            for(ClusterItem clusterItem : cluster.getItems()){
//                Log.d("HAHAHA","GETITEMS"+clusterItem.getPosition());
//            }

//            textview.setBackground(ContextCompat.getDrawable(mContext,R.drawable.background_circle));
            //해당 클러스터에 포함된 마커 개수들 표현 디자인 설정
            textview.setText("");
            Log.d("HAHAHA","TotalClusterItem" + TotalClusterItem);
            Log.d("HAHAHA","markerItem" + markerItem.getLocation());
            textview.setTextSize(15f);
            textview.setGravity(View.TEXT_ALIGNMENT_CENTER);

            mClusterIconGenerator.setContentView(activityview);
            // mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext,R.drawable.background_circle));
            mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);
            final Bitmap icon = mClusterIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
//            markerOptions.title("HAHAHA");
        }


    }
}
