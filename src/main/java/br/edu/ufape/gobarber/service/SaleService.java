package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.SaleEmailDTO;
import br.edu.ufape.gobarber.model.Sale;
import br.edu.ufape.gobarber.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {


    private final SaleRepository saleRepository;
    private final EmailService emailService;

    public void sendPromotionalEmail(Integer idSale) {
        Sale sale = saleRepository.getById(idSale);

        SaleEmailDTO saleEmailDTO = new SaleEmailDTO(sale.getName(), sale.getTotalPrice());

        //List<String> costumersEmails = costumerRepository.findAllEmails();
        List<String> costumersEmails = List.of(
                "cadu29bahia@gmail.com",
                "Mestreguga24@gmail.com",
                "testeemail@gmail.com"
        );

        for(String email : costumersEmails) {
            emailService.sendPromotionalEmail(email, saleEmailDTO);
        }

    }
}
