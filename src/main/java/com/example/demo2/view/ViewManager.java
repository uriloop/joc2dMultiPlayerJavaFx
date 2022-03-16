package com.example.demo2.view;

import com.example.demo2.TheGameMain;
import com.example.demo2.model.Boto;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ViewManager {

    private final int WIDTH = 1200;
    private final int HEIGHT = 800;
private TextField ipField;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainstage;
    private TheGameMain theGameMain;

    public ViewManager(TheGameMain theGameMain) {

        this.theGameMain=theGameMain;
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        // si no tira li posaré akí aixó
        //mainScene = new Scene(theGameMain.createContent(),HEIGHT,WIDTH,ip);
        //

        mainstage = new Stage();
        mainstage.setScene(mainScene);

        createButtons();
        createBackground();
        createLogo();
        createIPField();
        createLabelIP();

    }

    public void start(String ip){
        theGameMain.setIp(ip);
        mainScene = new Scene(theGameMain.createContent(),HEIGHT,WIDTH);
    }

    private void createLabelIP() {
        Label labelIP =new Label();
        labelIP.setLayoutX(WIDTH/2f-130);
        labelIP.setLayoutY(420);

        labelIP.setText("Enter your server IP:");
        try{
            labelIP.setFont(Font.loadFont(new FileInputStream("src/main/resources/retrolab_400.otf"),28));
        }catch(FileNotFoundException e){
            labelIP.setFont(Font.font("Verdana",28));
        }
        mainPane.getChildren().add(labelIP);
    }

    private void createIPField() {

        ipField= new TextField();
        ipField.setLayoutX(WIDTH/2f-(120));
        ipField.setLayoutY(500);
        ipField.setAlignment(Pos.CENTER);
        ipField.setPrefWidth(250);
        ipField.setPrefHeight(50);
        mainPane.getChildren().add(ipField);

    }

    private void createButtons(){
     createPlayButton();

    }

    private void createPlayButton() {
        Boto playButton= new Boto("Play!");
        playButton.setLayoutY(600);
        playButton.setLayoutX(WIDTH/2f-(playButton.getWidthBoto()/2));

        playButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    start(ipField.getText());
                }
            }
        });


        mainPane.getChildren().add(playButton);


    }

    private void createBackground(){
        Image backgroundImage = new Image("background_grass.png",1200,800,false,true);
        BackgroundImage background= new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,null);
        mainPane.setBackground(new Background(background));
    }

    private void createLogo(){
        ImageView logo = new ImageView("logo_joc_javafx.png");
logo.setLayoutX(WIDTH/2f-140);
logo.setLayoutY(40);
logo.setOnMouseEntered(new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent mouseEvent) {
        logo.setEffect(new DropShadow());
    }
});
logo.setOnMouseExited(new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent mouseEvent) {
        logo.setEffect(null);
    }
});

mainPane.getChildren().add(logo);

    }

    public Scene getMainScene() {
        return mainScene;
    }

    public Stage getMainStage() {

        return mainstage;
    }
}
