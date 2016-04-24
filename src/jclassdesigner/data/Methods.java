package jclassdesigner.data;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Daniel Peterson
 */
public class Methods 
{
    private TreeMap<String, String> args;
    private String name;
    private String val;
    private String access;
    
    private boolean isStatic;
    private boolean isAbstract;

    public Methods() 
    {
        args = new TreeMap<>();
        name = "";
        val = "";
        access = "";
        
        isStatic = false;
        isAbstract = false;
    }

    public Methods(String name, String val, String access, boolean isStatic, boolean isAbstract) 
    {
        this.args = new TreeMap<>();
        this.name = name;
        this.val = val;
        this.access = access;
        
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
    }
    
    public TreeMap<String, String> getArgs() 
    {
        return args;
    }
    
    public void addArg(String argName, String argType) 
    {
        args.put(argName, argType);
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getType() 
    {
        return val;
    }

    public void setType(String val) 
    {
        this.val = val;
    }

    public String getAccess() 
    {
        return access;
    }

    public void setAccess(String access) 
    {
        this.access = access;
    }

    public boolean getIsStatic() 
    {
        return isStatic;
    }

    public void setStatic(boolean isStatic) 
    {
        this.isStatic = isStatic;
    }

    public boolean getAbstract() 
    {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) 
    {
        this.isAbstract = isAbstract;
    }

    //prints the data for the method to be displayed
    @Override
    public String toString() 
    {
        String str = "";

        if(this.isAbstract) 
        {
            str = str + "{abstract} ";
        }
        switch(this.access.toLowerCase()) {
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
        str = str + this.name + "(";

        String methArg = "";
        if(!this.args.isEmpty()) 
        {
            int x = 0;
            for(Map.Entry<String, String> entry : this.args.entrySet()) 
            {
                String key = entry.getKey();
                String value = entry.getValue();
                methArg = key + " : " + value;
                if(x == (this.args.size() - 1)) 
                {
                    str = str + methArg;
                } 
                else 
                {
                    str = str + methArg + ", ";
                }
                x++;
            }
        }

        if(!(this.val.equals(""))) 
        {
            str = str + ") : " + this.val;
        }
        else 
        { 
            str = str + ")";
        }
        return str;
    }
}
