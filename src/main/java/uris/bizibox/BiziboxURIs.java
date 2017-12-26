package uris.bizibox;

import common.CommonSystemProperties;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;


public enum BiziboxURIs {

    HOME_PAGE(BiziboxPaths.HOME_PAGE_PATH),
    SETTINGS_PAGE(BiziboxPaths.SETTINGS_PAGE_PATH),
    ACCOUNTANT_PAGE(BiziboxPaths.ACCOUNTANT_PAGE_PATH),
    MAIN_PAGE(BiziboxPaths.MAIN_PAGE_PATH),
    COMPANYS_PAGE(BiziboxPaths.COMPANYS_PAGE_PATH),
    EXPORT_HASHV_PAGE(BiziboxPaths.EXPORT_HASHV_PAGE_PATH),
    CARDS_PAGE(BiziboxPaths.CARDS_PAGE_PATH),
    REPORTS_MAIN_PAGE(BiziboxPaths.REPORTS_MAIN_PAGE_PATH),
    MANAGEMENT_TEAM_PAGE(BiziboxPaths.MANAGEMENT_TEAM_PAGE_PATH);

    private final String path;

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
