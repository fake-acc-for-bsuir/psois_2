package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.CDVideo;
import tech.wcobalt.lab_impl.domain.CDVideoFilter;
import tech.wcobalt.lab_impl.domain.Entry;

import java.util.List;

public interface CDVideosRepository {
    CDVideo loadCDVideo(int id);

    List<CDVideo> loadAllCDVideos();

    CDVideo createCDVideo(CDVideo CDVideo);

    void saveCDVideo(CDVideo CDVideo);

    void removeCDVideo(Entry CDVideo);

    List<CDVideo> loadCDVideosByFilter(CDVideoFilter CDVideoFilter);
}
