import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import grails.util.Environment

def commonPattern = '%d{dd.MM.yyyy HH:mm:ss.SSS} [%thread] %-5level %logger{35} - %msg%n'

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = commonPattern
    }
}

if (Environment.current == Environment.DEVELOPMENT) {
    root(INFO, ['STDOUT'])
} else {
    appender('ROLLING', RollingFileAppender) {
        file = '/var/log/sparkwork/backend.log'
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = commonPattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = '/var/log/sparkwork/backend-%d.log.zip'
        }
    }

    appender('PERFORMANCE', RollingFileAppender) {
        file = '/var/log/sparkwork/performance.log'
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = commonPattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = '/var/log/sparkwork/performance-%d.log.zip'
        }
    }
    appender('FRONTEND', RollingFileAppender) {
        file = '/var/log/sparkwork/frontend.log'
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = commonPattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = '/var/log/sparkwork/frontend-%d.log.zip'
        }
    }

    appender('MOBILE', RollingFileAppender) {
        file = '/var/log/sparkwork/mobile.log'
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = commonPattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = '/var/log/sparkwork/mobile-%d.log.zip'
        }
    }

    appender('SQS', RollingFileAppender) {
        file = '/var/log/sparkwork/sqs.log'
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = commonPattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = '/var/log/sparkwork/sqs-%d.log.zip'
        }
    }

    appender('SEARCH', RollingFileAppender) {
        file = '/var/log/sparkwork/search.log'
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = commonPattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = '/var/log/sparkwork/search-%d.log.zip'
        }
    }

    appender('SSO', RollingFileAppender) {
        file = '/var/log/sparkwork/sso.log'
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = commonPattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = '/var/log/sparkwork/sso-%d.log.zip'
        }
    }

    appender('RESTNOTIFICATION', RollingFileAppender) {
        file = '/var/log/sparkwork/restnotification.log'
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = commonPattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = '/var/log/sparkwork/restnotification-%d.log.zip'
        }
    }

    appender('VAPI', RollingFileAppender) {
        file = '/var/log/sparkwork/vapi.log'
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = commonPattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = '/var/log/sparkwork/vapi-%d.log.zip'
        }
    }

    logger('PERFORMANCE', DEBUG, ['PERFORMANCE'])
    logger('FRONTEND', ERROR, ['FRONTEND'], false)
    logger('MOBILE', ERROR, ['MOBILE'], false)
    logger('SQS', INFO, ['SQS'])
    logger('SEARCH', INFO, ['SEARCH'])
    logger('SSO', INFO, ['SSO'])
    logger('VAPI', DEBUG, ['VAPI'], false)
    logger('RESTNOTIFICATION', ERROR, ['RESTNOTIFICATION'])
    root(WARN, ['ROLLING'])
}
/*Suppressed noisy package from user agent parser lib*/
logger('nl.basjes.parse.useragent.utils', ERROR)
logger('nl.basjes.parse.useragent', ERROR)
//logger('ws.schild.jave.FFMPEGExecutor', DEBUG)
//logger('ws.schild.jave.Encoder', DEBUG)
