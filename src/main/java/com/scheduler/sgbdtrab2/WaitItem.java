package com.scheduler.sgbdtrab2;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;

public class WaitItem  {

    private ObservableList<WaitItemsRecord> waitItemsRecords;


    public WaitItem(){
        this.waitItemsRecords = FXCollections.observableArrayList();

    }

    public ObservableList<WaitItemsRecord> getWaitItemRecords(){
        return this.waitItemsRecords;
    }

    public void addWaitItemRecord(WaitItemsRecord wt) {
        this.waitItemsRecords.add(wt);
    }

//    public void removeWaitItemList(String idItem) {
//        for (WaitItemsRecord itemRecord:
//                waitItemsRecords) {
//            if(itemRecord.getIdItem().equals(idItem)){
//                itemRecord.removeTransaction();
//            }
//        }
//        return null;
//    }

    public boolean hasWaitItem(String idItem) {
        for (WaitItemsRecord itemRecord:
                waitItemsRecords) {
            if(itemRecord.getIdItem().equals(idItem)){
                return true;
            }
        }
        return false;
    }

    public WaitItemsRecord getWaitItem(String idItem) {
        for (WaitItemsRecord itemRecord:
                waitItemsRecords) {
            if(itemRecord.getIdItem().equals(idItem)){
                return itemRecord;
            }
        }
        return null;
    }

    public void addWaitingTransaction(String idItem, int trId, TrManager trManager, LockTable lockTable) {
        boolean existsWaitItemRecord = hasWaitItem(idItem);
        if(!existsWaitItemRecord){ //se nao existir um Registro de WaitItem ele cria
            this.addWaitItemRecord(new WaitItemsRecord(idItem,lockTable, trManager));
        }
        WaitItemsRecord waitItemRecord = getWaitItem(idItem);
        waitItemRecord.addTransaction(trId);
        trManager.setStatus(trId, "esperando");
    }



    public boolean hasWaitingTransactions(String idItem) {
        WaitItemsRecord waitItemRecord = getWaitItem(idItem);
        return !waitItemRecord.getWaitingTransactions().isEmpty();
    }

    public void printWaitItem() {
        String texto = "";
        for ( WaitItemsRecord waitItem : this.waitItemsRecords) {
            texto = texto + "| " + waitItem.getIdItem() + " | " + waitItem.getWaitingTransactions().toString() + " |\n";
        }
        System.out.println("Wait Item:");
        System.out.println("Id Item | Waiting Transactions");
        System.out.println(texto);
    }
}

