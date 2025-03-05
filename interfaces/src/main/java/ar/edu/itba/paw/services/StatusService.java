package ar.edu.itba.paw.services;

import java.util.List;

public interface StatusService {
    List<String> getAllStatus();

    List<String> getAllStatus(int type);
}
