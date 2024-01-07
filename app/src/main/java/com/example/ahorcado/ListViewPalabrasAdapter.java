package com.example.ahorcado;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ListViewPalabrasAdapter extends ArrayAdapter<Palabra> {
    AppCompatActivity appCompatActivity;
    List<Palabra> listaPalabras;

    ListViewPalabrasAdapter(AppCompatActivity context, List<Palabra> listaPalabras) {
        super(context, R.layout.sqlite_activity, listaPalabras);
        this.listaPalabras = listaPalabras;
        appCompatActivity = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        View item = inflater.inflate(R.layout.list_view_palabra, null);

        TextView textViewNombre = item.findViewById(R.id.nombrePalabra);
        textViewNombre.setText(listaPalabras.get(position).getNombre());
        TextView textViewId = item.findViewById(R.id.idPalabra);
        textViewId.setText(listaPalabras.get(position).getId().toString());
        return (item);
    }
}
