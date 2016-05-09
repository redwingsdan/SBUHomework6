package jclassdesigner.controller;

import java.io.IOException;
import saf.AppTemplate;
import saf.ui.AppGUI;
import saf.ui.AppMessageDialogSingleton;
import jclassdesigner.data.Rectangles;
import jclassdesigner.data.DataManager;
import jclassdesigner.gui.Workspace;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import jclassdesigner.data.Methods;
import jclassdesigner.data.Variables;
import jclassdesigner.file.FileManager;

/**
 * @author Daniel Peterson
 */
public class PageEditController 
{

    static final String DEF_CLASS_NAME = "Insert_Class_Name";
    static final String DEF_PACKAGE_NAME = "Insert_Package_Name";
    static final String DUPLICATE_TITLE = "Warning";
    static final String DUPLICATE_CLASS_MESSAGE = "There are currently two classes with the same name in the same package";

    Rectangles selectedRect;
    ArrayList<Variables> variables;
    ArrayList<Rectangles> classes;
    AppTemplate app;
    AppGUI gui;
    boolean selecting;
    boolean classAdding;
    boolean classResizing;
    double xPos;
    double yPos;
    double startX;
    double startY;
    ObservableList<Object> vars;
    ObservableList<Object> meths;
    
    public PageEditController(AppTemplate initApp) 
    {
        variables = new ArrayList<>();
        classes = new ArrayList<>();
        app = initApp;
        gui = app.getGUI();
        selecting = false;   
        classResizing = false;
    }

    public Rectangles getSelected()
    {
        return selectedRect;
    }
    
    public ArrayList<Rectangles> getClasses() 
    {
        return classes;
    }
    
    public ArrayList<Variables> getVariables() 
    {
        return variables;
    }

    public void setClassAdding(boolean adding) 
    {
        classAdding = adding;
    }
    
    public void setClassResizing(boolean resizing)
    {
        classResizing = resizing;
    }

    public void addClassRequestHandler() 
    {
        classResizing = false;
        classAdding = true;
        gui.getPrimaryScene().setCursor(Cursor.DEFAULT);
        gui.getSelectButton().setDisable(false);
        
        DataManager dataManager = (DataManager) app.getDataComponent();
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        workspace.getPackageNameTextField().clear();
        workspace.getClassNameTextField().clear(); 
        workspace.getPackageNameTextField().setText(DEF_PACKAGE_NAME);
        workspace.getClassNameTextField().setText(DEF_CLASS_NAME);
        
        Label className = new Label(DEF_CLASS_NAME);
        Rectangles classRect = new Rectangles(className, DEF_PACKAGE_NAME);
        classRect.setStyle("-fx-border-width: 5px;" + "-fx-border-color: yellow;");
        classRect.setSelected(true);
        classes.add(classRect);
        
        workspace.getParentNameComboBox().getItems().addAll(classRect.getClassName());
        dataManager.getNodes().add(classRect);
        workspace.getInnerPane().getChildren().add(classRect);
        dragRequestHandler((Rectangles) classRect);

        workspace.reloadWorkspace();
    }
    
    public void addInterfaceRequestHandler() 
    {
        classResizing = false;
        classAdding = true;
        gui.getPrimaryScene().setCursor(Cursor.DEFAULT);
        gui.getSelectButton().setDisable(false);
        
        DataManager dataManager = (DataManager) app.getDataComponent();
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        workspace.getPackageNameTextField().clear();
        workspace.getClassNameTextField().clear(); 
        workspace.getPackageNameTextField().setText(DEF_PACKAGE_NAME);
        workspace.getClassNameTextField().setText(DEF_CLASS_NAME);
        
        Label className = new Label(DEF_CLASS_NAME);
        Rectangles classRect = new Rectangles(className, DEF_PACKAGE_NAME);
        classRect.setStyle("-fx-border-width: 5px;" + "-fx-border-color: yellow;");
        classRect.setSelected(true);
        classRect.setIsInterface(true);
        classes.add(classRect);
        
        workspace.getParentNameComboBox().getItems().addAll(classRect.getClassName());
        dataManager.getNodes().add(classRect);
        workspace.getInnerPane().getChildren().add(classRect);
        dragRequestHandler((Rectangles) classRect);
        
        workspace.reloadWorkspace();
    }
    
