package wizut.tpsi.lab9;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BlogRepository {

    @Autowired
    private DataSource dataSource;

    public List<BlogPost> getAllPosts() throws SQLException {
        List<BlogPost> posts = new ArrayList<>();

        String sqlQuery = "select * from blog_post";
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");

                BlogPost currentPost = new BlogPost(id, title, content);
                posts.add(currentPost);
            }
        }
        return posts;
    }

    public void createPost(BlogPost post) throws SQLException {
        String sqlQuery = "insert into blog_post(title, content) values(?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getContent());

            preparedStatement.executeUpdate();
        }
    }

    public void removePost(BlogPost post) throws SQLException {
        String sqlQuery = "delete from blog_post where id=?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, post.getId().toString());

            preparedStatement.executeUpdate();
        }
    }
}

