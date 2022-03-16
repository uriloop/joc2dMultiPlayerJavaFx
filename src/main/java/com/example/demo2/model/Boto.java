package com.example.demo2.model;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Boto extends Button {

    private final String FONT_PATH= "src/main/java/com/example/demo2/model/resources/retrolab_400.otf";
  /*  private static final String BUTTON_ON_STYLE= "-fx-background-color: transparent; -fx-background-image: url('../../../../../resources/imatges/button_play_on.png')";  // no em carrega les imatges
    private static final String BUTTON_OFF_STYLE= "-fx-background-color: transparent; -fx-background-image: url('../../../../../resources/imatges/button_play_off.png')";
*/

    private static final String BUTTON_ON_STYLE= "-fx-background-image: url('../../../../../resources/imatges/button_play_on.png')";  // no em carrega les imatges
    private static final String BUTTON_OFF_STYLE= "-fx-background-image: url('../../../../../resources/imatges/button_play_on.png')";  // no em carrega les imatges


    private float width=300;
    private float height=100;
    public Boto(String s) {
        setText(s);
        setButtonFont(FONT_PATH,23);
        setPrefWidth(width);
        setPrefHeight(height);
        setStyle(BUTTON_OFF_STYLE);
        initializeButtonListeners();
    }


    // Posem una font als botons i tambe li passem el tamany. si falla poso una font default
    private void setButtonFont(String font_path, int i) {
        try{
            setFont(Font.loadFont(new FileInputStream(FONT_PATH),i));
        }catch(FileNotFoundException e){
            setFont(Font.font("Verdana",i));
        }


    }
    private void setButtonPressed(){
        setStyle(BUTTON_ON_STYLE);

    }
    private void setButtonReleased(){
        setStyle(BUTTON_OFF_STYLE);
    }

    private void initializeButtonListeners(){


        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    setButtonPressed();
                }
            }
        });
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonReleased();
                }
            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setEffect(new DropShadow());
            }
        });
  setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setEffect(null);
            }
        });

    }


    public float getWidthBoto() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeightBoto() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
