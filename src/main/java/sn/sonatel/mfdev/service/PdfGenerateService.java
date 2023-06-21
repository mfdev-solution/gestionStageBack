package sn.sonatel.mfdev.service;

import java.util.Map;

public interface PdfGenerateService {
    boolean generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName);
}
