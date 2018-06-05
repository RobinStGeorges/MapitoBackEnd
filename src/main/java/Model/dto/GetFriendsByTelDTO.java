package Model.dto;

import java.util.ArrayList;
import java.util.List;

public class GetFriendsByTelDTO {
    public String token;
    public ArrayList<String> listNumero;

    public GetFriendsByTelDTO(String token, ArrayList<String> listNumero) {
        this.token = token;
        this.listNumero = listNumero;
    }

    public GetFriendsByTelDTO() {
    }
}


