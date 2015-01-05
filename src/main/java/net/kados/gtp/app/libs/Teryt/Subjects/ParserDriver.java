package net.kados.gtp.app.libs.Teryt.Subjects;

import java.io.FileNotFoundException;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import net.kados.gtp.app.libs.Teryt.Parser;
import net.kados.gtp.core.Database.Query;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ParserDriver
{
    @Autowired
    Query query;
    
    @Autowired
    Parser parser;

    protected final StreamFilter newLineFilter = (XMLStreamReader streamReader) ->  !streamReader.isWhiteSpace();

    public abstract void extract() throws XMLStreamException, FileNotFoundException;
}
