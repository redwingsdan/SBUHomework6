package jclassdesigner.data;

/**
 *
 * @author Daniel Peterson
 */
public class Variables 
{

    String name;
    String returnVal;
    String access;
    boolean isStatic;

    public Variables() 
    {
        name = "";
        returnVal = "";
        access = "";
        isStatic = false;
    }

    public Variables(String name, String returnVal, String access, boolean checked) 
    {
        this.name = name;
        this.returnVal = returnVal;
        this.access = access;
        this.isStatic = checked;
    }

    public String getName() 
    {
        return name;
    }
    
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getVal() 
    {
        return returnVal;
    }
    
    public void setVal(String val) 
    {
        this.returnVal = val;
    }

    public String getAccess() 
    {
        return access;
    }
    
    public void setAccess(String access) 
    {
        this.access = access;
    }
    
    public boolean getStatic() 
    {
        return isStatic;
    }

    public void setStatic(boolean isStatic) 
    {
        this.isStatic = isStatic;
    }
    
    //prints the data for the variable to be displayed
    @Override
    public String toString() 
    {
        String str = "";
        switch(this.access.toLowerCase()) 
        {
            case "package":
                str = str + "~";
                break;
            case "public":
                str = str + "+";
                break;
            case "protected":
                str = str + "#";
                break;
            case "private":
                str = str + "-";
                break;
            default:
                break;
        }
        if(this.isStatic) 
        {
            str = str + "$";
        }
        if((!this.name.equals("")) & (!this.name.equals(null))) 
        {
            str = str + this.name + " : " + this.returnVal;
        }
        return str;
    }
}
