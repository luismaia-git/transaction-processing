package com.scheduler.sgbdtrab2;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class LockTable {
    private ObservableList<LockRecord> lockRecords;
    private int DuracaoRL; //0 curta 1 longa
    private int DuracaoWL; //0 curta 1 longa

    public void addListChangeListener(ListChangeListener<LockRecord> listener) {
        lockRecords.addListener(listener);
    }

    public void removeListChangeListener(ListChangeListener<LockRecord> listener) {
        lockRecords.removeListener(listener);
    }
    public boolean contains(String item) {
        for (LockRecord lockRecord: lockRecords) {
            if(lockRecord.getIdItem().equals(item)){
                return true;
            }
        }
        return false;
    }
    public int getDuracaoRL() {
        return DuracaoRL;
    }

    public int getDuracaoWL() {
        return DuracaoWL;
    }

    public void setDuracaoRL(int duracaoRL) {
        DuracaoRL = duracaoRL;
    }

    public void setDuracaoWL(int duracaoWL) {
        DuracaoWL = duracaoWL;
    }

    public LockTable() {
        lockRecords = FXCollections.observableArrayList();
    }
    public ObservableList<LockRecord> getLockRecords() {
        return this.lockRecords;
    }

    public int hasLock(String item, int trId) {

        for(LockRecord lockRecord: lockRecords){
            if(lockRecord.getIdItem().equals(item)){
                if (lockRecord.getTrId() != trId) {
                    //System.out.println("w1 nao devia entrar aqui");
                    return lockRecord.getTrId();
                }
            }
        }
        //System.out.println("w1 devia entrar aqui");
        return 0;
    }

    public int hasLockWL(String item, int trId) {
    for (LockRecord lockRecord : lockRecords) {
        if (lockRecord.getIdItem().equals(item) && lockRecord.getTipo().equals("escrita")) {
            if (lockRecord.getTrId() != trId) {
                return lockRecord.getTrId();
            }
        }
    }
    return 0;
}
    /*public int hasLockWL(String item, int trId) {

        for(LockRecord lockRecord: lockRecords){
            if(lockRecord.getIdItem().equals(item) && lockRecord.getTipo().equals("escrita")){
                if(lockRecord.getTrId() != trId) {
                    return lockRecord.getTrId();
                }
            }
        }
        return 0;
    }*/

    //ver isso

    public void commit(Integer TrId) {

        Iterator<LockRecord> iterator = lockRecords.iterator();
        while(iterator.hasNext()){
            LockRecord currentLock = iterator.next();
            if(currentLock.getTrId() == TrId){
                iterator.remove();
            }
        }
        /*
            for(LockRecord lockRecord: lockRecords){
                if(lockRecord.getTrId() == (transaction.getTrId())){

                    if(lockRecord.getDuracao().equals("longa")){
                        UL(transaction, lockRecord.getIdItem());
                        if(!iditems.contains(lockRecord.getIdItem())){
                            iditems.add(lockRecord.getIdItem());
                        }
                    }
                }

            }

            System.out.printf("aaa: countLockRecord %s", countLockRecords);
            if(countLockRecordsLong != 0) {
                // a gente liberou todos os bloqueios da Tr X de longa duração
                for (String idItem: iditems) { // limpando lista de espera por todo item que foi liberado na lockTable
                    waitItem.getWaitItem(idItem).removeAllTransactions();
                }
                System.out.println("Bloqueios liberados");
            }else if(countLockRecords == 0){
                System.out.printf("Não existem bloqueios para a transação de id %d",transaction.getTrId());
            }else {
                System.out.printf("Não existem bloqueios de longa duração para a transação de id %d",transaction.getTrId());
            }*/
    }

    public void RL(Transaction Tr, String D) {
        String duracao = "";
        duracao = (this.DuracaoRL == 0) ?  "curta" : "longa";

        LockRecord lr = new LockRecord(D, Tr.getTrId(), "objeto", duracao, "leitura");
        this.lockRecords.add(lr);

    }

    public void WL(Transaction Tr, String D) {
        String duracao = "";
        duracao = (this.DuracaoWL == 0) ?  "curta" : "longa";

        LockRecord lr = new LockRecord(D, Tr.getTrId(), "objeto", duracao, "escrita");
        this.lockRecords.add(lr);

    }

    public void UL(Transaction tr, String D) {
        this.lockRecords.removeIf(lockRecord -> lockRecord.getTrId() == tr.getTrId() && lockRecord.getIdItem().equals(D)
        );
    }

    public LockRecord getLockRecord(int index) {
        // Retorna o registro de bloqueio do array com base no índice
        // ...
        return this.lockRecords.get(index);
    }

    public int getSize() {
        return this.lockRecords.size();
    }

    public void printLockTable() {
        String texto = "";
        for (LockRecord lockRecord : this.lockRecords) {
            texto = texto + "| " + lockRecord.getTipo() + " | " + lockRecord.getTrId() + " | " + lockRecord.getIdItem() + " | " + lockRecord.getDuracao() + " | " + lockRecord.getEscopo() + " |\n";
        }
        System.out.println("Lock Table");
        System.out.println("Tipo | TrId | Id Item | Duracao | Escopo:");
        System.out.println(texto);
    }


}

