package com.fashionstore.util;

public final class ImageUtil {

    private ImageUtil() {
    }

    /**
     * Builds a browser URL for product images stored under webapp (e.g. assets/images/products/1.png).
     * External URLs in the database are returned unchanged.
     */
    public static String toUrl(String contextPath, String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return contextPath + "/assets/images/products/1.png";
        }

        String path = imageUrl.trim();

        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }

        if (path.contains("FashionStore1") || path.contains("fhotos")) {
            return contextPath + "/assets/images/products/1.png";
        }

        if (path.startsWith("/")) {
            return contextPath + path;
        }

        return contextPath + "/" + path;
    }
}
