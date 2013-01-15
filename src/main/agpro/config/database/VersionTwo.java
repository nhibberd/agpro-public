package agpro.config.database;

import java.util.Arrays;
import java.util.List;

public class VersionTwo implements Patch {

    public Integer version() {
        return 2;
    }

    public List<String> changes() {
        return Arrays.asList("","");
    }
}