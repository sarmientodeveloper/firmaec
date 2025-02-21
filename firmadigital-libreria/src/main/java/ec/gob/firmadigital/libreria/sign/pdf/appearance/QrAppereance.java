/*
 * Copyright (C) 2021 
 * Authors: Ricardo Arguello
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.*
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ec.gob.firmadigital.libreria.sign.pdf.appearance;

import java.io.IOException;
import java.util.logging.Logger;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.signatures.PdfSignatureAppearance;

import ec.gob.firmadigital.libreria.utils.QRCode;

public class QrAppereance implements CustomAppearance {

    private String nombreFirmante;
    private String reason;
    private String location;
    private String signTime;
    private String infoQR;

    private static final Logger logger = Logger.getLogger(QrAppereance.class.getName());

    public QrAppereance(String nombreFirmante, String reason, String location, String signTime, String infoQR) {
        this.nombreFirmante = nombreFirmante;
        this.reason = reason;
        this.location = location;
        this.signTime = signTime;
        this.infoQR = infoQR;
    }

    @Override
    public void createCustomAppearance(PdfSignatureAppearance signatureAppearance, int pageNumber,
            PdfDocument pdfDocument, Rectangle signaturePositionOnPage) throws IOException {

        signatureAppearance.setPageRect(signaturePositionOnPage);
        signatureAppearance.setPageNumber(pageNumber);

        PdfFormXObject layer2 = signatureAppearance.getLayer2();
        PdfCanvas canvas = new PdfCanvas(layer2, pdfDocument);

        PdfFont fontCourier = PdfFontFactory.createFont(StandardFonts.COURIER);
        PdfFont fontCourierBold = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);

        // Imagen
        byte[] byteQR = null;

        // QR
        String text = "FIRMADO POR: " + nombreFirmante.trim() + "\n";
        text = text + "RAZON: " + reason + "\n";
        text = text + "LOCALIZACION: " + location + "\n";
        text = text + "FECHA: " + signTime + "\n";
        text = text + infoQR;

        try {
            byteQR = QRCode.generateQR(text, (int) signaturePositionOnPage.getHeight(),
                    (int) signaturePositionOnPage.getHeight());
        } catch (Exception e) {
            logger.warning("Error al generar QR: " + e);
        }

        // QR
        Rectangle dataRect = new Rectangle(0, 0, signaturePositionOnPage.getWidth(),
                signaturePositionOnPage.getHeight());

        Rectangle signatureRect = new Rectangle(signaturePositionOnPage.getWidth() / 3, 0,
                signaturePositionOnPage.getWidth(), signaturePositionOnPage.getHeight());

        Div imageDiv = new Div();
        imageDiv.setHeight(dataRect.getHeight());
        imageDiv.setWidth(dataRect.getWidth());
        imageDiv.setVerticalAlignment(VerticalAlignment.MIDDLE);
        imageDiv.setHorizontalAlignment(HorizontalAlignment.CENTER);

        Image image = new Image(ImageDataFactory.create(byteQR));
        image.setAutoScale(true);
        imageDiv.add(image);

        Canvas imageLayoutCanvas = new Canvas(canvas, dataRect);
        imageLayoutCanvas.add(imageDiv);
        imageLayoutCanvas.close();

        Div textDiv = new Div();
        textDiv.setHeight(signatureRect.getHeight());
        textDiv.setWidth(signatureRect.getWidth() - signaturePositionOnPage.getWidth() / 3);
        textDiv.setVerticalAlignment(VerticalAlignment.MIDDLE);
        textDiv.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Text texto = new Text("Firmado electrónicamente por:\n");
        Paragraph paragraph = new Paragraph().add(texto).setFont(fontCourier).setMargin(0).setMultipliedLeading(0.9f)
                .setFontSize(3.25f);
        textDiv.add(paragraph);

        Text contenido = new Text(nombreFirmante.trim());
        paragraph = new Paragraph().add(contenido).setFont(fontCourierBold).setMargin(0).setMultipliedLeading(0.9f)
                .setFontSize(6.25f);
        textDiv.add(paragraph);

        Canvas textLayoutCanvas = new Canvas(canvas, signatureRect);
        textLayoutCanvas.add(textDiv);
        textLayoutCanvas.close();
    }
}
