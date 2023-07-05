package com.scheduler.sgbdtrab2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Optional;


public class TrManager {

    private ObservableList<Transaction> transacoes;

    public TrManager() {
        this.transacoes = FXCollections.observableArrayList();
    }

    public ObservableList<Transaction> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(ObservableList<Transaction> transacoes) {
        this.transacoes = transacoes;
    }

    public void adicionarTransacao(Transaction t){
        this.transacoes.add(t);
    }

    public void removerTransacao(int TrId){
        this.transacoes.removeIf(transaction -> transaction.getTrId().equals(TrId));
    }

    public void setStatus(int trId, String st){
        for (Transaction t : transacoes) {
            if(t.getTrId() == (trId)){
                System.out.printf("Atualizando o status da transacao %s: %s\n", trId,st);
                t.setStatus(st);
            }
        }
    }

    public Transaction getTransaction(int trId) {
        for (Transaction t : transacoes) {
            if(t.getTrId() == (trId)){
                return t;
            }
        }
        return null;
    }


    public boolean existsTransactionAndThatIsAborted(Integer trId){
        for (Transaction t: this.transacoes) {
            if(t.getTrId().equals(trId)){
                return t.getStatus().equals("abortada");
            }
        }
        return false;
    }

    public void printTrManager() {
        String texto = "";
        for (Transaction transaction : this.transacoes) {
            texto = texto + "| " + transaction.getTrId() +  " | " + transaction.getStatus() + " |\n";
        }

        System.out.println("Tr Manager: ");
        System.out.println("Tr Id | Status");
        System.out.println(texto);
    }
}
