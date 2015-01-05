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
import net.kados.gtp.app.repositories.Commune;
import net.kados.gtp.app.repositories.District;
import net.kados.gtp.app.repositories.Province;
import org.springframework.beans.factory.annotation.Autowired;

public class Units extends ParserDriver
{
    @Autowired
    private ParserMessages parserMessages;

    @Resource(name="bfis")
    private LinkedHashMap <String, FileInfo> bfis;

    @Autowired
    private Province province;

    @Autowired
    private District district;

    @Autowired
    private Commune commune;

    //ekstraktor STAX
    XMLInputFactory factory;
    XMLStreamReader readerRaw;
    XMLStreamReader reader;

    //dane ekstrakcji
    ArrayList<HashMap<String, String>> provinces  = new ArrayList<>();
    ArrayList<HashMap<String, String>> districts  = new ArrayList<>();
    ArrayList<HashMap<String, String>> communes   = new ArrayList<>();

    //parsing tmp
    HashMap currentRow;
    String tagContent;
    String currentColType;

    @Override
    public void extract() throws XMLStreamException, FileNotFoundException
    {
        FileInfo terc = this.bfis.get(Parser.Types.TERC.toString());

        this.factory     = XMLInputFactory.newInstance();
        this.readerRaw   = factory.createXMLStreamReader(new BufferedInputStream(new FileInputStream(terc.getFile())));
        this.reader      = factory.createFilteredReader(readerRaw, this.newLineFilter);

        while (reader.hasNext())
        {
            this.parser.sendProgress(reader.getLocation().getLineNumber(), terc.getLines());

            switch (reader.next())
            {
                case XMLStreamConstants.START_ELEMENT:
                    this.newElement(reader.getLocalName());
                    break;

                case XMLStreamConstants.CHARACTERS:
                    tagContent = reader.getText();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    this.endElement(reader.getLocalName());
                    break;
            }

            parserMessages.sendProgress(reader.getLocation().getLineNumber(), terc.getLines());
        }

        try
        {
            province.fillTable(provinces);
            district.fillTable(districts);
            commune.fillTable(communes);
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
                this.appendData(currentRow.get("NAZDOD").toString());
                break;

            case "col":
                currentRow.put(currentColType, tagContent);
                break;
        }
    }

    private void appendData(String type)
    {
        switch(type)
        {
            case "województwo":
                provinces.add(currentRow);
                break;

            case "powiat":
            case "miasto na prawach powiatu":
            case "miasto stołeczne, na prawach powiatu":
                districts.add(currentRow);
                break;

            default:
                communes.add(currentRow);
                break;
        }
    }
}
