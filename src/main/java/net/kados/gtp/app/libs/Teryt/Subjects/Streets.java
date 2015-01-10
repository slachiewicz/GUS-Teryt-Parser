/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import net.kados.gtp.app.repositories.Precinct;
import net.kados.gtp.app.repositories.Street;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Mateusz
 */
public class Streets extends ParserDriver
{
    @Autowired
    private ParserMessages parserMessages;

    @Resource(name="bfis")
    private LinkedHashMap <String, FileInfo> bfis;

    @Autowired
    private Street street;
    
    @Autowired
    private Precinct precinct;

    //ekstraktor STAX
    XMLInputFactory factory;
    XMLStreamReader readerRaw;
    XMLStreamReader reader;

    //dane ekstrakcji
    ArrayList<HashMap<String, String>> streets = new ArrayList<>();

    //parsing tmp
    HashMap currentRow;
    String tagContent;
    String currentColType;

    @Override
    public void extract() throws XMLStreamException, FileNotFoundException
    {
        FileInfo ulic = this.bfis.get(Parser.Types.ULIC.toString());

        this.factory     = XMLInputFactory.newInstance();
        this.readerRaw   = factory.createXMLStreamReader(new BufferedInputStream(new FileInputStream(ulic.getFile())));
        this.reader      = factory.createFilteredReader(readerRaw, this.newLineFilter);
        
        while(reader.hasNext()) 
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

            if(streets.size() >= 1000)
            {
                this.save();
            }

            parserMessages.sendProgress(reader.getLocation().getLineNumber(), ulic.getLines());
        }

        this.save();
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

    protected void save()
    {
        try
        {
            this.street.fillTable(streets);
            streets.clear();
        } 
        catch (SQLException ex)
        {
            parserMessages.sendMessage(ex.toString());
        }
    }

    private void endElement(String type)
    {
        switch (type) 
        {
            case "row":
                streets.add(currentRow);
                break;

            case "col":
                currentRow.put(currentColType, tagContent);
                break;
        }
    }
}
