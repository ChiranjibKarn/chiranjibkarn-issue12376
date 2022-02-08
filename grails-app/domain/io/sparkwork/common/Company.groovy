package io.sparkwork.common

import org.bson.Document
import org.bson.types.ObjectId

class Company {

    ObjectId id
    String name //Full name of Company.
    String shortName //Simplified short name, used in URL, Super Admin will do that
    String domain //Domain name of user for google market place e.g. sparkwork.io
    String hashId //Hash identifier used in places like S3.
    String status //Use CompanyStatus enum
    Map config = [:]
    Document mobileConfig //navBar[], showLogout, sessionTimeOut
    Document configFeatures //course,series,checklist,chat,secfiles,reporting,workflows,events
    List<Document> whiteListIPs //document will have name, ip, setOn and setBy
    Date accExpire
    Date setOn
    Date lastUpd
    Document contract = new Document() //maxUsers INT, maxGB INT, slaType INT, slaUsers INT, billPeriod String, pilotStart Date, liveDate Date, marketing boolean
    Document userFields = new Document()
    String freshDeskId
    List<Document> userCustomFields = []
    static transients = ['populateUserFields']
    static constraints = {
        name nullable: false, blank: false
        hashId nullable: false, blank: false
        domain nullable: true
        status nullable: false
        accExpire nullable: true
        setOn nullable: true
        lastUpd nullable: true
        mobileConfig nullable: true
        freshDeskId nullable: true

    }

    def beforeInsert() {
        //List of fields whose values are allowed to change by learner
        Document userFields = new Document()
        userFields.picture = true //Profile picture
        userFields.first = true
        userFields.surname = true
        userFields.title = true
        userFields.telephone = true
        userFields.email = true
        userFields.externalId = true
        userFields.lang = true
        userFields.bio = true
        userFields.skills = true
        userFields.interests = true
        userFields.customFields = true //All or none
        this.userFields = userFields
    }

}
