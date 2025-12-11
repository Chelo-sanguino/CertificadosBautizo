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
            // 1. Configuración de página (Horizontal A4) y márgenes
            Document document = new Document(PageSize.A4.rotate(), 40, 40, 35, 35);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            document.open();
            PdfContentByte canvas = writer.getDirectContent();

            // 2. Dibujar Marcos Decorativos
            dibujarBordesDecorativo(canvas, document);

            // 3. Fuentes Personalizadas
            // Titulos grandes y elegantes
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, new BaseColor(40, 40, 90)); 
            // Texto normal
            Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            // Texto destacado (Nombres)
            Font nombreFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 24, Font.BOLD, new BaseColor(139, 0, 0)); // Rojo oscuro elegante
            // Etiquetas (ej: "Padres:")
            Font etiquetaFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.DARK_GRAY);
            // Firmas
            Font firmaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC, BaseColor.BLACK);
            // Ornamentos
            Font ornamentoFont = FontFactory.getFont(FontFactory.ZAPFDINGBATS, 14, new BaseColor(139, 0, 0));

            // --- INICIO DEL CONTENIDO ---
            
            document.add(new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, 10))); // Espacio inicial

            // Ornamento Superior
            Paragraph ornamento = new Paragraph("❊", ornamentoFont);
            ornamento.setAlignment(Element.ALIGN_CENTER);
            ornamento.setSpacingAfter(10);
            document.add(ornamento);

            // TÍTULO PRINCIPAL
            Paragraph titulo = new Paragraph("CERTIFICADO DE BAUTISMO", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(5);
            document.add(titulo);

            // Subtítulo solemne
            Paragraph subtitulo = new Paragraph("Que el agua del bautismo sea la base de una vida de fe y compasión", 
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12, BaseColor.GRAY));
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(25);
            document.add(subtitulo);

            // Cuerpo del certificado
            Paragraph certifica = new Paragraph("Se certifica que:", textoFont);
            certifica.setAlignment(Element.ALIGN_CENTER);
            certifica.setSpacingAfter(10);
            document.add(certifica);

            // NOMBRE DEL BAUTIZADO (Grande y destacado)
            Paragraph nombreCompleto = new Paragraph(
                bautizo.getNombreBautizado().toUpperCase() + " " + bautizo.getApellidoBautizado().toUpperCase(), 
                nombreFont
            );
            nombreCompleto.setAlignment(Element.ALIGN_CENTER);
            nombreCompleto.setSpacingAfter(5);
            document.add(nombreCompleto);
            
            // Línea divisoria
            Paragraph linea = new Paragraph("_____________________________________________", textoFont);
            linea.setAlignment(Element.ALIGN_CENTER);
            linea.setSpacingAfter(20);
            document.add(linea);

            // Texto central
            Paragraph textoCentral = new Paragraph("Recibió el Santo Sacramento del Bautismo", textoFont);
            textoCentral.setAlignment(Element.ALIGN_CENTER);
            textoCentral.setSpacingAfter(20);
            document.add(textoCentral);

            // TABLA DE LUGAR Y FECHA (Para alinear bonito)
            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(80);
            tablaInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            // Lugar
            PdfPCell celdaLugar = new PdfPCell();
            celdaLugar.setBorder(Rectangle.NO_BORDER);
            celdaLugar.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaLugar.addElement(new Paragraph("En la Parroquia:", etiquetaFont));
            celdaLugar.addElement(new Paragraph("Perpetuo Socorro", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            
            // Fecha (Formateada en español)
            LocalDate fecha = bautizo.getFechaBautizo() != null ? bautizo.getFechaBautizo() : LocalDate.now();
            String fechaStr = fecha.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES")));
            
            PdfPCell celdaFecha = new PdfPCell();
            celdaFecha.setBorder(Rectangle.NO_BORDER);
            celdaFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaFecha.addElement(new Paragraph("Fecha del Bautizo:", etiquetaFont));
            celdaFecha.addElement(new Paragraph(fechaStr, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));

            tablaInfo.addCell(celdaLugar);
            tablaInfo.addCell(celdaFecha);
            document.add(tablaInfo);

            // Espacio
            document.add(new Paragraph("\n", textoFont));

            // TABLA DE PADRES Y PADRINOS
            PdfPTable tablaFamilia = new PdfPTable(2);
            tablaFamilia.setWidthPercentage(90);
            tablaFamilia.setSpacingBefore(10);
            
            // Celda Padres
            PdfPCell celdaPadres = new PdfPCell();
            celdaPadres.setBorder(Rectangle.NO_BORDER);
            celdaPadres.setPadding(10);
            celdaPadres.addElement(new Paragraph("PADRES", etiquetaFont));
            celdaPadres.addElement(new Paragraph(bautizo.getNombrePadre(), textoFont));
            celdaPadres.addElement(new Paragraph(bautizo.getNombreMadre(), textoFont));

            // Celda Padrinos
            PdfPCell celdaPadrinos = new PdfPCell();
            celdaPadrinos.setBorder(Rectangle.NO_BORDER);
            celdaPadrinos.setPadding(10);
            celdaPadrinos.addElement(new Paragraph("PADRINOS", etiquetaFont));
            celdaPadrinos.addElement(new Paragraph(bautizo.getNombrePadrino(), textoFont));
            celdaPadrinos.addElement(new Paragraph(bautizo.getNombreMadrina(), textoFont));

            tablaFamilia.addCell(celdaPadres);
            tablaFamilia.addCell(celdaPadrinos);
            document.add(tablaFamilia);

            // Espacio final antes de firmas
            document.add(new Paragraph("\n\n\n", textoFont));

            // FIRMAS
            PdfPTable tablaFirmas = new PdfPTable(2);
            tablaFirmas.setWidthPercentage(80); // Ocupa el 80% del ancho de la página
            tablaFirmas.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            // Forzamos que las columnas tengan el mismo ancho (50% y 50%)
            tablaFirmas.setWidths(new float[]{1, 1});

            // --- Firma 1: Pastor ---
            PdfPCell firma1 = new PdfPCell();
            firma1.setBorder(Rectangle.NO_BORDER);
            firma1.setHorizontalAlignment(Element.ALIGN_CENTER); // Centra el contenido en la celda
            firma1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            // Usamos Paragraph para la línea y el texto, asegurando alineación
            Paragraph lineaFirma1 = new Paragraph("__________________________", textoFont);
            lineaFirma1.setAlignment(Element.ALIGN_CENTER);
            firma1.addElement(lineaFirma1);
            
            Paragraph textoFirma1 = new Paragraph("Firma del Pastor", firmaFont);
            textoFirma1.setAlignment(Element.ALIGN_CENTER);
            firma1.addElement(textoFirma1);

            // --- Firma 2: Secretario/a ---
            PdfPCell firma2 = new PdfPCell();
            firma2.setBorder(Rectangle.NO_BORDER);
            firma2.setHorizontalAlignment(Element.ALIGN_CENTER);
            firma2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            Paragraph lineaFirma2 = new Paragraph("__________________________", textoFont);
            lineaFirma2.setAlignment(Element.ALIGN_CENTER);
            firma2.addElement(lineaFirma2);
            
            Paragraph textoFirma2 = new Paragraph("Secretario(a)", firmaFont);
            textoFirma2.setAlignment(Element.ALIGN_CENTER);
            firma2.addElement(textoFirma2);

            tablaFirmas.addCell(firma1);
            tablaFirmas.addCell(firma2);
            document.add(tablaFirmas);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
    }

    // Método para el borde (se mantiene igual, es un buen estilo)
    private void dibujarBordesDecorativo(PdfContentByte canvas, Document document) {
        float width = document.getPageSize().getWidth();
        float height = document.getPageSize().getHeight();
        float margin = 20;

        canvas.setColorStroke(new BaseColor(40, 40, 90));
        canvas.setLineWidth(3);
        canvas.rectangle(margin, margin, width - 2*margin, height - 2*margin);
        canvas.stroke();
        
        // Borde interno fino
        canvas.setLineWidth(1);
        canvas.rectangle(margin + 5, margin + 5, width - 2*margin - 10, height - 2*margin - 10);
        canvas.stroke();
    }
}