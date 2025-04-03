package com.theultimatetoolbox.generadores;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.net.whois.WhoisClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.theultimatetoolbox.modelage.Articulo;


public class GeneradoresWeb {

    private static final Logger logger = LogManager.getLogger(GeneradoresWeb.class);

    public static String obtenerTituloWebsite(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.title();
    }

    /**
     * Devuelve la direcci n IP de la m quina en la que se est  ejecutando este c digo.
     *
     * @return La direcci n IP de la m quina en la que se est  ejecutando este c digo o "1.1.1.1" si no se pudo obtener.
     */
    public static String getMyIPAddress() {
        try {
            InetAddress ipAddress = InetAddress.getLocalHost();
            return ipAddress.getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Unable to get IP address: " + e.getMessage());
            return "1.1.1.1";
        }
    }

    public static String generarSitemapXML(String urlBase) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        org.w3c.dom.Document doc = docBuilder.newDocument();
        Element urlset = doc.createElement("urlset");
        urlset.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
        doc.appendChild(urlset);

        Element url = doc.createElement("url");
        Element loc = doc.createElement("loc");
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
        robotsTxt.append("User-agent: *\n");
        for (String regla : reglas) {
            robotsTxt.append(regla).append("\n");
        }
        return robotsTxt.toString();
    }

    public static List<String> obtenerMetatagsSEO(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements metaTags = doc.select("meta");

        List<String> seoTags = new ArrayList<>();
        for (org.jsoup.nodes.Element tag : metaTags) {
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
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
            var firstLink = links.first();
            if (firstLink != null) {
                String faviconUrl = firstLink.attr("href");
                if (faviconUrl != null) {
                    if (faviconUrl.startsWith("http")) {
                        return faviconUrl;
                    } else {
                        URL baseUrl = new URL(url);
                        return new URL(baseUrl, faviconUrl).toString();
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
        Element rss = doc.createElement("rss");
        rss.setAttribute("version", "2.0");
        doc.appendChild(rss);

        Element channel = doc.createElement("channel");
        rss.appendChild(channel);

        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode("Mi Feed RSS"));
        channel.appendChild(title);

        for (Articulo articulo : articulos) {
            Element item = doc.createElement("item");

            Element itemTitle = doc.createElement("title");
            itemTitle.appendChild(doc.createTextNode(articulo.getTitulo()));
            item.appendChild(itemTitle);

            Element link = doc.createElement("link");
            link.appendChild(doc.createTextNode(articulo.getEnlace()));
            item.appendChild(link);

            Element description = doc.createElement("description");
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
     * @param ancho El ancho de la imagen QR
     * @param alto El alto de la imagen QR
     * @return Un array de bytes representando la imagen QR en formato PNG
     * @throws WriterException Si hay un error al generar el QR
     * @throws IOException Si hay un error de E/S
     */
    public static byte[] generarQRCode(String contenido, int ancho, int alto) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(contenido, BarcodeFormat.QR_CODE, ancho, alto);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    public static void generarQRCode(String contenido,String rutaArchivo, int ancho, int alto) throws WriterException, IOException {
        try {
            // Generar el código QR
            byte[] qrCodeBytes = generarQRCode(contenido, ancho, alto);

            // Guardar el código QR como un archivo de imagen
            try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
                fos.write(qrCodeBytes);
            }

            System.out.println("Código QR generado y guardado como: " + rutaArchivo);

            // Intentar abrir la imagen (esto funciona en sistemas de escritorio)
            try {
                java.awt.Desktop.getDesktop().open(Paths.get(rutaArchivo).toFile());
            } catch (IOException e) {
                System.out.println("No se pudo abrir la imagen automáticamente. Por favor, busca el archivo " + rutaArchivo + " en tu sistema de archivos.");
            }

        } catch (WriterException | IOException e) {
            logger.error("Error al generar el código QR: {}", e.getMessage());
        }
    }
}
