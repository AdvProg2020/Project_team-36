package Models;

public class Response {
    private String returnType;
    private String data;

    public Response(String returnType, String data) {
        this.returnType = returnType;
        this.data = data;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
