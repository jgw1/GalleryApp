package com.example.galleryapp.CompareFilter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.galleryapp.R;
import com.example.galleryapp.Util.BitmapUtils;
import com.example.galleryapp.Util.GalleryAppCode;

import java.io.File;

import static com.example.galleryapp.CompareFilter.OneFilter.File_Name;

public class practiceseekbar extends AppCompatActivity {
    private ImageView imageview;
    private SeekBar seekbar;
    private File ImageFile;
    private Bitmap bitmap;
    private PictureThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practiceseekbar);

        imageview = findViewById(R.id.practiceImageview);
        seekbar = findViewById(R.id.practiceseekBar);
        File_Name = getIntent().getStringExtra(GalleryAppCode.GoToFilterPath);
        ImageFile = new File(GalleryAppCode.Path+File_Name);
        bitmap = BitmapFactory.decodeFile(ImageFile.getPath());
        imageview.setImageBitmap(bitmap);

        thread = new PictureThread(imageview,bitmap);
        thread.start();

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               thread.adjustBrightness(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
