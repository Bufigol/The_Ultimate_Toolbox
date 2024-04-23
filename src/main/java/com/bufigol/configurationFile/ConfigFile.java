package com.bufigol.configurationFile;

import com.bufigol.errorHandler.ConfigException;
import com.bufigol.fileManagment.ErrorLogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigFile {

    /**
     * Propiedades del archivo de configuración.
     */
    private final Properties properties;

    /**
     * Ruta del archivo de configuración.
     */
    private final String configFilePath;

    /**
     * Constructor por defecto. Utiliza la ruta del archivo de configuración predeterminada.
     */
    public ConfigFile() {
        this.properties = new Properties();
        this.configFilePath = "config.properties";
    }

    /**
     * Constructor con ruta de archivo personalizada.
     *
     * @param configFilePath Ruta del archivo de configuración.
     */
    public ConfigFile(String configFilePath) {
        this.properties = new Properties();
        this.configFilePath = configFilePath;
        try {
            leerConfiguracion();
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Escribe las propiedades actuales al archivo de configuración.
     *
     * @throws ConfigException Si ocurre un error al escribir el archivo.
     */
    public void escribirConfiguracion() throws ConfigException {
        try (FileOutputStream output = new FileOutputStream(configFilePath)) {
            properties.store(output, null);
            ErrorLogger.logInfo("Configuración guardada.");
        } catch (IOException e) {
            throw new ConfigException("Error al escribir el archivo de configuración.", e);
        }
    }

    /**
     * Obtiene el valor de una propiedad específica.
     *
     * @param clave Clave de la propiedad.
     * @return Valor de la propiedad.
     */
    public String obtenerValor(String clave) {
        return properties.getProperty(clave);
    }

    /**
     * Establece el valor de una propiedad.
     *
     * @param clave Clave de la propiedad.
     * @param valor Nuevo valor de la propiedad.
     */
    public void establecerValor(String clave, String valor) {
        properties.setProperty(clave, valor);
    }

    /**
     * Crea una nueva categoría sin valor en el archivo de configuración.
     *
     * @param nombreCategoria Nombre de la nueva categoría.
     */
    public void crearNuevaCategoria(String nombreCategoria) {
        properties.setProperty(nombreCategoria, "");
    }

    /**
     * Crea una nueva categoría con un valor específico en el archivo de configuración.
     *
     * @param nombreCategoria Nombre de la nueva categoría.
     * @param valor           Valor de la nueva categoría.
     */
    public void crearNuevaCategoriaValor(String nombreCategoria, String valor) {
        crearNuevaCategoria(nombreCategoria);
        establecerValor(nombreCategoria, valor);
    }

    /**
     * Lee las propiedades del archivo de configuración.
     *
     * @throws ConfigException Si el archivo no se encuentra o si ocurre un error al leerlo.
     */
    private void leerConfiguracion() throws ConfigException {
        try (FileInputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
            ErrorLogger.logInfo("Configuración cargada.");
        } catch (FileNotFoundException e) {
            throw new ConfigException("Archivo de configuración no encontrado: " + configFilePath, e);
        } catch (IOException e) {
            throw new ConfigException("Error al leer el archivo de configuración.", e);
        }
    }
}
