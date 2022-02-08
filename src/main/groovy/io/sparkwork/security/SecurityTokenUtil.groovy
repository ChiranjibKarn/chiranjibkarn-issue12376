package io.sparkwork.security


class SecurityTokenUtil {

    static String dynamicImageUrl(String url, String renderType = '', boolean allowMp4BypassUrl = true) {
        String dynaUrl = ''

        return dynaUrl
    }

    static String extractRelativePath(String encodedUrl) {
        String decodedUrl = ''

        return decodedUrl
    }

    static String extractObjectKey(String encodedUrl) {
        String decodedUrl = ''

        return decodedUrl
    }

    static String dynamicPortraitUrl(String url) {
        String dynaUrl = ''

        return dynaUrl
    }

    static def dynamicLinkDetailUrls(def linkDetails) {
        if (linkDetails) {
            if (linkDetails.thumbnail_url && !linkDetails.thumbnail_url.toString().startsWith('http')) {
                linkDetails.thumbnail_url = dynamicImageUrl(linkDetails.thumbnail_url.toString())
            } else if (linkDetails.image && !linkDetails.image.toString().startsWith('http')) {
                linkDetails.image = dynamicImageUrl(linkDetails.image.toString())
            }
        }
        return linkDetails
    }

    static String createCFSignedURL(String theUrl, Integer expiryTimeInMin = null) {
        return ''
    }

    static Map<String, String> createCFSignedCookies(String wildcard = null, Integer expiresAfterMin = null) {
        Map<String, String> cookiesForFE = [:]

        return cookiesForFE
    }



}
