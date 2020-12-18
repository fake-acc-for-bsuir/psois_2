package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CDAudiosRepositoryImpl implements CDAudiosRepository {
    private CategoriesRepository categoriesRepository;
    private EntriesRepository entriesRepository;
    private List<CDAudio> cdAudios;

    public CDAudiosRepositoryImpl(CategoriesRepository categoriesRepository, EntriesRepository entriesRepository,
                               List<CDAudio> cdAudios) {
        this.categoriesRepository = categoriesRepository;
        this.entriesRepository = entriesRepository;
        this.cdAudios = cdAudios;
    }

    private CDAudio loadCDAudio(CDAudio cdAudio) {
        CDAudio copy = copyCDAudio(cdAudio);

        copyEntryAttributesToCDAudio(entriesRepository.loadEntry(cdAudio.getId()), copy);

        return copy;
    }

    @Override
    public CDAudio loadCDAudio(int id) {
        for (CDAudio cdAudio : cdAudios) {
            if (cdAudio.getId() == id) {
                return loadCDAudio(cdAudio);
            }
        }

        return null;
    }

    @Override
    public List<CDAudio> loadAllCDAudios() {
        List<CDAudio> copy = new ArrayList<>();

        for (CDAudio cdAudio : cdAudios)
            copy.add(loadCDAudio(cdAudio));

        return copy;
    }

    @Override
    public CDAudio createCDAudio(CDAudio cdAudio) {
        Entry entry = new Entry(new Date(), cdAudio.getComment(), cdAudio.getAuthor(), -1, cdAudio.getCategory(), cdAudio.getTypeId(), cdAudio.getTitle());
        entry = entriesRepository.createEntry(entry);

        CDAudio copy = copyCDAudio(cdAudio);
        copy.setId(entry.getId());

        cdAudios.add(copy);

        return copyCDAudio(copy);
    }

    @Override
    public void saveCDAudio(CDAudio cdAudio) {
        removeCDAudio(cdAudio);

        cdAudios.add(copyCDAudio(cdAudio));

        Entry cdAudioEntry = entriesRepository.loadEntry(cdAudio.getId());

        cdAudioEntry.setCategory(cdAudio.getCategory());
        cdAudioEntry.setComment(cdAudio.getComment());
        cdAudioEntry.setTitle(cdAudio.getTitle());

        entriesRepository.saveEntry(cdAudioEntry);
    }

    @Override
    public void removeCDAudio(Entry cdAudio) {
        entriesRepository.removeEntry(cdAudio);

        cdAudios.removeIf(b -> b.getId() == cdAudio.getId());
    }

    @Override
    public List<CDAudio> loadCDAudiosByFilter(CDAudioFilter cdAudioFilter) {
        List<CDAudio> result = new ArrayList<>();

        for (CDAudio cdAudio : cdAudios) {
            if (checkEntryFilter(cdAudio, cdAudioFilter) &&
                    checkTextFilterMatch(cdAudio.getLabel(), cdAudioFilter.getLabelValue(), cdAudioFilter.isOnlyFullLabelMatch(), cdAudioFilter.isDoSearchByLabel()) &&
                    checkDate(cdAudio.getReleaseYear(), cdAudioFilter.getReleaseYearNotAfter(), cdAudioFilter.getReleaseYearNotBefore(), cdAudioFilter.isDoSearchByReleaseYear()))
                result.add(copyCDAudio(cdAudio));
        }

        return result;
    }

    private CDAudio copyCDAudio(CDAudio cdAudio) {
        return new CDAudio(new Date(cdAudio.getAddDate().getTime()), cdAudio.getComment(), cdAudio.getAuthor(), cdAudio.getId(),
                cdAudio.getCategory(), cdAudio.getTitle(), new Date(cdAudio.getReleaseYear().getTime()),
                cdAudio.getLabel(), cdAudio.getCoverUrl());
    }

    private void copyEntryAttributesToCDAudio(Entry entry, CDAudio cdAudio) {
        cdAudio.setCategory(entry.getCategory());
        cdAudio.setComment(entry.getComment());
        cdAudio.setId(entry.getId());
        cdAudio.setTitle(entry.getTitle());
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
