package com.example.galleryapp.Camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.galleryapp.DB.GalleryDBAccess;
import com.example.galleryapp.Gallery.GalleryFragment;
import com.example.galleryapp.R;
import com.example.galleryapp.Util.CustomGridView;
import com.example.galleryapp.Util.HashtagCustomDialog;
import com.google.android.material.snackbar.Snackbar;

import static android.view.View.GONE;

public class CameraActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,View.OnClickListener{
    private HashtagCustomDialog hashtagCustomDialog;
    private GalleryDBAccess galleryDBAccess;
    private static final String TAG = "android_camera_example";
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION};
    private int CameraID;
    private static final int CAMERA_FACING_BACK = Camera.CameraInfo.CAMERA_FACING_BACK;
    private static final int CAMERA_FACING_FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private Camera camera;
    private Context context;
    private CustomGridView customgridview;
    private SurfaceView surfaceView;
    private CameraPreview mCameraPreview;
    private boolean cameraFront = false;
    private View mLayout;
    // (참고로 Toast에서는 Context가 필요했습니다.)


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 상태바를 안보이도록 합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 화면 켜진 상태를 유지합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_camera);
        CameraID = CAMERA_FACING_BACK;
        initComponents();
        CameraPermissionCheck(CameraID);



    }

    private void initComponents(){
        mLayout = findViewById(R.id.layout_main);
        customgridview = findViewById(R.id.customgridview);
        customgridview.bringToFront();
         surfaceView = findViewById(R.id.camera_preview_main);
        galleryDBAccess = GalleryDBAccess.getInstance(getApplicationContext());
        hashtagCustomDialog = new HashtagCustomDialog(this);
        // 런타임 퍼미션 완료될때 까지 화면에서 보이지 않게 해야합니다.

        surfaceView.setVisibility(GONE);
        ImageView button = findViewById(R.id.button_main_capture);
        ImageView IB_GoToGallery = findViewById(R.id.GoToGallery);
        ImageView IB_FlipCamera = findViewById(R.id.FlipCamera);

        button.setOnClickListener(this::onClick);
        IB_GoToGallery.setOnClickListener(this::onClick);
        IB_FlipCamera.setOnClickListener(this::onClick);
    }

    private void CameraPermissionCheck(int CameraID){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int accessfineLocationPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);

            if ( cameraPermission == PackageManager.PERMISSION_GRANTED
                    && writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED &&
                    accessfineLocationPermission == PackageManager.PERMISSION_GRANTED) {
                startCamera(CameraID);


            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this,REQUIRED_PERMISSIONS[2])) {

                    Snackbar.make(mLayout, "이 앱을 실행하려면 카메라와 외부 저장소 접근 권한이 필요합니다.",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            ActivityCompat.requestPermissions( CameraActivity.this, REQUIRED_PERMISSIONS,
                                    PERMISSIONS_REQUEST_CODE);
                        }
                    }).show();


                } else {
                    // 2. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                    // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                    ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);
                }

            }

        } else {

            final Snackbar snackbar = Snackbar.make(mLayout, "디바이스가 카메라를 지원하지 않습니다.",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("확인", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }



    void startCamera(int CameraID){
        mCameraPreview = new CameraPreview(this, this, CameraID, surfaceView);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {

        if ( requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {

                startCamera(CameraID);
            }
            else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[2])) {

                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();

                }else {
                    Snackbar.make(mLayout, "설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_main_capture:
                mCameraPreview.takePicture();
                hashtagCustomDialog.show();
                break;
            case R.id.GoToGallery:
                startActivity(new Intent(this, GalleryFragment.class));
                break;
            case R.id.FlipCamera:
                if(cameraFront) {
                    mCameraPreview.ChangeCamera(CAMERA_FACING_BACK, surfaceView.getHolder());
                    cameraFront = false;
                }else{
                    mCameraPreview.ChangeCamera(CAMERA_FACING_FRONT, surfaceView.getHolder());
                    cameraFront = true;
                }
               break;

        }
    }
}
