package io.sparkwork.security

import static org.apache.commons.lang3.StringUtils.isBlank
import static org.joda.time.DateTime.now

import org.joda.time.DateTime

class SecurityToken {
    String path
    long issuedAt

    SecurityToken(String filePath) {
        path = filePath
        issuedAt = now().millis
    }

    def isValid(int maxTokenValidityInMinutes, String otherPath) {
        if (isBlank(path) || isBlank(otherPath)) {
            return false
        }
        DateTime grantDate = new DateTime(issuedAt)
        DateTime grantWindowStart = now().minusMinutes(maxTokenValidityInMinutes)
        return grantDate.isAfter(grantWindowStart) && path == otherPath
    }
}
