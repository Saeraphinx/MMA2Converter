import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Converter {
    private String pathIn;
    private String pathOut;
    private String levelData;

    public Converter(String path) {
        this.pathIn = path;
        this.pathOut = this.pathIn;



    }

    private void loadLevelData() {
        FileReader reader = new FileReader(pathIn);
        Scanner scan = new Scanner(reader);
        levelData = scan.nextLine();
        scan.close();
    }

    public void checkEvents() {

    }
}
