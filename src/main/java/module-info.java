module www.ieslosmontecillos.es.appayuda {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jdk.jsobject;


    opens www.ieslosmontecillos.es.appayuda to javafx.fxml;
    exports www.ieslosmontecillos.es.appayuda;
}