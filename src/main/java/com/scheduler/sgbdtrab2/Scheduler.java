package com.scheduler.sgbdtrab2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ObjectInputFilter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scheduler {
    private TrManager trManager;
    private LockTable lockTable;
    private WaitItem waitItem;
    private WaitDie waitDie;
    private Graph grafo;

    private ArrayList<String> inputOperations;

    private ObservableList<String> scheduleList;

    private int isolationLevel;

    public TrManager getTrManager() {
        return trManager;
    }

    public LockTable getLockTable() {
        return lockTable;
    }

    public WaitItem getWaitItem() {
        return waitItem;
    }

    public ArrayList<String> getInputOperations() {
        return inputOperations;
    }

    public ObservableList<String> getScheduleList() {
        return scheduleList;
    }

    public int getIsolationLevel() {
        return isolationLevel;
    }

    public Scheduler() {
        this.lockTable = new LockTable();
        this.trManager = new TrManager();
        this.waitItem = new WaitItem();
        this.scheduleList = FXCollections.observableArrayList();
        this.waitDie = new WaitDie();
        this.grafo = new Graph();
    }
    public void setIsolationLevel(int isolationLevel){
        this.isolationLevel = isolationLevel;
    }

    public void execute(String input, int isolationLevel) throws InterruptedException {
        setIsolationLevel(isolationLevel);
        this.inputOperations = new ArrayList<>(Arrays.asList(input.split("(?<=\\))")));

        switch (isolationLevel){
            case 1 -> { //serializable (objeto) //repeatable read (objeto)
                this.lockTable.setDuracaoRL(1); //longa
                this.lockTable.setDuracaoWL(1); //longa
            }
            case 2 -> { // read committed
                this.lockTable.setDuracaoRL(0); //curta
                this.lockTable.setDuracaoWL(1); //longa
            }
            case 3 -> { // read uncommitted
                this.lockTable.setDuracaoWL(0); //curta
            }
        }
        System.out.printf("Nivel de isolamento: %s\n", this.isolationLevel);
        System.out.printf("Schedule de entrada %s: \n", this.inputOperations);
        while(!this.inputOperations.isEmpty()){

            Iterator<String> iterator = this.inputOperations.iterator();

            while (iterator.hasNext()) {
//                System.out.println(this.inputOperations);
                Thread.sleep(1000);

                String command = iterator.next();

                String[] parts = command.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)");
                String operation = parts[0];
                String transaction = parts[1];
                int trId = Integer.parseInt(transaction);

                String itemWithParentesis = parts[2];

                if(operation.equals("BT(")){
                    Transaction t = new Transaction(trId, "ativa");
                    trManager.adicionarTransacao(t);
                    scheduleList.add(command);
                    PrintEstado();
                    iterator.remove();
                }else{
                    String item = extrairLetra(itemWithParentesis);
                    Transaction findedTransaction = trManager.getTransaction(trId);
                      if(!findedTransaction.getStatus().equals("abortada")){
                        if(!findedTransaction.getStatus().equals("esperando") ){
                            System.out.printf("Operação lida: %s\n", command);
                            switch (operation) {
                                case "r" -> { //leitura
                                    if(isolationLevel == 3){ // read uncommited nao tem bloqueio de leitura
                                        schedule(command, iterator); //escalona
                                    }else{

                                        int lockWL = lockTable.hasLockWL(item, trId);
                                        if (lockWL == 0) {
                                            lockTable.RL(findedTransaction, item); //manda bloqueio de leitura sobre o item
                                            schedule(command, iterator); //escalona

                                            if(lockTable.getDuracaoRL() == 0){ //se duração for curta
                                                lockTable.UL(findedTransaction, item);
                                            }
                                        }else{
                                            //tem bloqueio
                                            String status = waitDie.execute(trId, lockWL, this.trManager, this.lockTable, this.grafo); //Aborta ou espera

                                            if(status.equals("esperando")){
                                                waitItem.addWaitingTransaction(item, trId, trManager, lockTable);
                                            }else{
                                                iterator = this.inputOperations.iterator();
                                                removeTransactionFromScheduleResult(trId);
                                                grafo.removeEdgeAborted(findedTransaction.getTrId());
                                            }
//                                            else{
////                                                removeTransactionFromOperations(trId);
//                                            }
                                        }
                                    }
                                    PrintEstado();
                                }
                                case "w" -> {
                                    int lock = lockTable.hasLock(item, trId);
                                    if (lock == 0) {
                                        lockTable.WL(findedTransaction, item);
                                        schedule(command, iterator); //escalona

                                        if(lockTable.getDuracaoWL() == 0){ //se duração for curta
                                            lockTable.UL(findedTransaction, item); //liberar bloqueios
                                        }
                                    }else{
                                        //tem bloqueio
                                        String status = waitDie.execute(trId, lock, this.trManager, this.lockTable, this.grafo); //Aborta ou espera

                                        if(status.equals("esperando")){
                                            waitItem.addWaitingTransaction(item, trId, trManager, lockTable); //adiciona na lista de espera

                                        }else {
                                            iterator = this.inputOperations.iterator();
                                            removeTransactionFromScheduleResult(trId);
                                            grafo.removeEdgeAborted(findedTransaction.getTrId());
                                        }
//                                        else{ // se abortou
////                                            removeTransactionFromOperations(trId); // remove todas as operações
//                                        }
                                    }

                                    //BT(1) r1(x) BT(2) w2(x) BT(3) w3(x)
                                    PrintEstado();
                                }
                                case "C(" -> {
                                    lockTable.commit(findedTransaction.getTrId());
                                    schedule(command, iterator);
                                    grafo.removeEdgeAborted(findedTransaction.getTrId());
                                    PrintEstado();
                                }
                            }
                        }
                    }
                }
            }

            if(justTransactionsAborted()){
                break;
            }
            System.out.println(this.inputOperations);
        }
        System.out.printf("Schedule resultante %s: \n", scheduleList);

        }


        private void schedule(String commandInput, Iterator<String> iterator){
            System.out.println("+============+");
            System.out.printf("Escalonando %s\n", commandInput);
            iterator.remove();
            this.scheduleList.add(commandInput);
            System.out.println("+============+");
        }

    private void removeTransactionFromScheduleResult(int trId) {
//        System.out.println("=============================");
//        System.out.printf("Removendo todas as operações da transação %s\n", trId);
        Iterator<String> iterator = this.scheduleList.iterator();

        while (iterator.hasNext()) {

            String command = iterator.next();

            String[] parts = command.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)");
            String transaction = parts[1];
            int TrId = Integer.parseInt(transaction);
            if (TrId == trId) {
                iterator.remove();
//                System.out.printf("Removeu: %s\n", command);
            }
        }
//        System.out.println("=============================");
    }

    public boolean justTransactionsAborted() {
        for (String operation : this.inputOperations) {
            String[] parts = operation.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)");
            String transaction = parts[1];
            int TrId = Integer.parseInt(transaction);
            if(!trManager.existsTransactionAndThatIsAborted(TrId)) {
                return false;
            }
        }
        return true;
    }

    public void PrintEstado(){
        System.out.println("====================PrintState=======================");
        System.out.printf("Schedule de saida: %s \n", scheduleList);
        this.waitItem.printWaitItem();
        this.lockTable.printLockTable();
        this.trManager.printTrManager();
        this.grafo.showGraph();
        System.out.println("=====================================================");
    }

    public static String extrairLetra(String texto) {
        String padrao = "\\((.*?)\\)";
        Pattern pattern = Pattern.compile(padrao);
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
