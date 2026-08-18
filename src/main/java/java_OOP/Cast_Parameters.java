package java_OOP;

public class Cast_Parameters {

    public static void main(String[] args) {

    String username = "//input[@name='login']//text()='%s'";

    String locator;
    locator = String.format(username, "Fill in username");
    System.out.println(locator);


    }
}
