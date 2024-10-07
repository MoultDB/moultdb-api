package org.moultdb.importer.taxonannotation;

import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-10-01
 */
public class DevStageMappingBean {
    private String devStageName;
    private String uberonId;
    private String uberonName;
    
    public DevStageMappingBean() {
    }
    
    public String getDevStageName() {
        return devStageName;
    }
    
    public void setDevStageName(String devStageName) {
        this.devStageName = devStageName;
    }
    
    public String getUberonId() {
        return uberonId;
    }
    
    public void setUberonId(String uberonId) {
        this.uberonId = uberonId;
    }
    
    public String getUberonName() {
        return uberonName;
    }
    
    public void setUberonName(String uberonName) {
        this.uberonName = uberonName;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DevStageMappingBean.class.getSimpleName() + "[", "]")
                .add("devStageName='" + devStageName + "'")
                .add("uberonId='" + uberonId + "'")
                .add("uberonName='" + uberonName + "'")
                .toString();
    }
}
