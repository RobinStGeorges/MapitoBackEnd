package service;

import Model.Position;
import Model.Utilisateur;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class Jsonation {
    public String jsonnationUtilisatur(Utilisateur user)throws IOException{
            ObjectMapper mapation = new ObjectMapper();
            String jsonette = mapation.writeValueAsString(user);
            return jsonette;
    }
}
