package uris.bizibox;

import common.CommonSystemProperties;
import org.testng.log4testng.Logger;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;


public enum BiziboxURIs {

    HOME_PAGE(BiziboxPaths.HOME_PAGE_PATH);

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
        URI biziboxURI = URI.create(CommonSystemProperties.BIZIBOX_SERVER_URL);
        return UriBuilder.fromUri(biziboxURI)
                .path(pathURI.getPath())
                .replaceQuery(pathURI.getRawQuery())
                .build();
    }


    public URI getFullURI(String biziboxServer) {
        URI pathURI = URI.create(path);
        URI biziboxURI = URI.create(biziboxServer);
        return UriBuilder.fromUri(biziboxURI)
                .path(pathURI.getPath())
                .replaceQuery(pathURI.getRawQuery())
                .build();
    }

    public URI getFullURIWithSuffix(String suffix) {
        URI pathURI = URI.create(path);
        URI biziboxURI = URI.create(CommonSystemProperties.BIZIBOX_SERVER_URL);
        return UriBuilder.fromUri(biziboxURI)
                .path(pathURI.getPath())
                .path(suffix)
                .replaceQuery(pathURI.getRawQuery())
                .build();
    }
}
