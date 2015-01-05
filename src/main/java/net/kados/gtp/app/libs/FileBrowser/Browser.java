package net.kados.gtp.app.libs.FileBrowser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import net.kados.gtp.core.SceneManager.SceneManager;
import org.controlsfx.dialog.Dialogs;
import org.springframework.beans.factory.annotation.Autowired;

public class Browser
{
    @Autowired
    private SceneManager man;
    
    private File currentFile;

    public FileInfo promptForFile(String text, String pattern, String title)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new ExtensionFilter(text, pattern));
        
        String head = null;
        String mess = null;
        
        this.currentFile = fileChooser.showOpenDialog(man.getStageCurrent());

        if( currentFile == null )
        {
            head = "Akcja zakończyła się niepowodzeniem!";
            mess = "Upawnij się czy wskazałeś odpowiedni plik.";
        }
        else if( ! currentFile.exists())
        {
            head = "Wskazany plik nie istnieje!";
            mess = "Sprawdz czy wskazany plik nie został przypadkiem usunięty.";    
        }
        else if( ! currentFile.canRead())
        {
            head = "Wskazany plik jest zablokowany!";
            mess = "Sprawdz czy wskazany plik jest udostępniony do odczytu.";    
        }
        
        if( head != null && mess != null )
        {
            Dialogs.create().owner(man.getStageCurrent())
                    .title("Błąd wskazania pliku")
                    .masthead(head)
                    .message(mess)
                    .showInformation();

            return null;
        }
        
        return new FileInfo(currentFile, this.getFilelineNumber(this.currentFile));
    }
 
    private int getFilelineNumber(File file)
    {
        int lineNumber = 0;
        
        try
        {
            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            lnr.skip(Long.MAX_VALUE);
            lineNumber = lnr.getLineNumber();
        }
        catch (IOException ex)
        {
            //
        }
        
        return lineNumber;
    }
}
