package com.draw_lessons.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import draw_lessons.com.drawlessons.R;

/**
 * Created by javichaques on 29/1/15.
 */
public class adapter_curso extends RecyclerView.Adapter<adapter_curso.MyViewHolder> {

    private ArrayList<item_curso> cursosDataSet;
    private webservice wb = new webservice();
    private Context context;

    public adapter_curso(ArrayList<item_curso> curso, Context context) {
        this.cursosDataSet = curso;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nombre_curso;
        ImageView img_curso;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.tv_nombre_curso = (TextView) itemView.findViewById(R.id.tv_nombre_curso);
            this.img_curso = (ImageView) itemView.findViewById(R.id.img_curso);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView tv_nombre_curso = holder.tv_nombre_curso;
        ImageView img_curso = holder.img_curso;

        tv_nombre_curso.setText(cursosDataSet.get(listPosition).getNombre());
        String img_url = cursosDataSet.get(listPosition).getImagen();

        Picasso.with(context).load(img_url).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).into(img_curso);
    }

    @Override
    public int getItemCount() {
        return cursosDataSet.size();
    }
}
