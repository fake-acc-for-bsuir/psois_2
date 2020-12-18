package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.CDVideo;
import tech.wcobalt.lab_impl.domain.CDVideoFilter;
import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.domain.EntryFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CDVideosRepositoryImpl implements CDVideosRepository {
    private CategoriesRepository categoriesRepository;
    private EntriesRepository entriesRepository;
    private List<CDVideo> cdVideos;

    public CDVideosRepositoryImpl(CategoriesRepository categoriesRepository, EntriesRepository entriesRepository,
                                  List<CDVideo> cdVideos) {
        this.categoriesRepository = categoriesRepository;
        this.entriesRepository = entriesRepository;
        this.cdVideos = cdVideos;
    }

    private CDVideo loadCDVideo(CDVideo cdVideo) {
        CDVideo copy = copyCDVideo(cdVideo);

        copyEntryAttributesToCDVideo(entriesRepository.loadEntry(cdVideo.getId()), copy);

        return copy;
    }

    @Override
    public CDVideo loadCDVideo(int id) {
        for (CDVideo cdVideo : cdVideos) {
            if (cdVideo.getId() == id) {
                return loadCDVideo(cdVideo);
            }
        }

        return null;
    }

    @Override
    public List<CDVideo> loadAllCDVideos() {
        List<CDVideo> copy = new ArrayList<>();

        for (CDVideo cdVideo : cdVideos)
            copy.add(loadCDVideo(cdVideo));

        return copy;
    }

    @Override
    public CDVideo createCDVideo(CDVideo cdVideo) {
        Entry entry = new Entry(new Date(), cdVideo.getComment(), cdVideo.getAuthor(), -1, cdVideo.getCategory(), cdVideo.getTypeId(), cdVideo.getTitle());
        entry = entriesRepository.createEntry(entry);

        CDVideo copy = copyCDVideo(cdVideo);
        copy.setId(entry.getId());

        cdVideos.add(copy);

        return copyCDVideo(copy);
    }

    @Override
    public void saveCDVideo(CDVideo cdVideo) {
        removeCDVideo(cdVideo);

        cdVideos.add(copyCDVideo(cdVideo));

        Entry cdVideoEntry = entriesRepository.loadEntry(cdVideo.getId());

        cdVideoEntry.setCategory(cdVideo.getCategory());
        cdVideoEntry.setComment(cdVideo.getComment());
        cdVideoEntry.setTitle(cdVideo.getTitle());

        entriesRepository.saveEntry(cdVideoEntry);
    }

    @Override
    public void removeCDVideo(Entry cdVideo) {
        entriesRepository.removeEntry(cdVideo);

        cdVideos.removeIf(b -> b.getId() == cdVideo.getId());
    }

    @Override
    public List<CDVideo> loadCDVideosByFilter(CDVideoFilter cdVideoFilter) {
        List<CDVideo> result = new ArrayList<>();

        for (CDVideo cdVideo : cdVideos) {
            if (checkEntryFilter(cdVideo, cdVideoFilter) &&
                    checkDate(cdVideo.getReleaseYear(), cdVideoFilter.getReleaseYearNotAfter(), cdVideoFilter.getReleaseYearNotBefore(), cdVideoFilter.isDoSearchByReleaseYear()))
                result.add(copyCDVideo(cdVideo));
        }

        return result;
    }

    private CDVideo copyCDVideo(CDVideo cdVideo) {
        return new CDVideo(new Date(cdVideo.getAddDate().getTime()), cdVideo.getComment(), cdVideo.getAuthor(), cdVideo.getId(),
                cdVideo.getCategory(), cdVideo.getTitle(), new Date(cdVideo.getReleaseYear().getTime()),
                cdVideo.getCoverUrl());
    }

    private void copyEntryAttributesToCDVideo(Entry entry, CDVideo cdVideo) {
        cdVideo.setCategory(entry.getCategory());
        cdVideo.setComment(entry.getComment());
        cdVideo.setId(entry.getId());
        cdVideo.setTitle(entry.getTitle());
    }

    //i really don't care
    boolean checkEntryFilter(Entry entry, EntryFilter entryFilter) {
        return checkDate(entry.getAddDate(), entryFilter.getAddDateNotAfter(), entryFilter.getAddDateNotBefore(), entryFilter.isDoSearchByAddDate()) &&
                checkTextFilterMatch(entry.getAuthor(), entryFilter.getAuthorValue(), entryFilter.isOnlyFullAuthorMatch(), entryFilter.isDoSearchByAuthor()) &&
                checkTextFilterMatch(entry.getComment(), entryFilter.getCommentValue(), entryFilter.isOnlyFullCommentMatch(), entryFilter.isDoSearchByComment()) &&
                checkTextFilterMatch(entry.getTitle(), entryFilter.getTitleValue(), entryFilter.isOnlyFullTitleMatch(), entryFilter.isDoSearchByTitle()) &&
                (!entryFilter.isDoSearchByCategory()
                        || categoriesRepository.loadChildrenCategories(entryFilter.getCategoryForSearch(),
                        entryFilter.isDoSearchByCategoryRecursively()).stream().anyMatch(c -> entry.getCategory() == c.getId()));
    }

    boolean checkTextFilterMatch(String original, String value, boolean onlyFullMatch, boolean doSearch) {
        return !doSearch || ((onlyFullMatch && original.contains(value.toLowerCase())) || (!onlyFullMatch && original.toLowerCase().equals(value.toLowerCase())));
    }

    boolean checkDate(Date original, Date notAfter, Date notBefore, boolean doSearch) {
        return !doSearch || (original.before(notAfter) && original.after(notBefore));
    }
}
