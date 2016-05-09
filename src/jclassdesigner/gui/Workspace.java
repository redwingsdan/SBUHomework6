package jclassdesigner.gui;

import java.awt.Checkbox;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.ALWAYS;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;
import static jclassdesigner.PropertyType.ADD_ICON;
import static jclassdesigner.PropertyType.MINUS_ICON;
import jclassdesigner.data.Rectangles;
import jclassdesigner.controller.PageEditController;
import jclassdesigner.data.DataManager;
import jclassdesigner.data.Methods;
import jclassdesigner.data.Variables;
import jclassdesigner.file.FileManager;
import properties_manager.PropertiesManager;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import static saf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static saf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static saf.settings.AppPropertyType.WORK_FILE_EXT;
import static saf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;
import static saf.settings.AppStartupConstants.PATH_WORK;
import saf.ui.AppMessageDialogSingleton;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Daniel Peterson
 */
public class Workspace extends AppWorkspaceComponent {

    static final String CLASS_HEADING_LABEL = "heading_label";
    static final String CLASS_SUBHEADING_LABEL = "subheading_label";
    static final String CLASS_EDIT_TOOLBAR_ROW = "row";
    int COUNT = 0;
    int MAXCOUNT = 0;
    boolean flag = false;
    AppTemplate app;
    ObservableList<Object> vars;
    ObservableList<Object> meths;

    AppGUI gui;

    PropertiesManager properties;

    BorderPane bigPane;

    ScrollPane diagramPane;
    Pane innerPane;

    Pane optionsPane;
    VBox optionsPaneOrganizer;

    HBox className;
    Label classNameLabel;
    TextField classNameTextField;

    HBox packageName;
    Label packageNameLabel;
    TextField packageNameTextField;

    HBox parentName;
    Label parentNameLabel;
    ComboBox<String> parentNameComboBox;

    Label variablesLabel;
    HBox variablesHBox;

    ImageView addVariableButton;
    ImageView removeVariableButton;
    HBox minusHBoxVariable;

    TableView<Object> variablesTable;
    ScrollPane variablesTablePane;
    TableColumn<Object, String> variablesNameColumn;
    TableColumn<Object, String> variablesTypeColumn;
    TableColumn<Object, CheckBox> variablesStaticColumn;
    TableColumn<Object, String> variablesAccessColumn;
    HBox variablesTableHBox;
    TextField variablesName;
    TextField variablesType;
    TextField variablesStatic;
    TextField variablesAccess;
    Button addVar;
    

    Label methodsLabel;
    HBox methodsHBox;

    ImageView addMethodButton;
    ImageView removeMethodButton;
    HBox minusHBoxMethod;

    TableView<Object> methodsTable;
    ScrollPane methodsTablePane;
    TableColumn<Object, String> methodsNameColumn;
    TableColumn<Object, String> methodsReturnColumn;
    TableColumn<Object, CheckBox> methodsStaticColumn;
    TableColumn<Object, CheckBox> methodsAbstractColumn;
    TableColumn<Object, String> methodsAccessColumn;
    TableColumn<Object, String> methodsArgs1Column;
    HBox methodsTableHBox;
    TextField methodsName;
    TextField methodsType;
    TextField methodsStatic;
    TextField methodsAccess;
    TextField methodsArgs;
    TextField methodsAbstract;

    Text debugText;

    PageEditController pageEditController;
    

