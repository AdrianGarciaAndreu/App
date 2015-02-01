package com.draw_lessons.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Path;



import java.util.ArrayList;

import draw_lessons.com.drawlessons.R;

/**
 * Created by Adrian on 30/01/2015.
 */
public class Cleaner {

    private int doBack;
    private ArrayList<Path> Trazos;
    private Runnable r;


    /**
     * Constructor para limpiar los Trazos
     * de la pila de trazos guardados
     * @param c
     * @param p
     */
    public Cleaner(int c,ArrayList<Path> p){
       this.doBack = c;
       this.Trazos = p;

        this.cleanPaths();
        new Thread(this.r).start();
    }


    private Cnv ca;
    private Context c;

    /**
     * Constructor para
     * limpiar el lienzo del canvas
    */
    public Cleaner(Context c,Cnv ca){
        this.c = c;
        this.ca = ca;
    }


    public void cleanCanvas(){


        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setMessage(R.string.clean_dialog);
        b.setCancelable(true);

        b.setPositiveButton(R.string.positive_button,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ca.Clean();
                    }
                }
                );

        b.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No hacer nada
            }
        });


        b.setTitle("Limpiar lienzo");
        b.setIcon(c.getResources().getDrawable(R.drawable.rubish));

        AlertDialog d = b.create();
        d.show();



    }




    public void cleanPaths(){
         r = new Runnable() {
            @Override
            public void run() {

                int c = doBack;

                while(c>1){
                    try{
                        Trazos.remove( (Trazos.size()-1) );
                        c--;
                    } catch (ArrayIndexOutOfBoundsException e){
                        break;
                    }
                }

                doBack=1;

            }
        };


    }


    public ArrayList<Path> getTrazos(){
        return this.Trazos;
    }

    public int getDoBack(){
        return this.doBack;
    }

}
