package io.sparkwork.security

import org.bson.types.ObjectId

class AuthorizationCode {

    ObjectId id
    byte[] authentication
    String code

    static constraints = {
        code nullable: false, blank: false, unique: true
        authentication nullable: false, minSize: 1, maxSize: 1024 * 4
    }

    static mapping = {
        version false
    }
}
