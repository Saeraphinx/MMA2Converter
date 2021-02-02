import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

// not completely finished, but hopefully it's close.

public class Converter {
    private String pathIn;
    private String pathOut;
    private String levelData;
    private String levelDataBackup;
    private int laserCount;
    private final String rLaserType = "\"_type\":13";
    private final String lLaserType = "\"_type\":12";

    public Converter(String path) throws FileNotFoundException, IOException {
        this.pathIn = path;
        this.pathOut = this.pathIn;
        this.levelData = loadLevelData();
    }

    public Converter(String pathIn, String pathOut) throws FileNotFoundException, IOException {
        this.pathIn = pathIn;
        this.pathOut = pathOut;
        this.levelData = loadLevelData();
    }

    private String loadLevelData() throws FileNotFoundException, IOException {
        try {
            FileReader reader = new FileReader(pathIn);
            Scanner scan = new Scanner(reader);
            String localLevelData = scan.nextLine();
            scan.close();
            levelDataBackup = localLevelData;
            return localLevelData;
        } catch (Exception FileNotFoundException) {
            return "Error";
        }
    }

    private void backupLevelData() throws IOException {
        String backupLocation = pathIn.substring(0, pathIn.indexOf(".dat")) + "_backup.dat";
        File file = new File(backupLocation);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(levelDataBackup);
        fileWriter.flush();
        fileWriter.close();
    }

    private void outputLevelData() throws IOException {
        File file = new File(pathOut);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(levelData);
        fileWriter.flush();
        fileWriter.close();
    }

    public void checkEvents() {
        int LaserCount = 0;
        System.out.print("Looking for Laser events... \r");

        for (int i = 0; i < (levelData.length() - rLaserType.length() - 5); i++) {
            if (levelData.substring(i, (i + rLaserType.length())).equals(rLaserType)) {
                System.out.print("Looking for " + lLaserType + " and " + rLaserType + ". Found: " + LaserCount + " PROGRESS: " + i + "/" + levelData.length() + "\r");
                LaserCount++;
            } else if (levelData.substring(i, (i + lLaserType.length())).equals(lLaserType)) {
                System.out.print("Looking for " + lLaserType + " and " + rLaserType + ". Found: " + LaserCount + " PROGRESS: " + i + "/" + levelData.length() + "\r");
                LaserCount++;
            }
        }

        System.out.println("Found " + LaserCount + " matches for laser speed events.                                  ");
        LaserCount = laserCount;
    }

    public void replaceEvents() {
        int replaceCount = 0;
        // Start replacing based on type 13's value
        for (int i = 0; i < (levelData.length() - rLaserType.length() - 5); i++) {
            if (levelData.substring(i, (i + rLaserType.length())).equals(rLaserType)) {
                String event = levelData.substring(i, levelData.indexOf("}", i));
                int eventValue = Integer.parseInt(event.substring(event.lastIndexOf(":") + 1));
                int eventType = Integer.parseInt(event.substring(event.indexOf(":") + 1, event.indexOf(",")));
                switch (eventValue) {
                    // Switch event into 360 event
                    case 0: case 1: case 2: case 3:
                    case 4: case 5: case 6: case 7:
                        eventType = 15;
                        break;
                    // keep event as laser
                    default:
                        eventValue = Math.abs(eventValue - 8);
                        break;
                }
                event = "\"_type\":" + eventType + ",\"_value\":" + eventValue;
                levelData = levelData.substring(0, i) + event + levelData.substring(levelData.indexOf("}", i));
                replaceCount++;

                // Start replacing based on type 12's value
            } else if (levelData.substring(i, (i + lLaserType.length())).equals(lLaserType)) {
                String event = levelData.substring(i, levelData.indexOf("}", i));
                int eventValue = Integer.parseInt(event.substring(event.lastIndexOf(":") + 1));
                int eventType = Integer.parseInt(event.substring(event.indexOf(":") + 1, event.indexOf(",")));
                switch (eventValue) {
                    // Switch event into 360 event
                    case 0: case 1: case 2: case 3:
                    case 4: case 5: case 6: case 7:
                        eventType = 14;
                        break;
                    // keep event as laser
                    default:
                        eventValue = Math.abs(eventValue - 8);
                        break;
                }
                event = "\"_type\":" + eventType + ",\"_value\":" + eventValue;
                levelData = levelData.substring(0, i) + event + levelData.substring(levelData.indexOf("}", i));
                replaceCount++;
            }
            System.out.print("Converting laser events...  Currently on " + replaceCount + "/" + laserCount + "\r");
        }
        System.out.println("Converting laser events... Done. " + replaceCount + "/" + laserCount + "             ");
    }
}
