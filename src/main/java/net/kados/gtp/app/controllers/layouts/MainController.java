package net.kados.gtp.app.controllers.layouts;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import net.kados.gtp.core.SceneManager.Interfaces.SceneManagerShowable;
import net.kados.gtp.core.SceneManager.SceneManager;

import org.springframework.beans.factory.annotation.Autowired;

public class MainController implements Initializable, SceneManagerShowable
{
    @Autowired private SceneManager sceneManager;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        
    }
    
    @Override
    public void showed()
    {
        sceneManager.nestPartInScene("form");
    }
}
