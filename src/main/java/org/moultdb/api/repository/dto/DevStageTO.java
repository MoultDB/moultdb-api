package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2022-05-02
 */
public class DevStageTO extends NamedEntityTO<String> {
    
    @Serial
    private static final long serialVersionUID = 3036045739159225675L;
    
    private final int leftBound;
    private final int rightBound;
    
    public DevStageTO(String id, String name, String description, int leftBound, int rightBound) throws IllegalArgumentException {
        super(id, name, description);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }
    
    public int getLeftBound() {
        return leftBound;
    }
    
    public int getRightBound() {
        return rightBound;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DevStageTO.class.getSimpleName() + "[", "]")
                .add("id=" + getId())
                .add("name=" + getName())
                .add("description=" + getDescription())
                .add("leftBound=" + leftBound)
                .add("rightBound=" + rightBound)
                .toString();
    }
}
