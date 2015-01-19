package net.kados.gtp.app.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import net.kados.gtp.core.Database.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class Precinct implements TableFillable
{
    @Autowired
    private Query query;

    @Override
    public boolean fillTable(ArrayList<HashMap<String, String>> precincts) throws SQLException
    {
        int sizeCount        = 0;
        PreparedStatement ps = query.getConn().prepareStatement(
                "INSERT IGNORE INTO "
                + "location_precinct(idCity, idCommune, idDistrict, idProvince, idCountry, tidPrecinct, tidCity, tidCommune, tidDistrict, tidProvince, name) "
                + "VALUES ((SELECT id FROM location_city WHERE `tidCity` = ?),"
                        + "(SELECT id FROM location_commune WHERE `tidProvince` = ? AND `tidDistrict` = ? AND `tidCommune` = ?),"
                        + "(SELECT id FROM location_district WHERE `tidProvince` = ? AND `tidDistrict` = ? LIMIT 1),"
                        + "(SELECT id FROM location_province WHERE `tidProvince` = ?),"
                        + "?, ?, ?, ?, ?, ?, ?);");
        
        query.getConn().setAutoCommit(false);

        for(HashMap<String, String> hm : precincts)
        {
            ps.setString(1, hm.get("SYMPOD"));

            ps.setString(2, hm.get("WOJ"));
            ps.setString(3, hm.get("POW"));
            ps.setString(4, hm.get("GMI"));

            ps.setString(5, hm.get("WOJ"));
            ps.setString(6, hm.get("POW"));

            ps.setString(7, hm.get("WOJ"));

            ps.setInt(8, 167);
            ps.setString(9, hm.get("SYM"));
            ps.setString(10, hm.get("SYMPOD"));
            ps.setString(11, hm.get("GMI"));
            ps.setString(12, hm.get("POW"));
            ps.setString(13, hm.get("WOJ"));
            ps.setString(14, hm.get("NAZWA"));
            ps.addBatch();

            if(++sizeCount % 1000 == 0)
            {
                ps.executeBatch();
                query.getConn().commit();
            }
        }

        ps.executeBatch();
        query.getConn().commit();
        query.getConn().setAutoCommit(true);
        ps.close();

        return (sizeCount == precincts.size());
    }

    @Override
    public int getCount() throws SQLException
    {
        ResultSet rs = query.getConn()
                .createStatement()
                .executeQuery("SELECT count(id) as count FROM location_precinct");
        
        return rs.next() ? rs.getInt("count") : null;
    }
}
