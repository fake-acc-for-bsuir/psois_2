package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.CDAudio;
import tech.wcobalt.lab_impl.domain.CDAudioFilter;
import tech.wcobalt.lab_impl.domain.Entry;

import java.util.List;

public interface CDAudiosRepository {
    CDAudio loadCDAudio(int id);

    List<CDAudio> loadAllCDAudios();

    CDAudio createCDAudio(CDAudio CDAudio);

    void saveCDAudio(CDAudio CDAudio);

    void removeCDAudio(Entry CDAudio);

    List<CDAudio> loadCDAudiosByFilter(CDAudioFilter CDAudioFilter);
}
