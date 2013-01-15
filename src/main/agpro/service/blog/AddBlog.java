package agpro.service.blog;

import agpro.data.blog.Blog;
import agpro.data.core.Function;
import agpro.data.core.Function2;
import agpro.data.core.Status;
import agpro.db.EdgePreparedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static agpro.tool.Generators.generatorAge;
import static agpro.tool.Init.statement;
import static agpro.tool.Validations.checkrownu;

public class AddBlog implements Function2<Connection,Blog,Status> {
    public Status apply(Connection connection, final Blog data) {
        data.timestamp = generatorAge();

        String sqlInsert = "INSERT INTO \"blog\"( title, content, timestamp) VALUES (?, ?, ?)";
        return statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1, data.title);
                q.setString(2, data.content);
                q.setLong(3, data.timestamp);
                return checkrownu(q.executeUpdate());
            }
        });

    }
}
