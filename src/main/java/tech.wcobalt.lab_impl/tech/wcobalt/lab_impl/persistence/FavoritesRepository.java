package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Favorites;

public interface FavoritesRepository {
    Favorites loadFavorites(int familyMember);

    void saveFavorites(Favorites favorites);
}
