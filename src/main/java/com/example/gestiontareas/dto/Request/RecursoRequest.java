package com.example.gestiontareas.dto.Request;

public class RecursoRequest {

    private String nombre;
    private int cantidad;
    private String unidadMedida;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return unidadMedida;
    }
    
    public void setUnidad(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
}
