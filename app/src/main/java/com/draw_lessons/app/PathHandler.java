package com.draw_lessons.app;

import android.graphics.Path;

import java.util.ArrayList;

/**
 * Created by Adrian on 27/01/2015.
 *
 * Clase destinada al manejo de la lista
 * de Paths cuando se destruye el activity de dibujo
 *
 * clase utilizada a modo de Singleton.
 */
public class PathHandler {

    private static PathHandler handler;
    private ArrayList<Path> list;

    private PathHandler() {
    }


    /**
     * Obtiene una instancia del Objeto
     * @return
     */
    public static PathHandler getInstance() {
        if (handler == null)
            handler = new PathHandler();
        return handler;
    }

    /**
     * Obtiene la Lista de Objetos
     * @return
     */
    public ArrayList<Path> getList() {
        return list;
    }

    /**
     * Establece una lista de Objetos
     * @param list
     */
    public void setList(ArrayList<Path> list) {
        this.list = list;
    }
}
