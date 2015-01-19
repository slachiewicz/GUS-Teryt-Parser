package net.kados.gtp.core.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import net.kados.gtp.core.SceneManager.SceneManager;
import org.springframework.beans.factory.annotation.Autowired;

public class Query
{
    @Autowired
    private SceneManager sceneManager;
    private HashMap<String, String> data;
    
    private Connection conn;
    
    public Query setConnectionData(String host, String port, String base, String login, String pass)
    {
        data = new HashMap<>();
        
        data.put("host", host);
        data.put("port", port);
        data.put("base", base);
        data.put("login", login);
        data.put("pass", pass);
        
        return this;
    }

    public Connection getConn()
    {
        try
        {
            if(this.conn == null || this.conn.isClosed() )
            {
                Class.forName("com.mysql.jdbc.Driver");
                
                this.conn = DriverManager.getConnection(
                        MessageFormat.format("jdbc:mysql://{0}:{1}/{2}?&useUnicode=true&characterEncoding=UTF-8", data.get("host"), data.get("port"), data.get("base")),
                        data.get("login"),
                        data.get("pass")
                );
            }
        } 
        catch (SQLException | ClassNotFoundException ex)
        {
            System.out.println(ex.toString());
        }

        return this.conn;
    }
}
