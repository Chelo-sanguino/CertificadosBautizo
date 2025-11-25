package com.iglesia.certificados.service;

import com.iglesia.certificados.model.Bautizo;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.awt.Color;

@Service
public class PdfService {

    public void generarCertificadoPdf(HttpServletResponse response, Bautizo bautizo) throws IOException {
        // 1. Configurar el documento (Tamaño carta, márgenes)
        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // 2. Fuentes (Tipos de letra)
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Color.DARK_GRAY);
        Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 14, Color.GRAY);
        Font fuenteTexto = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
        Font fuenteNegrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);

        // 3. Contenido del PDF
        
        // Título
        Paragraph titulo = new Paragraph("CERTIFICADO DE BAUTISMO", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        // Subtítulo (Nombre de la Iglesia)
        Paragraph iglesia = new Paragraph("Iglesia San Roque", fuenteSubtitulo);
        iglesia.setAlignment(Element.ALIGN_CENTER);
        iglesia.setSpacingAfter(40);
        document.add(iglesia);

        // Texto del Certificado
        Paragraph cuerpo = new Paragraph();
        cuerpo.setAlignment(Element.ALIGN_JUSTIFIED);
        cuerpo.setLeading(25f); // Espaciado entre líneas

        cuerpo.add(new Chunk("Por medio de la presente se certifica que ", fuenteTexto));
        cuerpo.add(new Chunk(bautizo.getNombreBautizado() + " " + bautizo.getApellidoBautizado(), fuenteNegrita));
        
        cuerpo.add(new Chunk("\n\nNacido(a) el: ", fuenteTexto));
        cuerpo.add(new Chunk(bautizo.getFechaNacimiento() != null ? bautizo.getFechaNacimiento().toString() : "________", fuenteNegrita));
        
        cuerpo.add(new Chunk(" en ", fuenteTexto));
        cuerpo.add(new Chunk(bautizo.getLugarNacimiento() != null ? bautizo.getLugarNacimiento() : "________", fuenteNegrita));

        cuerpo.add(new Chunk("\n\nFue bautizado(a) el día: ", fuenteTexto));
        cuerpo.add(new Chunk(bautizo.getFechaBautizo() != null ? bautizo.getFechaBautizo().toString() : "________", fuenteNegrita));

        cuerpo.add(new Chunk("\n\nPadres: ", fuenteTexto));
        cuerpo.add(new Chunk(bautizo.getNombrePadre() + " y " + bautizo.getNombreMadre(), fuenteNegrita));

        cuerpo.add(new Chunk("\n\nPadrinos: ", fuenteTexto));
        cuerpo.add(new Chunk(bautizo.getNombrePadrino() + " y " + bautizo.getNombreMadrina(), fuenteNegrita));

        cuerpo.add(new Chunk("\n\nMinistro Oficiante: ", fuenteTexto));
        cuerpo.add(new Chunk(bautizo.getMinistroOficiante(), fuenteNegrita));

        document.add(cuerpo);

        // Datos de Registro al final
        Paragraph registro = new Paragraph("\n\n\nDatos de Registro Eclesiástico:", fuenteSubtitulo);
        document.add(registro);
        
        List lista = new List(List.UNORDERED);
        lista.add(new ListItem("Libro: " + (bautizo.getLibro() != null ? bautizo.getLibro() : "-"), fuenteTexto));
        lista.add(new ListItem("Folio: " + (bautizo.getFolio() != null ? bautizo.getFolio() : "-"), fuenteTexto));
        lista.add(new ListItem("Partida: " + (bautizo.getPartida() != null ? bautizo.getPartida() : "-"), fuenteTexto));
        document.add(lista);

        // Firma
        Paragraph firma = new Paragraph("\n\n\n__________________________\nFirma del Pastor / Encargado", fuenteTexto);
        firma.setAlignment(Element.ALIGN_CENTER);
        document.add(firma);

        document.close();
    }
}