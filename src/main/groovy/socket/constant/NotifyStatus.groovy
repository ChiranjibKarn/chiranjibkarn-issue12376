package socket.constant

/**
 * Created by ajay on 26/9/17.
 */
enum NotifyStatus {
    READ('READ'),
    ALERT('ALERT')

    final String value

    NotifyStatus(String value) {
        this.value = value
    }

    @Override
    String toString() {
        return value
    }

    String getKey() {
        return name()
    }

    String getValue() {
        return value
    }

    static List<NotifyStatus> list() {
        return [READ, ALERT]
    }
}
