package agpro.data.blog;

import agpro.data.auth.HasUser;
import agpro.data.auth.ResultUser;

public class Blog implements HasUser<Blog> {
    public String title;
    public String content;
    public Long timestamp;

    public Blog(String title, String content, Long timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public ResultUser<Integer> get() {
        return ResultUser.error();
    }
}
