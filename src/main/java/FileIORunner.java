import java.io.IOException;

public class FileIORunner {
    public static void main(String[] args) throws IOException {
        //writeToFile("person.csv", "example of writing data to file");
        UserOutputService userOutputService = new SysoutUserOutputService();
        try (UserInputService userInputService = new ScannerUserInputService(userOutputService)) {
            boolean runValue = true;
            PersonBuilderService personBuilderService = new PersonBuilderService(userInputService);
            PersonListBuilderService personListBuilderService = new PersonListBuilderService(personBuilderService, userInputService);

            //Ask user if they want to restore a list if not create one
            String fileName = userInputService.askUserAboutFile();
            String verifiedFileName;
            if (!fileName.isBlank()) {
                String nonBlankName = personListBuilderService.readFromFile(fileName);
                verifiedFileName=nonBlankName;
            } else {
                String newFileName = userInputService.getUserInput("Please give the name of this new file. It will be made a .csv file by the system.");
                newFileName = newFileName + ".csv";
                verifiedFileName=newFileName;
            }


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

    static int getUserAction(UserInputService userInputService) {
        int choice = userInputService.getUserInputInt("What would you like to do?\n1. Add a person to the list.\n2. Print the list of current people.\n3. Exit the program.");
        return choice;
    }

}