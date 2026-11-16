package com.gestion.service;

import com.gestion.dto.Tabla;
import com.gestion.exception.GestionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

// Exporta cualquier Tabla a CSV o XML usando solo clases de Java
public class ExportacionService {

    private static final Logger log = LoggerFactory.getLogger(ExportacionService.class);
    private static final Path CARPETA = Path.of("exportaciones");
    private static final DateTimeFormatter MARCA_TIEMPO = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public Path exportarCSV(Tabla tabla) {
        Path fichero = rutaFichero(tabla.titulo(), "csv");
        // \uFEFF (BOM) hace que Excel reconozca el UTF-8 y muestre bien las tildes
        StringBuilder contenido = new StringBuilder("\uFEFF");
        contenido.append(lineaCSV(tabla.columnas()));
        tabla.filas().forEach(fila -> contenido.append(lineaCSV(fila)));

        try {
            Files.writeString(fichero, contenido.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new GestionException("No se pudo crear el fichero CSV.", e);
        }
        log.info("Exportado CSV: {}", fichero);
        return fichero;
    }

    public Path exportarXML(Tabla tabla) {
        Path fichero = rutaFichero(tabla.titulo(), "xml");
        try {
            Document documento = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element raiz = documento.createElement("informe");
            raiz.setAttribute("titulo", tabla.titulo());
            raiz.setAttribute("generado", LocalDateTime.now().withNano(0).toString());
            documento.appendChild(raiz);

            List<String> etiquetas = tabla.columnas().stream().map(this::nombreValido).toList();
            for (List<String> fila : tabla.filas()) {
                Element registro = documento.createElement("registro");
                for (int i = 0; i < etiquetas.size(); i++) {
                    Element campo = documento.createElement(etiquetas.get(i));
                    campo.setTextContent(fila.get(i));
                    registro.appendChild(campo);
                }
                raiz.appendChild(registro);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(documento), new StreamResult(fichero.toFile()));
        } catch (ParserConfigurationException | TransformerException e) {
            throw new GestionException("No se pudo crear el fichero XML.", e);
        }
        log.info("Exportado XML: {}", fichero);
        return fichero;
    }

    private String lineaCSV(List<String> valores) {
        return valores.stream().map(this::escaparCSV).collect(Collectors.joining(";")) + System.lineSeparator();
    }

    private String escaparCSV(String valor) {
        if (valor.contains(";") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }

    private String nombreValido(String texto) {
        String limpio = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_|_$", "");
        return limpio.isEmpty() || Character.isDigit(limpio.charAt(0)) ? "campo_" + limpio : limpio;
    }

    private Path rutaFichero(String titulo, String extension) {
        try {
            Files.createDirectories(CARPETA);
        } catch (IOException e) {
            throw new GestionException("No se pudo crear la carpeta de exportaciones.", e);
        }
        return CARPETA.resolve(nombreValido(titulo) + "_" + LocalDateTime.now().format(MARCA_TIEMPO) + "." + extension);
    }
}