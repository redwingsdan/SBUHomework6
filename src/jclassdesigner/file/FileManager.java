package jclassdesigner.file;

import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import jclassdesigner.data.DataManager;
import jclassdesigner.data.Methods;
import jclassdesigner.data.Rectangles;
import jclassdesigner.data.Variables;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jclassdesigner.gui.Workspace;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author Daniel Peterson
 * @version 1.0
 */
public class FileManager implements AppFileComponent {

    static final String JSON_UML_DIAGRAM = "UML Diagram";
    
    static final String JSON_CLASS_NAME = "Class Name";
    static final String JSON_PACKAGE_NAME = "Package Name";
    static final String JSON_PARENTS = "Parents";
    
    static final String JSON_METHODS = "Methods";
    static final String JSON_METHOD_NAME = "Method Name";
    static final String JSON_VARIABLES = "Variables";
    static final String JSON_VARIABLE_NAME = "Variable Name";
    
    static final String JSON_LAYOUT_X = "LayoutX";
    static final String JSON_LAYOUT_Y = "LayoutY";
    static final String JSON_MAX_X = "MaxX";
    static final String JSON_MAX_Y = "MaxY";
    static final String JSON_MIN_X = "MinX";
    static final String JSON_MIN_Y = "MinY";
    
    static final String JSON_ABSTRACT = "Is Abstract";
    static final String JSON_INTERFACE = "Is Interface";
    static final String JSON_STATIC = "Is Static";
    static final String JSON_RET_TYPE = "Type";
    
    static final String JSON_ACCESS = "Access";
    static final String JSON_METHOD_ARGS = "Arguments";
    static final String JSON_METH_ARG_NAME = "Argument Name";
    static final String JSON_METH_ARG_TYPE = "Argument Type";
      
    static final String JSON_BOTTOM = "Bottom Padding";
    static final String JSON_LEFT = "Left Padding";
    static final String JSON_RIGHT = "Right Padding";
    static final String JSON_SPACING = "Spacing";
    static final String JSON_TOP = "Top Padding";

