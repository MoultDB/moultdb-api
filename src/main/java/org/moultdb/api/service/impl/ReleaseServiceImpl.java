package org.moultdb.api.service.impl;

import org.moultdb.api.exception.MoultDBException;
import org.moultdb.api.model.ReleaseInfo;
import org.moultdb.api.model.ReleaseVersion;
import org.moultdb.api.model.Statistics;
import org.moultdb.api.repository.dao.ReleaseDAO;
import org.moultdb.api.service.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    @Autowired
    private ReleaseDAO dao;

    @Override
    public ReleaseInfo getReleaseInfo() {
        return (new ReleaseInfo(dao.findReleaseVersion(), new Statistics(dao.findGenomeCount(), dao.findAnnotationCount())));
    }
    
    @Override
    public ReleaseVersion createNewVersion() {
        int rows = dao.insertNewReleaseVersion();
        if (rows == 1) {
            return dao.findReleaseVersion();
        }
        throw new MoultDBException("Error during insertion of a new release version");
    }
}