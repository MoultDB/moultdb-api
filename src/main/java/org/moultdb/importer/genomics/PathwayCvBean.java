package org.moultdb.importer.genomics;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2024-03-25
 */
public class PathwayCvBean {
    
    private String id;
    private String name;
    private String reference;
    private String description;
    private Set<Integer> figureIds;
    
    public PathwayCvBean() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getReference() {
        return reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<Integer> getFigureIds() {
        return figureIds;
    }
    
    public void setFigureIds(Set<Integer> figureIds) {
        this.figureIds = figureIds;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathwayCvBean that = (PathwayCvBean) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(reference, that.reference)
                && Objects.equals(description, that.description);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, reference, description);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", PathwayCvBean.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("reference='" + reference + "'")
                .add("description='" + description + "'")
                .add("figureIds=" + figureIds)
                .toString();
    }
}
