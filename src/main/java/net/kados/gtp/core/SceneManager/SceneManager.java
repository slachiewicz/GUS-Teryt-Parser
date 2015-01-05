package net.kados.gtp.core.SceneManager;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.PropertyResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.kados.gtp.core.SceneManager.Interfaces.SceneManagerShowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public class SceneManager
{
    @Autowired private ApplicationContext applicationContext;

    private Stage stageCurrent;
    private Scene sceneCurrent;
    private Pane lookup;

    public Stage getStageCurrent()
    {
        return this.stageCurrent;
    }

    public SceneManager setStageCurrent(Stage stageCurrent)
    {
        this.stageCurrent = stageCurrent;
        return this;
    }

    public Scene getSceneCurrent()
    {
        return sceneCurrent;
    }

    public SceneManager setSceneCurrent(Scene SceneCurrent)
    {
        this.sceneCurrent = SceneCurrent;
        return this;
    }

    public void moveToScene(String viewName)
    {
        Resource res = this.applicationContext.getResource(MessageFormat.format("views/layouts/{0}.fxml", viewName));

        try
        {
            FXMLLoader loader = new FXMLLoader(res.getURL());
            loader.setControllerFactory((Class<?> clazz) -> this.applicationContext.getBean(clazz) );

            this.sceneCurrent   = new Scene((Parent) loader.load());
            this.lookup         = (Pane) this.sceneCurrent.lookup("#injectPane");

            this.stageCurrent.setScene(this.sceneCurrent);
            this.stageCurrent.show();

            SceneManagerShowable ctrl       = loader.getController();

            if(ctrl != null)
            {
                ctrl.showed();
            }
        }
        catch (IOException ex)
        {
            System.err.println(" View (Main Scene) don't exist: " + res.toString() + " \n" + ex.getMessage() );
        }
    }

    public void nestPartInScene(String viewName)
    {
        Resource resView = this.applicationContext.getResource(MessageFormat.format("views/modules/{0}.fxml", viewName));
        Resource resLang = this.applicationContext.getResource(MessageFormat.format("views/modules/{0}.properties", viewName));

        try
        {
            FXMLLoader loader   = new FXMLLoader(resView.getURL());
            loader.setResources(new PropertyResourceBundle(resLang.getInputStream()));
            loader.setControllerFactory((Class<?> clazz) -> this.applicationContext.getBean(clazz) );

            ObservableList<Node> children = this.lookup.getChildren();
            
            if(children.size() > 0)
            {
                children.remove(children.size() - 1);
            }
            children.add((Node) loader.load());

            SceneManagerShowable ctrl = loader.getController();

            if(ctrl != null)
            {
                ctrl.showed();
            }
        }
        catch (IOException ex)
        {
            System.err.println(" Part view don't exist: " + resView.toString() + " \n" + ex.getLocalizedMessage() );
        }
    }
}