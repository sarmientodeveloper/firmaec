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

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.signatures.PdfSignatureAppearance;

public class Information2Appearance implements CustomAppearance {

    private String nombreFirmante;
    private String reason;
    private String location;
    private String signTime;

    public Information2Appearance(String nombreFirmante, String reason, String location, String signTime) {
        this.nombreFirmante = nombreFirmante;
        this.reason = reason;
        this.location = location;
        this.signTime = signTime;
    }

    @Override
    public void createCustomAppearance(PdfSignatureAppearance signatureAppearance, int pageNumber,
            PdfDocument pdfDocument, Rectangle signaturePositionOnPage) throws IOException {

        PdfFont fontHelvetica = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        PdfFormXObject layer2 = signatureAppearance.getLayer2();
        PdfCanvas canvas = new PdfCanvas(layer2, pdfDocument);

        Rectangle signatureRect = new Rectangle(0, 0, signaturePositionOnPage.getWidth(),
                signaturePositionOnPage.getHeight());

        Div textDiv = new Div();
        textDiv.setHeight(signatureRect.getHeight());
        textDiv.setWidth(signatureRect.getWidth());
        textDiv.setVerticalAlignment(VerticalAlignment.MIDDLE);
        textDiv.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Text texto = new Text("Firmado electrónicamente por:\n");
        Paragraph paragraph = new Paragraph().add(texto).setFont(fontHelvetica).setMargin(0).setMultipliedLeading(0.9f)
                .setFontSize(3.25f);
        textDiv.add(paragraph);

        Text contenido = new Text(nombreFirmante.trim());
        paragraph = new Paragraph().add(contenido).setFont(fontHelvetica).setMargin(0).setMultipliedLeading(0.9f)
                .setFontSize(6.25f);
        textDiv.add(paragraph);

        contenido = new Text("Razón: " + reason);
        paragraph = new Paragraph().add(contenido).setFont(fontHelvetica).setMargin(0).setMultipliedLeading(0.9f)
                .setFontSize(4.25f);
        textDiv.add(paragraph);

        contenido = new Text("Localización: " + location);
        paragraph = new Paragraph().add(contenido).setFont(fontHelvetica).setMargin(0).setMultipliedLeading(0.9f)
                .setFontSize(4.25f);
        textDiv.add(paragraph);

        contenido = new Text("Fecha: " + signTime);
        paragraph = new Paragraph().add(contenido).setFont(fontHelvetica).setMargin(0).setMultipliedLeading(0.9f)
                .setFontSize(4.25f);
        textDiv.add(paragraph);

        Canvas textLayoutCanvas = new Canvas(canvas, signatureRect);
        textLayoutCanvas.add(textDiv);
        textLayoutCanvas.close();
    }
}
