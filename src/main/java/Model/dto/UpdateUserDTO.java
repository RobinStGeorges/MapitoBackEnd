package Model.dto;

public class UpdateUserDTO {

    public String token, field, value;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(String token, String field, String value) {
        this.token = token;
        this.field = field;
        this.value = value;
    }
}
