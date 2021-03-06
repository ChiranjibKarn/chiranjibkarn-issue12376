package io.sparkwork.security

import org.bson.types.ObjectId

class RefreshToken {

    ObjectId id
    String value
    Date expiration
    byte[] authentication

    static constraints = {
        value nullable: false, blank: false, unique: true
        expiration nullable: true
        authentication nullable: false, minSize: 1, maxSize: 1024 * 4
    }

    static mapping = {
        version false
    }
}
