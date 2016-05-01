package jclassdesigner.data;

import saf.AppTemplate;
import saf.components.AppDataComponent;
import jclassdesigner.gui.Workspace;
import java.util.ArrayList;
import javafx.scene.Node;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @author Daniel Peterson
 * @version 1.0
 */
public class DataManager implements AppDataComponent 
{

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    ArrayList<Node> nodes;

    /**
     * This constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     * @throws java.lang.Exception
     */
    public DataManager(AppTemplate initApp) throws Exception 
    {
        // KEEP THE APP FOR LATER
        app = initApp;
        nodes = new ArrayList<>();
    }

    public DataManager() 
    {
        nodes = new ArrayList<>();
    }

    public void setNodes(ArrayList<Node> nodes) 
    {
        this.nodes = nodes;
    }

    public ArrayList<Node> getNodes() 
    {
        return nodes;
    }

    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void reset() 
    {
        nodes.clear();
        if (app != null) 
        {
            Workspace workspace = (Workspace) app.getWorkspaceComponent();
            workspace.getInnerPane().getChildren().clear();
            workspace.getPageEditController().getClasses().clear();
        }
    }
}
