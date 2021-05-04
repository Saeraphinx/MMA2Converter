import java.util.Scanner;

import javax.swing.JTextArea;

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
    private int replacedLaserCount;
    private final String rLaserType = "\"_type\":13";
    private final String lLaserType = "\"_type\":12";

    public Converter(String path) {
        this.pathIn = path;
        this.pathOut = this.pathIn;
        this.levelData = loadLevelData();
    }

    public Converter(String pathIn, String pathOut) {
        this.pathIn = pathIn;
        this.pathOut = pathOut;
        this.levelData = loadLevelData();
    }

    public int getLaserCount() {
        return laserCount;
    }

    public int getReplacedLaserCount() {
        return replacedLaserCount;
    }

    private String loadLevelData() {
        try {
            FileReader reader = new FileReader(pathIn);
            Scanner scan = new Scanner(reader);
            String localLevelData = scan.nextLine();
            scan.close();
            levelDataBackup = localLevelData;
            return localLevelData;
        } catch (Exception e) {
            return "Error";
        }
    }

    public void backupLevelData(JTextArea log) {
        String backupLocation = pathIn.substring(0, pathIn.indexOf(".dat")) + "_backup.dat";
        try {
            File file = new File(backupLocation);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(levelDataBackup);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            log.append("ERROR: idk wtf when worng lol");
        }
    }

    public void outputLevelData(JTextArea log) {
        try {
            File file = new File(pathOut);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(levelData);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            log.append("ERROR: idk wtf when worng lol");
        }
    }

    public void checkEvents(JTextArea log) {
        laserCount = 0;
        log.append("Looking for Laser events... \n");

        for (int i = 0; i < (levelData.length() - rLaserType.length() - 5); i++) {
            if (levelData.substring(i, (i + rLaserType.length())).equals(rLaserType)) {
                log.append("Looking for " + lLaserType + " and " + rLaserType + ". Found: " + laserCount + " PROGRESS: " + i + "/" + levelData.length() + "\n");
                laserCount++;
            } else if (levelData.substring(i, (i + lLaserType.length())).equals(lLaserType)) {
                log.append("Looking for " + lLaserType + " and " + rLaserType + ". Found: " + laserCount + " PROGRESS: " + i + "/" + levelData.length() + "\n");
                laserCount++;
            }
        }

        log.append("Found " + laserCount + " matches for laser speed events. \n");
    }

    public void replaceEvents(JTextArea log) {
        replacedLaserCount = 0;
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
                replacedLaserCount++;
                log.append("Converting laser events...  Currently on " + replacedLaserCount + "/" + laserCount + "\n");

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
                replacedLaserCount++;
                log.append("Converting laser events...  Currently on " + replacedLaserCount + "/" + laserCount + "\n");
            }
        }
        log.append("Converting laser events... Done. " + replacedLaserCount + "/" + laserCount + "\n");
    }
/*  Used for holding old method, as I would still like to keep it in the branch for refrence for now.
    private static void prototype() throws FileNotFoundException, IOException {
        // Call for user file loc.
        Scanner userPrompt = new Scanner(System.in);
        System.out.println("Only run this once per file. Do not open files generated with this tool in MMA2.");
        System.out.println("Where is the level.dat located?");
        String oFile = userPrompt.nextLine().trim();
        System.out.println("Where do you want your want your level.dat to be exported to?\nNOTE: Blank will overwrite the existing file.");
        String nFileInput = userPrompt.nextLine();
        String nFile;
        if (nFileInput.isBlank()) {
            nFile = oFile;
        } else {
            nFile = nFileInput.trim();
        }
        nFileInput = null;

        // Read file
        FileReader reader = new FileReader(oFile);
        Scanner scan = new Scanner(reader);
        String levelData = scan.nextLine();
        scan.close();

        //For error checking later
        final String rLaserType = "\"_type\":13";
        final String lLaserType = "\"_type\":12";
        int LaserCount = 0;
        System.out.print("Looking for Laser events... \r");

        for (int i = 0; i < (levelData.length() - rLaserType.length() - 5); i++) {
            if (levelData.substring(i,(i + rLaserType.length())).equals(rLaserType)) {
                System.out.print("Looking for " + lLaserType  + " and " + rLaserType +". Found: " + LaserCount + " PROGRESS: " + i + "/" + levelData.length() + "\r");
                LaserCount++;
            } else if (levelData.substring(i,(i + lLaserType.length())).equals(lLaserType)) {
                System.out.print("Looking for " + lLaserType  + " and " + rLaserType +". Found: " + LaserCount + " PROGRESS: " + i + "/" + levelData.length() + "\r");
                LaserCount++;
            }
        }

        System.out.println("Found " + LaserCount + " matches for laser speed events.                                  ");

        // confirm details with the user, if not exit.
        System.out.println("Would you like to continue? Y/n (Default is Y)");
        String confirm = userPrompt.nextLine();
        if (confirm.equals("Y") || confirm.isBlank()) {    
        } else {
            userPrompt.close();
            System.exit(0);
        }

        int replaceCount = 0;
        // Start replacing based on type 13's value
        for (int i = 0; i < (levelData.length() - rLaserType.length() - 5); i++) {
            if (levelData.substring(i,(i + rLaserType.length())).equals(rLaserType)) {
                String event = levelData.substring(i,levelData.indexOf("}", i));
                int eventValue = Integer.parseInt(event.substring(event.lastIndexOf(":") + 1));
                int eventType = Integer.parseInt(event.substring(event.indexOf(":") + 1, event.indexOf(",")));
                switch (eventValue) {
                    // Switch event into 360 event
                    case 0: case 1: case 2: case 3:
                    case 4: case 5: case 6: case 7:
                    eventType = 15; break;
                    // keep event as laser
                    default:
                    eventValue = Math.abs(eventValue - 8); break;
                }
                event = "\"_type\":" + eventType + ",\"_value\":" + eventValue;
                levelData = levelData.substring(0,i) + event + levelData.substring(levelData.indexOf("}", i));
                replaceCount++;


                // Start replacing based on type 12's value
            } else if (levelData.substring(i,(i + lLaserType.length())).equals(lLaserType)) {
                String event = levelData.substring(i,levelData.indexOf("}", i));
                int eventValue = Integer.parseInt(event.substring(event.lastIndexOf(":") + 1));
                int eventType = Integer.parseInt(event.substring(event.indexOf(":") + 1, event.indexOf(",")));
                switch (eventValue) {
                    // Switch event into 360 event
                    case 0: case 1: case 2: case 3:
                    case 4: case 5: case 6: case 7:
                    eventType = 14; break;
                    // keep event as laser
                    default:
                    eventValue = Math.abs(eventValue - 8); break;
                }
                event = "\"_type\":" + eventType + ",\"_value\":" + eventValue;
                levelData = levelData.substring(0,i) + event + levelData.substring(levelData.indexOf("}", i));
                replaceCount++;
            }
            System.out.print("Converting laser events...  Currently on " + replaceCount + "/" + LaserCount + "\r");
        }
        System.out.println("Converting laser events... Done. " + replaceCount + "/" + LaserCount + "             ");
        System.out.println("Outputting file at " + nFile);

        File file = new File(nFile);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(levelData);
		fileWriter.flush();
        fileWriter.close();
        userPrompt.close();
    }
    */
}
