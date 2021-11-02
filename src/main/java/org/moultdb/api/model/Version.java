package org.moultdb.api.model;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-20
 */
public class Version {
    
    public final User creationUser;
    public final Timestamp creationDate;
    public final User lastUpdateUser;
    public final Timestamp lastUpdateDate;
    public final Integer versionNumber;
    
    public Version(User creationUser, Timestamp creationDate, User lastUpdateUser, Timestamp lastUpdateDate,
                   Integer versionNumber) {
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.lastUpdateUser = lastUpdateUser;
        this.lastUpdateDate = lastUpdateDate;
        this.versionNumber = versionNumber;
    }
    
    public User getCreationUser() {
        return creationUser;
    }
    
    public Timestamp getCreationDate() {
        return creationDate;
    }
    
    public User getLastUpdateUser() {
        return lastUpdateUser;
    }
    
    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }
    
    public Integer getVersionNumber() {
        return versionNumber;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Version version = (Version) o;
        return Objects.equals(creationUser, version.creationUser)
                && Objects.equals(creationDate, version.creationDate)
                && Objects.equals(lastUpdateUser, version.lastUpdateUser)
                && Objects.equals(lastUpdateDate, version.lastUpdateDate)
                && Objects.equals(versionNumber, version.versionNumber);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(creationUser, creationDate, lastUpdateUser, lastUpdateDate, versionNumber);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Version.class.getSimpleName() + "[", "]")
                .add("creationUser=" + creationUser)
                .add("creationDate=" + creationDate)
                .add("lastUpdateUser=" + lastUpdateUser)
                .add("lastUpdateDate=" + lastUpdateDate)
                .add("versionNumber=" + versionNumber)
                .toString();
    }
}
