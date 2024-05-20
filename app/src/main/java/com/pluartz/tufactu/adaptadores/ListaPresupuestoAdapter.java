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
import com.pluartz.tufactu.entidades.LPresupuesto;

import java.util.ArrayList;

public class ListaPresupuestoAdapter extends RecyclerView.Adapter<ListaPresupuestoAdapter.presupuestoViewHolder> {
    static ArrayList<LPresupuesto> listaPresupuesto;
    public ListaPresupuestoAdapter(ArrayList<LPresupuesto> listaPresupuesto){
        ListaPresupuestoAdapter.listaPresupuesto = listaPresupuesto;
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
                Intent intent = new Intent(context, VerCliente.class);
                intent.putExtra("ID", listaPresupuesto.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            });
        }
    }
}
