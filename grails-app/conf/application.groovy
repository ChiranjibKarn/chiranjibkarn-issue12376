
es.dbName = 'sparkwork'
grails.plugin.console.enabled = true
grails.resources.pattern = '/**'

grails.cache.config = {
    cache {
        name 'userAccess'
        maxElementsInMemory 1000
        eternal false
        timeToIdleSeconds 120
        timeToLiveSeconds 120
        overflowToDisk false
        memoryStoreEvictionPolicy 'LRU'
    }
}

// environment specific settings
environments {
    development {
        grails {
            serverURL = 'http://localhost:9001/mvp/'
            frontendUrl = 'http://localhost:1300/#/'
            mongodb {
                databaseName = 'sparkwork' /////////
                host = 'localhost'
                port = 27017

            }
            mail {
                disabled = false
            }
            my {
                check = 'XXXXYYYY'
            }
        }
    }
    dev_integration {
        grails {
            println("Loading config from ${userHome}/conf")
            config {
                locations = ["~/conf/extConfigDevApp.groovy"] //User home dir
            }
        }
    }

    production {
        grails {
            println("Loading config from ${userHome}/conf")
            config {
                locations = ["~/conf/extConfigProd.groovy"] //User home dir
            }
        }
    }
    staging {
        grails {
            println("Loading config from ${userHome}/conf")
            config {
                locations = ["~/conf/extConfigStaging.groovy"] //User home dir
            }
        }
    }

}

//mail plugin configurations
grails {
    mail {
        host = 'smtp.gmail.com'
        port = 465
        username = 'aaaaaaaa@sparkwork.io'
        password = 'XXXXXXXXXXXXXX'
        props = ['mail.smtp.auth'                  : 'true',
                 'mail.smtp.starttls.enable'       : 'true',
                 'mail.smtp.socketFactory.port'    : '465',
                 'mail.smtp.socketFactory.class'   : 'javax.net.ssl.SSLSocketFactory',
                 'mail.smtp.socketFactory.fallback': 'false',]
    }
}

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'io.sparkwork.security.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'io.sparkwork.security.UserRole'
grails.plugin.springsecurity.authority.className = 'io.sparkwork.security.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        [pattern: '/oauth/authorize',           access: "isFullyAuthenticated() and (request.getMethod().equals('GET') or request.getMethod().equals('POST'))"],
        [pattern: '/oauth/token',               access: "isFullyAuthenticated() and request.getMethod().equals('POST')"],
        [pattern: '/', access: ['permitAll']],
        [pattern: '/stomp/**', access: ['ROLE_SUPER_ADMIN', 'ROLE_HELPDESK', 'ROLE_ACCOUNT_OWNER', 'ROLE_ADMIN', 'ROLE_AUTHOR', 'ROLE_LEARNER']],
        [pattern: '/error', access: ['permitAll']],
        [pattern: '/index', access: ['permitAll']],
        [pattern: '/index.gsp', access: ['permitAll']],
        [pattern: '/assets/**', access: ['permitAll']],
        [pattern: '/**/js/**', access: ['permitAll']],
        [pattern: '/**/css/**', access: ['permitAll']],
        [pattern: '/**/images/**', access: ['permitAll']],
        [pattern: '/**/favicon.ico', access: ['permitAll']],

        [pattern: '/internalErrorHandler/**', access: ['permitAll']],
        [pattern: '/console/**', access: ['ROLE_SUPER_ADMIN']],
        [pattern: '/plugins/console/**', access: ['ROLE_SUPER_ADMIN']],
        [pattern: '/sudo/**', access: ['ROLE_SUPER_ADMIN', 'ROLE_HELPDESK']],
        [pattern: '/scorm/**', access: ['ROLE_SUPER_ADMIN', 'ROLE_HELPDESK', 'ROLE_ACCOUNT_OWNER', 'ROLE_ADMIN', 'ROLE_AUTHOR', 'ROLE_LEARNER']],
]

