package com.the_ultimate_toolbox.util.generadores;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.the_ultimate_toolbox.models.core.Articulo;
import org.apache.commons.net.whois.WhoisClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class GeneradoresWeb {

    private static final Logger logger = LogManager.getLogger(GeneradoresWeb.class);

    public static String obtenerTituloWebsite(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.title();
    }

    /**
     * Devuelve la direcci n IP de la m quina en la que se est  ejecutando este c digo.
     *
     * @return La direcci n IP de la m quina en la que se est  ejecutando este c digo.
     * @throws UnknownHostException si no se pudo obtener la direcci n IP.
     */
    public static String getMyIPAddress() throws UnknownHostException {
        InetAddress ipAddress = InetAddress.getLocalHost();
        return ipAddress.getHostAddress();
    }

    public static String generarSitemapXML(String urlBase) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        org.w3c.dom.Document doc = docBuilder.newDocument();
        org.w3c.dom.Element urlset = doc.createElement("urlset");
        urlset.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
        doc.appendChild(urlset);

        org.w3c.dom.Element url = doc.createElement("url");
        org.w3c.dom.Element loc = doc.createElement("loc");
        loc.appendChild(doc.createTextNode(urlBase));
        url.appendChild(loc);
        urlset.appendChild(url);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.getBuffer().toString();
    }

    public static String generarRobotsTxt(List<String> reglas) {
        StringBuilder robotsTxt = new StringBuilder();
        robotsTxt.append("User-agent: *");
        for (String regla : reglas) {
            robotsTxt.append(regla).append("\n");
        }
        return robotsTxt.toString();
    }

    public static List<String> obtenerMetatagsSEO(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements metaTags = doc.select("meta");

        List<String> seoTags = new ArrayList<>();
        for (Element tag : metaTags) {
            String name = tag.attr("name");
            String content = tag.attr("content");
            if (name.toLowerCase().contains("description") || name.toLowerCase().contains("keywords")) {
                seoTags.add(name + ": " + content);
            }
        }
        return seoTags;
    }

    public static String generarHTMLBasico(String titulo, String contenido) {
        return """
                <!DOCTYPE html>
                <html lang=\"es\">
                <head>
                    <meta charset=\"UTF-8\">
                    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
                    <title>%s</title>
                </head>
                <body>
                    %s
                </body>
                </html>""".formatted(titulo, contenido);
    }

    public static String obtenerFaviconURL(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("link[rel~=icon]");

        if (!links.isEmpty()) {
            Element firstLink = links.first();
            if (firstLink != null) {
                String faviconUrl = firstLink.attr("href");
                if (faviconUrl != null) {
                    if (faviconUrl.startsWith("http")) {
                        return faviconUrl;
                    } else {
                        try {
                            URL baseUrl = new URI(url).toURL();
                            return new URI(baseUrl.toString()).resolve(faviconUrl).toURL().toString();
                        } catch (URISyntaxException e) {
                            logger.error("Error al construir la URL del favicon: {}", e.getMessage());
                        }
                    }
                }
            }
        }
        return url + "/favicon.ico"; // URL por defecto si no se encuentra
    }

    public static String generarFeedRSS(List<Articulo> articulos) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        org.w3c.dom.Document doc = docBuilder.newDocument();
        org.w3c.dom.Element rss = doc.createElement("rss");
        rss.setAttribute("version", "2.0");
        doc.appendChild(rss);

        org.w3c.dom.Element channel = doc.createElement("channel");
        rss.appendChild(channel);

        org.w3c.dom.Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode("Mi Feed RSS"));
        channel.appendChild(title);

        for (Articulo articulo : articulos) {
            org.w3c.dom.Element item = doc.createElement("item");

            org.w3c.dom.Element itemTitle = doc.createElement("title");
            itemTitle.appendChild(doc.createTextNode(articulo.getTitulo()));
            item.appendChild(itemTitle);

            org.w3c.dom.Element link = doc.createElement("link");
            link.appendChild(doc.createTextNode(articulo.getEnlace()));
            item.appendChild(link);

            org.w3c.dom.Element description = doc.createElement("description");
            description.appendChild(doc.createTextNode(articulo.getDescripcion()));
            item.appendChild(description);

            channel.appendChild(item);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.getBuffer().toString();
    }

    public static String obtenerDatosWhois(String dominio) throws IOException {
        WhoisClient whois = new WhoisClient();
        try {
            whois.connect(WhoisClient.DEFAULT_HOST);
            String result = whois.query(dominio);
            whois.disconnect();
            return result;
        } finally {
            if (whois.isConnected()) {
                whois.disconnect();
            }
        }
    }

    public static String generarHTACCESS(List<String> redirecciones) {
        StringBuilder htaccess = new StringBuilder();
        htaccess.append("RewriteEngine On\n");
        for (String redireccion : redirecciones) {
            htaccess.append("RewriteRule ").append(redireccion).append(" [R=301,L]\n");
        }
        return htaccess.toString();
    }

    /**
     * Genera un código QR a partir de una cadena de texto.
     *
     * @param contenido El contenido a codificar en el QR
     * @param ancho     El ancho de la imagen QR
     * @param alto      El alto de la imagen QR
     * @return Un array de bytes representando la imagen QR en formato PNG
     * @throws WriterException Si hay un error al generar el QR
     * @throws IOException     Si hay un error de E/S
     */
    public static byte[] generarQRCode(String contenido, int ancho, int alto) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(contenido, BarcodeFormat.QR_CODE, ancho, alto);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    public static void generarQRCode(String contenido, String rutaArchivo, int ancho, int alto) {
        try {
            // Generar el código QR
            byte[] qrCodeBytes = generarQRCode(contenido, ancho, alto);

            // Guardar el código QR como un archivo de imagen
            try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
                fos.write(qrCodeBytes);
            }

            logger.info("Código QR generado y guardado como: {}", rutaArchivo);

            // Intentar abrir la imagen (esto funciona en sistemas de escritorio)
            try {
                java.awt.Desktop.getDesktop().open(Paths.get(rutaArchivo).toFile());
            } catch (IOException e) {
                logger.warn("No se pudo abrir la imagen automáticamente. Por favor, busca el archivo {} en tu sistema de archivos.", rutaArchivo);
            }

        } catch (WriterException | IOException e) {
            logger.error("Error al generar el código QR: {}", e.getMessage());
        }
    }
}