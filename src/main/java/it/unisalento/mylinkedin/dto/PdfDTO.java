package it.unisalento.mylinkedin.dto;

import javax.validation.constraints.NotNull;

public class PdfDTO {
    @NotNull
    int[] idPostArray;

    public PdfDTO(){}

    public PdfDTO(@NotNull int[] idPostArray) {
        this.idPostArray = idPostArray;
    }

    public int[] getIdPostArray() {
        return idPostArray;
    }

    public void setIdPostArray(int[] idPostArray) {
        this.idPostArray = idPostArray;
    }
}
