package Models;

import java.util.HashMap;
import java.util.Map;

public class Query {
    private String token;
    private String controllerName;
    private String methodName;
    private Map<String,String> methodInputs;

    public Query(String token, String controllerName, String methodName) {
        this.token = token;
        this.controllerName = controllerName;
        this.methodName = methodName;
        this.methodInputs = new HashMap<>();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Map<String, String> getMethodInputs() {
        return methodInputs;
    }

    public void setMethodInputs(Map<String, String> methodInputs) {
        this.methodInputs = methodInputs;
    }
}
