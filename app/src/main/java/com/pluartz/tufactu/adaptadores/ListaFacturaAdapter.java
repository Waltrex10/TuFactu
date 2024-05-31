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
import com.pluartz.tufactu.VerFactura;
import com.pluartz.tufactu.entidades.LFactura;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaFacturaAdapter extends RecyclerView.Adapter<ListaFacturaAdapter.facturaViewHolder> {
    static ArrayList<LFactura> listaFactura;
    static ArrayList<LFactura> listaFacturab;
    public ListaFacturaAdapter(ArrayList<LFactura> listaFactura){
        ListaFacturaAdapter.listaFactura = listaFactura;
        listaFacturab = new ArrayList<>();
        listaFacturab.addAll(listaFactura);
    }
    @NonNull
    @Override
    public ListaFacturaAdapter.facturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_factura, parent, false);
        return new facturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaFacturaAdapter.facturaViewHolder holder, int position) {
        LFactura factura = listaFactura.get(position);
        holder.tv_numerof.setText(factura.getNumero());
        holder.tv_fechaf.setText(factura.getFecha());
        holder.tv_descripcionf.setText(factura.getDescripcion());
    }

    public void filtradof(final String buscar) {
        int longitud = buscar.length();
        if (longitud == 0) {
            listaFactura.clear();
            listaFactura.addAll(listaFacturab);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<LFactura> collecion = listaFactura.stream().filter(i -> i.getNumero().toLowerCase().contains(buscar.toLowerCase())).collect(Collectors.toList());
                listaFactura.clear();
                listaFactura.addAll(collecion);
            } else {
                for (LFactura c : listaFacturab) {
                    if (c.getNumero().toLowerCase().contains(buscar.toLowerCase())) {
                        listaFactura.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaFactura.size();
    }

    public class facturaViewHolder extends RecyclerView.ViewHolder {
        TextView tv_numerof, tv_fechaf, tv_descripcionf;
        public facturaViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_numerof = itemView.findViewById(R.id.tv_numerof);
            tv_fechaf = itemView.findViewById(R.id.tv_fechaf);
            tv_descripcionf = itemView.findViewById(R.id.tv_descripcionf);

            itemView.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, VerFactura.class);
                intent.putExtra("ID", listaFactura.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            });
        }
    }
}
