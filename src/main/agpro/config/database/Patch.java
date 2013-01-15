package agpro.config.database;

import java.util.List;

public interface Patch {
    Integer version();
    List<String> changes();
}
