module com.scheduler.sgbdtrab2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.scheduler.sgbdtrab2 to javafx.fxml;
    exports com.scheduler.sgbdtrab2;
}