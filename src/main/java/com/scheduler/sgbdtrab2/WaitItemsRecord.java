package com.scheduler.sgbdtrab2;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class WaitItemsRecord implements ListChangeListener<LockRecord> {
    private StringProperty idItem;

    public StringProperty idItemProperty() {
        if (idItem == null) idItem = new SimpleStringProperty(this, "idItem");
        return idItem;
    }
    private ListProperty<Integer> waitingTransactions;
//    private Queue<Integer> waitingTransactions; //lista fifo


    public ObservableList<Integer> getWaitingTransactions() { return waitingTransactionsProperty().get(); }
    public ListProperty<Integer> waitingTransactionsProperty() {

        if (waitingTransactions == null) waitingTransactions = new SimpleListProperty<>(this, "waitingTransactions");
        return waitingTransactions;
    }

    private LockTable lockTable;
    private TrManager trManager;

    public WaitItemsRecord(String idItem, LockTable lockTable, TrManager trManager) {
        this.idItem = new SimpleStringProperty(idItem);
        Queue<Integer> minhaFila = new LinkedList<>();
        ObservableList<Integer> observableList = FXCollections.observableArrayList(minhaFila);
        this.waitingTransactions = new SimpleListProperty<>(observableList);
        this.lockTable = lockTable;
        this.trManager = trManager;
        lockTable.addListChangeListener(this);
    }

    @Override
    public void onChanged(Change<? extends LockRecord> c) {
        if (!lockTable.contains(this.idItem.get())) {
//            System.out.printf("Nao contem o item %s \n", idItem.get());
            //libera a primeira transação da lista
            removeAllTransactions();
        }
    }

    public String getIdItem() {
        return idItem.get();
    }

//    public Queue<Integer> getWaitingTransactions() {
//        return this.waitingTransactions;
//    }
    public void setIdItem(String value) { idItemProperty().set(value); }

    public void addTransaction(int trId) {
        if (!this.waitingTransactions.contains(trId)) {
            this.waitingTransactions.get().add(trId);
        }
    }
    /*
    public Integer removeTransaction() {
        Integer firstElement = waitingTransactions.poll(); // Remove e retorna o primeiro elemento da fila
        if (firstElement != null) {
            System.out.println(waitingTransactions);
            return firstElement;
        } else {
            return 0;
        }

    }*/

    private void removeAllTransactions() {
        Iterator<Integer> iterator = getWaitingTransactions().iterator();
        while(iterator.hasNext()){
            Integer trId = iterator.next();
            iterator.remove();
            trManager.setStatus(trId, "ativa");
        }
    }
}
