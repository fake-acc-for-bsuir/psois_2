package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Session;
import tech.wcobalt.lab_impl.infrastructure.SessionSerializer;

import java.io.*;

public class SaveLoadSessionsUseCaseImpl implements SaveLoadSessionsUseCase {
    private SessionSerializer sessionSerializer;
    private static final String FILENAME = "session";
    private File sessionFile;

    public SaveLoadSessionsUseCaseImpl(SessionSerializer sessionSerializer) {
        this.sessionSerializer = sessionSerializer;

        sessionFile = new File(FILENAME);
    }

    @Override
    public Session loadSession() {
        if (sessionFile.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(sessionFile)))) {
                String session = bufferedReader.readLine();

                return sessionSerializer.deserializeSession(session);
            } catch (IOException exc) {
                exc.printStackTrace(); //that's bad
            }
        }

        return null;
    }

    @Override
    public void saveSession(Session session) {
        try {
            if (!sessionFile.exists())
                sessionFile.createNewFile();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sessionFile)));

            bufferedWriter.write(sessionSerializer.serializeSession(session));
        } catch (IOException exc) {
            exc.printStackTrace(); //that's bad
        }
    }
}
