package br.edu.ufape.gobarber.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class SaleEmailDTO {

    private String name;
    private double totalPrice;
    private String coupon;
    private LocalDate endDate;
}
