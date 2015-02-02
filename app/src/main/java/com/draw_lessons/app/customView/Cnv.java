package com.draw_lessons.app.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.draw_lessons.app.extras.SavePaths;
import com.draw_lessons.app.R;
import com.draw_lessons.app.activities.activity_draw;
import com.draw_lessons.app.actions.Cleaner;
import com.draw_lessons.app.extras.SavePaths;

import java.util.ArrayList;



/**
 * Clase que actuá como un Objeto View personalizado
 * esta clase hace de Canvas.
 * Se utiliza el Canvas para dibujar, y el dibujado se representa
 * en un Bitmap
 * @author Theidel
 *
 */
public class Cnv extends View{

    /*
        Constantes de tamaño de la brocha
     */
    public static final int SIZE_SMALL = 5;
    public static final int SIZE_MEDIUM = 8;
    public static final int SIZE_MAX = 12;
    private int rubIcon; // id de referencia al icono de la papelera


    // Herramientas del canvas
    public static boolean Ruler = false;
    public static boolean Compass = false;
    public static boolean HandMade = false;
    public static boolean Eraser = false;


    private Canvas cnv; //Objeto Canvas para re-renderizar el View
    private Bitmap bmp; //Imagen que queda dibujada sobre nuestro Canvas
    public static Paint p; //Brocha de dibujado principal
    private Path mPa; //Trazo principal de dibujo


    private int brushSize; // Variable para guardar el tamaño actual del Paint
    private int resX,resY; // Variables para indicar la resolución de nuestro Bitmap


    private int doBack = 1;
    private boolean isUnDone = false;

    //Arrays y listas
    public ArrayList<Path> Trazos = new ArrayList<Path>(); //Array para almacenar los pasos hechos en el dibujo
    private ArrayList<Integer> earserPaths = new ArrayList<Integer>(); //Almacena los paths hechos con le goma


    /*
     * Variables de la herrmaienta
     * Regla recta
     */
    public static Bitmap rulerBmp;
    public static boolean rulerLayer = false;
    public static boolean rulerT1=false, rulerT2=false;

    private float rulerX1=0, rulerX2=0, rulerY1=0, rulerY2=0;


    /*
     * Variables de la herramienta
     * Compas
     */
    public static Bitmap compassBmp;
    public static boolean compasLayer = false;
    public static boolean compassT1=false,compassT2=false;

    private float compassX1=0.0F, compassX2=0.0F, compassY1=0.0F, compassY2=0.0F;


   /*
    * Variables inter-herramientas
    */
    private boolean accepted=false; //especifica si está aceptado o no el trazo realizado
    private boolean drawing=true; // especifica si está en fase de dibujado al utilizar una herramienta
    private double p1=0.00d; // variabls para calcular una distancia
    private double p2=0.00d;





    /**
     * Constructores heredados de View
     * @param context
     */

    public Cnv(Context context) {
        super(context);
    }




    /**
     * Metodo para preparar el Cnavas, el bitmap y el Objeto Paint
     * para poder dibujar sobre el View
     */
    public void prepareCancas(){

        //crea el bitmap
        Bitmap.Config  bcfg = Config.RGB_565 ;
        this.bmp = Bitmap.createBitmap(this.resX, this.resY, bcfg);

        //crea la brocha
        this.p = new Paint();
        this.p.setStrokeWidth(this.brushSize);

        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setColor(this.getResources().getColor(R.color.stroke_color));

        this.cnv = new Canvas(this.bmp);
        this.cnv.drawColor(this.getResources().getColor(R.color.back_color));

        this.mPa = new Path();
        this.mPa.moveTo(0, 0);

        this.Trazos.add(mPa);
        this.mPa = new Path();

        this.HandMade=true; //por defecto pintura a mano alzada


    }




