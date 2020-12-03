/*
 * This will be the worst code you have ever seen. Sorry but I don't care.
 * 
 * Title: MMA2 360 Converter
 * Author: TM0D
 * Description: Converts type 12 & type 13 events into laser & 360 events based on their value 
 *  (0-7 for 360 and 8 - * for laser speed)
 * 
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // Call for user file loc.
        Scanner userPrompt = new Scanner(System.in);
        System.out.println("Only convert files once. Do not open files generated with this tool in MMA2. \nWhere is the level.dat located?");
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

        System.out.println("Found " + LaserCount + " matches for right laser speed events.                                  ");

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
}

