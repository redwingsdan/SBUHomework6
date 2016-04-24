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
public class PageEditController {

    static final String DEFAULT_CLASS_NAME = "Insert_Class_Name";
    static final String DEFAULT_PACKAGE_NAME = "Insert_Package_Name";
    static final String DUPLICATE_TITLE = "Warning";
    static final String DUPLICATE_CLASS_MESSAGE = "There are currently two classes with the same name in the same package";

    AppTemplate app;

    AppGUI gui;

    boolean selecting;
    boolean isClassAdding;

    ArrayList<Rectangles> classes;
    double posX;
    double posY;
    double startX;
    double startY;
    
    public PageEditController(AppTemplate initApp) {
        app = initApp;
        gui = app.getGUI();
        selecting = false;
        classes = new ArrayList<>();
    }

    public ArrayList<Rectangles> getClasses() {
        return classes;
    }

    public void setIsClassAdding(boolean adding) {
        isClassAdding = adding;
    }

    public void handleAddClassRequest() {
        DataManager dataManager = (DataManager) app.getDataComponent();

        gui.getPrimaryScene().setCursor(Cursor.DEFAULT);
        gui.getSelectButton().setDisable(false);

        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        workspace.getClassNameTextField().clear();
        workspace.getPackageNameTextField().clear();
        workspace.getClassNameTextField().setText(DEFAULT_CLASS_NAME);
        workspace.getPackageNameTextField().setText(DEFAULT_PACKAGE_NAME);

        isClassAdding = true;

        Label className = new Label(DEFAULT_CLASS_NAME);
        Rectangles umlclass = new Rectangles(className, DEFAULT_PACKAGE_NAME);
        umlclass.setSelected(true);
        umlclass.setStyle("-fx-border-color: yellow;" + "-fx-border-width: 5px");
        classes.add(umlclass);

        workspace.getParentNameComboBox().getItems().addAll(DEFAULT_CLASS_NAME);

        dataManager.getNodes().add(umlclass);
        workspace.getInnerPane().getChildren().add(umlclass);

        handleDragRequest((Rectangles) umlclass);
    }

    public void handleSelectRequest() {
        gui.getSelectButton().setDisable(true);
        Scene scene = gui.getPrimaryScene();
        scene.setCursor(Cursor.DEFAULT);

        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        isClassAdding = false;
    }

    public void handleDragRequest(Pane box) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Rectangles theBox = (Rectangles) box;
        theBox.setOnMousePressed(pressed -> {
            if (!isClassAdding) {
                workspace.reloadWorkspace();
                isClassAdding = false;
                classes.stream().filter((myClass) -> ((Pane) myClass == theBox)).map((myClass) -> {
                    myClass.setSelected(true);
                    return myClass;
                }).map((myClass) -> {
                    myClass.setStyle("-fx-border-color: yellow;" + "-fx-border-width: 5px");
                    return myClass;
                }).map((myClass) -> {
                    workspace.getClassNameTextField().setText(myClass.getClassName());
                    return myClass;
                }).forEach((myClass) -> {
                    workspace.getPackageNameTextField().setText(myClass.getPackageName());
                });
            }

            classes.stream().filter((myClass) -> ((Pane) myClass == theBox)).filter((myClass) -> (gui.getPrimaryScene().getCursor().toString().equals("DEFAULT") && myClass.isSelected())).map((myClass) -> {
                myClass.setStyle("-fx-border-color: yellow;" + "-fx-border-width: 5px");
                return myClass;
            }).map((e) -> {
                posX = pressed.getSceneX();
                return e;
            }).map((e) -> {
                posY = pressed.getSceneY();
                return e;
            }).map((e) -> {
                startX = theBox.getLayoutX();
                return e;
            }).forEach((e) -> {
                startY = theBox.getLayoutY();
            });
        });

        theBox.setOnMouseDragged(dragged -> {
            classes.stream().filter((myClass) -> ((Pane) myClass == theBox)).filter((myClass) -> (gui.getPrimaryScene().getCursor().toString().equals("DEFAULT") && myClass.isSelected())).map((_item) -> dragged.getSceneX() - posX).map((diffX) -> {
                double diffY = dragged.getSceneY() - posY;
                startX += diffX;
                startY += diffY;
                return diffX;
            }).map((e) -> startX).map((scaledX) -> {
                double scaledY = startY;
                posX = dragged.getSceneX();
                posY = dragged.getSceneY();
                theBox.setLayoutX(scaledX);
                theBox.setLayoutY(scaledY);
                return scaledX;
            }).forEach((e) -> {
                dragged.consume();
            });
        });
    }

    public void handleClassName(String classNameText) {
        classes.stream().filter((myClass) -> (myClass.isSelected())).filter((myClass) -> (!checkClassDuplicateClassUpdate(myClass, classNameText))).forEach((myClass) -> {
            myClass.setClassName(classNameText);
        });
    }

    public void handlePackageName(String packageNameText) {
        classes.stream().filter((myClass) -> (myClass.isSelected())).filter((myClass) -> (!checkClassDuplicatePackageUpdate(myClass, packageNameText))).forEach((myClass) -> {
            myClass.setPackageName(packageNameText);
        });
    }

    public boolean checkClassDuplicateClassUpdate(Rectangles umlclass, String className) {
        for (Rectangles myClass : classes) {
            if (myClass != umlclass) {
                if (className.equals(myClass.getClassName())) {
                    if (myClass.getPackageName().equals(umlclass.getPackageName())) {
                        if (myClass.getPackageName().equals(DEFAULT_PACKAGE_NAME)) {
                            if (myClass.getClassName().equals(DEFAULT_CLASS_NAME)) {
                            } else {
                                AppMessageDialogSingleton.getSingleton().show(DUPLICATE_TITLE, DUPLICATE_CLASS_MESSAGE);
                                return true;
                            }
                        } else {
                            AppMessageDialogSingleton.getSingleton().show(DUPLICATE_TITLE, DUPLICATE_CLASS_MESSAGE);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean checkClassDuplicatePackageUpdate(Rectangles umlclass, String packageName) {
        for (Rectangles myClass : classes) {
            if (myClass != umlclass) {
                if (packageName.equals(myClass.getPackageName())) {
                    if (myClass.getClassName().equals(umlclass.getClassName())) {
                        if (myClass.getPackageName().equals(DEFAULT_PACKAGE_NAME)) {
                            if (myClass.getClassName().equals(DEFAULT_CLASS_NAME)) {
                            } else {
                                AppMessageDialogSingleton.getSingleton().show(DUPLICATE_TITLE, DUPLICATE_CLASS_MESSAGE);
                                return true;
                            }
                        } else {
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
