package agpro.http.marshal;

import agpro.data.auth.HasUser;
import agpro.data.core.*;
import agpro.data.core.Error;
import agpro.db.Connector;
import agpro.http.auth.Authenticator;
import agpro.http.auth.CheckId;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

public class ActionMarshaller<A extends HasUser<A>> {
    private final Gson gson = new Gson();
    private final Empty empty = new Empty();
    private final Class<A> source;
    private final Function2<Connection, A, Status> service;
    private final Connector connector;

    public ActionMarshaller(Class<A> source, Function2<Connection, A, Status> service, Connector connector) {
        this.source = source;
        this.service = service;
        this.connector = connector;
    }

    public void marshal(HttpServletRequest req, HttpServletResponse resp) {
        final A a = read(req);
        final CheckId checkId = new CheckId(req);

        try {
            Status b = connector.withConnection(new Function<Connection, Status>() {
                public Status apply(Connection connection) {
                    if (checkId.check()) {
                        Authenticator<A, Integer> authenticator = new Authenticator<A, Integer>( a, checkId.getId());
                        if (authenticator.auth())
                            return service.apply(connection, a);
                        else
                            return Status.NOT_FOUND;
                    } else {
                        return Status.NOT_FOUND;
                    }
                }
            });
            if (b == Status.OK)
                write(resp, empty, HttpServletResponse.SC_OK);
            else if (b == Status.NOT_FOUND)
                write(resp, empty, HttpServletResponse.SC_NOT_FOUND);
            else if (b == Status.NOT_AUTH)
                write(resp, empty, HttpServletResponse.SC_UNAUTHORIZED);
            else if (b == Status.BAD_REQUEST)
                write(resp, empty, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            Error error = new Error(e.getClass().getSimpleName(), e.getMessage());
            write(resp, error, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void write(HttpServletResponse resp, Object o, int status) {
        String json = gson.toJson(o);
        PrintWriter writer = writer(resp);
        resp.setStatus(status);
        resp.setContentType("text/json");
        writer.println(json);
    }

    private PrintWriter writer(HttpServletResponse resp) {
        try {
            return resp.getWriter();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private A read(HttpServletRequest req) {
        BufferedReader reader = reader(req);
        return gson.fromJson(reader, source);
    }

    private BufferedReader reader(HttpServletRequest req) {
        try {
            return req.getReader();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
