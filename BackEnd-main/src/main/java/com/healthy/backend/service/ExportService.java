package com.healthy.backend.service;

import com.healthy.backend.entity.Users;
import com.healthy.backend.repository.UserRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
public class ExportService {

    private final UserRepository userRepository;

    public byte[] exportUserData(String userId, String format) {

        Users user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return switch (format.toLowerCase()) {
            case "csv" -> exportUserAsCSV(user);
            case "json" -> exportUserAsJSON(user);
            case "pdf" -> exportUserAsPDF(user);
            default -> null;
        };
    }

    private byte[] exportUserAsCSV(Users user) {
        String csvData = "UserId, Name, Email\n" + user.getUserId() + ", " + user.getFullName() + ", " + user.getEmail();
        return csvData.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] exportUserAsJSON(Users user) {
        String jsonData = "{ \"userId\": \"" + user.getUserId() + "\", \"name\": \"" + user.getFullName() + "\", \"email\": \"" + user.getEmail() + "\" }";
        return jsonData.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] exportUserAsPDF(Users user) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument, PageSize.A4);

            document.setMargins(50, 50, 50, 50);

            PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Add title
            Paragraph title = new Paragraph("User Details Report")
                    .setFont(titleFont)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Add user details
            document.add(new Paragraph("User ID: " + user.getUserId()).setFont(normalFont).setFontSize(12));
            document.add(new Paragraph("Name: " + user.getFullName()).setFont(normalFont).setFontSize(12));
            document.add(new Paragraph("Email: " + user.getEmail()).setFont(normalFont).setFontSize(12));

            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
