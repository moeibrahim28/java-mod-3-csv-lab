import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileIORunner {
    public static void main(String[] args) throws IOException {
        // writeToFile("person.csv", "example of writing data to file");
        UserOutputService userOutputService = new SysoutUserOutputService();
        try (UserInputService userInputService = new ScannerUserInputService(userOutputService)) {
            boolean runValue = true;
            PersonListBuilderService personListBuilderService = new PersonListBuilderService(userInputService);

            // Ask user if they want to restore a list if not create one
            String fileName = userInputService.askUserAboutFile();
            int extensionChoice = getFileExtenion(userInputService);
            String verifiedFileName;
            String fileExtension = "";

            // check validity of file name and get a verified filename
            if (!fileName.isBlank()) {


                String nonBlankName = "";
                switch (extensionChoice) {
                    case 1:
                        fileExtension = ".csv";
                        break;
                    case 2:
                        fileExtension = ".json";
                        break;
                    default:
                        System.out.println("Invalid input. Choose one of the following options:");
                        extensionChoice = getFileExtenion(userInputService);
                }

                // load for csv file
                if (fileExtension.equals(".csv")) {
                    personListBuilderService.readFromCSV(fileName, fileExtension);
                    verifiedFileName=fileName;
                }

                // load json file
                else {
                    personListBuilderService.verifyFile(personListBuilderService.getNewFileName(), ".json");
                    verifiedFileName=fileName;
                    personListBuilderService.readPersonListFromJSON(fileName+ ".json");

                }
            }
            // get new file name and repeat
            else {
                String newFileName = userInputService.getUserInput(
                        "Please give the name of this new file. It will be made a .csv file or .json file by the system.");
                extensionChoice = getFileExtenion(userInputService);
                newFileName = newFileName + extensionChoice;
                verifiedFileName = newFileName;
            }

            // loops for asking user to pick an action
            while (runValue) {
                UserOutputService userChoiceOutputService = new SysoutUserOutputService();
                UserInputService userChoiceInputService = new ScannerUserInputService(userChoiceOutputService);
                int userChoice = getUserAction(userChoiceInputService);
                switch (userChoice) {
                    case 1:
                        personListBuilderService.addPersonToList(verifiedFileName,fileExtension);
                        break;
                    case 2:
                        if(fileExtension.equals(".csv")) {
                            personListBuilderService.printFromFile(verifiedFileName + fileExtension, true);
                        }
                        else{
                            personListBuilderService.printJson(personListBuilderService.getPersonList());
                        }
                        break;
                    case 3:
                        runValue = false;
                        savePersonListCSV(verifiedFileName, fileExtension, personListBuilderService.getPersonList());
                        personListBuilderService.writeJson(personListBuilderService.getPersonList(),verifiedFileName);

                        break;
                    default:
                        System.out.println("Invalid input. Choose one of the following options:");
                        userChoice = getUserAction(userChoiceInputService);
                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // ask user if they want to add a person, print, or exit
    static int getUserAction(UserInputService userInputService) {
        int choice = userInputService.getUserInputInt(
                "What would you like to do?\n1. Add a person to the list.\n2. Print the list of current people.\n3. Exit the program.");
        return choice;
    }

    static int getFileExtenion(UserInputService userInputService) {
        int fileExtension = userInputService.getUserInputInt("What extension is your file?\n1. \".csv\"\n2. \".json\"");
        return fileExtension;
    }

    // print each person in JSON format
    static void printPersonListAsJSON(List<Person> personList) throws Exception {
        String json = new ObjectMapper().writeValueAsString(personList);
        System.out.println(json);
    }

    // save to json file
    static void savePersonListCSV(String fileName, String fileExtension, List<Person> personList) throws Exception {
        StringBuffer allPersonsAsCSV = new StringBuffer();
        if(fileExtension.equals(".csv")){
            personList.forEach((person) -> {
            String personString = person.formatAsCSV();
            allPersonsAsCSV.append(personString + "\n");
        });
        writeToFile(fileName,allPersonsAsCSV.toString());
        printPersonListAsJSON(personList);
    }
    else {
            personList.stream().map(person -> "").forEach(personString -> {
                try {
                    String json = new ObjectMapper().writeValueAsString(personList);
                    allPersonsAsCSV.append(json);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                try {
                    writeToFile(fileName, allPersonsAsCSV.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }



    static void writeToFile(String fileName, String text) throws IOException {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.write(text);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (fileWriter != null)
                fileWriter.close();
        }
    }



}