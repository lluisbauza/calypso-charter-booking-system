package com.lluisbauza.calipso.util;

import com.lluisbauza.calipso.dto.ReservationSummary;
import com.lluisbauza.calipso.model.Reservation;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfName;
import org.openpdf.text.pdf.PdfString;
import org.openpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReservationConfirmationFileGenerator {

    private ReservationConfirmationFileGenerator() {
    }

    public static void generateReservationConfirmation(
            Reservation reservation) throws IOException {

        Path outputDirectory = Path.of("generated", "reservations");
        Files.createDirectories(outputDirectory);

        Path pdfPath = outputDirectory.resolve(
                "reservation_" + reservation.getReservationCode() + ".pdf"
        );

        Document document = new Document();

        try {
            PdfWriter writer = PdfWriter.getInstance(
                    document,
                    new FileOutputStream(pdfPath.toFile())
            );

            document.open();

            writer.getInfo().put(
                    PdfName.CREATOR,
                    new PdfString(Document.getVersion())
            );

            document.add(new Paragraph(
                    "************************************************************"
            ));

            document.add(new Paragraph(
                    "CALIPSO - Reservation Confirmation"
            ));

            document.add(new Paragraph(" "));

            document.add(new Paragraph(
                    "Reservation code: "
                            + reservation.getReservationCode()
            ));

            document.add(new Paragraph(
                    "Name: "
                            + reservation.getClient().getName()
            ));

            document.add(new Paragraph(
                    "Boat: "
                            + reservation.getTripType().getBoat().getBoatName()
            ));

            document.add(new Paragraph(
                    "Date: "
                            + reservation.getReservationDate()
            ));

            document.add(new Paragraph(
                    "Departure: "
                            + reservation.getTripType().getDepartureTime()
            ));

            document.add(new Paragraph(
                    "Trip Option: "
                            + reservation.getTripType().getTripOption()
            ));

            document.add(new Paragraph(" "));

            document.add(new Paragraph(
                    "************************************************************"
            ));

        } catch (DocumentException e) {
            throw new IOException(
                    "Could not generate reservation confirmation PDF.",
                    e
            );
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }
}