    public Workspace(AppTemplate initApp) {

        app = initApp;
        gui = app.getGUI();
        properties = PropertiesManager.getPropertiesManager();
        workspace = new Pane();
        bigPane = new BorderPane();
        diagramPane = new ScrollPane();
        innerPane = new Pane();
        diagramPane.setContent(innerPane);
        optionsPane = new Pane();
        optionsPaneOrganizer = new VBox();
        className = new HBox(5);
        packageName = new HBox(49);
        parentName = new HBox(68);
        variablesHBox = new HBox(10);
        methodsHBox = new HBox(10);
        pageEditController = new PageEditController(app);
        meths = FXCollections.observableArrayList();
        vars = FXCollections.observableArrayList();

        diagramPane.setHbarPolicy(ALWAYS);
        diagramPane.setVbarPolicy(ALWAYS);
        diagramPane.setPrefViewportHeight(gui.getPrimaryScene().getHeight() * 0.9);
        diagramPane.setPrefViewportWidth(gui.getPrimaryScene().getWidth() * 0.7);

        classNameLabel = new Label("Class Name: ");
        classNameTextField = new TextField();
        classNameTextField.setPromptText("Enter Class Name");
        className.getChildren().addAll(classNameLabel, classNameTextField);

        packageNameLabel = new Label("Package Name: ");
        packageNameTextField = new TextField();
        packageNameTextField.setPromptText("Enter Package Name");
        packageName.getChildren().addAll(packageNameLabel, packageNameTextField);

        parentNameLabel = new Label("Parent Name: ");
        parentNameComboBox = new ComboBox<>();
        parentName.getChildren().addAll(parentNameLabel, parentNameComboBox);

        variablesLabel = new Label("Variables: ");
        addVariableButton = new ImageView(FILE_PROTOCOL + PATH_IMAGES + properties.getProperty(ADD_ICON.toString()));
        removeVariableButton = new ImageView(FILE_PROTOCOL + PATH_IMAGES + properties.getProperty(MINUS_ICON.toString()));
        minusHBoxVariable = new HBox(5);
        minusHBoxVariable.getChildren().add(removeVariableButton);
        minusHBoxVariable.setAlignment(Pos.CENTER);
        variablesHBox.getChildren().addAll(variablesLabel, addVariableButton, minusHBoxVariable);

        variablesTable = new TableView<>();
        variablesTable.setEditable(true);
        variablesTable.setFixedCellSize(24);
        variablesTable.setPlaceholder(new Label(""));

        variablesTablePane = new ScrollPane();
        variablesTablePane.setHbarPolicy(ALWAYS);
        variablesTablePane.setVbarPolicy(ALWAYS);
        variablesTablePane.setPrefViewportWidth(375);
        variablesTablePane.setPrefViewportHeight(200);

        variablesNameColumn = new TableColumn<>("Name");
        variablesNameColumn.setResizable(false);
        variablesNameColumn.prefWidthProperty().bind(variablesTablePane.widthProperty().multiply(0.25));
        variablesTypeColumn = new TableColumn<>("Type");
        variablesTypeColumn.setResizable(false);
        variablesTypeColumn.prefWidthProperty().bind(variablesTablePane.widthProperty().multiply(0.25));
        variablesStaticColumn = new TableColumn<>("Static");
        variablesStaticColumn.setResizable(false);
        variablesStaticColumn.prefWidthProperty().bind(variablesTablePane.widthProperty().multiply(0.25));
        variablesAccessColumn = new TableColumn<>("Access");
        variablesAccessColumn.setResizable(false);
        variablesAccessColumn.prefWidthProperty().bind(variablesTablePane.widthProperty().multiply(0.25));
        variablesTable.getColumns().addAll(variablesNameColumn, variablesTypeColumn, variablesStaticColumn, variablesAccessColumn);
        variablesName = new TextField();
        variablesName.setMaxWidth(200);
        variablesType = new TextField();
        variablesType.setMaxWidth(200);
        variablesStatic = new TextField();
        variablesStatic.setMaxWidth(200);
        variablesAccess = new TextField();
        variablesAccess.setMaxWidth(200);
        addVar = new Button("Add");

        variablesTablePane.setContent(variablesTable);
        variablesTableHBox = new HBox(10);
        variablesTableHBox.getChildren().add(variablesTablePane);

        methodsLabel = new Label("Methods: ");
        addMethodButton = new ImageView(FILE_PROTOCOL + PATH_IMAGES + properties.getProperty(ADD_ICON.toString()));
        removeMethodButton = new ImageView(FILE_PROTOCOL + PATH_IMAGES + properties.getProperty(MINUS_ICON.toString()));
        minusHBoxMethod = new HBox(5);
        minusHBoxMethod.getChildren().add(removeMethodButton);
        minusHBoxMethod.setAlignment(Pos.CENTER);
        methodsHBox.getChildren().addAll(methodsLabel, addMethodButton, minusHBoxMethod);

        methodsTable = new TableView<>();
        methodsTable.setEditable(true);
        methodsTable.setFixedCellSize(24);
        methodsTable.setPlaceholder(new Label(""));

        methodsTablePane = new ScrollPane();
        methodsTablePane.setHbarPolicy(ALWAYS);
        methodsTablePane.setVbarPolicy(ALWAYS);
        methodsTablePane.setPrefViewportWidth(375);
        methodsTablePane.setPrefViewportHeight(200);

        methodsNameColumn = new TableColumn<>("Name");
        methodsNameColumn.setResizable(false);
        methodsNameColumn.prefWidthProperty().bind(methodsTablePane.widthProperty().multiply(0.16));
        methodsReturnColumn = new TableColumn<>("Return");
        methodsReturnColumn.setResizable(false);
        methodsReturnColumn.prefWidthProperty().bind(methodsTablePane.widthProperty().multiply(0.16));
        methodsStaticColumn = new TableColumn<>("Static");
        methodsStaticColumn.setResizable(false);
        methodsStaticColumn.prefWidthProperty().bind(methodsTablePane.widthProperty().multiply(0.16));
        methodsAbstractColumn = new TableColumn<>("Abstract");
        methodsAbstractColumn.setResizable(false);
        methodsAbstractColumn.prefWidthProperty().bind(methodsTablePane.widthProperty().multiply(0.16));
        methodsAccessColumn = new TableColumn<>("Access");
        methodsAccessColumn.setResizable(false);
        methodsAccessColumn.prefWidthProperty().bind(methodsTablePane.widthProperty().multiply(0.16));
        methodsArgs1Column = new TableColumn<>("Args1");
        methodsArgs1Column.setResizable(false);
        methodsArgs1Column.prefWidthProperty().bind(methodsTablePane.widthProperty().multiply(0.16));
        methodsTable.getColumns().addAll(methodsNameColumn, methodsReturnColumn, methodsStaticColumn, methodsAbstractColumn, methodsAccessColumn, methodsArgs1Column);
        methodsName = new TextField();
        methodsName.setMaxWidth(200);
        methodsType = new TextField();
        methodsType.setMaxWidth(200);
        methodsStatic = new TextField();
        methodsStatic.setMaxWidth(200);
        methodsAccess = new TextField();
        methodsAccess.setMaxWidth(200);
        methodsAbstract = new TextField();
        methodsAbstract.setMaxWidth(200);
        methodsArgs = new TextField();
        methodsArgs.setMaxWidth(200);

        methodsTablePane.setContent(methodsTable);
        methodsTableHBox = new HBox(10);
        methodsTableHBox.getChildren().add(methodsTablePane);

        optionsPaneOrganizer.getChildren().addAll(className, packageName, parentName, variablesHBox, variablesTableHBox, variablesName,variablesType, variablesStatic ,variablesAccess, methodsHBox, methodsTableHBox, methodsName, methodsType,methodsStatic, methodsAbstract, methodsAccess);

        optionsPane.getChildren().addAll(optionsPaneOrganizer);
        optionsPane.setPrefWidth(gui.getPrimaryScene().getWidth());

        bigPane.setCenter(diagramPane);
        bigPane.setRight(optionsPane);
        workspace.getChildren().add(bigPane);

        parentNameComboBox.placeholderProperty().addListener((x, y, z) -> {
           try{
                ListCell<String> str = parentNameComboBox.getButtonCell();
                String str2 = str.toString();
                System.out.println(str2);
                }
                catch(Exception e)
                {
       
                }
        });  
        gui.getAddClassButton().setOnAction(e -> {
            gui.setSelectButtonSelected(false);
            gui.getSelectButton().setDisable(gui.getSelectButtonSelected());
            pageEditController.addClassRequestHandler();
        });
        
        gui.getAddInterfaceButton().setOnAction(e -> {
            gui.setSelectButtonSelected(false);
            gui.getSelectButton().setDisable(gui.getSelectButtonSelected());
            pageEditController.addInterfaceRequestHandler();
        });

        gui.getSelectButton().setOnAction(e -> {
            gui.setSelectButtonSelected(true);
            gui.getSelectButton().setDisable(gui.getSelectButtonSelected());
            pageEditController.selectRequestHandler();
        });
        
        gui.getResizeButton().setOnAction(e -> {
            gui.setSelectButtonSelected(true);
            pageEditController.selectRequestHandler2();
        });
        
        gui.getUndoButton().setOnAction(e -> {
            if(COUNT >= 2)
            {
                FileManager fileManager = (FileManager) app.getFileComponent();
                DataManager dataManager = (DataManager) app.getDataComponent();
                try {
                    fileManager.loadData(dataManager, "temp" + (COUNT-1) );
                } catch (IOException ex) {
                    Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex);
                }
                 flag = true;
                COUNT -= 1;
                reloadWorkspace();
            }
        });
        
