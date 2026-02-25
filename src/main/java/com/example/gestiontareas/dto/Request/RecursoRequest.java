package com.example.gestiontareas.dto.Request;

public class RecursoRequest {

    /* =========================
       ATRIBUTOS
       ========================= */

    private String nombre;
    private int stockTotal;
    private String unidad;
    private Long proyectoId;


    /* =========================
       GETTERS Y SETTERS
       ========================= */

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }
}