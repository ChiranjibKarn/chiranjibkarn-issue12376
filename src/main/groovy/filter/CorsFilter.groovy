import static org.apache.commons.lang3.StringUtils.isBlank
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_MAX_AGE
import static org.springframework.http.HttpHeaders.ORIGIN
import static org.springframework.http.HttpHeaders.REFERER
import static org.springframework.http.HttpMethod.OPTIONS

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.filter.OncePerRequestFilter

import javax.annotation.Priority
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Priority(Integer.MIN_VALUE)
class CorsFilter extends OncePerRequestFilter {

    def grailsApplication

    /*@Value('${grails.frontendUrl}')
    private URL frontEndUrl*/

    /*@Value('${grails.allowedOriginRegEx:.*}')
    private String allowedOriginRegEx*/

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        try {
            String allowedOrig = allowedOrigin(req)
            resp.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, allowedOrig)
            resp.addHeader(ACCESS_CONTROL_ALLOW_METHODS, 'DELETE, GET, OPTIONS, POST, PUT')
            resp.addHeader(ACCESS_CONTROL_ALLOW_HEADERS, 'Accept, Authorization, Content-Type, Content-Encoding, Origin, X-Requested-With, Sender, feVersion, App, Mobile, Cache-Control, If-Modified-Since, Pragma, SparkworkAppType, SparkworkAppVersion')
            resp.addHeader(ACCESS_CONTROL_MAX_AGE, '3600')
            resp.addHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, 'true')

            if (OPTIONS.name() != req.method) {
                chain.doFilter(req, resp)
            }
        } catch (Exception e) {
             e.printStackTrace()
            logger.error(e.message)
        }
    }

    private String allowedOrigin(HttpServletRequest req) {

        String FEUrl = grailsApplication.config.getProperty('grails.frontendUrl')
        String allowedOriginRegEx = grailsApplication.config.getProperty('grails.allowedOriginRegEx', String, '.*')
        URL frontEndUrl = new URL(FEUrl)


        String referer = req.getHeader(ORIGIN) ? req.getHeader(ORIGIN) + '/' : req.getHeader(REFERER)
        String app = req.getHeader('App') ?: req.getHeader('app')
        String domainName = ''
        String refererDomain = ''
        String refererDomainStaging = ''
        String refererDomainDevApp = ''
        String refererDomainAgularApp = ''
        String refererDomainDevChat = ''
        String refererDomainStagingChat = ''
        String refererDomainProductionChat = ''
        String refererDomainDevAppLXP = ''



        //For websocket from mobile
        if (req.requestURL && req.requestURL.contains('/stomp/') && (req.getParameter('App') != null)) {
            domainName = req.getParameter('App').replace('sparkworkmobile-', '')
            req.setAttribute('domainName', domainName)
            req.setAttribute('mobReq', true)
            if (referer.contains('httpsionic')) {
                referer = referer.replace('httpsionic://', 'http://')
            }
            if (referer == 'file://') {
                return referer
            } else if (referer != null && referer.length() > 0 && referer.charAt(referer.length() - 1) == '/') {
                referer = referer.substring(0, referer.length() - 1)
            }

            return referer
        }

        //For FE
        String feAppUrl = "${frontEndUrl.protocol}://${frontEndUrl.host}"
        if (referer == null) {
            return feAppUrl
        }

        //For crux
        String ip = req.getHeader('X-Forwarded-For') ?: req.remoteAddr
        if (referer) {
            if (req.requestURL && req.requestURL.contains('/stomp/')) {
                def url = new URL(referer)
                if (url.host.tokenize('.').size() > 0) {
                    domainName = url.host.tokenize('.')[0]

                }
            } else {
                String url = referer.replace('https://', '').replace('http://', '').replace('file://', '').replace('httpsionic://', '')
                if (url && url.tokenize('.').size() > 0) {
                    domainName = url.tokenize('.')[0]
                }
            }
                if (domainName.contains(':')) { //remove port
                    domainName = domainName.tokenize(':')[0]
                }
                req.setAttribute('domainName', domainName)
                req.setAttribute('mobReq', false)
           // }
        }

        //Everything else
        if (isBlank(allowedOriginRegEx)
                || referer ==~ ~/${allowedOriginRegEx}/
                || (referer.startsWith(refererDomain))
                || (referer.startsWith(refererDomainDevApp))
                || (referer.startsWith(refererDomainAgularApp))
                || (referer.startsWith(refererDomainDevChat))
                || (referer.startsWith(refererDomainStagingChat))
                || (referer.startsWith(refererDomainProductionChat))
                || (referer.startsWith(refererDomainDevAppLXP))
                || (referer.startsWith(refererDomainStaging))
                || checkForSisuCare(referer)) {
            if (referer != null && referer.length() > 0 && referer.charAt(referer.length() - 1) == '/') {
                referer = referer.substring(0, referer.length() - 1)
            }

            return referer
        }

        feAppUrl
    }

    private boolean checkForSisuCare(String referer) {
        boolean retVal = false
        return retVal
    }

}