        gui.getRedoButton().setOnAction(e -> {
            if(COUNT < MAXCOUNT)
            {
                FileManager fileManager = (FileManager) app.getFileComponent();
                DataManager dataManager = (DataManager) app.getDataComponent();
                try {
                    fileManager.loadData(dataManager, "temp" + (COUNT+1) );
                } catch (IOException ex) {
                    Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex);
                }
                 flag = true;
                COUNT += 1;
                reloadWorkspace();
            }
        });
        
        gui.getPhotoButton().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                WritableImage snapshot = diagramPane.snapshot(new SnapshotParameters(), null);
                PropertiesManager props = PropertiesManager.getPropertiesManager();
                        FileChooser fc = new FileChooser();
                        fc.setInitialDirectory(new File(PATH_WORK));
                        fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
                        fc.getExtensionFilters().addAll(
                                new ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)));
                        File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
                try
                  {
                  File file = new File("Photo.png");
                  file = selectedFile;
                  File file2 = new File(file.getPath() + ".png");
                  ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file2);
                  }
                catch(Exception e)
                  {
                      
                  }
            }
        });
        
        gui.getRemoveButton().setOnAction(e -> {
           DataManager dataManager = (DataManager) app.getDataComponent();
           ArrayList<Node> nodes = dataManager.getNodes();
           Rectangles r = (Rectangles) dataManager.getSelected();
           nodes.remove(r);
           reloadWorkspace();
        });
        
        gui.getZoomInButton().setOnAction(e -> {
            diagramPane.setScaleX(diagramPane.getScaleX() * 1.01);
            diagramPane.setScaleY(diagramPane.getScaleY() * 1.01);
        });
        
        gui.getZoomOutButton().setOnAction(e -> {
            diagramPane.setScaleX(diagramPane.getScaleX() / 1.01);
            diagramPane.setScaleY(diagramPane.getScaleY() / 1.01);
        });

        classNameTextField.textProperty().addListener((x, y, z) -> {
            pageEditController.classNameHandler(classNameTextField.getText());
        });

        packageNameTextField.textProperty().addListener((x, y, z) -> {
            pageEditController.packageNameHandler(packageNameTextField.getText());
        });

        EventHandler handler = null;
        handler = new EventHandler() {
            @Override
            public void handle(Event event) {
                //System.out.println("added variable");
                Variables var = pageEditController.addVariableRequestHandler();
                vars.add(var);
            }
        };
        EventHandler handler2 = null;
        handler2 = new EventHandler() {
            @Override
            public void handle(Event event) {
                //System.out.println("removed variable");
                Variables var = pageEditController.removeVariableRequestHandler();
                vars.remove(var);
            }
        };
        EventHandler handler3 = null;
        handler3 = new EventHandler() {
            @Override
            public void handle(Event event) {
               // System.out.println("added method");
                Methods meth = pageEditController.addMethodRequestHandler();
                meths.add(meth);
            }
        };
        EventHandler handler4 = null;
        handler4 = new EventHandler() {
            @Override
            public void handle(Event event) {
                //System.out.println("removed method");
                Methods meth = pageEditController.removeMethodRequestHandler();
                meths.remove(meth);
            }
        };
        addVariableButton.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
        removeVariableButton.addEventHandler(MouseEvent.MOUSE_CLICKED, handler2);
        addMethodButton.addEventHandler(MouseEvent.MOUSE_CLICKED, handler3);
        removeMethodButton.addEventHandler(MouseEvent.MOUSE_CLICKED, handler4);
        
        gui.getCodeButton().setOnAction(e -> {
            FileManager fileManager = (FileManager) (app.getFileComponent());
            DataManager dataManager = (DataManager) (app.getDataComponent());
            String filePath = "./work/java files/src";
            try {
                fileManager.exportData(dataManager, filePath);
            } catch (IOException io) {
            }
        });
        
        variablesNameColumn.setCellValueFactory(
                            new PropertyValueFactory<>("Name")
                        );
                variablesTypeColumn.setCellValueFactory(
                            new PropertyValueFactory<>("Types")
                        );
                variablesStaticColumn.setCellValueFactory(
                            new PropertyValueFactory<>("Static")
                        );
                variablesAccessColumn.setCellValueFactory(
                            new PropertyValueFactory<>("Access")
                        );
                variablesTable.setItems((ObservableList<Object>) vars);
                variablesTable.setEditable(true);
        methodsNameColumn.setCellValueFactory(
                            new PropertyValueFactory<>("Name")
                        );
                methodsReturnColumn.setCellValueFactory(
                            new PropertyValueFactory<>("Type")
                        );
                methodsStaticColumn.setCellValueFactory(
                            new PropertyValueFactory<>("Statics")
                        );
                methodsAccessColumn.setCellValueFactory(
                            new PropertyValueFactory<>("Access")
                        );
                methodsAbstractColumn.setCellValueFactory(
                            new PropertyValueFactory<>("Abstract")
                        );
                methodsArgs1Column.setCellValueFactory(
                            new PropertyValueFactory<>("Args")
                        );
                methodsTable.setItems((ObservableList<Object>) meths);
                methodsTable.setEditable(true);
    }

    public TextField getVarName()
    {
        return variablesName;
    }
    
    public TextField getVarType()
    {
        return variablesType;
    }
    
    public TextField getVarStatic()
    {
        return variablesStatic;
    }
    
    public TextField getVarAccess()
    {
        return variablesAccess;
    }
    
    public TextField getMethName()
    {
        return methodsName;
    }
    
    public TextField getMethType()
    {
        return methodsType;
    }
    
    public TextField getMethStatic()
    {
        return methodsStatic;
    }
    
    public TextField getMethAccess()
    {
        return methodsAccess;
    }
    
    public TextField getMethAbstract()
    {
        return methodsAbstract;
    }
    
    public TextField getMethArgs()
    {
        return methodsArgs;
    }
    
    public TableView getVarTable()
    {
      return variablesTable;   
    }
    
    public TableView getMethTable()
    {
      return methodsTable;   
    }
    
    public void setDebugText(String text) {
        debugText.setText(text);
    }

    public Pane getInnerPane() {
        return innerPane;
    }

    public TextField getPackageNameTextField() {
        return packageNameTextField;
    }

    public TextField getClassNameTextField() {
        return classNameTextField;
    }

    public ComboBox getParentNameComboBox() {
        return parentNameComboBox;
    }

    public PageEditController getPageEditController() {
        return pageEditController;
    }

    public void setImage(ButtonBase button, String fileName) {
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + fileName;
        Image buttonImage = new Image(imagePath);

        button.setGraphic(new ImageView(buttonImage));
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    @Override
    public void initStyle() {
        diagramPane.getStyleClass().add("scroll_pane");
        optionsPane.getStyleClass().add("options_pane");
        classNameLabel.getStyleClass().add(CLASS_HEADING_LABEL);
        packageNameLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        parentNameLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        variablesLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        methodsLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        classNameLabel.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        packageNameLabel.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        parentNameLabel.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        variablesHBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        variablesTableHBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        methodsHBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        methodsTableHBox.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        classNameTextField.getStyleClass().add("hbox");
        packageNameTextField.getStyleClass().add("hbox2");
        parentNameComboBox.getStyleClass().add("combobox");
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {
        try {
            innerPane.getChildren().clear();
            gui.getPrimaryScene().setCursor(Cursor.DEFAULT);
            getParentNameComboBox().getItems().remove(0, getParentNameComboBox().getItems().size());
            DataManager dataManager = (DataManager) app.getDataComponent();
            ArrayList<Node> nodes = dataManager.getNodes();
            if (!nodes.isEmpty()) {
                pageEditController.setClassAdding(true);
                nodes.stream().map((node) -> (Rectangles) node).map((umlclass) -> {
                    pageEditController.getClasses().add(umlclass);
                    return umlclass;
                }).map((umlclass) -> {
                    umlclass.setSelected(false);
                    return umlclass;
                }).map((umlclass) -> {
                    pageEditController.dragRequestHandler(umlclass);
                    return umlclass;
                }).forEach((umlclass) -> {
                    innerPane.getChildren().add(umlclass);
                    getParentNameComboBox().getItems().addAll(umlclass.getClassName());
                });
            }
           // System.out.println(nodes.size());
            Iterator iter = innerPane.getChildren().iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof Rectangles) {
                    Rectangles umlclass = (Rectangles) obj;
                    umlclass.setStyle("-fx-border-color: black;" + "-fx-border-width: 1px");
                }
                pageEditController.getClasses().stream().forEach((myClass) -> {
                    myClass.setSelected(false);
                });
            }
            FileManager fileManager = (FileManager) app.getFileComponent();
            fileManager.saveData(dataManager, "temp" + COUNT);
            if(flag == false)
            {
            MAXCOUNT = COUNT;
            COUNT += 1;
            flag = false;
            }
            if(COUNT > 1)
            {
                gui.getUndoButton().setDisable(false);
            }
            else
            {
                gui.getUndoButton().setDisable(true);
            }
            if(COUNT < (MAXCOUNT))
            {
                gui.getRedoButton().setDisable(false);
            }
            else
            {
               gui.getRedoButton().setDisable(true); 
            }
            if(nodes.isEmpty())
            {
                gui.getSelectButton().setDisable(true);
                gui.getRemoveButton().setDisable(true);
            }
            else
            {
               gui.getSelectButton().setDisable(false); 
               gui.getRemoveButton().setDisable(false);
            }
            gui.getSaveButton().setDisable(false);
        } catch (Exception e) {
        }
    }

    private void addEventFilter(EventType<MouseEvent> MOUSE_CLICKED, EventHandler<MouseEvent> eventHandler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
