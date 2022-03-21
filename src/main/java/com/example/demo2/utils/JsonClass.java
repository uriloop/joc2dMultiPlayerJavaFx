package com.example.demo2.utils;

import com.example.demo2.model.Joc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe que controla el mapeig i desmapeig a json
 */
public class JsonClass {

    /** Retorna un json donat un estat del joc
     * @param estatJoc
     * @return
     */
    public String getJSON(Joc estatJoc) {
        String resposta="No s'ha processat el json";
        try {
            resposta= new ObjectMapper().writeValueAsString(estatJoc);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resposta;
    }

    /**
     * retorna la instància de joc donat un json
     * @param json
     * @return
     */
    public Joc getObject(String json) {

        try {
            return new ObjectMapper().readValue(json, Joc.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
