package io.sparkwork.security

import groovy.time.TimeCategory

import io.sparkwork.common.Company
import org.apache.commons.lang3.StringUtils
import org.bson.types.ObjectId

class AuthenticationToken {

    ObjectId id
    String tokenValue
    String username
    Date lastLogin = new Date()
    Date lastUsed = new Date()
    Company cId
    String uuId
    String fcmId
    String deviceType
    String loginType
    ObjectId userId

    def afterLoad() {
        AuthenticationToken.withNewSession {
            if (this.cId && this.cId.config.sessionTimeOut && StringUtils.isNumeric(this.cId.config.sessionTimeOut.toString())) {
                int minutes = TimeCategory.minus(new Date(), this.lastUsed).minutes
                int sessionTimeOut = Integer.parseInt(this.cId.config.sessionTimeOut.toString())
                if (minutes > sessionTimeOut) {
                    this.delete(flush: true, failOnError: true)
                } else {
                    this.lastUsed = new Date()
                    this.save(flush:true, failOnError: true)
                }
            } else {
                this.lastUsed = new Date()
                this.save(flush:true, failOnError: true)
            }
            true
        }
    }

    static constraints = {
        uuId nullable: true
        fcmId nullable: true
        userId nullable: true
        loginType nullable: true
        deviceType nullable: true
    }
    static mapping = {
        version false
    }

}
