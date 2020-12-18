package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Favorites;

import java.util.List;

public class FavoritesRepositoryImpl implements FavoritesRepository {
    private List<Favorites> favoritesList;

    public FavoritesRepositoryImpl(List<Favorites> favoritesList) {
        this.favoritesList = favoritesList;
    }

    @Override
    public Favorites loadFavorites(int familyMember) {
        for (Favorites favorites : favoritesList) {
            if (favorites.getFamilyMember() == familyMember)
                return copyFavorites(favorites);
        }

        return null;
    }

    @Override
    public void saveFavorites(Favorites favorites) {
        Favorites original = loadFavorites(favorites.getFamilyMember());

        original.getEntries().clear();
        original.getEntries().addAll(favorites.getEntries());
    }

    private Favorites copyFavorites(Favorites favorites) {
        Favorites copy = new Favorites(favorites.getFamilyMember());

        copy.getEntries().addAll(favorites.getEntries());

        return copy;
    }
}
