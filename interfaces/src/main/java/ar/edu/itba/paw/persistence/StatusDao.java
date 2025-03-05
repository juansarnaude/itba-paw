package ar.edu.itba.paw.persistence;

import java.util.List;

public interface StatusDao {
    List<String> getAllStatus();

    List<String> getAllStatus(int type);
}
