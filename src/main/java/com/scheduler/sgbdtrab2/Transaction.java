package com.scheduler.sgbdtrab2;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Transaction {
    private IntegerProperty trId;
    private StringProperty status;

    public Transaction(Integer trId, String status) {
        this.trId = new SimpleIntegerProperty(trId);
        this.status = new SimpleStringProperty(status);
    }

    public Integer getTrId() {
        return trId.get();
    }

    public void setTrId(Integer trId) {
        this.trId.set(trId);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public IntegerProperty trIdProperty() {
        if (trId == null) trId = new SimpleIntegerProperty(this, "trId");
        return trId;
    }

    public StringProperty statusProperty() {
        if (status == null) status = new SimpleStringProperty(this, "status");
        return status;
    }
}
