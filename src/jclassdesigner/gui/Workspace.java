package jclassdesigner.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.ALWAYS;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import static jclassdesigner.PropertyType.ADD_ICON;
import static jclassdesigner.PropertyType.MINUS_ICON;
import jclassdesigner.data.MyRectangle;
import jclassdesigner.controller.PageEditController;
import jclassdesigner.data.DataManager;
import jclassdesigner.file.FileManager;
import properties_manager.PropertiesManager;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;

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
    AppTemplate app;

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

        methodsTablePane.setContent(methodsTable);
        methodsTableHBox = new HBox(10);
        methodsTableHBox.getChildren().add(methodsTablePane);

        optionsPaneOrganizer.getChildren().addAll(className, packageName, parentName, variablesHBox, variablesTableHBox, methodsHBox, methodsTableHBox);

        optionsPane.getChildren().addAll(optionsPaneOrganizer);
        optionsPane.setPrefWidth(gui.getPrimaryScene().getWidth());

        bigPane.setCenter(diagramPane);
        bigPane.setRight(optionsPane);
        workspace.getChildren().add(bigPane);

        gui.getAddClassButton().setOnAction(e -> {
            gui.setSelectButtonSelected(false);
            gui.getSelectButton().setDisable(gui.getSelectButtonSelected());
            pageEditController.handleAddClassRequest();
        });

        gui.getSelectButton().setOnAction(e -> {
            gui.setSelectButtonSelected(true);
            gui.getSelectButton().setDisable(gui.getSelectButtonSelected());
            pageEditController.handleSelectRequest();
        });

        classNameTextField.textProperty().addListener((x, y, z) -> {
            pageEditController.handleClassName(classNameTextField.getText());
        });

        packageNameTextField.textProperty().addListener((x, y, z) -> {
            pageEditController.handlePackageName(packageNameTextField.getText());
        });
        
        gui.getCodeButton().setOnAction(e -> {
            FileManager fileManager = (FileManager) (app.getFileComponent());
            DataManager dataManager = (DataManager) (app.getDataComponent());
            String filePath = "./work/java files/src";
            try {
                fileManager.exportData(dataManager, filePath);
            } catch (IOException io) {
            }
        });
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

            DataManager dataManager = (DataManager) app.getDataComponent();
            ArrayList<Node> nodes = dataManager.getNodes();
            if (!nodes.isEmpty()) {
                pageEditController.setIsClassAdding(true);
                nodes.stream().map((node) -> (MyRectangle) node).map((umlclass) -> {
                    pageEditController.getClasses().add(umlclass);
                    return umlclass;
                }).map((umlclass) -> {
                    umlclass.setSelected(false);
                    return umlclass;
                }).map((umlclass) -> {
                    pageEditController.handleDragRequest(umlclass);
                    return umlclass;
                }).forEach((umlclass) -> {
                    innerPane.getChildren().add(umlclass);
                });
            }

            Iterator iter = innerPane.getChildren().iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (obj instanceof MyRectangle) {
                    MyRectangle umlclass = (MyRectangle) obj;
                    umlclass.setStyle("-fx-border-color: black;" + "-fx-border-width: 1px");
                }
                pageEditController.getClasses().stream().forEach((myClass) -> {
                    myClass.setSelected(false);
                });
            }
        } catch (Exception e) {
        }
    }
}
