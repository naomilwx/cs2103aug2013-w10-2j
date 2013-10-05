package nailit.common;
import java.io.Serializable;

public enum TaskPriority implements Serializable {
    LOW,MEDIUM,HIGH;

    public String toString(){
        switch(this){
        case LOW :
            return "LOW";
        case MEDIUM :
            return "MEDIUM";
        case HIGH :
            return "HIGH";
        }
        return null;
    }

    public static TaskPriority valueOf(Class<TaskPriority> enumType, String value){
        if(value.equalsIgnoreCase(LOW.toString()))
            return TaskPriority.LOW;
        else if(value.equalsIgnoreCase(HIGH.toString()))
            return TaskPriority.HIGH;
        else if(value.equalsIgnoreCase(MEDIUM.toString()))
            return TaskPriority.MEDIUM;
        else
            return null;
    }
}