    /**
     * This method is for saving user work, which in the case of this
     * application means the data that constitutes the page DOM.
     *
     * @param data The data management component for this application.
     *
     * @param filePath Path (including file name/extension) to where to save the
     * data to.
     *
     * @throws IOException Thrown should there be an error writing out data to
     * the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException 
    {
        // GET THE DATA
        DataManager dataManager = (DataManager) data;

        // NOW BUILD THE JSON OBJCTS TO SAVE
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ArrayList<Node> nodes = dataManager.getNodes();
        nodes.stream().filter((node) -> (node instanceof Rectangles)).map((node) -> (Rectangles) node).map((umlClass) -> {
            double layoutX = umlClass.getLayoutX();
            double layoutY = umlClass.getLayoutY();
            double scaleX = umlClass.getScaleX();
            double scaleY = umlClass.getScaleY();
            String className = umlClass.getClassName();
            String packageName = umlClass.getPackageName();
            String parentName = null;
            ArrayList<Rectangles> parents = umlClass.getParents();
            if (parents == null || parents.isEmpty() || parents.get(0).getClassName().equals("")) 
            {
                parentName = "";
            } 
            else 
            {
                parentName += parents.get(0).getClassName();
                for (int i = 1; i < parents.size(); i++) 
                {
                    parentName += ", " + parents.get(i).getClassName();
                }
            }
            ArrayList<Variables> variables = umlClass.getVariables();
            ArrayList<Methods> methods = umlClass.getMethods();
            if (packageName == null) 
            {
                packageName = "";
            }
            String checkInterface = null;
            if (umlClass.getIsInterface())
            {
                checkInterface = "yes";
            } 
            else 
            {
                checkInterface = "no";
            }
            String checkAbstract = null;
            if (umlClass.getIsAbstract()) 
            {
                checkAbstract = "yes";
            }
            else 
            {
                checkAbstract = "no";
            }
            JsonObject umlClassJson = Json.createObjectBuilder()
                    .add(JSON_CLASS_NAME, className)
                    .add(JSON_PACKAGE_NAME, packageName)
                    .add(JSON_PARENTS, parentName)
                    .add(JSON_INTERFACE, checkInterface)
                    .add(JSON_ABSTRACT, checkAbstract)
                    .add(JSON_VARIABLES, makeJsonVariableArray(variables))
                    .add(JSON_METHODS, makeJsonMethodsArray(methods))
                    .add(JSON_LAYOUT_X, Double.toString(layoutX))
                    .add(JSON_LAYOUT_Y, Double.toString(layoutY))
                    .add("Scale_X", Double.toString(scaleX))
                    .add("Scale_Y", Double.toString(scaleY))
                    .build();
            return umlClassJson;
        }).forEach((umlClassJson) -> {
            arrayBuilder.add(umlClassJson);
        });
        JsonArray umlArray = arrayBuilder.build();

        // THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_UML_DIAGRAM, umlArray)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        try (JsonWriter jsonWriter = writerFactory.createWriter(sw)) 
        {
            jsonWriter.writeObject(dataManagerJSO);
        }

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        try (PrintWriter pw = new PrintWriter(filePath)) 
        {
            pw.write(prettyPrinted);
        }
    }

    private JsonArray makeJsonVariableArray(ArrayList<Variables> variables) 
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        variables.stream().map((variable) -> {
            String str = "no";
            if (variable.getStatic()) {
                str = "yes";
            }
            JsonObject varJson = Json.createObjectBuilder()
                    .add(JSON_VARIABLE_NAME, variable.getName())
                    .add(JSON_RET_TYPE, variable.getVal())
                    .add(JSON_ACCESS, variable.getAccess())
                    .add(JSON_STATIC, str)
                    .build();
            return varJson;
        }).forEach((varJson) -> {
            arrayBuilder.add(varJson);
        });
        JsonArray jA = arrayBuilder.build();
        return jA;
    }

    private JsonArray makeJsonMethodsArray(ArrayList<Methods> methods) 
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        methods.stream().map((method) -> {
            String str = "no";
            String str1 = "no";
            if (method.getIsStatic()) 
            {
                str = "yes";
            }
            if (method.getAbstract()) 
            {
                str1 = "yes";
            }
            JsonObject methodJson = Json.createObjectBuilder()
                    .add(JSON_METHOD_NAME, method.getName())
                    .add(JSON_RET_TYPE, method.getType())
                    .add(JSON_ACCESS, method.getAccess())
                    .add(JSON_STATIC, str)
                    .add(JSON_ABSTRACT, str1)
                    .add(JSON_METHOD_ARGS, makeJsonMethodArgsArray(method.getArgs()))
                    .build();
            return methodJson;
        }).forEach((methodJson) -> {
            arrayBuilder.add(methodJson);
        });
        JsonArray jA = arrayBuilder.build();
        return jA;
    }

    private JsonArray makeJsonMethodArgsArray(TreeMap<String, String> args) 
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Map.Entry<String, String> entry : args.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();

            JsonObject argJson = Json.createObjectBuilder()
                    .add(JSON_METH_ARG_NAME, key)
                    .add(JSON_METH_ARG_TYPE, value)
                    .build();
            arrayBuilder.add(argJson);
        }
        JsonArray jA = arrayBuilder.build();
        return jA;
    }

    /**
     * This method loads data from a JSON formatted file into the data
     * management component and then forces the updating of the workspace such
     * that the user may edit the data.
     *
     * @param data Data management component where we'll load the file into.
     *
     * @param filePath Path (including file name/extension) to where to load the
     * data from.
     *
     * @throws IOException Thrown should there be an error reading in data from
     * the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException 
    {
        DataManager dataManager = (DataManager) data;
        dataManager.reset();

        JsonObject json = loadJSONFile(filePath);

        JsonArray nodesJsonArray = json.getJsonArray(JSON_UML_DIAGRAM);
        loadInfo(nodesJsonArray, dataManager);
    }

    private JsonObject loadJSONFile(String jsonFilePath) throws IOException 
    {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    private void loadInfo(JsonArray nodesJsonArray, DataManager dataManager) 
    {
        ArrayList<Node> nodes = dataManager.getNodes();
        for (int i = 0; i < nodesJsonArray.size(); i++) 
        {
            JsonObject nodeJso = nodesJsonArray.getJsonObject(i);
            Rectangles umlclass = new Rectangles();
            umlclass.setClassName(nodeJso.getString(JSON_CLASS_NAME));
            umlclass.setPackageName(nodeJso.getString(JSON_PACKAGE_NAME));
            umlclass.getParents().add(new Rectangles(new Label(nodeJso.getString(JSON_PARENTS)), ""));

            if (nodeJso.getString(JSON_INTERFACE).equals("yes")) 
            {
                umlclass.setIsInterface(true);
            } 
            else 
            {
                umlclass.setIsInterface(false);
            }

            if (nodeJso.getString(JSON_ABSTRACT).equals("yes")) 
            {
                umlclass.setAbstract(true);
            } 
            else 
            {
                umlclass.setAbstract(false);
            }

            loadVariables(umlclass.getVariables(), nodeJso.getJsonArray(JSON_VARIABLES));
            loadMethods(umlclass.getMethods(), nodeJso.getJsonArray(JSON_METHODS));

            double layoutX = Double.valueOf(nodeJso.getString(JSON_LAYOUT_X));
            double layoutY = Double.valueOf(nodeJso.getString(JSON_LAYOUT_Y));
            umlclass.setLayoutX(layoutX);
            umlclass.setLayoutY(layoutY);
            umlclass.setScaleX(Double.valueOf(nodeJso.getString("Scale_X")));
            umlclass.setScaleY(Double.valueOf(nodeJso.getString("Scale_Y")));

            nodes.add(umlclass);
        }
    }

    private void loadVariables(ArrayList<Variables> list, JsonArray variablesJA) 
    {
        for (int i = 0; i < variablesJA.size(); i++) 
        {
            JsonObject varJso = variablesJA.getJsonObject(i);
            String varName = varJso.getString(JSON_VARIABLE_NAME);
            String returnType = varJso.getString(JSON_RET_TYPE);
            String access = varJso.getString(JSON_ACCESS);
            boolean isStatic = false;
            if (varJso.getString(JSON_STATIC).equals("yes")) 
            {
                isStatic = true;
            } 
            else if (varJso.getString(JSON_STATIC).equals("no")) 
            {
                isStatic = false;
            }
            Variables var = new Variables(varName, returnType, access, isStatic);
            list.add(var);
        }
    }

    private void loadMethods(ArrayList<Methods> list, JsonArray methodsJA) 
    {
        for (int i = 0; i < methodsJA.size(); i++) 
        {
            JsonObject methodJso = methodsJA.getJsonObject(i);
            String methodName = methodJso.getString(JSON_METHOD_NAME);
            String returnType = methodJso.getString(JSON_RET_TYPE);
            String access = methodJso.getString(JSON_ACCESS);
            boolean isStatic = false;
            boolean isAbstract = false;
            if (methodJso.getString(JSON_STATIC).equals("yes")) 
            {
                isStatic = true;
            } 
            else if (methodJso.getString(JSON_STATIC).equals("no")) 
            {
                isStatic = false;
            }
            if (methodJso.getString(JSON_ABSTRACT).equals("yes")) 
            {
                isAbstract = true;
            } 
            else if (methodJso.getString(JSON_ABSTRACT).equals("no")) 
            {
                isAbstract = false;
            }

            Methods method = new Methods(methodName, returnType, isStatic, isAbstract, access);

            JsonArray argJA = methodJso.getJsonArray(JSON_METHOD_ARGS);
            for (int j = 0; j < argJA.size(); j++) 
            {
                JsonObject argJso = argJA.getJsonObject(j);
                String argName = argJso.getString(JSON_METH_ARG_NAME);
                String argType = argJso.getString(JSON_METH_ARG_TYPE);
                method.getArgs().put(argName, argType);
            }

            list.add(method);
        }
    }

    /**
     * This method exports the contents of the data manager to a Web page
     * including the html page, needed directories, and the CSS file.
     *
     * @param data The data management component.
     *
     * @param filePath Path (including file name/extension) to where to export
     * the page to.
     *
     * @throws IOException Thrown should there be an error writing out data to
     * the file.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException 
    {
        try 
        {
            DataManager dataManager = (DataManager) data;
            for (Node node : dataManager.getNodes()) 
            {
                Rectangles umlClass = (Rectangles) node;
                String packageName = umlClass.getPackageName();
                String tmp = "";
                if (packageName.indexOf(".") > 0) 
                {
                    String[] strs = packageName.split("\\.");
                    tmp = filePath + "/" + strs[0] + "/";

                    for (int i = 1; i < strs.length - 1; i++) 
                    {
                        tmp += strs[i];
                    }
                    if (strs.length > 0) 
                    {
                        tmp += strs[strs.length - 1];
                    }
                } 
                else 
                {
                    tmp = filePath + "/" + umlClass.getPackageName();
                }

                File dir = new File(tmp);
                if (!dir.exists()) 
                {
                    dir.mkdir();
                }
                File javaFile = new File(dir + "/" + umlClass.getClassName() + ".java");

                ArrayList<String> list = new ArrayList<>();
                fillArrayListWithData(umlClass, list);

                FileWriter fWriter = new FileWriter(javaFile.getPath());
                try (BufferedWriter writer = new BufferedWriter(fWriter)) 
                {
                    for (String value : list) 
                    {
                        writer.write(value);
                        writer.newLine();
                    }
                }
            }
        } 
        catch (FileNotFoundException e) 
        {
            // NOTHING
        }
    }

    private void fillArrayListWithData(Rectangles umlClass, ArrayList<String> list) 
    {
        String packageLine = "package " + umlClass.getPackageName() + ";";
        list.add(packageLine);

        String classTitle = "public ";
        if (umlClass.getIsAbstract()) 
        {
            classTitle += "abstract ";
        }
        if (umlClass.getIsInterface()) 
        {
            classTitle += "interface ";
        }

        classTitle += "class " + umlClass.getClassName() + " ";

        ArrayList<Rectangles> parents = umlClass.getParents();
        boolean printed = false;
        Iterator iter = parents.iterator();
        while (iter.hasNext()) 
        {
            Rectangles parentClass = (Rectangles) iter.next();

            if (!printed) 
            {
                if (parentClass.getIsInterface()) 
                {
                    classTitle += "implements " + parentClass.getClassName() + ", ";
                } 
                else 
                {
                    classTitle += "extends " + parentClass.getClassName() + " ";
                }
                printed = true;
            } 
            else 
            {
                if (parentClass.getIsInterface()) 
                {
                    if (!iter.hasNext())
                    {
                        classTitle += parentClass.getClassName();
                    } 
                    else 
                    {
                        classTitle += parentClass.getClassName() + ", ";
                    }
                }
            }
        }

        classTitle += "{";
        list.add(classTitle);

        addVariables(umlClass, list);
        addMethods(umlClass, list);
        list.add("}");
    }

    private void addVariables(Rectangles umlClass, ArrayList<String> list) 
    {
        ArrayList<Variables> variables = umlClass.getVariables();
        String varAccess = "";
        String varIsStatic = "";
        String varIsFinal = "";
        String varType = "";
        String varName = "";
        for (Variables variable : variables) 
        {
            String varLine = "";
            varAccess = variable.getAccess();
            if (variable.getStatic()) 
            {
                varIsStatic = "static";
            }
            varName = variable.getName();
            if (isAllUpperCase(varName)) 
            {
                varIsFinal = "final";
            }
            varType = variable.getVal();

            varLine += varAccess + " ";
            if (varIsStatic.equals("static")) 
            {
                varLine += varIsStatic + " ";
            }
            if (varIsFinal.equals("final"));
            varLine += varIsFinal + " ";
            varLine += varType + " " + varName + ";";

            list.add(varLine);
        }
    }

    private void addMethods(Rectangles umlClass, ArrayList<String> list) 
    {
        ArrayList<Methods> methods = umlClass.getMethods();
        String methodAccess = "";
        String methodAbstract = "";
        String methodType = "";
        String methodName = "";
        String methodStatic = "";
        TreeMap<String, String> methodArgs;

        for (Methods method : methods) 
        {
            String methodLine = "";
            methodAccess = method.getAccess();
            methodType = method.getType();
            methodName = method.getName();
            if (method.getAbstract()) 
            {
                methodAbstract = "abstract";
            }
            if (method.getIsStatic()) 
            {
                methodStatic = "static";
            }
            methodArgs = method.getArgs();

            methodLine += methodAccess;
            if (methodAbstract.equals("abstract")) 
            {
                methodLine += " " + methodAbstract;
            }
            if (methodStatic.equals("static")) 
            {
                methodLine += " " + methodStatic;
            }

            methodLine += " " + methodType + " " + methodName + "(";

            if (!methodArgs.isEmpty()) 
            {
                Iterator iter = methodArgs.entrySet().iterator();
                while (iter.hasNext()) 
                {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    if (!iter.hasNext()) 
                    {
                        methodLine += value + " " + key;
                    }
                    else 
                    {
                        methodLine += value + " " + key + ", ";
                    }
                }
            }

            methodLine += ") { }";
            list.add(methodLine);
        }
    }

    private boolean isAllUpperCase(String str)
    {
        for (int i = 0; i < str.length(); i++) 
        {
            char c = str.charAt(i);
            if (c >= 97 && c <= 122) 
            {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is provided to satisfy the compiler, but it is not used by
     * this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException 
    {
    }
}
