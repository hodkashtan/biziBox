package uris.bizibox;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.testng.log4testng.Logger;

import java.net.URI;
import java.net.URISyntaxException;

public class URIUtil {
    private static final Logger LOGGER = Logger.getLogger(URIUtil.class);

    public static URI createURI(String base, String... paths) {
        URI uri = null;
        try {
            URIBuilder builder = new URIBuilder(base);
            for (String path: paths) {
                builder.setPath(path);
            }
            uri = builder.build();
        } catch (URISyntaxException e) {
            LOGGER.error("Could not create uri from paths", e);
        }

        return uri;
    }

    public static String removeTrailingSlash(String url) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }
        url = url.trim();
        while (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }
}
