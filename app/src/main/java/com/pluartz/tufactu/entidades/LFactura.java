package com.pluartz.tufactu.entidades;

import com.pluartz.tufactu.Clientes;
import com.pluartz.tufactu.Facturas;

public class LFactura  extends Facturas {
    private int id;
    private String numero;
    private String fecha;
    private String descripcion;
    private String dniusuario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDniusuario() {
        return dniusuario;
    }

    public void setDniusuario(String dniusuario) {
        this.dniusuario = dniusuario;
    }
}
