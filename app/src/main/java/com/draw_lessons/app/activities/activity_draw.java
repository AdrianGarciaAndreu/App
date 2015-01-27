package com.draw_lessons.app.activities;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.draw_lessons.app.R;
import com.draw_lessons.app.customView.Cnv;

import java.io.File;
import java.util.ArrayList;

public class activity_draw extends ActionBarActivity {

    private LinearLayout l1;
    private Cnv canvas;
    private MenuItem items[];
    private int AppColor = 0x5500AAEE; //color básico de la aplicacion


    public boolean toolClicked = true;
    public int ClickedID = 0;

    private String appPath=Environment.getExternalStorageDirectory().toString()+"/DrawLessons";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        this.createDrawer();
        //this.ToolbarCustom();

        this.items = new MenuItem[4];
        this.prepareFolders();

    }



    NotificationCompat.Builder nb;


    public void prepareFolders(){

        nb = new NotificationCompat.Builder(this);

        new Thread(new Runnable() {

            @Override
            public void run() {

                File f = new File(appPath);
                if (!f.exists()){
                    f.mkdirs();

                    nb.setSmallIcon(R.drawable.ic_launcher);
                    nb.setContentTitle("Directorio correcto");
                    nb.setContentText("Directorio para DrawLessons creado correctamente.");
                    Uri u = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    NotificationManager nmc = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                    nmc.notify(1, nb.build());


                }

            }
        }).start();

    }


    /**
     * Método que comprueba la versión del S.O
     * y si es superior a las versiones más bajas
     * aumenta la calidad del dibujo.
     * @param c
     */
    public void getVersion(Cnv c){
        int v= Integer.valueOf(Build.VERSION.SDK_INT);
        if (v >= 17){
            //si la version de android es inferior a el nivel de API 17, se reduce la calidad
            // del dibujo.
            c.ImproveQuality();
        }


    }

    /**
     * Método que capta la resolucion de la pantalla, coge el Layout del activity, crea un Objeto
     * de la clase Cnv, le da una resolucion X y una resolucion Y al canvas, lanza el método que
     * prepara el Objeto de Cnv, para poder dibujar, y le da un Tamaño al Objeto Paint o pincel.
     * Agrega al layout recogido, el Objeto de tipo Cnv a forma de Objeto View
     */
    public void createDrawer(){

        int x = this.getWindowManager().getDefaultDisplay().getWidth(); //resolucion del ancho de la pantalla
        int y = this.getWindowManager().getDefaultDisplay().getHeight(); // resolucion del alto de la pantalla

        this.l1 =(LinearLayout)this.findViewById(R.id.LinearLCnv1);


        this.canvas = new Cnv(this);

        this.canvas.setResX(x);
        this.canvas.setResY(y);
        this.canvas.prepareCancas();
        this.canvas.setStrokeSize(canvas.SIZE_SMALL);
        this.canvas.setRubishIcon(R.drawable.rubish);


        this.l1.addView(canvas);
    }


    /**
     * Metodo para gestionar
     * la personalización de la barra de
     * tareas
     */
    public void ToolbarCustom(){
        ActionBar ab = this.getActionBar();
        ColorDrawable cd = new ColorDrawable(0x5500AAEE);
        ab.setBackgroundDrawable(cd);

    }


    /**
     * Método para manjera los menus
     * de la ActionBar del activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.addMenuActions(menu);

        return true;
    }



    /**
     * Metodo para manjera los eventos de los
     * menus del ActionBar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.addMenuItems(item);

        return super.onOptionsItemSelected(item);
    }






    /**
     * Oculta todos los elementos del menu
     * menos el indicado por parametro
     * @param h
     */
    public void hide(int h){

        for (int i=0;i<this.items.length;i++){
            if (this.items[i]!=null){
                if (this.items[i].getItemId()!=h){ this.items[i].setVisible(false); }
            }
        }

    }


    /**
     * Des-Oculta todos los elemenos
     * del menu
     */
    public void UnHide(){
        for (int i=0; i<this.items.length; i++) {
            if(this.items[i]!=null){
                this.items[i].setVisible(true);
                //this.toolClicked=false;
            }
        }
    }




    /**
     * Metodo para añadir menus a la
     * Barra de acion del activity
     * @param menu
     */
    public void addMenuActions(Menu menu){

        this.items[0] = menu.add(0, 0, menu.NONE, "Mano alzada");
        this.items[0].setIcon(R.drawable.hand);
        this.items[0].setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        this.items[1] = menu.add(0, 1, menu.NONE, "Regla Recta");
        this.items[1].setIcon(R.drawable.ruler);
        this.items[1].setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        this.items[1].setVisible(false);

        this.items[2] = menu.add(0, 2, menu.NONE, "Borrado");
        this.items[2].setIcon(R.drawable.eraser);
        this.items[2].setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        this.items[2].setVisible(false);

        this.items[3] = menu.add(0, 4, menu.NONE, "Compas");
        this.items[3].setIcon(R.drawable.compass);
        this.items[3].setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        this.items[3].setVisible(false);

        menu.add(0,3, menu.NONE, "Limpiar").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0,5, menu.NONE, "Guardar").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0,6, Menu.NONE, "Deshacer").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }



    /**
     * Metodo para añadir acciones de control a los items
     * de los Menus de la actionBar
     * @param item
     */
    public void addMenuItems(MenuItem item){
        int id = item.getItemId();

        if(id == 2){
            this.canvas.useEraser();
            this.ClickedID = id;

        }if (id == 1){
            this.canvas.useRuler();
            this.ClickedID = id;
        }if (id == 0){
            this.canvas.useHand();
            this.ClickedID = id;
        }if (id == 4){
            this.canvas.useCompass();
            this.ClickedID = id;
        }

        if ( id !=3 && id !=5 && id !=6){
            if (this.toolClicked==true){
                this.UnHide();
                this.toolClicked=false;
            }
            else{
                this.hide(id);
                this.toolClicked=true;
            }

        }

        if (id == 3){this.canvas.Clean();}
        if (id == 5) {
            this.canvas.SaveIMG();

        }
        if (item.getItemId() == 6){
            this.canvas.Undo();
        }



    }

    //////////////////////////////////


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        this.canvas.savePaths();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.canvas.restorePaths();
    }

    /**
     * retorna el color de la app
     * @return
     */
    public int getAppColor(){
        return this.AppColor;
    }
}