grails.plugin.springsecurity.filterChain.chainMap = [
        [pattern: '/assets/**', filters: 'none'],
        [pattern: '/**/js/**', filters: 'none'],
        [pattern: '/**/css/**', filters: 'none'],
        [pattern: '/**/images/**', filters: 'none'],
        [pattern: '/**/favicon.ico', filters: 'none'],
        [pattern: '/api/integrate/logo/*', filters: 'none'],
        [pattern: '/internalErrorHandler/**', filters: 'JOINED_FILTERS, -oauth2ProviderFilter'],

        [
                pattern: '/api/**',
                filters: 'JOINED_FILTERS, -oauth2ProviderFilter, -anonymousAuthenticationFilter, -exceptionTranslationFilter, -authenticationProcessingFilter, -securityContextPersistenceFilter, -rememberMeAuthenticationFilter'
        ],
        //Traditional, stateful chain
        [
                pattern: '/stateful/**',
                filters: 'JOINED_FILTERS, -restTokenValidationFilter, -restExceptionTranslationFilter'
        ],
        [pattern: '/oauth/token', filters: 'JOINED_FILTERS,--restTokenValidationFilter, -oauth2ProviderFilter,-securityContextPersistenceFilter,-logoutFilter,-authenticationProcessingFilter,-rememberMeAuthenticationFilter,-exceptionTranslationFilter'],
        [pattern: '/v1/**', filters: 'JOINED_FILTERS,-restTokenValidationFilter, -restExceptionTranslationFilter, -securityContextPersistenceFilter,-logoutFilter,-authenticationProcessingFilter,-rememberMeAuthenticationFilter,-oauth2BasicAuthenticationFilter,-exceptionTranslationFilter'],
        [pattern: '/**', filters: 'JOINED_FILTERS,-statelessSecurityContextPersistenceFilter,-oauth2ProviderFilter,-clientCredentialsTokenEndpointFilter,-oauth2BasicAuthenticationFilter,-oauth2ExceptionTranslationFilter']
]

// Spring security rest plugin settings
grails.plugin.springsecurity.rest.login.useJsonCredentials = true
grails.plugin.springsecurity.rest.login.failureStatusCode = 401
//grails.plugin.springsecurity.rest.token.storage.useGorm = false
grails.plugin.springsecurity.rest.token.storage.useGorm = true
grails.plugin.springsecurity.rest.token.storage.gorm.tokenDomainClassName = 'io.sparkwork.security.AuthenticationToken'
grails.plugin.springsecurity.rest.token.storage.gorm.tokenValuePropertyName = 'tokenValue'
grails.plugin.springsecurity.rest.token.storage.gorm.usernamePropertyName = 'username'
//grails.plugin.springsecurity.rest.token.storage.gorm.passwordPropertyName = 'password'
grails.gorm.default.mapping = {
    autowire true
}
grails.plugin.springsecurity.rest.token.validation.useBearerToken = true

grails.plugin.springsecurity.rest.logout.endpointUrl = '/api/logout'
grails.plugin.springsecurity.rest.token.validation.headerName = 'X-Auth-Token'

grails.plugin.springsecurity.rest.token.rendering.usernamePropertyName = 'username'
grails.plugin.springsecurity.rest.token.rendering.authoritiesPropertyName = 'roles'

//Config for Spring Security REST plugin

//login
grails.plugin.springsecurity.rest.login.active = true
grails.plugin.springsecurity.rest.login.endpointUrl = '/api/login'
////token generation
grails.plugin.springsecurity.rest.token.generation.useSecureRandom = true
grails.plugin.springsecurity.rest.token.generation.useUUID = false
grails.plugin.springsecurity.rest.login.usernamePropertyName = 'username'
grails.plugin.springsecurity.rest.login.passwordPropertyName = 'password'
grails.plugin.springsecurity.adh.errorPage = '/internalErrorHandler/forbiddenAccess'
grails.plugin.springsecurity.scr.allowSessionCreation = false
grails.plugin.springsecurity.scpf.forceEagerSessionCreation = false
grails.plugin.springsecurity.apf.allowSessionCreation = false

grails.mime.types = [html         : ['text/html', 'application/xhtml+xml'],
                     xml          : ['text/xml', 'application/xml'],
                     text         : 'text-plain',
                     js           : 'text/javascript',
                     rss          : 'application/rss+xml',
                     atom         : 'application/atom+xml',
                     css          : 'text/css',
                     csv          : 'text/csv',
                     pdf          : 'application/pdf',
                     rtf          : 'application/rtf',
                     excel        : 'application/vnd.ms-excel',
                     ods          : 'application/vnd.oasis.opendocument.spreadsheet',
                     all          : '*/*',
                     json         : ['application/json', 'text/json'],
                     form         : 'application/x-www-form-urlencoded',
                     multipartForm: 'multipart/form-data',
]

grails.plugin.springsecurity.useSecurityEventListener = true


// Added by the Spring Security OAuth2 Provider plugin:
grails.plugin.springsecurity.oauthProvider.clientLookup.className = 'io.sparkwork.security.Client'
grails.plugin.springsecurity.oauthProvider.authorizationCodeLookup.className = 'io.sparkwork.security.AuthorizationCode'
grails.plugin.springsecurity.oauthProvider.accessTokenLookup.className = 'io.sparkwork.security.AccessToken'
grails.plugin.springsecurity.oauthProvider.refreshTokenLookup.className = 'io.sparkwork.security.RefreshToken'
