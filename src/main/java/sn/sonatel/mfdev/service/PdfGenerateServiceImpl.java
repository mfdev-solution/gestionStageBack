package sn.sonatel.mfdev.service;

import com.lowagie.text.DocumentException;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import sn.sonatel.mfdev.config.ApplicationProperties;
import sn.sonatel.mfdev.domain.Stagiaire;

@Service
@Data
public class PdfGenerateServiceImpl implements PdfGenerateService {

    private Logger logger = LoggerFactory.getLogger(PdfGenerateServiceImpl.class);

    private final TemplateEngine templateEngine;

    private final ApplicationProperties applicationProperties;

    @Autowired
    public PdfGenerateServiceImpl(TemplateEngine templateEngine, ApplicationProperties applicationProperties) {
        this.templateEngine = templateEngine;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public boolean generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName) {
        Context context = new Context();
        context.setVariables(data);

        String htmlContent = templateEngine.process(templateName, context);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(applicationProperties.getDirectoty() + pdfFileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();
            return true;
        } catch (FileNotFoundException | DocumentException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public byte[] returnPdfFile(String templateName, Map<String, Object> data) {
        Context context = new Context();
        context.setVariables(data);

        String htmlContent = templateEngine.process(templateName, context);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();
            return outputStream.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] exportToExcel(List<Stagiaire> stagiaires) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Stagiaires");

        // Create header row
        Row headerRow = sheet.createRow(0);
        //        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(0).setCellValue("Nom");
        headerRow.createCell(1).setCellValue("Prénom");
        headerRow.createCell(2).setCellValue("CNI");
        headerRow.createCell(3).setCellValue("Numéro de téléphone");
        headerRow.createCell(4).setCellValue("Formation en cours");
        headerRow.createCell(5).setCellValue("Diplôme obtenu");
        headerRow.createCell(6).setCellValue("Genre");
        headerRow.createCell(7).setCellValue("Niveau D'etude");
        headerRow.createCell(8).setCellValue("Profile");
        headerRow.createCell(9).setCellValue("Ecole de Formation");
        headerRow.createCell(10).setCellValue("Tel Urgence");
        headerRow.createCell(11).setCellValue("Situation Matrimonial");
        headerRow.createCell(12).setCellValue("Adresse ");
        headerRow.createCell(13).setCellValue("Nationalite");
        headerRow.createCell(14).setCellValue("Email");
        headerRow.createCell(15).setCellValue("Date Naissance");
        headerRow.createCell(16).setCellValue("Lieu Naissance ");
        headerRow.createCell(17).setCellValue("Date de Debut");
        headerRow.createCell(18).setCellValue("Date de Fin");
        headerRow.createCell(19).setCellValue("Rémunération");
        headerRow.createCell(20).setCellValue("Direction");
        headerRow.createCell(21).setCellValue("Departement");
        headerRow.createCell(22).setCellValue("Service");
        headerRow.createCell(23).setCellValue("prénom Manager");
        headerRow.createCell(24).setCellValue("Nom Manager");
        headerRow.createCell(25).setCellValue("Matricule du Manager");
        // Add more headers for other attributes

        // Populate data rows
        int rowNum = 1;
        for (Stagiaire stagiaire : stagiaires) {
            Row row = sheet.createRow(rowNum++);
            //            row.createCell(0).setCellValue(stagiaire.getId());
            row.createCell(0).setCellValue(stagiaire.getNom());
            row.createCell(1).setCellValue(stagiaire.getPrenom());
            row.createCell(2).setCellValue(stagiaire.getCni());
            row.createCell(3).setCellValue(stagiaire.getNumeroTelephone());
            row.createCell(4).setCellValue(stagiaire.getFormationEnCours().toString());
            row.createCell(5).setCellValue(stagiaire.getDiplomeObtenu());
            row.createCell(6).setCellValue(stagiaire.getGenre());
            row.createCell(7).setCellValue(stagiaire.getNiveauEtude());
            row.createCell(8).setCellValue(" ");
            row.createCell(9).setCellValue(stagiaire.getEcole());
            row.createCell(10).setCellValue(stagiaire.getNumeroTelUrgence());
            row.createCell(11).setCellValue(stagiaire.getSituationMatrimonial());
            row.createCell(12).setCellValue(stagiaire.getAdresse());
            row.createCell(13).setCellValue(stagiaire.getNationalite());
            row.createCell(14).setCellValue(stagiaire.getEmail());
            row.createCell(15).setCellValue(stagiaire.getDateNaissance());
            row.createCell(16).setCellValue(stagiaire.getLieuNaissance());
            row.createCell(17).setCellValue("");
            row.createCell(18).setCellValue("");
            row.createCell(19).setCellValue("");
            row.createCell(20).setCellValue("");
            row.createCell(21).setCellValue("");
            row.createCell(22).setCellValue("");
            row.createCell(23).setCellValue("");
            row.createCell(24).setCellValue("");
            row.createCell(25).setCellValue("");
            // Add more cells for other attributes
        }
        // Auto-size columns
        for (int i = 0; i < stagiaires.size(); i++) {
            sheet.autoSizeColumn(i);
        }
        // Write the workbook to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        return outputStream.toByteArray();
    }
}
