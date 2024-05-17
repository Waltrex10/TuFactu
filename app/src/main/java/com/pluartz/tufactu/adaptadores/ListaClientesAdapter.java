package com.pluartz.tufactu.adaptadores;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pluartz.tufactu.R;
import com.pluartz.tufactu.entidades.LClientes;

import java.util.ArrayList;


public class ListaClientesAdapter extends RecyclerView.Adapter<ListaClientesAdapter.clienteViewHolder> {

    ArrayList<LClientes> listaClientes;
    public ListaClientesAdapter(ArrayList<LClientes> listaClientes){
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
        holder.viewNombre.setText(listaClientes.get(position).getNombre());
        holder.viewApellidos.setText(listaClientes.get(position).getApellidos());
        holder.viewDni.setText(listaClientes.get(position).getDni());
        holder.viewCorreo.setText(listaClientes.get(position).getCorreo());
        holder.viewDireccion.setText(listaClientes.get(position).getDireccion());
        holder.viewTelefono.setText(listaClientes.get(position).getTelefono());
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public class clienteViewHolder extends RecyclerView.ViewHolder {

        TextView viewNombre, viewApellidos, viewDni, viewCorreo, viewDireccion, viewTelefono;
        public clienteViewHolder(@NonNull View itemView) {
            super(itemView);
            viewNombre = itemView.findViewById(R.id.tv_nombrec);
            viewApellidos = itemView.findViewById(R.id.tv_nombrec);
            viewDni = itemView.findViewById(R.id.tv_nombrec);
            viewCorreo = itemView.findViewById(R.id.tv_nombrec);
            viewDireccion = itemView.findViewById(R.id.tv_nombrec);
            viewTelefono = itemView.findViewById(R.id.tv_nombrec);

        }
    }
}
