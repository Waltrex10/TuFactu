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
import com.pluartz.tufactu.VerPresupuesto;
import com.pluartz.tufactu.entidades.LClientes;
import com.pluartz.tufactu.entidades.LPresupuesto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaPresupuestoAdapter extends RecyclerView.Adapter<ListaPresupuestoAdapter.presupuestoViewHolder> {
    static ArrayList<LPresupuesto> listaPresupuesto;
    static ArrayList<LPresupuesto> listaPresupuestob;
    public ListaPresupuestoAdapter(ArrayList<LPresupuesto> listaPresupuesto){
        ListaPresupuestoAdapter.listaPresupuesto = listaPresupuesto;
        listaPresupuestob = new ArrayList<>();
        listaPresupuestob.addAll(listaPresupuesto);
    }
    @NonNull
    @Override
    public ListaPresupuestoAdapter.presupuestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_presupuesto, parent, false);
        return new presupuestoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaPresupuestoAdapter.presupuestoViewHolder holder, int position) {
        LPresupuesto presupuesto = listaPresupuesto.get(position);
        holder.tv_numerop.setText(presupuesto.getNumero());
        holder.tv_fechap.setText(presupuesto.getFecha());
        holder.tv_descripcionp.setText(presupuesto.getDescripcion());
    }

    public void filtradop(final String buscar) {
        int longitud = buscar.length();
        if (longitud == 0) {
            listaPresupuesto.clear();
            listaPresupuesto.addAll(listaPresupuestob);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<LPresupuesto> collecion = listaPresupuesto.stream().filter(i -> i.getNumero().toLowerCase().contains(buscar.toLowerCase())).collect(Collectors.toList());
                listaPresupuesto.clear();
                listaPresupuesto.addAll(collecion);
            } else {
                for (LPresupuesto c : listaPresupuestob) {
                    if (c.getNumero().toLowerCase().contains(buscar.toLowerCase())) {
                        listaPresupuesto.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaPresupuesto.size();
    }

    public class presupuestoViewHolder extends RecyclerView.ViewHolder {
        TextView tv_numerop, tv_fechap, tv_descripcionp;
        public presupuestoViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_numerop = itemView.findViewById(R.id.tv_numerop);
            tv_fechap = itemView.findViewById(R.id.tv_fechap);
            tv_descripcionp = itemView.findViewById(R.id.tv_descripcionp);

            itemView.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, VerPresupuesto.class);
                intent.putExtra("ID", listaPresupuesto.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            });
        }
    }
}
