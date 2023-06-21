package sn.sonatel.mfdev.web.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.mfdev.domain.Test;
import sn.sonatel.mfdev.repository.TestRepository;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final TestRepository testRepository;

    @Autowired
    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Read the Excel file
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Process the data and save to the database
            List<Test> tests = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getCell(0).getStringCellValue().equals("nom")) continue;
                Test test = new Test();
                test.setNom(row.getCell(0).getStringCellValue());
                test.setPrenom(row.getCell(1).getStringCellValue());
                tests.add(test);
            }
            testRepository.saveAll(tests);

            workbook.close();
            return ResponseEntity.ok("File uploaded and data imported successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during file upload.");
        }
    }
}
