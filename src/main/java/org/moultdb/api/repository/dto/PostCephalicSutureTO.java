package org.moultdb.api.repository.dto;

import java.io.Serial;

/**
 * @author Valentine Rech de Laval
 * @since 2024-08-14
 */
public class PostCephalicSutureTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = -3687990462763626119L;
    
    public PostCephalicSutureTO(Integer id, String name) throws IllegalArgumentException {
        super(id, name, null);
    }
}