    /**
     * Metodo heradado que se encarga
     * de repintar la pantalla al crear el View, o al recibir una orden "Invalidate()"
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.bmp, 0, 0, this.p);

        if (this.rulerLayer==true){
            canvas.drawBitmap(this.rulerBmp, 0, 0,this.p);
        }else if (this.compasLayer==true){
            canvas.drawBitmap(this.compassBmp, 0, 0, this.p);
        }
        else {
            canvas.drawBitmap(this.bmp, 0, 0, this.p);
        }

    }





    /**
     * Método que sobre-escribe lo que ocurre al generar un evento de tipo Touch
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        activity_draw da = (activity_draw)this.getContext();

        if (da.toolClicked==false) {
            da.hide(da.ClickedID);
        }

        if(this.isUnDone==true){
            this.isUnDone=false;
            this.cleanPaths();
        }

        if (this.HandMade==true){
            this.onHandMade(event);
        }
        else if (this.Compass==true){
            this.onCompassTouch2(event);
        }else if(this.Ruler==true){
            this.onRulerTouch(event);
        }else if (this.Eraser==true){
            this.onEraserTouch(event);
        }

        return true;
    }


    /**
     * Método de dibujado de la regla recta
     * @param event
     */
    public void onRulerTouch(MotionEvent event){

        this.rulerBmp = Bitmap.createBitmap(this.resX,this.resY,Bitmap.Config.ARGB_4444);
        Canvas tmpCNV = new Canvas(this.rulerBmp);
        tmpCNV.drawColor(Color.TRANSPARENT);

        Paint tmpP = new Paint();

        tmpP.setStyle(Paint.Style.STROKE);
        tmpP.setStrokeWidth(5);
        tmpP.setColor(this.getResources().getColor(R.color.app_color));

        if (this.drawing==true) {

            if (rulerT1 == true && rulerT2 == true) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        p1 = this.getDistance(this.rulerX1, this.rulerY1, event.getX(), event.getY());
                        p2 = this.getDistance(this.rulerX2, this.rulerY2, event.getX(), event.getY());

                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (p1 < p2) {
                            this.rulerX1 = event.getX();
                            this.rulerY1 = event.getY();
                            tmpCNV.drawLine(this.rulerX1, this.rulerY1, this.rulerX2, this.rulerY2, this.p);
                            tmpCNV.drawCircle(this.rulerX1, this.rulerY1, 50, tmpP);

                            this.invalidate();
                        } else {
                            this.rulerX2 = event.getX();
                            this.rulerY2 = event.getY();
                            tmpCNV.drawLine(this.rulerX1, this.rulerY1, this.rulerX2, this.rulerY2, this.p);
                            tmpCNV.drawCircle(this.rulerX2, this.rulerY2, 50, tmpP);

                            this.invalidate();

                        }
                        break;

                    case MotionEvent.ACTION_UP:

                        tmpCNV.drawLine(this.rulerX1, this.rulerY1, this.rulerX2, this.rulerY2, this.p);
                        tmpCNV.drawCircle(this.rulerX1, this.rulerY1, 50, tmpP);
                        tmpCNV.drawCircle(this.rulerX2, this.rulerY2, 50, tmpP);
                        this.invalidate();

                        break;
                }

            }

            if (Cnv.rulerT1 == true && Cnv.rulerT2 == false) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        Cnv.rulerLayer = true;
                        tmpCNV.drawCircle(event.getX(), event.getY(), 50, tmpP);
                        tmpCNV.drawCircle(this.rulerX1, this.rulerY1, 50, tmpP);
                        this.invalidate();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        tmpCNV.drawCircle(event.getX(), event.getY(), 50, tmpP);
                        tmpCNV.drawCircle(this.rulerX1, this.rulerY1, 50, tmpP);
                        tmpCNV.drawLine(this.rulerX1,this.rulerY1,event.getX(),event.getY(),this.p);
                        this.invalidate();
                        break;

                    case MotionEvent.ACTION_UP:
                        tmpCNV.drawCircle(event.getX(), event.getY(), 50, tmpP);
                        tmpCNV.drawCircle(this.rulerX1, this.rulerY1, 50, tmpP);
                        this.rulerX2 = event.getX();
                        this.rulerY2 = event.getY();
                        tmpCNV.drawPoint(event.getX(), event.getY(), this.p);


