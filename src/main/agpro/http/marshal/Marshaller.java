package agpro.http.marshal;

import agpro.data.core.Function;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Marshaller<A, B> {
    private final Gson gson = new Gson();
    private final Function<A, B> service;
    private final Class<A> source;

    public Marshaller(Class<A> source, Function<A, B> service) {
        this.source = source;
        this.service = service;
    }

    public void marshal(HttpServletRequest req, HttpServletResponse resp) {
        BufferedReader reader = reader(req);
        A a = gson.fromJson(reader, source);
        B b = service.apply(a);
        String json = gson.toJson(b);
        PrintWriter writer = writer(resp);
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

    private BufferedReader reader(HttpServletRequest req) {
        try {
            return req.getReader();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
