package com.rs.stayconnected.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import timber.log.Timber;

import static com.rs.stayconnected.utils.Constants.IMAGES_DIR;

/**
 * Created by ranjeetsinha on 19/01/18.
 */

public class ImageUtils {

    private final Context context;

    public ImageUtils(Context context) {
        this.context = context;
    }


    public void saveBitmap(Bitmap bitmapImage, String fileName) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile(fileName));
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Timber.i(":Image Saved" + fileName);
    }

    @NonNull
    private File createFile(String fileName) {
        File directory;

        directory = context.getApplicationContext().getDir(IMAGES_DIR, Context.MODE_PRIVATE);

        if (!directory.exists() && !directory.mkdirs()) {
            Timber.e("Error creating directory " + directory);
        }

        return new File(directory, fileName);
    }


    public Bitmap loadImageFile(String fileName) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile(fileName));
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}

