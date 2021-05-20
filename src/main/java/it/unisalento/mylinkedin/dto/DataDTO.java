package it.unisalento.mylinkedin.dto;

import it.unisalento.mylinkedin.domain.Data;

import javax.validation.constraints.NotBlank;

public class DataDTO{
    private int id;
    @NotBlank
    private String field;
    private String data;
    private String dataFilePath;

    public DataDTO(){}
    public DataDTO(int id, String field, String data, String dataFilePath) {
        this.id = id;
        this.field = field;
        this.data = data;
        this.dataFilePath = dataFilePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public DataDTO dtoFromDoman(Data data){
        return new DataDTO(data.getId(), data.getField(), data.getData(), data.getDataImage());
    }
}
