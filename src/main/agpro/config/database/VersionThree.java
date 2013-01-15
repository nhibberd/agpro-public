package agpro.config.database;


import java.util.Arrays;
import java.util.List;

public class VersionThree implements Patch {
    public Integer version() {
        return 3;
    }

    public List<String> changes() {
        return Arrays.asList("","");
    }
}