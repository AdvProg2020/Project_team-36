package Models;

import java.util.Map;

public class Query {
    private String token;
    private String controllerName;
    private String methodName;
    private Map<String,String> methodInputs;

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
