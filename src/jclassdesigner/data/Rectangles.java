package jclassdesigner.data;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author Daniel Peterson
 */
public class Rectangles extends VBox 
{
    ArrayList<Variables> variables;
    ArrayList<Methods> methods;
    ArrayList<Rectangles> parents;
    
    VBox variableBox;
    VBox methodBox;
    VBox classBox;
    String packageName;
    String className;
    String interfaceString = "<<interface>>";
    String abstractString = "{abstract}";
    Label classNameLabel;
    
    boolean selected;
    boolean isInterface;
    boolean isAbstract;

    public Rectangles(Label classNameLabel, String packageName) 
    {
        this.variables = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.parents = new ArrayList<>();  
        this.packageName = packageName;
        this.className = classNameLabel.getText();
        this.classNameLabel = classNameLabel;
        begin();
    }

    public Rectangles() 
    {
        variables = new ArrayList<>();
        methods = new ArrayList<>();
        parents = new ArrayList<>();
        packageName = "";
        className = "";
        classNameLabel = new Label(className);
        begin();
    }

    //gets variables
    public ArrayList<Variables> getVariables() 
    {
        return variables;
    }

    //gets methods
    public ArrayList<Methods> getMethods() 
    {
        return methods;
    }
    
    //gets the class, interface, or abstract class name
    public ArrayList<Rectangles> getParents() 
    {
        return parents;
    }

    //is the rectangle selected
    public boolean isSelected() 
    {
        return selected;
    }

    //select the rectangle and load the boxes
    public void setSelected(boolean selected) 
    {
        this.selected = selected;
        begin();
    }

    //sets the class name and reloads the boxes
    public void setClassName(String className) 
    {
        this.className = className;
        classNameLabel.setText(className);
        begin();
    }

    //retrieve the class name
    public String getClassName() 
    {
        return className;
    }

    //gets the package name
    public String getPackageName() 
    {
        return packageName;
    }

    //sets the package name and reloads the boxes
    public void setPackageName(String packageName) 
    {
        this.packageName = packageName;
        begin();
    }

    //sets the class, interface, or abstract class and reloads the boxes
    public void setParentClass(ArrayList<Rectangles> parent) 
    {
        this.parents = parent;
        begin();
    }

    //checks if the rectangle is an interface
    public boolean getIsInterface() 
    {
        return isInterface;
    }

    //changes the interface status of the rectangle
    public void setIsInterface(boolean isInterface) 
    {
        this.isInterface = isInterface;
        begin();
    }

    //checks if the rectangle is an abstract
    public boolean getIsAbstract() 
    {
        return isAbstract;
    }

    //changes the abstract status of the rectangle
    public void setAbstract(boolean isAbstract) 
    {
        this.isAbstract = isAbstract;
        begin();
    }
    
    //Initializes the boxes
    public void begin() 
    {
        this.getChildren().clear();
        //Sets up boxes for every variable
        variableBox = new VBox();
        if(!variables.isEmpty()) 
        {
            variables.stream().forEach((var) -> {
                variableBox.getChildren().add(new Label(var.toString()));
            });
            variableBox.setStyle("-fx-border-color: black;" + "-fx-background-color: white;" + "-fx-border-style: solid;");;
        }
        //Sets up boxes for every method
        methodBox = new VBox();
        if(!methods.isEmpty()) 
        {
            methods.stream().forEach((method) -> {
                methodBox.getChildren().add(new Label(method.toString()));
            });
            methodBox.setStyle("-fx-border-color: black;" + "-fx-background-color: white;" + "-fx-border-style: solid;");
        }
        //Sets up boxes for the class, interface, or abstract class
        classBox = new VBox();
        if(isInterface) 
        {
            classBox.getChildren().addAll(new Label(interfaceString), classNameLabel);
        } 
        else if(isAbstract) 
        {
            classBox.getChildren().addAll(new Label(abstractString), classNameLabel);
        } 
        else 
        {
            classBox.getChildren().add(classNameLabel);
        }
        classBox.setStyle("-fx-border-color: black;" + "-fx-background-color: white;" + "-fx-border-style: solid;");

        this.setStyle("-fx-border-color: black;" + "-fx-border-style: solid;");
        this.getChildren().addAll(classBox, variableBox, methodBox);
    }
}
