package com.duoc.backend.invoice;

import com.duoc.backend.care.Care;
import com.duoc.backend.medication.Medication;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    private InvoiceService invoiceService;

    public record InvoiceRequest(
            String patientName,
            LocalDate date,
            LocalTime time,
            List<Long> careIds,
            List<Long> medicationIds
    ) {}

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return (List<Invoice>) invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public Invoice getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id);
    }

    @PostMapping
    public Invoice saveInvoice(@RequestBody InvoiceRequest request) {
        if (request.careIds() == null || request.medicationIds() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<Care> cares = request.careIds().stream()
                .map(id -> {
                    Care care = new Care("", 0.0);
                    care.setId(id);
                    return care;
                })
                .toList();

        List<Medication> medications = request.medicationIds().stream()
                .map(id -> {
                    Medication medication = new Medication("", 0.0);
                    medication.setId(id);
                    return medication;
                })
                .toList();

        Invoice invoice = new Invoice(
                null,
                request.patientName(),
                request.date(),
                cares,
                medications
        );
        invoice.setTime(request.time());

        return invoiceService.saveInvoice(invoice);
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> generateInvoicePdf(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);

        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            Paragraph title = new Paragraph("Veterinaria Mi Mascota")
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(24)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(title);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Factura ID: " + invoice.getId()));
            document.add(new Paragraph("Paciente: " + invoice.getPatientName()));
            document.add(new Paragraph("Fecha: " + invoice.getDate()));
            document.add(new Paragraph("\n"));

            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));
            table.addCell("Descripción");
            table.addCell("Costo");

            for (Care care : invoice.getCares()) {
                table.addCell(care.getName());
                table.addCell("$" + care.getCost());
            }

            for (Medication medication : invoice.getMedications()) {
                table.addCell(medication.getName());
                table.addCell("$" + medication.getCost());
            }

            document.add(table);
            document.add(new Paragraph("\nTotal: $" + invoice.getTotalCost()));
            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData(
                    "attachment",
                    "invoice_" + id + ".pdf"
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(out.toByteArray());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}