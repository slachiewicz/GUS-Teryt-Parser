package net.kados.gtp.app.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import net.kados.gtp.core.Database.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class Province implements TableFillable
{
    @Autowired
    private Query query;
   
    @Override
    public boolean fillTable(ArrayList<HashMap<String, String>> provinces) throws SQLException
    {
        int sizeCount        = 0;
        PreparedStatement ps = query.getConn().prepareStatement(
                "INSERT IGNORE INTO "
                + "location_province(idCountry, tidProvince, name) "
                + "VALUES (?, ?, ?);");

        for(HashMap<String, String> hm : provinces)
        {
            ps.setInt(1, 167);
            ps.setString(2, hm.get("WOJ"));
            ps.setString(3, hm.get("NAZWA"));
            ps.addBatch();

            if(++sizeCount % 1000 == 0)
            {
                ps.executeBatch();
            }
        }

        ps.executeBatch();
        ps.close();

        return (sizeCount == provinces.size());
    }
    
    @Override
    public int getCount() throws SQLException
    {
        ResultSet rs = query.getConn()
                .createStatement()
                .executeQuery("SELECT count(id) as count FROM location_province");
        
        return rs.next() ? rs.getInt("count") : null;
    }
}
