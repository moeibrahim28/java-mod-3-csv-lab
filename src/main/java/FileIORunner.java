import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileIORunner {
    public static void main(String[] args) throws IOException {
        //writeToFile("person.csv", "example of writing data to file");
        UserOutputService userOutputService = new SysoutUserOutputService();
        try (UserInputService userInputService = new ScannerUserInputService(userOutputService)) {
            boolean runValue = true;
            PersonListBuilderService personListBuilderService = new PersonListBuilderService(userInputService);

            //Ask user if they want to restore a list if not create one
            String fileName = userInputService.askUserAboutFile();
            String verifiedFileName;

            //check validity of file name and get a verified filename
            if (!fileName.isBlank()) {
                String nonBlankName = personListBuilderService.readFromFile(fileName);
                verifiedFileName = nonBlankName;
            } else {
                String newFileName = userInputService.getUserInput("Please give the name of this new file. It will be made a .csv file by the system.");
                newFileName = newFileName + ".csv";
                verifiedFileName = newFileName;
            }

            //loops for asking user to pick an action
            while (runValue) {
                UserOutputService userChoiceOutputService = new SysoutUserOutputService();
                UserInputService userChoiceInputService = new ScannerUserInputService(userChoiceOutputService);
                int userChoice = getUserAction(userChoiceInputService);
                switch (userChoice) {
                    case 1:
                        personListBuilderService.addPersonToList(verifiedFileName);
                        break;
                    case 2:
                        personListBuilderService.printFromFile(verifiedFileName, true);
                        break;
                    case 3:
                        runValue = false;
                        savePersonListJSON(verifiedFileName.substring(0,verifiedFileName.length() - 4)+".json", personListBuilderService.getPersonList());
                        printPersonListAsJSON(personListBuilderService.getPersonList());
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


    //ask user if they want to add a person, print, or exit
    static int getUserAction(UserInputService userInputService) {
        int choice = userInputService.getUserInputInt("What would you like to do?\n1. Add a person to the list.\n2. Print the list of current people.\n3. Exit the program.");
        return choice;
    }

    //print each person in JSON format
    static void printPersonListAsJSON(List<Person> personList) throws Exception {
        String json = new ObjectMapper().writeValueAsString(personList);
        System.out.println(json);
}


    //save to json file
    static void savePersonListJSON(String fileName, List<Person> personList) throws Exception {
        String allPersonsAsJSON = new ObjectMapper().writeValueAsString(personList);
        writeToFile(fileName, allPersonsAsJSON);
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