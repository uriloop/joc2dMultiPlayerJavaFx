package com.example.demo2;

import com.example.demo2.model.Boto;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewManager {

    private final int WIDTH = 1200;
    private final int HEIGHT = 800;

    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainstage;

    public ViewManager(TheGameMain theGameMain) {

        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        // si no tira li posaré akí aixó
        //mainScene = new Scene(theGameMain.createContent(),HEIGHT,WIDTH);

        //
        mainstage = new Stage();
        mainstage.setScene(mainScene);

        createButtons();

    }

    private void createButtons(){
        Boto playButton= new Boto("Play!");
        playButton.setLayoutY(HEIGHT/2f);
        playButton.setLayoutX(WIDTH/2f-(playButton.getWidthBoto()/2));
        mainPane.getChildren().add(playButton);

    }


    public Scene getMainScene() {
        return mainScene;
    }

    public Stage getMainStage() {

        return mainstage;
    }
}
