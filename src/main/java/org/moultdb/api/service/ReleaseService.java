package org.moultdb.api.service;

import org.moultdb.api.model.ReleaseInfo;
import org.moultdb.api.model.ReleaseVersion;

public interface ReleaseService {
    
    public ReleaseInfo getReleaseInfo();
    
    public ReleaseVersion createNewVersion();
}
