package agpro.http.marshal;

import agpro.data.core.*;
import agpro.data.core.Error;
import agpro.db.Connector;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

public class ReturnMarshaller<B> {
    private final Gson gson = new Gson();
    private final Empty empty = new Empty();
    private final ResultFunction<Connection, B> service;
    private final Connector connector;

    public ReturnMarshaller(ResultFunction<Connection, B> service, Connector connector) {
        this.service = service;
        this.connector = connector;
    }
    
    public void marshal(HttpServletResponse resp) {
        try {
            Result<B> b = connector.withConnection(new Function<Connection, Result<B>>() {
                public Result<B> apply(Connection connection) {
                    return service.apply(connection);
                }
            });
            if (b.status() == Status.OK)
                write(resp, b.value(), HttpServletResponse.SC_OK);
            else if (b.status() == Status.NOT_FOUND)
                write(resp, empty, HttpServletResponse.SC_NOT_FOUND);
            else if (b.status() == Status.NOT_AUTH)
                write(resp, empty, HttpServletResponse.SC_UNAUTHORIZED);
            else if (b.status() == Status.BAD_REQUEST)
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
}
