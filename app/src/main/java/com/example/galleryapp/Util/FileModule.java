package com.example.galleryapp.Util;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileModule extends Application {

    public static File latestFileModified(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }
        return choice;
    }

    public static Bitmap MakeThumbnail(String PATH){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(PATH);
        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(0);

        return bitmap;
    }

    public static class SaveImage extends AsyncTask<byte[], Void, Void> {
        private Activity activity;

        public SaveImage(Activity activity){
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;


            try {

                File path = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest");
                if (!path.exists()) {
                    path.mkdirs();
                }
                String time =  String.valueOf(System.currentTimeMillis());
                String fileName = time + ".jpg";
//                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                Log.d("time","Preview TIME = " + time);
                File outputFile = new File(path, fileName);

                outStream = new FileOutputStream(outputFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();


                // 갤러리에 반영
                Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(outputFile));
                activity.sendBroadcast(mediaScanIntent);
//                getApplicationContext().sendBroadcast(mediaScanIntent);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}
