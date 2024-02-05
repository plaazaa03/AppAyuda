package www.ieslosmontecillos.es.appayuda;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class WebViewSample extends Application {
    private Scene scene;

    @Override
    public void start(Stage stage) {
        //creamos la escena
        stage.setTitle("Web View");
        scene = new Scene(new Browser(),900,600, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add(WebViewSample.class.getResource("css/BrowserToolbar.css").toExternalForm()
        );
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

    class Browser extends Region{
        //Estamos creando una barra de herramientas con 4 items
        private HBox toolBar;
        private static String[] imageFiles = new String[]{
                "/Images/product.png",
                "/Images/blog.png",
                "/Images/documentation.png",
                "/Images/partners.png "

        };
        private static String[] captions = new String[]{
                "Products",
                "Blogs",
                "Documentation",
                "Partners "

        };
        private static String[] urls = new String[]{
                "http://www.oracle.com/products/index.html",
                "http://blogs.oracle.com/",
                "http://docs.oracle.com/javase/index.html",
                "http://www.oracle.com/partners/index.html ",

        };
        final ImageView selectedImage = new ImageView();
        final Hyperlink[] hpls = new Hyperlink[captions.length];
        final Image[] images = new Image[imageFiles.length];
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        public Browser(){
            //Aplica el estilo del css
            getStylesheets().add("browser");

            //Para tratar los 3 enlaces
            for (int i = 0; i < captions.length; i++) {
                final Hyperlink hpl = hpls[i] = new Hyperlink(captions[i]);
                InputStream stream = getClass().getResourceAsStream(imageFiles[i]);
                Image image = images[i] = new Image(stream);
                hpl.setGraphic(new ImageView(image));
                final String url = urls[i];

                //proccess event
                hpl.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent e) {
                                        webEngine.load(url);
                                    }
                });

                //Carga la pagina web
                webEngine.load("http://www.oracle.com/products/index.html");

                //Creamos el toolbar
                    toolBar = new HBox();
                    toolBar.setAlignment(Pos.CENTER);
                    toolBar.getStylesheets().add("browser-toolbar");
                    toolBar.getChildren().addAll(createSpacer());

                //Añadimos el componente toolbar
                getChildren().add(toolBar);
                //Añade la vista de la web en la escena
                getChildren().add(browser);
            }
        }
        private Node createSpacer() {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            return spacer;
        }
        @Override
        protected void layoutChildren() {
            double w = getWidth();
            double h = getHeight();
            double tbHeight = toolBar.prefHeight(w);
            layoutInArea(browser,0,0,w,h-tbHeight,0, HPos.CENTER, VPos.CENTER);
            layoutInArea(toolBar,0,h- tbHeight,w,tbHeight,0,HPos.CENTER,VPos.CENTER);
        }


        @Override
        protected double computePrefWidth(double height) {
            return 750;
        }
        @Override
        protected double computePrefHeight(double width) {
            return 500;
        }



    }