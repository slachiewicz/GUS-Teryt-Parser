package net.kados.gtp.app.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import net.kados.gtp.core.Database.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class District
{
    @Autowired
    private Query query;
   
    public boolean fillTable(ArrayList<HashMap<String, String>> districts) throws SQLException 
    {
        int sizeCount        = 0;
        PreparedStatement ps = query.getConnection().prepareStatement(""
                + "INSERT IGNORE INTO "
                + "location_district(idProvince, idCountry, tidDistrict, tidProvince, name, type) "
                + "VALUES ((SELECT id FROM location_province WHERE `tidProvince` = ?), ?, ?, ?, ?, ?);");

        for(HashMap<String, String> hm : districts)
        {
            ps.setString(1, hm.get("WOJ"));
            ps.setInt(2, 167);
            ps.setString(3, hm.get("POW"));
            ps.setString(4, hm.get("WOJ"));
            ps.setString(5, hm.get("NAZWA"));
            ps.setString(6, hm.get("NAZDOD"));
            ps.addBatch();

            if(++sizeCount % 1000 == 0)
            {
                ps.executeBatch();
            }
        }

        ps.executeBatch();
        ps.close();

        return (sizeCount == districts.size());
    }
}