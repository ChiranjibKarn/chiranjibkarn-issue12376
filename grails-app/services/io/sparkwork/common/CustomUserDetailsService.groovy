package io.sparkwork.common

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.gorm.transactions.Transactional
import io.sparkwork.security.User
import org.grails.web.util.WebUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

import javax.security.auth.login.LoginException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Transactional
class CustomUserDetailsService implements UserDetailsService {
    private static final Logger LOG = LoggerFactory.getLogger('PERFORMANCE')

    @Transactional(readOnly = true, noRollbackFor = [IllegalArgumentException, UsernameNotFoundException])
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = WebUtils.retrieveGrailsWebRequest().currentRequest
        String domainName = request?.getAttribute('domainName')
        Company company = null
        if (domainName) {
            company = Company.findByShortNameIlike(domainName)
        }

        /*Allow range of white list IPs (separated by "-" sign). Also, non-range IPs should be compatible*/
        if (company && company.whiteListIPs && (company.whiteListIPs.size() > 0)) {
            List<String> simplifiedIps = []
            String remoteIP = request.getHeader('X-Forwarded-For') ?: request.remoteAddr
            company.whiteListIPs.each {
                if (it.ip && it.ip.toString().contains('-')) {
                    def ipTokens = ((String)it.ip).tokenize('-')
                    def delimiter = ipTokens[0].contains('.') ? '.' : ':'
                    def ipParts = ipTokens[0].tokenize(delimiter)
                    String lastDigit = ipParts.last()
                    (Integer.parseInt(lastDigit)..Integer.parseInt(ipTokens[1])).each { ipPa ->
                        simplifiedIps.add(((ipParts - lastDigit).join(delimiter) + delimiter + ipPa))
                    }
                } else {
                    simplifiedIps.add(it.ip)
                }
            }

            if (remoteIP && !simplifiedIps.contains(remoteIP)) {
                HttpServletResponse response = WebUtils.retrieveGrailsWebRequest().response
                response.addHeader('Failure-Reason', 'INVALID_IP_ADDRESS')
                def retMap = [:]
                retMap.status_code = '403'
                retMap.message = 'ERROR_403_INVALID_IP'
                response << 'ERROR_403_INVALID_IP'
                LOG.debug('Login Failed: Invalid IP {} for company {}', remoteIP, domainName)
                throw new LoginException('INVALID_IP_ADDRESS')
            }
        }
        if (company) {
            User user = User.findByCIdAndEmailIlikeAndAccountExpiredAndAccountLockedAndEnabledAndDelOnIsNull(company, username, false, false, true)
            if (user) {
                LOG.debug('Load user {} with email', user.email)
                return userDetails(user.email, user)
            }
            user = User.findByCIdAndExternalIdIlikeAndAccountExpiredAndAccountLockedAndEnabledAndDelOnIsNull(company, username, false, false, true)
            if (user) {
                LOG.debug('Load user {} with externalId', user.externalId)
                return userDetails(user.externalId, user)
            }
        } else if (checkNoDomain(domainName)) {
            User user = User.findByEmailIlikeAndAccountExpiredAndAccountLockedAndEnabledAndDelOnIsNull(username, false, false, true)
            if (user) {
                LOG.debug('Load user {} with email', user.email)
                return userDetails(user.email, user)
            }
            user = User.findByExternalIdIlikeAndAccountExpiredAndAccountLockedAndEnabledAndDelOnIsNull(username, false, false, true)
            if (user) {
                LOG.debug('Load user {} with externalId', user.externalId)
                return userDetails(user.externalId, user)
            }
        }

        LOG.debug('Login Failed: User with username {} not found for company {}', username, domainName)
        throw new UsernameNotFoundException('User not found')
    }

    private boolean checkNoDomain(String domainName) {
        final List<String> noDomains = ['localhost', 'localhost-data', 'dev-app', 'devapp-data', 'staging-data', 'data', 'dev-crux', 'angular', 'dev-chat', 'dev-lxp']
        return noDomains.contains(domainName)
    }

    private UserDetails userDetails(String usernameFieldValue, User user) {
        Collection<GrantedAuthority> userAuthorities = user.authorities.collect {
            new SimpleGrantedAuthority(it.authority)
        }
        Collection<GrantedAuthority> authorities = userAuthorities ?: [SpringSecurityUtils.NO_ROLE]
        return new GrailsUser(usernameFieldValue, user.password, user.enabled, !user.accountExpired, !user.passwordExpired, !user.accountLocked, authorities, user.id)
    }

    private boolean checkAppType(String appType, User user) {
        boolean allowed = false
        if (appType && appType.toLowerCase().startsWith('mobile-') && user.sysAccessChat) {
            allowed = true
        } else if (appType && appType.toLowerCase().startsWith('webapp') && user.sysAccessLXP) {
            allowed = true
        }
        return allowed
    }
}
