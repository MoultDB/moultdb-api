package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.sql.Timestamp;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-25
 */
public class VersionTO extends EntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -7821054265189878804L;
    
    private final UserTO creationUserTO;
    private final Timestamp creationDate;
    private final UserTO lastUpdateUserTO;
    private final Timestamp lastUpdateDate;
    private final Integer versionNumber;
    
    public VersionTO(Integer id, UserTO creationUserTO, Timestamp creationDate,
                     UserTO lastUpdateUserTO, Timestamp lastUpdateDate, Integer versionNumber) {
        super(id);
        this.creationUserTO = creationUserTO;
        this.creationDate = creationDate;
        this.lastUpdateUserTO = lastUpdateUserTO;
        this.lastUpdateDate = lastUpdateDate;
        this.versionNumber = versionNumber;
    }
    
    public UserTO getCreationUserTO() {
        return creationUserTO;
    }
    
    public Timestamp getCreationDate() {
        return creationDate;
    }
    
    public UserTO getLastUpdateUserTO() {
        return lastUpdateUserTO;
    }
    
    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }
    
    public Integer getVersionNumber() {
        return versionNumber;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", VersionTO.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("creationUserTO=" + creationUserTO)
                .add("creationDate=" + creationDate)
                .add("lastUpdateUserTO=" + lastUpdateUserTO)
                .add("lastUpdateDate=" + lastUpdateDate)
                .add("versionNumber=" + versionNumber)
                .toString();
    }
}
