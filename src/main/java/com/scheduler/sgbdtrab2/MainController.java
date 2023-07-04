package com.scheduler.sgbdtrab2;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private Stage parentStage;

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }
    @FXML
    private TextField input;

    @FXML
    private ChoiceBox<String> select;
    private String scheduleInput;

    private Integer isolationLevelInput;


    @FXML
    protected void selected() {
        String value = select.getValue();
        if(value.equals("serializable") || value.equals("repeatable read")){
            setIsolationLevelInput(1);
        } else if (value.equals("read committed")) {
            setIsolationLevelInput(2);
        } else if(value.equals("read uncommitted")){
            setIsolationLevelInput(3);
        }
    }

    @FXML
    protected void onSubmit() throws InterruptedException, IOException {
       selected();

       if(validarEntradaDeDados()){
           setScheduleInput(input.getText());
           start();
       }

    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (input.getText().isEmpty()) errorMessage += "Entrada inválida!\n";

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Mostrando a mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ");
            alert.setHeaderText("Campos inválido, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

    public void start() throws InterruptedException, IOException {
        Scheduler scheduler = new Scheduler();



        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                    try {
                        scheduler.execute(scheduleInput, isolationLevelInput);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        //parentStage.hide();



//        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("program-view.fxml"));
//
//        Scene sceneLocktable = new Scene(fxmlLoader.load());
//        ProgramController programController = fxmlLoader.getController();
//        programController.initData(scheduler.getLockTable(), scheduler.getTrManager(), scheduler.getWaitItem());
//        parentStage.setScene(sceneLocktable);
//        parentStage.setTitle("LockTable, WaitItem e TrManager");


    }


    public void setScheduleInput(String scheduleInput) {
        this.scheduleInput = scheduleInput;
    }

    public String getScheduleInput() {
        return scheduleInput;
    }

    public Integer getIsolationLevelInput() {
        return isolationLevelInput;
    }

    public void setIsolationLevelInput(Integer isolationLevelInput) {
        this.isolationLevelInput = isolationLevelInput;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        setScheduleInput("BT(1)r1(x)BT(2)w2(y)r2(y)r1(y)C(1)r2(z)C(2)");
    }
}
