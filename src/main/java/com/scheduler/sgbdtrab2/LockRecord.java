package com.scheduler.sgbdtrab2;

public class LockRecord {
    private String idItem;
    private int trId;
    private String escopo;
    private String duracao;
    private String tipo;

    public LockRecord(String idItem, int trId, String escopo, String duracao, String tipo) {
        this.idItem = idItem;
        this.trId = trId;
        this.escopo = escopo;
        this.duracao = duracao;
        this.tipo = tipo;
    }


    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public int getTrId() {
        return trId;
    }

    public void setTrId(int trId) {
        this.trId = trId;
    }

    public String getEscopo() {
        return escopo;
    }

    public void setEscopo(String escopo) {
        this.escopo = escopo;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
