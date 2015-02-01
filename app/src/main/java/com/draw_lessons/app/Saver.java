package com.draw_lessons.app;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Adrian on 30/01/2015.
 */
public class Saver {

    private String path;
    private Bitmap b;

    public Saver(Bitmap b){

        this.path = Environment.getExternalStorageDirectory().toString()+"/DrawLessons/Prueba.png";
        this.b = b;

    }


    public void Save(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    File f = new File(path);
                    FileOutputStream fos = new FileOutputStream(f);
                    b.compress(Bitmap.CompressFormat.PNG,100,fos);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }).start();


    }


}
