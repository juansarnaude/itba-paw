package ar.edu.itba.paw.services;

import java.util.List;

public interface LanguageService {
    List<String> getAllLanguages();

    List<String> getAllLanguages(int type);
}
