package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Session;

public interface SessionsRepository {
    Session loadSession(String token);

    Session createSession(Session session);

    void removeSession(Session session);

    void invalidateAllSessions(int familyMember);
}
