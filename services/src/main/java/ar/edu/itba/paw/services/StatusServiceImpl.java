package ar.edu.itba.paw.services;

import ar.edu.itba.paw.persistence.StatusDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusService{

    @Autowired
    private StatusDao statusDao;

    @Transactional(readOnly = true)
    @Override
    public List<String> getAllStatus() {
        return statusDao.getAllStatus();
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getAllStatus(int type) {
        return statusDao.getAllStatus(type);
    }
}
