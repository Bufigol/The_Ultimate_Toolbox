package com.bufigol.configurationFile;

import com.bufigol.errorHandler.ConfigException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class is responsible for handling the configuration file of the application.
 * It provides methods to read, write, and modify the properties in the configuration file.
 */
public class ConfigFile {

    /**
     * The Properties object that holds the configuration properties.
     */
    private final Properties properties;

    /**
     * The file path of the configuration file.
     */
    private final String configFilePath;

    /**
     * Default constructor that uses the default configuration file path.
     */
    public ConfigFile() {
        this.properties = new Properties(); // Initialize the Properties object
        this.configFilePath = "config.properties"; // Set the default file path
    }

    /**
     * Constructor with a custom configuration file path.
     *
     * @param configFilePath The file path of the configuration file.
     */
    public ConfigFile(String configFilePath) {
        this.properties = new Properties(); // Initialize the Properties object
        this.configFilePath = configFilePath; // Set the custom file path
        try {
            readConfiguration(); // Read the configuration file
        } catch (ConfigException e) {
            throw new RuntimeException(e); // Throw a runtime exception if there is an error reading the configuration file
        }
    }

    /**
     * Writes the current properties to the configuration file.
     *
     * @throws ConfigException If there is an error writing the file.
     */
    public void writeConfiguration() throws ConfigException {
        try (FileOutputStream output = new FileOutputStream(configFilePath)) {
            properties.store(output, null); // Store the properties in the file output stream
        } catch (IOException e) {
            throw new ConfigException("Error writing the configuration file.", e); // Throw a ConfigException if there is an error writing the file
        }
    }

    /**
     * Gets the value of a specific property.
     *
     * @param key The key of the property.
     * @return The value of the property.
     */
    public String getValue(String key) {
        return properties.getProperty(key); // Get the property value using its key
    }

    /**
     * Sets the value of a property.
     *
     * @param key   The key of the property.
     * @param value The new value of the property.
     */
    public void setValue(String key, String value) {
        properties.setProperty(key, value); // Set the property value using its key
    }

    /**
     * Creates a new category without a value in the configuration file.
     *
     * @param categoryName The name of the new category.
     */
    public void createNewCategory(String categoryName) {
        properties.setProperty(categoryName, ""); // Create a new category with an empty value
    }

    /**
     * Creates a new category with a specific value in the configuration file.
     *
     * @param categoryName The name of the new category.
     * @param value       The value of the new category.
     */
    public void createNewCategoryValue(String categoryName, String value) {
        createNewCategory(categoryName); // Create a new category
        setValue(categoryName, value); // Set the value of the new category
    }

    /**
     * Reads the properties from the configuration file.
     *
     * @throws ConfigException If the file is not found or if there is an error reading it.
     */
    private void readConfiguration() throws ConfigException {
        try (FileInputStream input = new FileInputStream(configFilePath)) {
            properties.load(input); // Load the properties from the file input stream
        } catch (FileNotFoundException e) {
            throw new ConfigException("Configuration file not found: " + configFilePath, e); // Throw a ConfigException if the file is not found
        } catch (IOException e) {
            throw new ConfigException("Error reading the configuration file.", e); // Throw a ConfigException if there is an error reading the file
        }
    }
}
