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
import com.pluartz.tufactu.entidades.lClientes;

import java.util.ArrayList;


public class ListaClientesAdapter extends RecyclerView.Adapter<ListaClientesAdapter.clienteViewHolder> {

    static ArrayList<lClientes> listaClientes;
    public ListaClientesAdapter(ArrayList<lClientes> listaClientes){
        this.listaClientes = listaClientes;
    }

    @NonNull
    @Override
    public clienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_clientes, parent, false);
        return new clienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaClientesAdapter.clienteViewHolder holder, int position) {
        lClientes cliente = listaClientes.get(position);
        holder.tv_nombrec.setText(cliente.getNombre());
        holder.tv_apellidosc.setText(cliente.getApellidos());
        holder.tv_dnic.setText(cliente.getDni());
        holder.tv_correoc.setText(cliente.getCorreo());
        holder.tv_direccionc.setText(cliente.getDireccion());
        holder.tv_telefonoc.setText(cliente.getTelefono());

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, VerCliente.class);
                    intent.putExtra("ID", listaClientes.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });

        }
    }
}
