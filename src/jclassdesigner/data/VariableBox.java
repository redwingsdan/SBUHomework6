package jclassdesigner.data;

/**
 *
 * @author Daniel Peterson
 */
public class VariableBox {

    String name;
    String returnType;
    String access;
    boolean isStatic;

    public VariableBox() {
        name = "";
        returnType = "";
        access = "";
        isStatic = false;
    }

    public VariableBox(String name, String returnType, String access, boolean checked) {
        this.name = name;
        this.returnType = returnType;
        this.isStatic = checked;
        this.access = access;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return returnType;
    }

    public boolean getIsStatic() {
        return isStatic;
    }

    public String getAccess() {
        return access;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.returnType = type;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    @Override
    public String toString() {
        String str = "";
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
        if (!name.equals("")) {
            str += name + " : " + returnType;
        }

        return str;
    }
}
