package ar.edu.itba.paw.persistence;

import java.util.List;

public interface LanguageDao {
    List<String> getAllLanguages();

    List<String> getAllLanguages(int type);
}
