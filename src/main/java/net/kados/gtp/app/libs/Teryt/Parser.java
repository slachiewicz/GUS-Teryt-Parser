package net.kados.gtp.app.libs.Teryt;

import net.kados.gtp.app.libs.Interfaces.Callable;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.concurrent.Task;
import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;
import net.kados.gtp.app.libs.FileBrowser.FileInfo;
import net.kados.gtp.app.libs.Interfaces.ParserMessages;
import net.kados.gtp.app.libs.Teryt.Subjects.ParserDriver;
import net.kados.gtp.core.SceneManager.SceneManager;
import org.controlsfx.dialog.Dialogs;
import org.springframework.beans.factory.annotation.Autowired;

public class Parser extends Task implements ParserMessages
{
    public enum Types { TERC, SIMC, WMRODZ, ULIC; };

    @Autowired
    private SceneManager sceneManager;

    @Autowired
    private LinkedHashMap <String, ParserDriver> parsers;
    
    @Resource(name="bfis")
    private LinkedHashMap <String, FileInfo> bfis;

    private Callable callebleAtFinish;

    public void setCallebleAtFinish(Callable callebleAtFinish)
    {
        this.callebleAtFinish = callebleAtFinish;
    }

    @Override
    public void sendMessage(String text)
    {
        System.out.println(text);
        updateMessage(text);
    }

    @Override
    public void sendProgress(double step, double max)
    {
        updateProgress(step, max);
    }

    public void setFile(Types type, FileInfo bfi)
    {
        this.bfis.put(type.toString(), bfi);
    }
    
    @Override
    public Object call()
    {
        parsers.entrySet().stream().forEach((Map.Entry<String, ParserDriver> entrySet) -> {

            if( !entrySet.getKey().equals("WMRODZ") )
            {
                try
                {
                    this.sendMessage("Rozpoczynam ekstrakcję: " + entrySet.getKey());

                    entrySet.getValue().extract();
                }
                catch (XMLStreamException | FileNotFoundException ex)
                {
                    System.out.println("lipen....");
                    Dialogs.create().owner(sceneManager.getStageCurrent()).title("Bład paerserów!").showException(ex);
                }
            }
        });

        this.callebleAtFinish.call();

        this.sendMessage("Kończę ekstrakcję... ");

        return null;
    }

    public boolean isReady()
    {
        return ! bfis.containsValue(null);
    }
}