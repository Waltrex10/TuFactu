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
import com.pluartz.tufactu.entidades.LClientes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ListaClientesAdapter extends RecyclerView.Adapter<ListaClientesAdapter.clienteViewHolder> {

    static ArrayList<LClientes> listaClientes;
    static ArrayList<LClientes> listaClientesb;
    public ListaClientesAdapter(ArrayList<LClientes> listaClientes){
        ListaClientesAdapter.listaClientes = listaClientes;
        listaClientesb = new ArrayList<>();
        listaClientesb.addAll(listaClientes);
    }

    @NonNull
    @Override
    public clienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_clientes, parent, false);
        return new clienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaClientesAdapter.clienteViewHolder holder, int position) {
        LClientes cliente = listaClientes.get(position);
        holder.tv_nombrec.setText(cliente.getNombre());
        holder.tv_apellidosc.setText(cliente.getApellidos());
        holder.tv_dnic.setText(cliente.getDni());
        holder.tv_correoc.setText(cliente.getCorreo());
        holder.tv_direccionc.setText(cliente.getDireccion());
        holder.tv_telefonoc.setText(cliente.getTelefono());
    }

    public void filtradoc(final String buscar) {
        int longitud = buscar.length();
        if (longitud == 0) {
            listaClientes.clear();
            listaClientes.addAll(listaClientesb);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<LClientes> collecion = listaClientes.stream().filter(i -> i.getNombre().toLowerCase().contains(buscar.toLowerCase())).collect(Collectors.toList());
                listaClientes.clear();
                listaClientes.addAll(collecion);
            } else {
                for (LClientes c : listaClientesb) {
                    if (c.getNombre().toLowerCase().contains(buscar.toLowerCase())) {
                        listaClientes.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public static class clienteViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nombrec, tv_apellidosc, tv_dnic, tv_correoc, tv_direccionc, tv_telefonoc;
        public clienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nombrec = itemView.findViewById(R.id.tv_nombrec);
            tv_apellidosc = itemView.findViewById(R.id.tv_apellidosc);
            tv_dnic = itemView.findViewById(R.id.tv_dnic);
            tv_correoc = itemView.findViewById(R.id.tv_correoc);
            tv_direccionc = itemView.findViewById(R.id.tv_direccionc);
            tv_telefonoc = itemView.findViewById(R.id.tv_telefonoc);

            itemView.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, VerCliente.class);
                intent.putExtra("ID", listaClientes.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            });

        }
    }
}
