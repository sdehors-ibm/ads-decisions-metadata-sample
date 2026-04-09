package com.ibm.ads.metadata.repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.ads.metadata.model.DecisionMetadata;

import ilog.rules.bom.annotations.NotBusiness;

/**
 * Singleton repository for accessing decision metadata from JSON files.
 * Loads metadata from all JSON files in the /metadata folder in the classpath.
 * Uses Bill Pugh Singleton Design (static inner class holder) for thread-safe
 * lazy initialization.
 */
@NotBusiness
public class DecisionRepository {

    private static final Logger logger = Logger.getLogger(DecisionRepository.class.getName());

    private static final String METADATA_FOLDER = "metadata";
    private final ObjectMapper objectMapper;
    private List<DecisionMetadata> decisions;

    /**
     * Private constructor to prevent instantiation from outside.
     * Initializes by loading from all JSON files in /metadata folder.
     */
    private DecisionRepository() {
        this.objectMapper = new ObjectMapper();
        loadDecisionsFromMetadataFolder();
    }

    private static class DecisionRepositoryHolder {
        static final DecisionRepository INSTANCE = new DecisionRepository();
    }

    /**
     * Get the singleton instance of DecisionRepository.
     *
     * @return the singleton instance
     */
    public static DecisionRepository getInstance() {
        return DecisionRepositoryHolder.INSTANCE;
    }

    /**
     * Load decisions from all JSON files in the /metadata folder in the classpath
     */
    private void loadDecisionsFromMetadataFolder() {
        decisions = new ArrayList<>();
        
        try {
            URL resource = DecisionRepository.class.getClassLoader().getResource(METADATA_FOLDER);
            
            if (resource == null) {
                logger.warning("Metadata folder not found in classpath: " + METADATA_FOLDER);
                decisions = Collections.emptyList();
                return;
            }

            URI uri = resource.toURI();
            Path metadataPath;
            FileSystem fileSystem = null;
            
            // Handle both file system and JAR resources
            if (uri.getScheme().equals("jar")) {
                fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                metadataPath = fileSystem.getPath("/" + METADATA_FOLDER);
            } else {
                metadataPath = Paths.get(uri);
            }

            // Find all JSON files in the metadata folder
            try (Stream<Path> paths = Files.walk(metadataPath, 1)) {
                paths.filter(Files::isRegularFile)
                     .filter(path -> path.toString().endsWith(".json"))
                     .forEach(path -> {
                         String fileName = METADATA_FOLDER + "/" + path.getFileName().toString();
                         logger.info("Loading decisions from: " + fileName);
                         loadDecisionsFromFile(fileName);
                     });
            }
            
            if (fileSystem != null) {
                fileSystem.close();
            }
            
            logger.info("Total decisions loaded: " + decisions.size());
            
        } catch (IOException | URISyntaxException e) {
            logger.severe("Failed to load decisions from metadata folder: " + e.getMessage());
            decisions = Collections.emptyList();
        }
    }

    /**
     * Load decisions from a specific JSON file and add to the decisions list
     */
    private void loadDecisionsFromFile(String filePath) {
        try (InputStream inputStream = DecisionRepository.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                logger.warning("File not found: " + filePath);
                return;
            }
            
            List<DecisionMetadata> fileDecisions = objectMapper.readValue(inputStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, DecisionMetadata.class));
            
            if (fileDecisions != null && !fileDecisions.isEmpty()) {
                decisions.addAll(fileDecisions);
                logger.info("Loaded " + fileDecisions.size() + " decision(s) from " + filePath);
            }
            
        } catch (IOException e) {
            logger.warning("Failed to load decisions from file: " + filePath + " - " + e.getMessage());
        }
    }

    /**
     * Get all decisions
     */
    public List<DecisionMetadata> getAllDecisions() {
        return new ArrayList<>(decisions);
    }

    /**
     * Get decision by ID
     */
    public DecisionMetadata getDecisionById(String id) {
        return decisions.stream()
                .filter(decision -> decision.getId().equals(id))
                .findFirst().orElse(null);
    }

    /**
     * Get decisions by name (partial match)
     */
    public List<DecisionMetadata> searchDecisionsByName(String namePattern) {
        List<DecisionMetadata> result = new ArrayList<>();
        String lowerPattern = namePattern.toLowerCase();
        for (DecisionMetadata decision : decisions) {
            if (decision.getName().toLowerCase().contains(lowerPattern)) {
                result.add(decision);
            }
        }
        return result;
    }

    /**
     * Get decision count
     */
    public int getDecisionCount() {
        return decisions.size();
    }
}
