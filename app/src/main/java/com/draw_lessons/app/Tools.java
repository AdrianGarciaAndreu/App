package com.draw_lessons.app;


/**
 * Created by Adrian on 30/01/2015.
 ^
 * Clase de métodos estaticos
 * para seleccionar la herramienta de uso en el
 * Canvas
 */
public class Tools {


    /**
     * Método para elegir la herramienta
     * de la regla
     */
   public static void useRuler(Cnv c){
       new Thread(new Runnable() {
           @Override
           public void run() {
               Cnv.Compass=false;
               Cnv.HandMade=false;
               Cnv.Eraser=false;
               Cnv.Ruler=true;

               Cnv.compasLayer = false;
               Cnv.compassBmp = null;
               Cnv.compassT1 = false;
               Cnv.compassT2 = false;

               Cnv.p.setColor(0xFF000000);
               Cnv.p.setStrokeWidth(Cnv.SIZE_SMALL);


           }
       }).start();
       c.invalidate();
    }



    /**
     * Método para elegir la herramienta
     * del compas
     */
    public static void useCompass(Cnv c){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Cnv.Ruler = false;
                Cnv.HandMade = false;
                Cnv.Compass = true;
                Cnv.Eraser = false;

                Cnv.rulerLayer = false;
                Cnv.rulerBmp = null;
                Cnv.rulerT1 = false;
                Cnv.rulerT2 = false;

                Cnv.p.setColor(0xFF000000);
                Cnv.p.setStrokeWidth(Cnv.SIZE_SMALL);

            }
        }).start();


        c.invalidate();
    }


    /**
     * Método para elegir la herramienta
     * de Mano alzada
     */
    public static void useHand(Cnv c){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cnv.Ruler = false;
                Cnv.HandMade = true;
                Cnv.Compass = false;
                Cnv.Eraser = false;

                Cnv.compasLayer=false;
                Cnv.compassBmp = null;
                Cnv.compassT1 = false;
                Cnv.compassT2 = false;

                Cnv.rulerLayer = false;
                Cnv.rulerBmp = null;
                Cnv.rulerT1 = false;
                Cnv.rulerT2 = false;

                Cnv.p.setColor(0xFF000000);
                Cnv.p.setStrokeWidth(Cnv.SIZE_SMALL);
            }
        }).start();

        c.invalidate();
    }


    /**
     * Método para elegir la herramienta
     * de Goma de borrar
     */
    public static void useEraser(Cnv c){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cnv.Ruler = false;
                Cnv.HandMade = false;
                Cnv.Compass = false;
                Cnv.Eraser = true;


                Cnv.compasLayer=false;
                Cnv.compassBmp = null;
                Cnv.compassT1 = false;
                Cnv.compassT2 = false;

                Cnv.rulerLayer = false;
                Cnv.rulerBmp = null;
                Cnv.rulerT1 = false;
                Cnv.rulerT2 = false;

                Cnv.p.setColor(0xFFFFFFFF);
                Cnv.p.setStrokeWidth(Cnv.SIZE_SMALL);
            }
        }).start();

        c.invalidate();
    }



}