    public Variables addVariableRequestHandler() 
    {
        classResizing = false;
        classAdding = true;
        gui.getPrimaryScene().setCursor(Cursor.DEFAULT);
        gui.getSelectButton().setDisable(false);
        DataManager dataManager = (DataManager) app.getDataComponent();
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
       // workspace.reloadWorkspace();
        
        TextField name1 = workspace.getVarName();
        String name = name1.getText();
        if(name == null)
        {
           name = "TestName";
        }
        TextField retVal1 = workspace.getVarType();
        String retVal = retVal1.getText();
        if(retVal == "")
        {
            retVal = "int";
        }
        TextField access1 = workspace.getVarAccess(); 
        String access = access1.getText();
        if(access == "")
        {
            access = "public";
        }
        TextField isStatic1 = workspace.getVarStatic();
        String isStatic2;
        isStatic2 = isStatic1.getText();
        if(isStatic2 == "")
        {
            isStatic2 = "true";
        }
        Boolean isStatic = false;
        if(isStatic2 == "true")
        {
           isStatic = true; 
        }
        Variables var = new Variables(name, retVal, access, isStatic);
        variables.add(var);
        vars.add(var);
        selectedRect.getVariables().add(var);       
        workspace.reloadWorkspace();
        return var;
    }
    
    public Variables removeVariableRequestHandler()
    {
        classResizing = false;
        gui.getPrimaryScene().setCursor(Cursor.DEFAULT);
        gui.getSelectButton().setDisable(false);
        DataManager dataManager = (DataManager) app.getDataComponent();
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Variables var = selectedRect.getVariables().get(selectedRect.getVariables().size()-1);
        selectedRect.getVariables().remove(selectedRect.getVariables().size()-1);
        workspace.reloadWorkspace();
        return var;
    }
    
    public Methods removeMethodRequestHandler()
    {
        classResizing = false;
        gui.getPrimaryScene().setCursor(Cursor.DEFAULT);
        gui.getSelectButton().setDisable(false);
        DataManager dataManager = (DataManager) app.getDataComponent();
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Methods meth = selectedRect.getMethods().get(selectedRect.getMethods().size()-1);
        selectedRect.getMethods().remove(selectedRect.getMethods().size()-1);

        workspace.reloadWorkspace();
        return meth;
    }
    
    public Methods addMethodRequestHandler() 
    {
        classResizing = false;
        classAdding = true;
        gui.getPrimaryScene().setCursor(Cursor.DEFAULT);
        gui.getSelectButton().setDisable(false);
        DataManager dataManager = (DataManager) app.getDataComponent();
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
       // workspace.reloadWorkspace();
        TextField name1 = workspace.getMethName();
        String name = name1.getText();
        if(name == null)
        {
           name = "TestName";
        }
        TextField retVal1 = workspace.getMethType();
        String retVal = retVal1.getText();
        if(retVal == "")
        {
            retVal = "int";
        }
        TextField access1 = workspace.getMethAccess(); 
        String access = access1.getText();
        if(access == "")
        {
            access = "public";
        }
        TextField isStatic1 = workspace.getMethStatic();
        String isStatic2;
        isStatic2 = isStatic1.getText();
        if(isStatic2 == "")
        {
            isStatic2 = "true";
        }
        Boolean isStatic = false;
        if(isStatic2 == "true")
        {
           isStatic = true; 
        }
        TextField isAbstract1 = workspace.getMethAbstract();
        String isAbstract2;
        isAbstract2 = isAbstract1.getText();
        if(isAbstract2 == "")
        {
            isAbstract2 = "true";
        }
        Boolean isAbstract = false;
        if(isAbstract2 == "true")
        {
           isStatic = true; 
        }
        TextField args1 = workspace.getMethArgs();
        String args = args1.getText();

        Methods meth = new Methods(name, retVal, isStatic, isAbstract, access);
        meths.add(meth);
        selectedRect.getMethods().add(meth);       
        workspace.reloadWorkspace();
        return meth;
    }
    
    public void undoRequestHandler(int COUNT) throws IOException
    {
        FileManager fileManager = (FileManager) app.getFileComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        fileManager.loadData(dataManager, "temp" + (COUNT-1) );
    }
    
