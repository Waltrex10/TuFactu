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
import com.pluartz.tufactu.VerCliente;
import com.pluartz.tufactu.VerInventario;
import com.pluartz.tufactu.entidades.LInventario;

import java.util.ArrayList;

public class ListaInventarioAdapter extends RecyclerView.Adapter<ListaInventarioAdapter.inventarioViewHolder> {

    static ArrayList<LInventario> listaInventario;
    public ListaInventarioAdapter(ArrayList<LInventario> listaInventario){
        ListaInventarioAdapter.listaInventario = listaInventario;
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
