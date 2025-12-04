package com.iglesia.certificados.service;

import com.iglesia.certificados.model.Bautizo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PdfService {

    public byte[] generarCertificadoBautizo(Bautizo bautizo) {
        try {
            // Crear documento PDF en orientación horizontal
            Document document = new Document(PageSize.A4.rotate(), 40, 40, 35, 35);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            document.open();

            // Obtener el contenido del PDF para dibujar bordes y decoraciones
            PdfContentByte canvas = writer.getDirectContent();

            // ===== DIBUJAR BORDES DECORATIVOS =====
            dibujarBordesDecorativo(canvas, document);

            // ===== CONFIGURAR FUENTES (tamaños reducidos) =====
            Font tituloFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 26, Font.BOLD, new BaseColor(40, 40, 90));
            Font subtituloFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.ITALIC, BaseColor.DARK_GRAY);
            Font nombreFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 22, Font.BOLD, new BaseColor(139, 0, 0));
            Font textoFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);
            Font textoNegrita = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK);
            Font textoItalica = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.DARK_GRAY);
            Font firmaFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK);
            Font ornamentoFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL, new BaseColor(139, 0, 0));

            // ===== ESPACIADO SUPERIOR =====
            document.add(new Paragraph(" ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 8)));

            // ===== ORNAMENTO SUPERIOR =====
            Paragraph ornamentoSuperior = new Paragraph("❊ ✦ ❊", ornamentoFont);
            ornamentoSuperior.setAlignment(Element.ALIGN_CENTER);
            ornamentoSuperior.setSpacingAfter(5);
            document.add(ornamentoSuperior);

            // ===== TÍTULO =====
            Paragraph titulo = new Paragraph("CERTIFICADO DE BAUTISMO", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(3);
            document.add(titulo);

            // ===== SUBTÍTULO =====
            Paragraph subtitulo = new Paragraph("CERTIFICATE OF BAPTISM", subtituloFont);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(8);
            document.add(subtitulo);

            // ===== ORNAMENTO INFERIOR DEL TÍTULO =====
            Paragraph ornamentoInferior = new Paragraph("❊ ✦ ❊", ornamentoFont);
            ornamentoInferior.setAlignment(Element.ALIGN_CENTER);
            ornamentoInferior.setSpacingAfter(12);
            document.add(ornamentoInferior);

            // ===== TEXTO INTRODUCTORIO =====
            Paragraph intro = new Paragraph("In obedience to the command of our Lord Jesus Christ", textoItalica);
            intro.setAlignment(Element.ALIGN_CENTER);
            intro.setSpacingAfter(12);
            document.add(intro);

            // ===== NOMBRE DEL BAUTIZADO =====
            Paragraph nombreCompleto = new Paragraph(
                bautizo.getNombreBautizado() + " " + bautizo.getApellidoBautizado(), 
                nombreFont
            );
            nombreCompleto.setAlignment(Element.ALIGN_CENTER);
            nombreCompleto.setSpacingAfter(2);
            document.add(nombreCompleto);

            // Línea decorativa bajo el nombre
            Paragraph lineaNombre = new Paragraph("_________________________________________", textoFont);
            lineaNombre.setAlignment(Element.ALIGN_CENTER);
            lineaNombre.setSpacingAfter(12);
            document.add(lineaNombre);

            // ===== TEXTO "WAS BAPTIZED" =====
            Paragraph wasBaptized = new Paragraph("was Baptized", textoFont);
            wasBaptized.setAlignment(Element.ALIGN_CENTER);
            wasBaptized.setSpacingAfter(15);
            document.add(wasBaptized);

            // ===== INFORMACIÓN DE LUGAR Y FECHA =====
            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(70);
            tablaInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaInfo.setSpacingAfter(15);

            // Celda "at"
            PdfPCell celdaAt = new PdfPCell();
            celdaAt.setBorder(Rectangle.NO_BORDER);
            Paragraph pAt = new Paragraph();
            pAt.add(new Chunk("at  ", textoFont));
            pAt.add(new Chunk("Parroquia Santa María", textoNegrita));
            pAt.add(new Chunk("\n__________________________", textoFont));
            pAt.setAlignment(Element.ALIGN_CENTER);
            celdaAt.addElement(pAt);
            celdaAt.setPaddingBottom(5);

            // Celda "on"
            PdfPCell celdaOn = new PdfPCell();
            celdaOn.setBorder(Rectangle.NO_BORDER);
            
            // Obtener fecha actual formateada
            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", new Locale("es", "ES"));
            String fechaFormateada = fechaActual.format(formatter);
            
            Paragraph pOn = new Paragraph();
            pOn.add(new Chunk("on  ", textoFont));
            pOn.add(new Chunk(fechaFormateada, textoNegrita));
            pOn.add(new Chunk("\n__________________________", textoFont));
            pOn.setAlignment(Element.ALIGN_CENTER);
            celdaOn.addElement(pOn);
            celdaOn.setPaddingBottom(5);

            tablaInfo.addCell(celdaAt);
            tablaInfo.addCell(celdaOn);
            document.add(tablaInfo);

            // ===== INFORMACIÓN DE PADRES Y PADRINOS =====
            PdfPTable tablaFamilia = new PdfPTable(2);
            tablaFamilia.setWidthPercentage(75);
            tablaFamilia.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaFamilia.setSpacingAfter(18);

            // Padres
            PdfPCell celdaPadres = new PdfPCell();
            celdaPadres.setBorder(Rectangle.NO_BORDER);
            Paragraph pPadres = new Paragraph();
            pPadres.add(new Chunk("Padres / Parents\n", textoNegrita));
            pPadres.add(new Chunk("Padre: ", textoFont));
            pPadres.add(new Chunk(bautizo.getNombrePadre() + "\n", textoItalica));
            pPadres.add(new Chunk("Madre: ", textoFont));
            pPadres.add(new Chunk(bautizo.getNombreMadre(), textoItalica));
            pPadres.setAlignment(Element.ALIGN_LEFT);
            celdaPadres.addElement(pPadres);
            celdaPadres.setPadding(8);

            // Padrinos
            PdfPCell celdaPadrinos = new PdfPCell();
            celdaPadrinos.setBorder(Rectangle.NO_BORDER);
            Paragraph pPadrinos = new Paragraph();
            pPadrinos.add(new Chunk("Padrinos / Godparents\n", textoNegrita));
            pPadrinos.add(new Chunk("Padrino: ", textoFont));
            pPadrinos.add(new Chunk(bautizo.getNombrePadrino() + "\n", textoItalica));
            pPadrinos.add(new Chunk("Madrina: ", textoFont));
            pPadrinos.add(new Chunk(bautizo.getNombreMadrina(), textoItalica));
            pPadrinos.setAlignment(Element.ALIGN_LEFT);
            celdaPadrinos.addElement(pPadrinos);
            celdaPadrinos.setPadding(8);

            tablaFamilia.addCell(celdaPadres);
            tablaFamilia.addCell(celdaPadrinos);
            document.add(tablaFamilia);

            // ===== FIRMAS =====
            PdfPTable tablaFirmas = new PdfPTable(2);
            tablaFirmas.setWidthPercentage(65);
            tablaFirmas.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaFirmas.setSpacingBefore(10);

            // Firma Sacerdote
            PdfPCell celdaSacerdote = new PdfPCell();
            celdaSacerdote.setBorder(Rectangle.NO_BORDER);
            Paragraph pSacerdote = new Paragraph();
            pSacerdote.add(new Chunk("\n\n__________________________\n", firmaFont));
            pSacerdote.add(new Chunk("Pastor Signature", firmaFont));
            pSacerdote.setAlignment(Element.ALIGN_CENTER);
            celdaSacerdote.addElement(pSacerdote);

            // Firma Secretario
            PdfPCell celdaSecretario = new PdfPCell();
            celdaSecretario.setBorder(Rectangle.NO_BORDER);
            Paragraph pSecretario = new Paragraph();
            pSecretario.add(new Chunk("\n\n__________________________\n", firmaFont));
            pSecretario.add(new Chunk("Secretary", firmaFont));
            pSecretario.setAlignment(Element.ALIGN_CENTER);
            celdaSecretario.addElement(pSecretario);

            tablaFirmas.addCell(celdaSacerdote);
            tablaFirmas.addCell(celdaSecretario);
            document.add(tablaFirmas);

            // Cerrar documento
            document.close();

            return baos.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage());
        }
    }

    // Método para dibujar bordes decorativos
    private void dibujarBordesDecorativo(PdfContentByte canvas, Document document) {
        float pageWidth = document.getPageSize().getWidth();
        float pageHeight = document.getPageSize().getHeight();
        float margen = 30;

        // Configurar color y grosor del borde
        canvas.setColorStroke(new BaseColor(40, 40, 90));
        canvas.setLineWidth(2f);

        // Borde exterior
        canvas.rectangle(margen, margen, pageWidth - 2 * margen, pageHeight - 2 * margen);
        canvas.stroke();

        // Borde interior (más delgado)
        canvas.setLineWidth(1f);
        float margenInterior = margen + 8;
        canvas.rectangle(margenInterior, margenInterior, 
                        pageWidth - 2 * margenInterior, 
                        pageHeight - 2 * margenInterior);
        canvas.stroke();

        // Decoraciones en las esquinas
        dibujarEsquinasDecorativas(canvas, margen, pageWidth, pageHeight);
    }

    // Método para dibujar decoraciones en las esquinas
    private void dibujarEsquinasDecorativas(PdfContentByte canvas, float margen, float pageWidth, float pageHeight) {
        float tamañoDecoracion = 20;
        canvas.setLineWidth(1.5f);

        // Esquina superior izquierda
        canvas.moveTo(margen + 15, margen + tamañoDecoracion + 15);
        canvas.lineTo(margen + 15, margen + 15);
        canvas.lineTo(margen + tamañoDecoracion + 15, margen + 15);
        canvas.stroke();

        // Esquina superior derecha
        canvas.moveTo(pageWidth - margen - 15, margen + tamañoDecoracion + 15);
        canvas.lineTo(pageWidth - margen - 15, margen + 15);
        canvas.lineTo(pageWidth - margen - tamañoDecoracion - 15, margen + 15);
        canvas.stroke();

        // Esquina inferior izquierda
        canvas.moveTo(margen + 15, pageHeight - margen - tamañoDecoracion - 15);
        canvas.lineTo(margen + 15, pageHeight - margen - 15);
        canvas.lineTo(margen + tamañoDecoracion + 15, pageHeight - margen - 15);
        canvas.stroke();

        // Esquina inferior derecha
        canvas.moveTo(pageWidth - margen - 15, pageHeight - margen - tamañoDecoracion - 15);
        canvas.lineTo(pageWidth - margen - 15, pageHeight - margen - 15);
        canvas.lineTo(pageWidth - margen - tamañoDecoracion - 15, pageHeight - margen - 15);
        canvas.stroke();
    }
}