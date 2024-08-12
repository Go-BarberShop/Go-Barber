package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.sale.SaleEmailDTO;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j //Anotação para geração de logs
@Component // Componente gernerico que deve ser gerenciado pelo Spring
@RequiredArgsConstructor //Anotação do lombok para a criação de um contrutor com os atributos final
public class EmailService {

    //A anotação do atributo como final, e a injeção dele no construtor substituiu o
    // @Autowired na injeção de dependências devido a problemas encontrados.
    private final freemarker.template.Configuration fmConfiguration;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    @Async
    public void sendPromotionalEmail(String recipitent, SaleEmailDTO sale) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(email);
            mimeMessageHelper.setTo(recipitent);
            mimeMessageHelper.setSubject("Promoção na GoBarber");

            Map<String, Object> promocionalData = new HashMap<>();
            promocionalData.put("nomePromocao",sale.getName());
            promocionalData.put("precoPromocao", String.format("%.2f", sale.getTotalPrice()));
            promocionalData.put("cupom", sale.getCoupon());
            promocionalData.put("endDate", sale.getEndDate().format(formatter));

            mimeMessageHelper.setText(getContentFromTemplate("sale.ftl", promocionalData), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            log.warn("Erro no serviço de email.");
        } catch (TemplateException | IOException e) {
            log.warn("Erro na escrita do template do email promocional");
        }
    }

    private String getContentFromTemplate(String archiveTemplate, Map<String, Object> data) throws IOException, TemplateException {

        Template template = fmConfiguration.getTemplate(archiveTemplate);
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        return html;
    }

}
