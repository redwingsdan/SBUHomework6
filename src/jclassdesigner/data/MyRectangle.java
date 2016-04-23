package jclassdesigner.data;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author Daniel Peterson
 */
public class MyRectangle extends VBox {

    Label classNameLabel;
    String packageName;
    String className;
    ArrayList<MyRectangle> parents;
    String interfaceString = "<<interface>>";
    String abstractString = "{abstract}";
    VBox classBox;
    VBox variableBox;
    VBox methodBox;

    ArrayList<VariableBox> variables;
    ArrayList<MethodBox> methods;

    boolean selected;
    boolean isInterface;
    boolean isAbstract;

    public MyRectangle(Label classNameLabel, String packageName) {
        this.classNameLabel = classNameLabel;
        this.packageName = packageName;
        className = classNameLabel.getText();
        variables = new ArrayList<>();
        methods = new ArrayList<>();
        parents = new ArrayList<>();
        init();
    }

    public MyRectangle() {
        packageName = "";
        className = "";
        classNameLabel = new Label(className);
        variables = new ArrayList<>();
        methods = new ArrayList<>();
        parents = new ArrayList<>();
        init();
    }

    public void init() {
        this.getChildren().clear();
        classBox = new VBox();
        if (isInterface) {
            classBox.getChildren().addAll(new Label(interfaceString), classNameLabel);
        } else if (isAbstract) {
            classBox.getChildren().addAll(new Label(abstractString), classNameLabel);
        } else {
            classBox.getChildren().add(classNameLabel);
        }
        classBox.setStyle("-fx-background-color:white;" + "-fx-border-style: solid;" + "-fx-border-color:black;");

        variableBox = new VBox();
        if (!variables.isEmpty()) {
            variables.stream().forEach((var) -> {
                variableBox.getChildren().add(new Label(var.toString()));
            });
            variableBox.setStyle("-fx-background-color:white;" + "-fx-border-style: solid;" + "-fx-border-color:black;");
        }
        methodBox = new VBox();

        if (!methods.isEmpty()) {
            methods.stream().forEach((method) -> {
                methodBox.getChildren().add(new Label(method.toString()));
            });
            methodBox.setStyle("-fx-background-color:white;" + "-fx-border-style: solid;" + "-fx-border-color:black;");
        }

        this.setStyle("-fx-border-style: solid;" + "-fx-border-color:black;");
        this.getChildren().addAll(classBox, variableBox, methodBox);
    }

    public ArrayList<VariableBox> getVariables() {
        return variables;
    }

    public ArrayList<MethodBox> getMethods() {
        return methods;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        init();
    }

    public void setClassName(String className) {
        this.className = className;
        classNameLabel.setText(className);
        init();
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
        init();
    }

    public ArrayList<MyRectangle> getParents() {
        return parents;
    }

    public void setParentClass(ArrayList<MyRectangle> parent) {
        this.parents = parent;
        init();
    }

    public boolean getIsInterface() {
        return isInterface;
    }

    public void setIsInterface(boolean isInterface) {
        this.isInterface = isInterface;
        init();
    }

    public boolean getIsAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
        init();
    }
}
