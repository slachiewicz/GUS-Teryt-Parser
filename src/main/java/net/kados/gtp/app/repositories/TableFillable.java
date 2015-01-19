package net.kados.gtp.app.repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public interface TableFillable
{
    abstract public boolean fillTable(ArrayList<HashMap<String, String>> communes) throws SQLException;
    
    abstract public int getCount() throws SQLException;
}
