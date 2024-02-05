module www.ieslosmontecillos.es.appayuda {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens www.ieslosmontecillos.es.appayuda to javafx.fxml;
    exports www.ieslosmontecillos.es.appayuda;
}