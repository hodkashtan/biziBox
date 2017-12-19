package uris.bizibox;

import common.CommonSystemProperties;
import org.apache.http.client.utils.URIBuilder;
import org.testng.log4testng.Logger;

import java.net.URI;
import java.net.URISyntaxException;


public enum BiziboxURIs {

    LOGIN_PAGE(BiziboxPaths.LOGIN_PATH);

    private final String path;
    private static final Logger LOGGER = Logger.getLogger(BiziboxURIs.class);

    private BiziboxURIs(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public URI getFullURI() {
        URI pathURI = URI.create(path);
        URIBuilder uriBuilder = new URIBuilder();
        URI uri = null;
        try {
            uri = uriBuilder.setHost(CommonSystemProperties.BIZIBOX_SERVER_URL)
                    .setPath(path)
                    .setCustomQuery(pathURI.getRawQuery())
                    .build();
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to create full uri using [" + CommonSystemProperties.BIZIBOX_SERVER_URL + "]", e);
        }
        return uri;
    }


    public URI getFullURI(String biziboxServer) {
        URI pathURI = URI.create(path);
        URIBuilder uriBuilder = new URIBuilder();
        URI uri = null;
        try {
            uri = uriBuilder.setHost(biziboxServer)
                    .setPath(path)
                    .setCustomQuery(pathURI.getRawQuery())
                    .build();
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to create full uri using [" + biziboxServer + "]", e);
        }

        return uri;
    }

    public URI getFullURIWithSuffix(String suffix) {
        URI pathURI = URI.create(path);
        URIBuilder uriBuilder = new URIBuilder();
        URI uri = null;
        try {
            uri = uriBuilder.setHost(CommonSystemProperties.BIZIBOX_SERVER_URL)
                    .setPath(path)
                    .setPath(suffix)
                    .setCustomQuery(pathURI.getRawQuery())
                    .build();
        } catch (URISyntaxException e) {
            LOGGER.error("Unable to create full uri using suffix [" + suffix + "]", e);
        }

        return uri;
    }
}
