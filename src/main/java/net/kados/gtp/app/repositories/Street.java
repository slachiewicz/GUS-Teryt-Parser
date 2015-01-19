package net.kados.gtp.app.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import net.kados.gtp.core.Database.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class Street implements TableFillable
{
    @Autowired
    private Query query;

    @Override
    public boolean fillTable(ArrayList<HashMap<String, String>> streets) throws SQLException 
    {
        int sizeCount           = 0;
        Connection connection   = query.getConn();
        PreparedStatement ps    = connection.prepareStatement(
                "INSERT IGNORE INTO "
                + "location_street(idCity, idCommune, idDistrict, idProvince, idCountry, tidStreet, tidCity, tidCommune, tidDistrict, tidProvince, name, type) "
                + "VALUES ((SELECT id FROM location_city WHERE `tidCity` = ?),"
                        + "(SELECT id FROM location_commune WHERE `tidProvince` = ? AND `tidDistrict` = ? AND `tidCommune` = ?),"
                        + "(SELECT id FROM location_district WHERE `tidProvince` = ? AND `tidDistrict` = ?),"
                        + "(SELECT id FROM location_province WHERE `tidProvince` = ?),"
                        + "?, ?, ?, ?, ?, ?, ?, ?);");
        String name;
        
        connection.setAutoCommit(false);

        for(HashMap<String, String> hm : streets)
        {
            if(hm.get("NAZWA_2").equals(hm.get("NAZWA_1")))
            {
                name = hm.get("NAZWA_2");
            }
            else
            {
                name = String.format("%s %s", (String) hm.get("NAZWA_2"), (String) hm.get("NAZWA_1"));
            }

            ps.setString(1, hm.get("SYM"));

            ps.setString(2, hm.get("WOJ"));
            ps.setString(3, hm.get("POW"));
            ps.setString(4, hm.get("GMI"));

            ps.setString(5, hm.get("WOJ"));
            ps.setString(6, hm.get("POW"));

            ps.setString(7, hm.get("WOJ"));

            ps.setInt(8, 167);
            ps.setString(9, hm.get("SYM_UL"));
            ps.setString(10, hm.get("SYM"));
            ps.setString(11, hm.get("GMI"));
            ps.setString(12, hm.get("POW"));
            ps.setString(13, hm.get("WOJ"));
            ps.setString(14, name );
            ps.setString(15, hm.get("CECHA"));
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

        return (sizeCount == streets.size());
    }
    
    @Override
    public int getCount() throws SQLException
    {
        ResultSet rs = query.getConn()
                .createStatement()
                .executeQuery("SELECT count(id) as count FROM location_street");
        
        return rs.next() ? rs.getInt("count") : null;
    }
}
