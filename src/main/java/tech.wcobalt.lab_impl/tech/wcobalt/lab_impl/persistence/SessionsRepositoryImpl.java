package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Session;

import java.util.List;

public class SessionsRepositoryImpl implements SessionsRepository {
    private List<Session> sessions;

    public SessionsRepositoryImpl(List<Session> sessions) {
        this.sessions = sessions;
    }

    @Override
    public Session loadSession(String token) {
        for (Session session : sessions)
            if (session.getToken().equals(token))
                return copySession(session);

        return null;
    }

    @Override
    public Session createSession(Session session) {
        sessions.add(copySession(session));

        return session;
    }

    @Override
    public void removeSession(Session session) {
        sessions.removeIf(s -> s.getToken().equals(session.getToken()));
    }

    @Override
    public void invalidateAllSessions(int familyMember) {
        sessions.removeIf(s -> s.getFamilyMember() == familyMember);
    }

    private Session copySession(Session session) {
        return new Session(session.getToken(), session.getSessionIsValidUntil(), session.getFamilyMember());
    }
}
