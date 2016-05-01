package jclassdesigner.controller;

import saf.AppTemplate;
import saf.ui.AppGUI;
import saf.ui.AppMessageDialogSingleton;
import jclassdesigner.data.Rectangles;
import jclassdesigner.data.DataManager;
import jclassdesigner.gui.Workspace;
import java.util.ArrayList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * @author Daniel Peterson
 */
public class PageEditController 
{

    static final String DEF_CLASS_NAME = "Insert_Class_Name";
    static final String DEF_PACKAGE_NAME = "Insert_Package_Name";
    static final String DUPLICATE_TITLE = "Warning";
    static final String DUPLICATE_CLASS_MESSAGE = "There are currently two classes with the same name in the same package";

    ArrayList<Rectangles> classes;
    AppTemplate app;
    AppGUI gui;
    boolean selecting;
    boolean classAdding;
    double xPos;
    double yPos;
    double startX;
    double startY;
    
    public PageEditController(AppTemplate initApp) 
    {
        classes = new ArrayList<>();
        app = initApp;
        gui = app.getGUI();
        selecting = false;        
    }

    public ArrayList<Rectangles> getClasses() 
    {
        return classes;
    }

    public void setClassAdding(boolean adding) 
    {
        classAdding = adding;
    }

    public void addClassRequestHandler() 
    {
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
        
        workspace.getParentNameComboBox().getItems().addAll(DEF_CLASS_NAME);
        dataManager.getNodes().add(classRect);
        workspace.getInnerPane().getChildren().add(classRect);
        dragRequestHandler((Rectangles) classRect);
    }

    public void selectRequestHandler() 
    {
        classAdding = false;
        gui.getSelectButton().setDisable(false);
        
        Scene scene = gui.getPrimaryScene();
        scene.setCursor(Cursor.DEFAULT);
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
    }

    public void dragRequestHandler(Pane box) 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Rectangles classBox = (Rectangles) box;
        
        classBox.setOnMousePressed(pressed -> {
            if (!classAdding) 
            {
                //classAdding = false;
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

    public void classNameHandler(String classNameText) 
    {
        classes.stream().filter((movableClass) -> (movableClass.isSelected())).filter((movableClass) -> (!isClassSame(movableClass, classNameText))).forEach((movableClass) -> {
            movableClass.setClassName(classNameText);
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
