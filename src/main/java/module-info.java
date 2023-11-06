module com.psdat.slangdictionary {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.psdat.slangdictionary to javafx.fxml;
    exports com.psdat.slangdictionary;
}