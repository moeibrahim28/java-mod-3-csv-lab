import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class ScannerUserInputService implements UserInputService {
    private final Scanner scanner;
    private final UserOutputService userOutputService;

    public ScannerUserInputService(UserOutputService userOutputService) {
        this.scanner = new Scanner(System.in);
        this.userOutputService = userOutputService;
    }

    public String getUserInput(String prompt) {
        userOutputService.printMessage(prompt);
        String input = scanner.nextLine();
        if (input.isBlank()) {
            return getUserInput(prompt);
        }
        return input;
    }

    public int getUserInputInt(String prompt) {
        userOutputService.printMessage(prompt);
        String input = scanner.nextLine();

        try {
            return Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println("Invalid input. Enter an integer.");
            return getUserInputInt(prompt);
        }
    }


    @Override
    public void close() throws Exception {
        scanner.close();

    }

    @Override
    public String askUserAboutFile() throws IOException {
        String fileReaderName = null;
        int input = getUserInputInt("Choose an option below: \n1. Use an existing file\n2. Create a new file");
        switch (input) {
            case 1:
                fileReaderName = getUserInput("Enter a file name without extension");
                break;
            case 2:
                fileReaderName = "";
                break;
            default:
                System.out.println("Invalid input. Choose one of the options listed:");
                askUserAboutFile();
        }

        return fileReaderName;
    }






}