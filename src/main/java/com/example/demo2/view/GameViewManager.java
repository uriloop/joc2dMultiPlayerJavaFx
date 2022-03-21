package com.example.demo2.view;

import com.example.demo2.conexio.Client;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que gestiona la vista del joc
 * Ens permet saltar d'un stage a un altre
 */
public class GameViewManager {

    private final int GAME_WIDTH = 1200;
    private final int GAME_HEIGHT = 800;
    private Stage mainStage;
    AnchorPane gamePane;
    Scene gameScene;
    Stage gameStage;


    private List<String> input = new ArrayList<>();
    private Client client;
    private String ip;
    private double cicles = 0;

    /**
     * Constructor de la classe
     */
    public GameViewManager() {

        initializeGame();
        createListeners();
    }

    /**
     * Crea els listeners dels inputs del joc
     */
    private void createListeners() {
        gameScene.setOnKeyPressed(e -> {
            input.add(e.getCode().toString());
        });
    }

    /**
     * Inicia l'stage del joc
     */
    private void initializeGame() {

        gamePane= new AnchorPane();
        gameScene= new Scene(gamePane, GAME_WIDTH,GAME_HEIGHT);
        gameStage= new Stage();
        gameStage.setResizable(true);
        gameStage.setScene(gameScene);

    }




    ////(/(////   aKI EL JOC PROPIAMENT DIT




}
