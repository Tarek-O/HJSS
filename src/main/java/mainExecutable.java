
public class mainExecutable {
    static Controller controller;
    public static String schoolName = "Hatfield Junior Swimming School";

    public static void main(String[] args){
        String fileDirectory = System.getProperty("user.dir") + "\\src\\main\\resources\\dataSet.txt";
        controller = new Controller(schoolName);
        controller.createDataFromFile(fileDirectory);
        controller.runMain();
    }
}