package www.ieslosmontecillos.es.appayuda;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import netscape.javascript.JSObject;

public class WebViewSample extends Application {

    private Scene scene;

    @Override public void start(Stage stage) {
        // create scene
        stage.setTitle("Web View");
        scene = new Scene(new Browser(),900,600, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add( WebViewSample.class.getResource("/css/BrowserToolbar.css").toExternalForm());
        // show stage
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
class Browser extends Region {
    private HBox toolBar;

    final private static String[] imageFiles = new String[]{
            "/Images/product.png",
            "/Images/blog.png",
            "/Images/documentation.png",
            "/Images/partners.png",
            "/Images/help.png",
            "/Images/twitter.jpg",
            "/Images/moodle.jpg",
            "/Images/facebook.jpg"
    };
    final private static String[] captions = new String[]{
            "Products",
            "Blogs",
            "Documentation",
            "Partners",
            "Help",
            "Twitter",
            "Moodle",
            "Facebook"
    };

    final private static String[] urls = new String[]{
            "http://www.oracle.com/products/index.html",
            "http://blogs.oracle.com/",
            "http://docs.oracle.com/javase/index.html",
            "http://www.oracle.com/partners/index.html",
            WebViewSample.class.getResource("/help.html").toExternalForm(),
            "https://twitter.com/losMontecillos",
            "https://educacionadistancia.juntadeandalucia.es/centros/login/index.php",
            "https://www.facebook.com/ieslosmontecillos/?locale=es_ES"
    };

    final ImageView selectedImage = new ImageView();
    final Hyperlink[] hpls = new Hyperlink[captions.length];
    final Image[] images = new Image[imageFiles.length];
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    final Button toggleHelpTopics = new Button("Toggle Help Topics");
    final WebView smallView = new WebView();
    private boolean needDocumentationButton = false;

    public Browser() {
        //apply the styles
        getStyleClass().add("browser");

        for (int i = 0; i < captions.length; i++) {
            final Hyperlink hpl = hpls[i] = new Hyperlink(captions[i]);
            Image image = images[i] = new Image(getClass().getResourceAsStream(imageFiles[i]));
            hpl.setGraphic(new ImageView (image));
            final String url = urls[i];
            final boolean addButton = (hpl.getText().equals("Help"));

            hpl.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    needDocumentationButton = addButton;
                    webEngine.load(url);
                }
            });
        }

        // create the toolbar
        toolBar = new HBox();
        toolBar.setAlignment(Pos.CENTER);
        toolBar.getStyleClass().add("browser-toolbar");
        toolBar.getChildren().addAll(hpls);
        toolBar.getChildren().add(createSpacer());

        //set action for the button
        toggleHelpTopics.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                webEngine.executeScript("toggle_visibility('help_topics')");
            }
        });
        smallView.setPrefSize(120, 80);
        //handle popup windows
        webEngine.setCreatePopupHandler(
                new Callback<PopupFeatures, WebEngine>() {
                    @Override public WebEngine call(PopupFeatures config) {
                        smallView.setFontScale(0.8);
                        if (!toolBar.getChildren().contains(smallView)) {
                            toolBar.getChildren().add(smallView);
                        }
                        return smallView.getEngine();
                    }
                }
        );
        // process page loading
        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> ov, Worker.State
                            oldState, Worker.State newState) {
                        toolBar.getChildren().remove(toggleHelpTopics);
                        if (newState == Worker.State.SUCCEEDED) {
                            JSObject win = (JSObject) webEngine.executeScript("window");
                            win.setMember("app", new JavaApp());
                            if (needDocumentationButton) {
                                toolBar.getChildren().add(toggleHelpTopics);
                            }
                        }
                    }
                }
        );

// load the home page
        webEngine.load("http://www.ieslosmontecillos.es");



        //add components
        getChildren().add(toolBar);
        getChildren().add(browser);
    }

    // JavaScript interface object
    public class JavaApp {

        public void exit() {
            Platform.exit();
        }
    }

    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        double tbHeight = toolBar.prefHeight(w);
        layoutInArea(browser,0,0,w,h-tbHeight,0, HPos.CENTER, VPos.CENTER);
        layoutInArea(toolBar,0,h-tbHeight,w,tbHeight,0,HPos.CENTER,VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 900;
    }

    @Override protected double computePrefHeight(double width) {
        return 600;
    }
}