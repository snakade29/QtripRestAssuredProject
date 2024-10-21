package POJO;

public class RegisterLoginPostCall {
    private String email;
    private String password;
    private String confirmpassword;

    // Constructor
    public RegisterLoginPostCall(String email, String password, String confirmpassword) {
        this.email = email;
        this.password = password;
        this.confirmpassword = confirmpassword;
    }

    public RegisterLoginPostCall(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
}
