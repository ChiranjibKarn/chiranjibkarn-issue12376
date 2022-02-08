package io.sparkwork.security

import grails.plugin.springsecurity.SpringSecurityService
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import io.sparkwork.common.Company
import org.bson.Document
import org.bson.types.ObjectId

@EqualsAndHashCode(includes = ['email', 'externalId'])
@ToString(includes = ['email', 'externalId'], includeNames = true, includePackage = false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    SpringSecurityService springSecurityService

    ObjectId id
    Company cId //Company Id
    String externalId
    String email // email id of user /can be user id for login
    String emailStatus //Email sent status FAILED/SUCCESS etc. Do not attempt to send email if it is FAILED.
    String first //First name
    String surName = '' //surname
    String profilePic //Link to S3 (Original image uploaded by user)
    String profilePic40 //Thumbnail of profilePic 40x40
    String profilePic80 //Thumbnail of profilePic 80x80
    String jobTitle
    String tel //Telephone
    String tel_country //Country code
    String tel_status //Unverified, Verified, Faulty (to check/update valid phone no. while sending SMS)
    String summary
    List<String> expertise = []
    List<String> interests = []
    Integer pointsTotal = 0
    List<Document> stickers //stickerId, setOn, courseId
    List<Document> badges //badgeId, setOn, seriesId
    List<Document> mobile //id, imei, osType,osVer,uuid,brand,model,setOn
    List<Document> certificates //certId, setOn, courseId
    List<Document> diplomas //diplomaId, setOn, seriesId
    List<Document> following //Items (Course, post etc) being followed
    List followUser = [] //List of users to follow
    List<Document> assigned //content that is assigned
    List<Document> signedUp //content that are attempted by user at his will
    List<Document> channels = [] //Subscribed channels and respective role/access
    String lang = 'en-US'//Prefferred language
    String tz //Timezone
    String password
    Date passChanged
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    boolean forcePassword
    boolean forceProfile
    List passHistory = []
    String lastUserAgent //Fetch user agent on login
    String role //role propery
    boolean enabled = true
    Date setOn = new Date() //Created date
    User setBy
    String setOrigin
    Date lastUpd //Last update date
    Date delOn //Deleted date
    List<ObjectId> orgUnits
    List<Document>reminders = []
    List<Document> supervisor //contains all the users, unit, course, seriesg list with id, setOn, type and Include sub-units that are monitored by this user
    List<Document> mentoredBy // this document contains information of all mentor who are mentoring this user(id, type, typeId)
    List<ObjectId> secFiles = []
    boolean sso = false
    String ssoType = '' //Provider name of SSO, will help logging-out user from the provider. Possible values may be azure or gsuite or any other SSO provider
    String azureId  //Azure user Id
    String hashId //For S3
    Document customFields
    Document stats
    Document statsLatest = new Document() //Stats about the latest content (only one of Course or Series) that this user has touched recently
    Date hardDelOn
    Date lastLoginTime
    Boolean sysAccessLXP
    Boolean sysAccessChat
    static transients = ['springSecurityService', 'fullName', 'currentRole']
    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this)*.role
    }

    def beforeInsert() {
        encodePassword()
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }



    String getFullName() {
        return this.first + ' ' + this.surName
    }

    String getCurrentRole() {
        return UserRole.findByUser(this).role.authority
    }

    static constraints = {
        email nullable: true
        emailStatus nullable: true
        password blank: false
        externalId nullable: true
        surName nullable: true
        profilePic nullable: true
        profilePic40 nullable: true
        profilePic80 nullable: true
        jobTitle nullable: true
        tel nullable: true
        lang nullable: true
        tz nullable: true
        passChanged nullable: true
        lastUserAgent nullable: true
        lastUpd nullable: true
        delOn nullable: true
        setBy nullable: true
        summary nullable: true
        role nullable: true
        tel_country nullable: true
        tel_status nullable: true
        ssoType nullable: true
        customFields nullable: true
        stats nullable: true
        hardDelOn nullable: true
        azureId nullable: true
        hashId nullable: true
        setOrigin nullable: true
        lastLoginTime nullable: true
        secFiles nullable: true
        sysAccessChat nullable: true
        sysAccessLXP nullable: true
        statsLatest nullable: true

    }

    static mapping = {
        password column: '`password`'
        summary type: 'text'
    }
}
