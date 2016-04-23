package jclassdesigner.data;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Daniel Peterson
 */
public class MethodBox {

    private String name;
    private String type;
    private String access;
    private TreeMap<String, String> arguments;
    private boolean isStatic;
    private boolean isAbstract;

    public MethodBox() {
        name = "";
        type = "";
        access = "";
        arguments = new TreeMap<>();
        isStatic = false;
        isAbstract = false;
    }

    public MethodBox(String name, String type, String access, boolean isStatic, boolean isAbstract) {
        this.name = name;
        this.type = type;
        this.access = access;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
        arguments = new TreeMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public boolean getIsStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public boolean getIsAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public TreeMap<String, String> getArgs() {
        return arguments;
    }

    public void addArg(String argName, String argType) {
        arguments.put(argName, argType);
    }

    @Override
    public String toString() {
        String str = "";

        if (isAbstract) {
            str += "{abstract} ";
        }

        if (access.toLowerCase().equals("public")) {
            str += "+";
        } else {
            switch (access.toLowerCase()) {
                case "private":
                    str += "-";
                    break;
                case "protected":
                    str += "#";
                    break;
                case "package":
                    str += "~";
                    break;
                default:
                    break;
            }
        }

        if (isStatic) {
            str += "$";
        }

        str += name + "(";

        String arg = "";
        if (!arguments.isEmpty()) {
            int i = 0;
            for (Map.Entry<String, String> entry : arguments.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                arg = key + " : " + value;
                if (i == arguments.size() - 1) {
                    str += arg;
                } else {
                    str += arg + ", ";
                }
                i++;
            }
        }

        if (type.equals("")) {
            str += ")";
        } else {
            str += ") : " + type;
        }

        return str;
    }
}
