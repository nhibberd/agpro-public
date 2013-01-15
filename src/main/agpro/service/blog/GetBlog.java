package agpro.service.blog;

import agpro.data.blog.Blog;
import agpro.data.core.Function;
import agpro.data.core.Result;
import agpro.data.core.ResultFunction;
import agpro.db.EdgePreparedStatement;
import agpro.db.EdgeResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static agpro.tool.Init.statement;

public class GetBlog implements ResultFunction<Connection,List<Blog>> {
    public Result<List<Blog>> apply(Connection connection) {
        String sqlGet = "SELECT * FROM \"blog\"";
        return statement.withStatement(connection, sqlGet, new Function<PreparedStatement, Result<List<Blog>>>() {
            public Result<List<Blog>> apply(PreparedStatement preparedStatement) {
                List<Blog> d = new ArrayList<Blog>();

                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                EdgeResultSet z = new EdgeResultSet(q);
                while (z.next()) {
                    d.add(new Blog(z.getString(1), z.getString(2), z.getLong(3)));
                }
                if (d.size() >= 1)
                    return Result.ok(d);
                else
                    return Result.notfound();
            }
        });
    }
}