                        tmpCNV.drawLine(this.rulerX1, this.rulerY1, this.rulerX2, this.rulerY2, this.p);
                        //Activar Check y la cruz

                        activity_draw.i1.setVisible(true);
                        activity_draw.i2.setVisible(true);

                        this.invalidate();

                        Cnv.rulerT2 = true;
                        break;
                }

            }

            if (Cnv.rulerT1 == false && Cnv.rulerT2 == false) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        Cnv.rulerLayer = true;
                        tmpCNV.drawCircle(event.getX(), event.getY(), 50, tmpP);
                        this.invalidate();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        tmpCNV.drawCircle(event.getX(), event.getY(), 50, tmpP);
                        this.invalidate();
                        break;

                    case MotionEvent.ACTION_UP:
                        tmpCNV.drawCircle(event.getX(), event.getY(), 50, tmpP);
                        this.rulerX1 = event.getX();
                        this.rulerY1 = event.getY();
                        tmpCNV.drawPoint(event.getX(), event.getY(), this.p);

                        Cnv.rulerT1 = true;
                        this.invalidate();
                        break;
                }

            }


        }else if (this.drawing==false){

            if (accepted) {
                this.mPa.moveTo(this.rulerX1, this.rulerY1);
                this.mPa.lineTo(this.rulerX2, this.rulerY2);
                this.cnv.drawPath(this.mPa, this.p);

                this.Trazos.add(this.mPa);
                this.mPa = new Path();

                this.rulerLayer = false;
                this.drawing=true;
                this.rulerT1 = false;
                this.rulerT2 = false;
                this.rulerBmp = null;
            }else {
                this.rulerLayer = false;
                this.drawing=true;
                this.rulerT1 = false;
                this.rulerT2 = false;
                this.rulerBmp = null;
            }
            activity_draw.i1.setVisible(false);
            activity_draw.i2.setVisible(false);
            this.invalidate();
        }


    }


    /**
     * Método para aceptar el trazo a realizar
     * por una herramienta
     */
    public void acceptDraw(){
        this.accepted=true;
        this.drawing=false;
        this.onRulerTouch(null);
        this.p1=0.00d;
        this.p2=0.00d;

    }

    /**
     * Método para rechazar el trazo
     * a una herramienta
     */
    public void dismissDraw(){
        this.accepted=false;
        this.drawing=false;
        this.onRulerTouch(null);
        this.p1=0.00d;
        this.p2=0.00d;
    }


    public static boolean compassT3=false;

    public void acceptCompassRadius(){

        Cnv.compassT3 = true;
        this.onCompassTouch2(null);

    }


    /**
     * aceptación final de el uso de el compás
     */
    public void acceptCompas(){

        this.cnv.drawPath(this.path,this.p);
        this.Trazos.add(this.path);
        this.path= new Path();

        Cnv.compassT3=false;
        Cnv.compassT2=false;
        Cnv.compassT1=false;

        Cnv.compasLayer=false;
        Cnv.compassBmp = null;
        this.circleFixed=false;

        this.invalidate();
    }

    public void dismissCompass(){
        this.path = new Path();
        this.ePa = new Path();
        this.iPa = new Path();

        Cnv.compassT3=false;
        Cnv.compassT2=false;
        Cnv.compassT1=false;

        Cnv.compasLayer=false;
        Cnv.compassBmp = null;
        this.circleFixed=false;

        this.invalidate();

    }


    public void dismissCompassRadius(){

        this.path = new Path();
        this.ePa = new Path();
        this.iPa = new Path();

        Cnv.compassT3=false;
        Cnv.compassT2=false;
        Cnv.compassT1=false;

        Cnv.compasLayer=false;
        Cnv.compassBmp = null;
        this.circleFixed=false;

        this.invalidate();
    }




    /**
     * Método de movimiento con el dibujado a mano alzada
     */
    public void onHandMade(MotionEvent event){

        int e = event.getAction();
        switch(e){
            case MotionEvent.ACTION_DOWN:

                this.mPa.moveTo(event.getX()-1, event.getY()-1);
                this.mPa.lineTo(event.getX()+1, event.getY()+1);
                this.cnv.drawPath(this.mPa, this.p);

                this.invalidate();

                break;

            case MotionEvent.ACTION_MOVE:

                this.mPa.lineTo(event.getX(), event.getY());
                this.cnv.drawPath(this.mPa, this.p);

                this.invalidate();

                break;

            case MotionEvent.ACTION_UP:

                this.Trazos.add(this.mPa);
                this.mPa = new Path();

                this.invalidate();

                break;

            case MotionEvent.ACTION_CANCEL:
                this.Trazos.add(this.mPa);
                this.mPa = new Path();

                this.invalidate();
                break;
        }

    }

    private boolean circleFixed=false;
    Path ePa = new Path();
    Path iPa = new Path();

    Path path = new Path();

    public boolean getCircleFixed(){
        return this.circleFixed;
    }

    public void setCircleFixed(boolean f){
        this.circleFixed = f;
    }


    //////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////

    /**
     * Método definitivo de compás
     * @param event
     */
    public void onCompassTouch2(MotionEvent event){

        Cnv.compassBmp = Bitmap.createBitmap(this.resX,this.resY,Config.ARGB_4444);
        Canvas tmpCnv = new Canvas(Cnv.compassBmp);

        Paint tmpP1 = new Paint();
        tmpP1.setStyle(Paint.Style.STROKE);
        tmpP1.setColor(this.getResources().getColor(R.color.app_color));
        tmpP1.setStrokeWidth(SIZE_SMALL);

        Paint paint = new Paint();
        paint.setStrokeWidth(SIZE_SMALL);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0x33BBBBBB);

        Paint tmpP2 = new Paint();
        tmpP2.setColor(Color.RED);



        if(this.drawing==true){

            if(Cnv.compassT1==true && Cnv.compassT2 == true && Cnv.compassT3 == true){
                float r =(float)this.getDistance(this.compassX1,this.compassY1,this.compassX2,this.compassY2);
                if(!circleFixed){
                    tmpCnv.drawCircle(this.compassX1,this.compassY1,r,paint);
                    tmpCnv.drawLine(this.compassX1, this.compassY1, this.compassX2, this.compassY2, paint);
                    this.circleFixed=true;
                }else{
                    tmpP2.setStyle(Paint.Style.STROKE);
                    ePa.addCircle(this.compassX1,this.compassY1,r,Direction.CCW);
                    tmpCnv.drawPath(ePa,tmpP2);

                    tmpP2.setStyle(Paint.Style.FILL);
                    iPa.addCircle(this.compassX1,this.compassY1,(r-SIZE_SMALL),Direction.CCW);
                    tmpCnv.drawPath(iPa,tmpP2);

                    tmpCnv.clipPath(ePa, Region.Op.REPLACE);
                    tmpCnv.clipPath(iPa, Region.Op.DIFFERENCE);

                    this.invalidate();
                   switch(event.getAction()){

                       case MotionEvent.ACTION_DOWN:
                           tmpCnv.drawCircle(this.compassX1,this.compassY1,r,paint);
                           tmpCnv.drawLine(this.compassX1, this.compassY1, this.compassX2, this.compassY2, paint);

                           this.path.moveTo(this.compassX1,this.compassY1);
                           this.path.lineTo(event.getX(),event.getY());
                           tmpCnv.drawPath(path,this.p);

                       break;

                       case MotionEvent.ACTION_MOVE:
                           tmpCnv.drawCircle(this.compassX1,this.compassY1,r,paint);
                           tmpCnv.drawLine(this.compassX1, this.compassY1, this.compassX2, this.compassY2, paint);

                           this.path.moveTo(this.compassX1,this.compassY1);
                           this.path.lineTo(event.getX(),event.getY());
                           tmpCnv.drawPath(path,this.p);

                           break;

                       case MotionEvent.ACTION_UP:
                           tmpCnv.drawCircle(this.compassX1,this.compassY1,r,paint);
                           tmpCnv.drawLine(this.compassX1, this.compassY1, this.compassX2, this.compassY2, paint);

                           this.path.moveTo(this.compassX1,this.compassY1);
                           this.path.lineTo(event.getX(),event.getY());
                           tmpCnv.drawPath(path,this.p);

                           break;

                    }

                    this.invalidate();
                }


                this.invalidate();
            }


            if(Cnv.compassT1==true && Cnv.compassT2==true && Cnv.compassT3==false){
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        float r =(float)this.getDistance(this.compassX1,this.compassY1,event.getX(),event.getY());
                        tmpCnv.drawCircle(this.compassX1,this.compassY1,r,paint);
                        tmpCnv.drawLine(this.compassX1,this.compassY1,event.getX(),event.getY(),paint);

                        tmpCnv.drawCircle(event.getX(),event.getY(),50,tmpP1);
                    break;
                    case MotionEvent.ACTION_MOVE:
                        r =(float)this.getDistance(this.compassX1,this.compassY1,event.getX(),event.getY());
                        tmpCnv.drawCircle(this.compassX1,this.compassY1,r,paint);
                        tmpCnv.drawLine(this.compassX1,this.compassY1,event.getX(),event.getY(),paint);

                        tmpCnv.drawCircle(event.getX(),event.getY(),50,tmpP1);

                        break;
                    case MotionEvent.ACTION_UP:
                        this.compassX2 = event.getX();
                        this.compassY2 = event.getY();
                        r =(float)this.getDistance(this.compassX1,this.compassY1,event.getX(),event.getY());
                        tmpCnv.drawCircle(this.compassX1,this.compassY1,r,paint);
                        tmpCnv.drawLine(this.compassX1,this.compassY1,this.compassX2,this.compassY2,paint);

                        tmpCnv.drawCircle(event.getX(),event.getY(),50,tmpP1);

                    break;
                }
                this.invalidate();
            }


            if(Cnv.compassT1==true && Cnv.compassT2==false && Cnv.compassT3==false){
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                            tmpCnv.drawCircle(event.getX(),event.getY(),50,tmpP1);
                            tmpCnv.drawCircle(this.compassX1,this.compassY1,50,tmpP1);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        tmpCnv.drawCircle(event.getX(),event.getY(),50,tmpP1);
                        tmpCnv.drawCircle(this.compassX1,this.compassY1,50,tmpP1);

                        //Circulo de referencia
                        float r =(float)this.getDistance(this.compassX1,this.compassY1,event.getX(),event.getY());
                        tmpCnv.drawCircle(this.compassX1,this.compassY1,r,this.p);
                        tmpCnv.drawLine(this.compassX1,this.compassY1,event.getX(),event.getY(),this.p);

                        break;
                    case MotionEvent.ACTION_UP:

                        this.compassX2 = event.getX();
                        this.compassY2 = event.getY();

                        r = (float)this.getDistance(this.compassX1,this.compassY1,this.compassX2,this.compassY2);
                        tmpCnv.drawCircle(this.compassX1,this.compassY1,r, paint);
                        tmpCnv.drawLine(this.compassX1,this.compassY1,this.compassX2,this.compassY2,paint);

                        activity_draw.i1.setVisible(true);
                        activity_draw.i2.setVisible(true);

                        Cnv.compassT2 = true;

                        break;
                }
                this.invalidate();
            }

            if(Cnv.compassT1==false && Cnv.compassT2==false && Cnv.compassT3==false){

               switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                       Cnv.compasLayer=true;
                       tmpCnv.drawCircle(event.getX(),event.getY(),50,tmpP1);
                       //this.invalidate();
                    break;
                    case MotionEvent.ACTION_MOVE:
                        tmpCnv.drawCircle(event.getX(),event.getY(),50,tmpP1);
                    break;
                    case MotionEvent.ACTION_UP:
                        this.compassX1=event.getX();
                        this.compassY1=event.getY();

                        tmpCnv.drawCircle(event.getX(),event.getY(),50,tmpP1);
                        tmpCnv.drawPoint(event.getX(),event.getY(),this.p);

                        Cnv.compassT1=true;

                    break;
               }
                this.invalidate();


            }


        }
    }





    /**
     * Metodo que representa
     * a una goma de borrar mediante
     * Objetos de la clase Path
     * @param event
     */
    public void onEraserTouch(MotionEvent event){

        this.p.setStrokeWidth(this.SIZE_MAX);
        this.p.setColor(this.getResources().getColor(R.color.back_color));

        int e = event.getAction();
        switch(e){
            case MotionEvent.ACTION_DOWN:

                this.mPa.moveTo(event.getX()-1, event.getY()-1);
                this.mPa.lineTo(event.getX()+1, event.getY()+1);
                this.cnv.drawPath(this.mPa, this.p);

                this.invalidate();

                break;

            case MotionEvent.ACTION_MOVE:

                this.mPa.lineTo(event.getX(), event.getY());
                this.cnv.drawPath(this.mPa, this.p);

                this.invalidate();

                break;

            case MotionEvent.ACTION_UP:

                this.Trazos.add(this.mPa);
                this.earserPaths.add(this.mPa.hashCode());
                this.mPa = new Path();
                this.invalidate();

                this.p.setStrokeWidth(this.SIZE_SMALL);
                this.p.setColor(this.getResources().getColor(R.color.stroke_color));


                break;

            case MotionEvent.ACTION_CANCEL:
                this.Trazos.add(this.mPa);
                this.earserPaths.add(this.mPa.hashCode());
                this.mPa = new Path();
                this.invalidate();

                this.p.setStrokeWidth(this.SIZE_SMALL);
                this.p.setColor(this.getResources().getColor(R.color.stroke_color));
                break;
        }

    }



    /**
     * Devuelve la distancia entre
     * 2 puntos
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public double getDistance(float x1, float y1, float x2, float y2){
        double c=0.00D;

        float a = (x1-x2);
        float b = (y1-y2);
        c = Math.sqrt((a*a)+(b*b));
        return c;
    }


    /**
     * devuelve la distancia entre 2 puntos.
     * Lo que seria el radio de un circulo
     * desde su centro
     * @return
     */
    public double getRadius(){

        float a = (this.compassX1 - this.compassX2);
        float b = (this.compassY1 - this.compassY2);

        double c = Math.sqrt((a*a)+(b*b));

        return c;

    }


    public void savePaths(){
        SavePaths p = SavePaths.getInstance();
        p.setList(this.Trazos);
        p.setListB(this.earserPaths);
    }


    /**
     * restore de Paths
     */
    public void restorePaths(){

        SavePaths p = SavePaths.getInstance();
        this.Trazos = p.getList();
        this.earserPaths = p.getList2();


        this.bmp = Bitmap.createBitmap(this.resX, this.resY, Bitmap.Config.ARGB_4444);
        this.cnv = new Canvas(this.bmp);
        this.cnv.drawColor(this.getResources().getColor(R.color.back_color));


        this.p = new Paint();
        this.p.setStrokeWidth(this.brushSize);
        this.p.setStyle(Paint.Style.STROKE);
        this.p.setStrokeCap(Paint.Cap.ROUND);
        this.p.setColor(this.getResources().getColor(R.color.stroke_color));


        for (int i=0; i<this.Trazos.size() ; i++){
            Path pa = this.Trazos.get(i);
            if(isDoneWithEraser(pa)){
                this.p.setColor(this.getResources().getColor(R.color.back_color));
                this.p.setStrokeWidth(this.SIZE_MAX);
                this.cnv.drawPath(this.Trazos.get(i),this.p);
            }
            else {
                this.p.setColor(this.getResources().getColor(R.color.stroke_color));
                this.p.setStrokeWidth(this.SIZE_SMALL);
                this.cnv.drawPath(this.Trazos.get(i), this.p);
            }
        }

        this.p.setColor(this.getResources().getColor(R.color.stroke_color));
        this.p.setStrokeWidth(this.SIZE_SMALL);

        this.invalidate();
    }



    /**
     * Repinta el canvas de color blanco para
     * re-establecer la imagen a su estado por defecto
     */
    public void Clean() {
            this.cnv.drawColor(0xFFFFFFFF);
            this.Trazos = new ArrayList<Path>();
            this.invalidate();
        Toast.makeText(this.getContext(),"Nuevo Lienzo",Toast.LENGTH_SHORT).show();
    }


    /**
     * Metodo para des-hacer los ultimos movimientos
     * realizados por el usuario
     */
    public void Undo(){

        this.cnv.drawColor(this.getResources().getColor(R.color.back_color));
        int c = (this.Trazos.size()-doBack)-1;
        int c2 = 0;

        while(c>-1){
            try {
                Path p = this.Trazos.get(c2);

                if (this.isDoneWithEraser(p)){

                    this.p.setColor(this.getResources().getColor(R.color.back_color));
                    this.p.setStrokeWidth(this.SIZE_MAX);

                    this.cnv.drawPath(this.Trazos.get(c2), this.p);
                }else {
                    this.p.setColor(this.getResources().getColor(R.color.stroke_color));
                    this.p.setStrokeWidth(this.SIZE_SMALL);

                    this.cnv.drawPath(this.Trazos.get(c2), this.p);

                }

            }
            catch (ArrayIndexOutOfBoundsException e){
                Toast.makeText(this.getContext(), "No hay más acciones para retroceder", Toast.LENGTH_SHORT).show();
            }
            c2++;
            c--;
        }

        this.invalidate();
        this.isUnDone=true;
        doBack++;
    }




    /**
     * Compreuab si un Path ha sido
     * dibujado con la goma de borrar
     * @param Ptmp
     * @return
     */
    public boolean isDoneWithEraser(Path Ptmp){
        boolean done=false;
        int c=0;

        while(c < (this.earserPaths.size()) ){

            if (this.earserPaths.get(c).hashCode() == Ptmp.hashCode() ){
                done=true;
            }
            c++;
        }
        return done;

    }


    /**
     * Limpia los movimientos realizados por el usuario,
     * ya des-hechos de el Array que los almacena
     */
    public void cleanPaths(){
        Cleaner c= new Cleaner(this.doBack,this.Trazos);
        this.Trazos = c.getTrazos();
        this.doBack = 1;
        this.isUnDone=false;
    }


	/* 
	 * -------------------
	 * -----------------
	 * Getters y Settrs 
	 * -----------------
	 * -------------------
	 */



    /**
     * Método para establecer la resolucion del eje X
     * @param n
     */
    public void setResX(int n){
        this.resX = n;
    }

    /**
     * Método para establecer la resolucion del eje Y
     * @param n
     */
    public void setResY(int n){
        this.resY = n;
    }


    /**
     * Método para establecer el tamaño de la brocha
     * @param n
     */
    public void setStrokeSize(int n){
        this.brushSize = n;
        this.p.setStrokeWidth(this.brushSize);
    }



    public void setRubishIcon(int rubish){
        this.rubIcon = rubish;
    }


    /**
     * get the Bitmap of canvas
     */
   public Bitmap getBitmapt(){
       return this.bmp;
   }


   public Canvas getCnv(){
       return this.cnv;
   }


    public ArrayList<Path> getTrazos() {
        return Trazos;
    }

    public void setTrazos(ArrayList<Path> trazos) {
        Trazos = trazos;
    }

    /**
     * Método para aumenar la calidad del dibujo
     */
    public void ImproveQuality(){

        this.p.setStrokeJoin(Paint.Join.ROUND);
        this.p.setAntiAlias(true);

    }

}