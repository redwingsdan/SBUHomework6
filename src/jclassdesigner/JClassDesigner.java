package jclassdesigner;

import java.util.Locale;
import saf.components.AppComponentsBuilder;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import jclassdesigner.data.DataManager;
import jclassdesigner.file.FileManager;
import jclassdesigner.gui.Workspace;
import static javafx.application.Application.launch;

/**
 * @author Daniel Peterson
 * @version 1.0
 */
public class JClassDesigner extends AppTemplate {
    
    @Override
    public AppComponentsBuilder makeAppBuilderHook() {
	return new AppComponentsBuilder() {
	    @Override
	    public AppDataComponent buildDataComponent() throws Exception {
		return new DataManager(JClassDesigner.this);
	    }
	    @Override
	    public AppFileComponent buildFileComponent() throws Exception {
		return new FileManager();
	    }
	    @Override
	    public AppWorkspaceComponent buildWorkspaceComponent() throws Exception {
		return new Workspace(JClassDesigner.this);
	    }
	};
    }
    
    public static void main(String[] args) {
	Locale.setDefault(Locale.US);
	launch(args);
    }
}
