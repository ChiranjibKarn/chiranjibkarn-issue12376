package io.sparkwork.json

import grails.converters.JSON
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.rendering.AccessTokenJsonRenderer
import grails.gorm.transactions.Transactional
import io.sparkwork.common.Company
import io.sparkwork.security.AuthenticationToken
import io.sparkwork.security.User
import io.sparkwork.security.UserRole
import org.bson.Document
import org.bson.types.ObjectId
import org.grails.web.util.WebUtils

import javax.servlet.http.HttpServletRequest

@Transactional
class JsonRenderer implements AccessTokenJsonRenderer {

    String generateJson(AccessToken accessToken, String loginType = '') {
        long start = System.currentTimeMillis()
        User user = User.findById(accessToken.principal.id)
        if (!user) {
            return [error: 'Authentication Failed'] as JSON
        }
        HttpServletRequest request = WebUtils.retrieveGrailsWebRequest().request
        def mobileInfo = request?.getHeader('Mobile') ? JSON.parse(request?.getHeader('Mobile')) :  null
        String role = UserRole?.findByUser(user)?.role?.authority
        Company company = user.cId
        AuthenticationToken authenticationToken = new AuthenticationToken()
        authenticationToken.tokenValue = accessToken?.accessToken
        authenticationToken.username = user.email ?: user.externalId
        authenticationToken.cId = user.cId
        authenticationToken.lastUsed = new Date()
        authenticationToken.userId = user?.id
        if (loginType) {
            authenticationToken.loginType = loginType
        } else {
            loginType = 'Credentials'
        }
        if (mobileInfo) {
            authenticationToken.uuId = mobileInfo?.uuid?.toString()
            authenticationToken.fcmId = mobileInfo?.senderId?.toString()
            authenticationToken.deviceType = mobileInfo?.osType?.toString()
        }
        authenticationToken.save(flush: true, failOnError: true)

        try {
            user.lastLoginTime = new Date()
            if (!(user?.lastUserAgent) || (user.lastUserAgent != request?.getHeader('User-Agent'))) {
                user.lastUserAgent = request?.getHeader('User-Agent')
            }
            if (mobileInfo) {
                List<Document> newMobileDocs = []
                Document mobileInfoMap = new Document()
                mobileInfoMap.id = new ObjectId()
                mobileInfoMap.imei = mobileInfo?.imei
                mobileInfoMap.osType = mobileInfo?.osType
                mobileInfoMap.osVer = mobileInfo?.osVer
                mobileInfoMap.uuid = mobileInfo?.uuid
                mobileInfoMap.brand = mobileInfo?.brand
                mobileInfoMap.model = mobileInfo?.model
                mobileInfoMap.fcmId = mobileInfo?.senderId
                mobileInfoMap.setOn = new Date()
                if (!user.mobile) {
                    newMobileDocs.add(mobileInfoMap)
                    user.mobile = newMobileDocs
                } else if (!(user?.mobile*.uuid.contains(mobileInfo?.uuid))) {
                    user.mobile.add(mobileInfoMap)
                } else if (!(user?.mobile*.fcmId.contains(mobileInfo?.senderId))) {
                    user?.mobile?.each {
                        if (mobileInfo?.uuid == it?.uuid) {
                            newMobileDocs.add(mobileInfoMap)
                        } else {
                            newMobileDocs.add(it)
                        }
                    }
                    user.mobile = newMobileDocs
                }
            }
            user.save(flush: true, failOnError: true)
        } catch (Exception e) {

        }
        Document deviceInfoMap = new Document()
        if (mobileInfo) {
            deviceInfoMap.appType = mobileInfo?.appType
            deviceInfoMap.os = mobileInfo?.osType
            deviceInfoMap.osVer = mobileInfo?.osVer
            deviceInfoMap.deviceBrand = mobileInfo?.brand
            deviceInfoMap.deviceModel = mobileInfo?.model
            deviceInfoMap.appVersion = mobileInfo?.version
        } else {
            def feversion = request?.getHeader('SparkworkAppVersion') ? request?.getHeader('SparkworkAppVersion') :  null
            deviceInfoMap.appType = 'WebApp'
            deviceInfoMap.appVersion = feversion
        }
        String userAgent = request?.getHeader('User-Agent')
        String auditMsg = 'Logged in using ' + loginType.toUpperCase() + '.'
        def res = [status_code: '200', userId: user.id, message: auditMsg]
        def userInfo = [user_id: user.id, cId: user?.cId?.id, reqUserName: user?.fullName, action: loginType, deviceInfoMap: deviceInfoMap, userAgent: userAgent, userFullName: user?.fullName]
        return [access_token: accessToken?.accessToken, current_role: [role]] as JSON
    }
}
