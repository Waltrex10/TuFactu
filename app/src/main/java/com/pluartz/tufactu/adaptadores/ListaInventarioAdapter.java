package com.pluartz.tufactu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pluartz.tufactu.R;
import com.pluartz.tufactu.VerInventario;
import com.pluartz.tufactu.entidades.LInventario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaInventarioAdapter extends RecyclerView.Adapter<ListaInventarioAdapter.inventarioViewHolder> {

    static ArrayList<LInventario> listaInventario;
    static ArrayList<LInventario> listaInventariob;
    public ListaInventarioAdapter(ArrayList<LInventario> listaInventario){
        ListaInventarioAdapter.listaInventario = listaInventario;
        listaInventariob = new ArrayList<>();
        listaInventariob.addAll(listaInventario);
    }
    @NonNull
    @Override
    public ListaInventarioAdapter.inventarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_inventario, parent, false);
        return new inventarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaInventarioAdapter.inventarioViewHolder holder, int position) {
        LInventario inventario = listaInventario.get(position);
        holder.tv_nombrei.setText(inventario.getNombre());
        holder.tv_precioi.setText(inventario.getPrecio());
    }

    public void filtradoi(final String buscar) {
        int longitud = buscar.length();
        if (longitud == 0) {
            listaInventario.clear();
            listaInventario.addAll(listaInventariob);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<LInventario> collecion = listaInventario.stream().filter(i -> i.getNombre().toLowerCase().contains(buscar.toLowerCase())).collect(Collectors.toList());
                listaInventario.clear();
                listaInventario.addAll(collecion);
            } else {
                for (LInventario c : listaInventariob) {
                    if (c.getNombre().toLowerCase().contains(buscar.toLowerCase())) {
                        listaInventario.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaInventario.size();
    }

    public class inventarioViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nombrei, tv_precioi;
        public inventarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nombrei = itemView.findViewById(R.id.tv_nombrei);
            tv_precioi = itemView.findViewById(R.id.tv_precioi);

            itemView.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, VerInventario.class);
                intent.putExtra("ID", listaInventario.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            });
        }
    }
}
