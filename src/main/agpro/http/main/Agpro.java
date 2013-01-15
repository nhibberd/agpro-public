package agpro.http.main;

import agpro.config.database.Versions;
import agpro.data.core.Action;
import agpro.data.user.Register;
import agpro.db.Connector;
import agpro.db.Db;
import agpro.http.servlet.AgproServlet;
import agpro.http.servlet.FileBasicWebServlet;
import agpro.http.servlet.FileServlet;
import agpro.http.servlet.FileWebServlet;
import agpro.schedule.background.Threads;
import agpro.schedule.jobs.*;
import agpro.service.user.CreateUser;
import agpro.tool.Init;
import io.mth.foil.j.*;

import java.io.File;
import java.sql.Connection;

import static agpro.tool.Dates.*;

public class Agpro {
    private static final Foils foils = new DefaultFoils();
    private static final Configs c = new DefaultConfigs();
    private static final agpro.db.Db Db = new Db();


    public static void main(String[] args) {
        Connector connector = Init.connector;
        final Versions db = new Versions();
        db.go();

        connector.withConnection(new Action<Connection>() {
            public void apply(Connection connection) {
                if (db.getCurrent(connection) == 0)
                    System.exit(1);
            }
        });

        //jobs
        Threads threads = new Threads();
        threads.add(new FileChecker(), 6 * MINUTE);
        threads.add(new FileSystem(), 12 * MINUTE);
        threads.add(new NewMailService(), 30 * SECOND);
        threads.add(new PasswordReset(), 5 * MINUTE);
        threads.add(new NotificationAge(), HOUR);
        threads.add(new Notify(), SECOND);
        threads.add(new HashMapClean(), HOUR);
        threads.add(new ProfileUpdate(), 30 * MINUTE);
        threads.add(new RegisterClean(), 30 * MINUTE);


        io.mth.foil.j.Config config = c.compound(
                c.servlet("/agpro", "/*", new AgproServlet()),
                c.servlet( "/client/documents", "/*", new FileServlet()),
                c.servlet( "", "/*", new FileBasicWebServlet()),
                c.servlet( "/client", "/*", new FileWebServlet())
        );

        Foil foil = foils.nu("agpro", 10080, config);
        try {
            foil.run();
        } finally {
            threads.stop();
        }
    }
}