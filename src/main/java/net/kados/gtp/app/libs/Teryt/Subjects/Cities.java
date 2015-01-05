package net.kados.gtp.app.libs.Teryt.Subjects;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.annotation.Resource;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import net.kados.gtp.app.libs.FileBrowser.FileInfo;
import net.kados.gtp.app.libs.Interfaces.ParserMessages;
import net.kados.gtp.app.libs.Teryt.Parser;
import net.kados.gtp.app.repositories.City;
import net.kados.gtp.app.repositories.Precinct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Mateusz
 */
public class Cities extends ParserDriver
{
    @Autowired
    private ParserMessages parserMessages;
    
    @Resource(name="bfis")
    private LinkedHashMap <String, FileInfo> bfis;

    @Autowired
    private City city;
    
    @Autowired
    private Precinct precinct;

    //ekstraktor STAX
    XMLInputFactory factory;
    XMLStreamReader readerRaw;
    XMLStreamReader reader;

    //dane ekstrakcji
    ArrayList<HashMap<String, String>> cities       = new ArrayList<>();
    ArrayList<HashMap<String, String>> precincts    = new ArrayList<>();

    //parsing tmp
    HashMap currentRow;
    String tagContent;
    String currentColType;

    @Override
    public void extract() throws XMLStreamException, FileNotFoundException
    {
        FileInfo simc   = this.bfis.get(Parser.Types.SIMC.toString());

        this.factory    = XMLInputFactory.newInstance();
        this.readerRaw  = factory.createXMLStreamReader(new BufferedInputStream(new FileInputStream(simc.getFile())));
        this.reader     = factory.createFilteredReader(readerRaw, this.newLineFilter);

        while (reader.hasNext()) 
        {
            int next = reader.next();

            switch (next) 
            {
                case XMLStreamConstants.START_ELEMENT:
                    this.newElement(reader.getLocalName());
                    break;

                case XMLStreamConstants.CHARACTERS:
                    tagContent = reader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    this.endElement(reader.getLocalName());
                    break;
            }
            
            parserMessages.sendProgress(reader.getLocation().getLineNumber(), simc.getLines());
        }

        try
        {
            city.fillTable(cities);
            cities.clear();

            precinct.fillTable(precincts);
            precincts.clear();
        } 
        catch (SQLException ex)
        {
            System.out.println(ex.toString());
        }
    }
    
    private void newElement(String type)
    {
        switch (type)
        {
            case "row":
                currentRow = new HashMap();
                break;

            case "col":
                currentColType = reader.getAttributeValue(0);
                break;
        }
    }

    private void endElement(String type)
    {
        switch (type) 
        {
            case "row":
                this.appendData(currentRow.get("SYM").toString(), currentRow.get("SYMPOD").toString());
                break;

            case "col":
                currentRow.put(currentColType, tagContent);
                break;
        }
    }

    private void appendData(String symbol, String subSymbol)
    {
        if (symbol.equals(subSymbol)) 
        {
            cities.add(currentRow);
        }
        else 
        {
            precincts.add(currentRow);
        }
    }
}