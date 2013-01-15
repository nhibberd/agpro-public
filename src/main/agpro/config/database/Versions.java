package agpro.config.database;

import agpro.data.core.Action;
import agpro.db.Connector;
import agpro.db.Db;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class Versions {
    public final Connector connector = new Connector("jdbc:postgresql:", "", "");
    private final Db Db = new Db();

    String init = "CREATE TABLE IF NOT EXISTS schema_version ( version VARCHAR(25) PRIMARY KEY )";
    String selectversion = "SELECT * FROM schema_version";
    String schemaversionexists = "SELECT TRUE from information_schema.tables where table_name = 'schema_version'";
    List<Patch> patches = Arrays.asList(new VersionOne(), new VersionTwo());

    public Boolean isInitialized(Connection connection){
        return Db.queryExists(connection, schemaversionexists);
    }

    public void initialize(Connection connection){
        Db.executeUpdate(connection,init);
        Db.executeUpdateOneInt(connection,"insert into schema_version values (?)",0);
    }

    public Integer getCurrent(Connection connection){
        return Db.queryInt(connection, selectversion, 1);
    }

    public void go(){
        connector.withConnection(new Action<Connection>() {
            public void apply(final Connection connection) {
                if (!isInitialized(connection)){
                    System.out.println("initializing");
                    initialize(connection);
                } else
                    System.out.println("apparently initialized");
                Integer current = getCurrent(connection);
                java.util.Collections.sort(patches, new PatchComparator());
                for (final Patch patch : patches) {
                    if (patch.version() > current) {
                        System.out.println("applying patch [" + patch.version() + "]");

                        for (String change : patch.changes()) {
                            Db.executeUpdate(connection, change);
                        }
                        Db.executeUpdateOneInt(connection, "update schema_version set version = ?", patch.version());
                    }
                }
            }
        });
    }
}
