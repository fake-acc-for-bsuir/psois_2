package tech.wcobalt.lab_impl.infrastructure;

import tech.wcobalt.lab_impl.domain.Session;

public interface SessionSerializer {
    String serializeSession(Session session);

    Session deserializeSession(String session);
}