    public void redoRequestHandler(int COUNT) throws IOException
    {
        FileManager fileManager = (FileManager) app.getFileComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        fileManager.loadData(dataManager, "temp" + (COUNT+1) );
    }

    public void selectRequestHandler() 
    {
        classResizing = false;
        classAdding = false;
        gui.getSelectButton().setDisable(false);
        
        Scene scene = gui.getPrimaryScene();
        scene.setCursor(Cursor.DEFAULT);
       // Workspace workspace = (Workspace) app.getWorkspaceComponent();
      //  workspace.reloadWorkspace();
    }
    
    public void selectRequestHandler2() 
    {
        classAdding = false;
        classResizing = true;
        Scene scene = gui.getPrimaryScene();
        scene.setCursor(Cursor.DEFAULT);
    }

    public void dragRequestHandler(Pane box) 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Rectangles classBox = (Rectangles) box;
        double difX = 0.0;
        double difY = 0.0;
        
        
        if(classResizing == true)
        {
            classBox.setOnMousePressed(pressed -> {
            
                workspace.reloadWorkspace();
                classes.stream().filter((movableClass) -> ((Pane) movableClass == classBox)).map((movableClass) -> {
                    movableClass.setSelected(true);
                    return movableClass;
                }).map((movableClass) -> {
                    movableClass.setStyle("-fx-border-width: 5px;" + "-fx-border-color: yellow;");
                    return movableClass;
                }).map((movableClass) -> {
                    workspace.getClassNameTextField().setText(movableClass.getClassName());
                    return movableClass;
                }).forEach((movableClass) -> {
                    workspace.getPackageNameTextField().setText(movableClass.getPackageName());
                    selectedRect = movableClass;
                    workspace.getMethTable();
                    workspace.getVarTable();
                    meths = FXCollections.observableArrayList();
                    vars = FXCollections.observableArrayList();
                });

            classes.stream().filter((movableClass) -> ((Pane) movableClass == classBox)).filter((movableClass) -> (gui.getPrimaryScene().getCursor().toString().equals("DEFAULT") && movableClass.isSelected())).map((movableClass) -> {
                movableClass.setStyle("-fx-border-width: 5px;" + "-fx-border-color: yellow;");
                return movableClass;
            }).map((e) -> {
                xPos = pressed.getSceneX();
                return e;
            }).map((e) -> {
                yPos = pressed.getSceneY();
                return e;
            }).map((e) -> {
                startX = classBox.getLayoutX();
                return e;
            }).forEach((e) -> {
                startY = classBox.getLayoutY();
            });
        });
            
            classBox.setOnMouseDragged(dragged -> {
            classes.stream().filter((movableClass) -> ((Pane) movableClass == classBox)).filter((movableClass) -> (gui.getPrimaryScene().getCursor().toString().equals("DEFAULT") && movableClass.isSelected())).map((_item) -> dragged.getSceneX() - xPos).map((diffX) -> {
                double diffY = dragged.getSceneY() - yPos;
                startX += diffX;
                startY += diffY;
               classBox.setScaleX(classBox.getScaleX() * (1 + (diffX/10000)) );
               classBox.setScaleY(classBox.getScaleY() * (1 + (diffY/10000)) );
                return diffX;
            }).map((e) -> startX).map((scaledX) -> {
                double scaledY = startY;
                return scaledX;
            }).forEach((e) -> {
                dragged.consume();
            });
        });
        }
        else{
        classBox.setOnMousePressed(pressed -> {
            if (!classAdding) 
            {
                workspace.reloadWorkspace();
                classes.stream().filter((movableClass) -> ((Pane) movableClass == classBox)).map((movableClass) -> {
                    movableClass.setSelected(true);
                    return movableClass;
                }).map((movableClass) -> {
                    movableClass.setStyle("-fx-border-width: 5px;" + "-fx-border-color: yellow;");
                    return movableClass;
                }).map((movableClass) -> {
                    workspace.getClassNameTextField().setText(movableClass.getClassName());
                    return movableClass;
                }).forEach((movableClass) -> {
                    workspace.getPackageNameTextField().setText(movableClass.getPackageName());
                    selectedRect = movableClass;
                    TableView meths1 = workspace.getMethTable();
                    TableView vars1 = workspace.getVarTable();
                    meths = FXCollections.observableArrayList();
                    vars = FXCollections.observableArrayList();
                });
            }

            classes.stream().filter((movableClass) -> ((Pane) movableClass == classBox)).filter((movableClass) -> (gui.getPrimaryScene().getCursor().toString().equals("DEFAULT") && movableClass.isSelected())).map((movableClass) -> {
                movableClass.setStyle("-fx-border-width: 5px;" + "-fx-border-color: yellow;");
                return movableClass;
            }).map((e) -> {
                xPos = pressed.getSceneX();
                return e;
            }).map((e) -> {
                yPos = pressed.getSceneY();
                return e;
            }).map((e) -> {
                startX = classBox.getLayoutX();
                return e;
            }).forEach((e) -> {
                startY = classBox.getLayoutY();
            });
        });

        classBox.setOnMouseDragged(dragged -> {
            classes.stream().filter((movableClass) -> ((Pane) movableClass == classBox)).filter((movableClass) -> (gui.getPrimaryScene().getCursor().toString().equals("DEFAULT") && movableClass.isSelected())).map((_item) -> dragged.getSceneX() - xPos).map((diffX) -> {
                double diffY = dragged.getSceneY() - yPos;
                startX += diffX;
                startY += diffY;
                return diffX;
            }).map((e) -> startX).map((scaledX) -> {
                double scaledY = startY;
                xPos = dragged.getSceneX();
                yPos = dragged.getSceneY();
                classBox.setLayoutX(scaledX);
                classBox.setLayoutY(scaledY);
                return scaledX;
            }).forEach((e) -> {
                dragged.consume();
            });
        });
    }
    }
    public void classNameHandler(String classNameText) 
    {
        classes.stream().filter((movableClass) -> (movableClass.isSelected())).filter((movableClass) -> (!isClassSame(movableClass, classNameText))).forEach((movableClass) -> {
            movableClass.setClassName(classNameText);
            Workspace workspace = (Workspace) app.getWorkspaceComponent();
            workspace.getParentNameComboBox().getItems().addAll(movableClass.getClassName());
        });
    }

    public void packageNameHandler(String packageNameText) 
    {
        classes.stream().filter((movableClass) -> (movableClass.isSelected())).filter((movableClass) -> (!isPackageSame(movableClass, packageNameText))).forEach((movableClass) -> {
            movableClass.setPackageName(packageNameText);
        });
    }

    public boolean isClassSame(Rectangles classRect, String className) 
    {
        for (Rectangles movableClass : classes) 
        {
            if (movableClass != classRect) 
            {
                if (className.equals(movableClass.getClassName())) 
                {
                    if (movableClass.getPackageName().equals(classRect.getPackageName())) 
                    {
                        if (movableClass.getPackageName().equals(DEF_PACKAGE_NAME)) 
                        {
                            if (movableClass.getClassName().equals(DEF_CLASS_NAME)) 
                            {
                            } 
                            else 
                            {
                                AppMessageDialogSingleton.getSingleton().show(DUPLICATE_TITLE, DUPLICATE_CLASS_MESSAGE);
                                return true;
                            }
                        } 
                        else 
                        {
                            AppMessageDialogSingleton.getSingleton().show(DUPLICATE_TITLE, DUPLICATE_CLASS_MESSAGE);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isPackageSame(Rectangles classRect, String packageName) 
    {
        for (Rectangles movableClass : classes) 
        {
            if (movableClass != classRect) 
            {
                if (packageName.equals(movableClass.getPackageName())) 
                {
                    if (movableClass.getClassName().equals(classRect.getClassName())) 
                    {
                        if (movableClass.getPackageName().equals(DEF_PACKAGE_NAME)) 
                        {
                            if (movableClass.getClassName().equals(DEF_CLASS_NAME)) 
                            {
                            } 
                            else 
                            {
                                AppMessageDialogSingleton.getSingleton().show(DUPLICATE_TITLE, DUPLICATE_CLASS_MESSAGE);
                                return true;
                            }
                        } 
                        else 
                        {
                            AppMessageDialogSingleton.getSingleton().show(DUPLICATE_TITLE, DUPLICATE_CLASS_MESSAGE);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
