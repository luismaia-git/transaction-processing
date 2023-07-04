package com.scheduler.sgbdtrab2;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;


public class ProgramController  {
    public Label text;
//    @FXML
//    public TableColumn<WaitItemsRecord,String> waitItemIdItem;
//    @FXML
//    public TableColumn<WaitItemsRecord, Queue<Integer>> waitItemIdTrs;

    @FXML
    private TableView<LockRecord> LockTable;

    @FXML
    private TableView<WaitItemsRecord> WaitItem;

    @FXML
    private TableView<Transaction> TrManager;

    @FXML
    public TableColumn<LockRecord,Integer> TrId;
    @FXML
    public TableColumn<LockRecord,String> IdItem;
    @FXML
    public TableColumn<LockRecord,String> Tipo;


    public TableColumn<LockRecord,String> Duracao;

    @FXML
    public TableColumn<LockRecord,String> Escopo;

    private LockTable lockTableScheduler;

    private TrManager trManagerScheduler;

    private WaitItem waitItemScheduler;
    public void initData(LockTable lockTable, TrManager trManager, WaitItem waitItem){

        this.lockTableScheduler = lockTable;
        this.trManagerScheduler = trManager;
        this.waitItemScheduler = waitItem;

        //locktable
        TrId.setCellValueFactory(new PropertyValueFactory<>("trId"));
        Tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        IdItem.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        Duracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        Escopo.setCellValueFactory(new PropertyValueFactory<>("escopo"));

        LockTable.setItems(lockTableScheduler.getLockRecords());

        

        //trManager

        TrManager.setItems(trManagerScheduler.getTransacoes());

        TableColumn<Transaction,Integer> trIdCol = new TableColumn<>("TR ID");
        trIdCol.setCellValueFactory(new PropertyValueFactory<>("trId"));
        TableColumn<Transaction,String> trStatusCol = new TableColumn<>("Status");
        trStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TrManager.getColumns().setAll(trIdCol , trStatusCol);
//        trManagertrId.setCellValueFactory(new PropertyValueFactory<>("trId"));
//        trManagerStatus.setCellValueFactory(new PropertyValueFactory<>("status"));





        //waitItem
        WaitItem.setItems(waitItemScheduler.getWaitItemRecords());
        
        TableColumn<WaitItemsRecord,String> IdItemCol = new TableColumn<>("Id Item");
        IdItemCol.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        TableColumn<WaitItemsRecord,Integer[]> IdTrsCol = new TableColumn<>("Transacoes");
        IdTrsCol.setCellValueFactory(new PropertyValueFactory<>("waitingTransactions") );
        WaitItem.getColumns().setAll(IdItemCol , IdTrsCol);
        

    }




}
