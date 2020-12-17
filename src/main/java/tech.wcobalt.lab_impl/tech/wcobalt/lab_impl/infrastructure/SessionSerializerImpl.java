package tech.wcobalt.lab_impl.infrastructure;

import tech.wcobalt.lab_impl.domain.Session;

import java.util.Date;

public class SessionSerializerImpl implements SessionSerializer {
    private static final String SEPARATOR = ":";

    @Override
    public String serializeSession(Session session) {
        return session.getToken() + SEPARATOR + session.getFamilyMember()
                + SEPARATOR + session.getSessionIsValidUntil().getTime();
    }

    @Override
    public Session deserializeSession(String session) {
        String[] parts = session.split(SEPARATOR);

        if (parts.length >= 3) {
            try {
                String token = parts[0];
                int familyMember = Integer.parseInt(parts[1]);
                Date validUntil = new Date(Long.parseLong(parts[2]));

                return new Session(token, validUntil, familyMember);
            } catch (NumberFormatException exc) {
                throw new IllegalArgumentException("Invalid session string content");
            }
        } else
            throw new IllegalArgumentException("Invalid session string format");
    }
}
