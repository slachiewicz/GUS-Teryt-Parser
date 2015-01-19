package net.kados.gtp.app.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import net.kados.gtp.core.Database.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class Commune implements TableFillable
{

    @Autowired
    private Query query;

    @Override
    public boolean fillTable(ArrayList<HashMap<String, String>> communes) throws SQLException 
    {
        int sizeCount           = 0;
        Connection connection   = query.getConn();
        PreparedStatement ps    = connection.prepareStatement(
                "INSERT IGNORE INTO "
                + "location_commune(idDistrict, idProvince, idCountry, tidCommune, tidDistrict, tidProvince, name, type) "
                + "VALUES ((SELECT id FROM location_district WHERE `tidProvince` = ? AND `tidDistrict` = ?), (SELECT id FROM location_province WHERE `tidProvince` = ?), ?, ?, ?, ?, ?, ?);");

        connection.setAutoCommit(false);
        
        for(HashMap<String, String> hm : communes)
        {
            ps.setString(1, hm.get("WOJ"));
            ps.setString(2, hm.get("POW"));
            ps.setString(3, hm.get("WOJ"));
            ps.setInt(4, 167);
            ps.setString(5, hm.get("GMI"));
            ps.setString(6, hm.get("POW"));
            ps.setString(7, hm.get("WOJ"));
            ps.setString(8, hm.get("NAZWA"));
            ps.setString(9, hm.get("NAZDOD"));
            ps.addBatch();

            if(++sizeCount % 1000 == 0)
            {
                ps.executeBatch();
            }
        }

        ps.executeBatch();
        connection.commit();
        connection.setAutoCommit(true);
        ps.close();

        return (sizeCount == communes.size());
    }
    
    @Override
    public int getCount() throws SQLException
    {
        ResultSet rs = query.getConn()
                .createStatement()
                .executeQuery("SELECT count(id) as count FROM location_commune");
        
        return rs.next() ? rs.getInt("count") : null;
    }
}
