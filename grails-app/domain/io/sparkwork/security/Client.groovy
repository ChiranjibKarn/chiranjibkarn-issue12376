package io.sparkwork.security

import org.bson.Document
import org.bson.types.ObjectId

class Client {

    private static final String NO_CLIENT_SECRET = ''

    transient springSecurityService

    ObjectId id
    String clientId
    String clientSecret

    Integer accessTokenValiditySeconds
    Integer refreshTokenValiditySeconds
    Date setOn

    String password //the default password when a new user is added using Vapi and
    //Map<String, Object> additionalInformation
    // Document additionalInformation
    ObjectId cId
    Date lastUsed = new Date()
    List<ObjectId> units = []
    List<Document> acl = [] //[{group: urlmapping group name, read: true/false, write: true/false, delete: true/false}, ...]
    //Bot info
    String first //First name of bot
    String surName //Surname of bot
    String jobTitle //Bot title
    String profilePic //Profile pic
    String profilePic40 //Profile pic thumbnail
    //For log
    Date debugOn //The date when debug started, supposed to get reset to null after 72h from cron job

    static hasMany = [
            authorities: String,
            authorizedGrantTypes: String,
            resourceIds: String,
            scopes: String,
            autoApproveScopes: String,
            redirectUris: String
    ]

    static transients = ['springSecurityService']

    static constraints = {
        clientId blank: false, unique: true
        clientSecret nullable: true
        setOn nullable: true

        accessTokenValiditySeconds nullable: true
        refreshTokenValiditySeconds nullable: true

        authorities nullable: true
        authorizedGrantTypes nullable: true

        resourceIds nullable: true

        scopes nullable: true
        autoApproveScopes nullable: true

        redirectUris nullable: true

        first nullable: true
        surName nullable: true
        jobTitle nullable: true
        profilePic nullable: true
        profilePic40 nullable: true
       // additionalInformation nullable: true
        debugOn nullable: true
        password nullable: true
    }

    def afterLoad() {

    }

    def beforeInsert() {
        encodeClientSecret()
    }

    protected void encodeClientSecret() {
        setOn = new Date()
        clientSecret = clientSecret ?: NO_CLIENT_SECRET
        clientSecret = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(clientSecret) : clientSecret
    }
}
