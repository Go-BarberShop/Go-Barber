package br.edu.ufape.gobarber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SaleEmailDTO {

    private String name;
    private double totalPrice;
}
