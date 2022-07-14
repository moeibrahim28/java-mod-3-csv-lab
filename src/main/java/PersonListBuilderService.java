import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PersonListBuilderService {
    private final PersonBuilderService personBuilderService;
    private final UserInputService userInputService;
    private final List<Person> personList;

    public PersonListBuilderService(PersonBuilderService personBuilderService, UserInputService userInputService) {
        this.personBuilderService = personBuilderService;
        this.userInputService = userInputService;
        personList = new ArrayList<>();
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void printFromFile(String fileName, boolean addNewLine) throws IOException {
        String returnString = "";
        Scanner fileReader = null;
        try {
            File myFile = new File(fileName);
            fileReader = new Scanner(myFile);
            while (fileReader.hasNextLine()) {
                returnString += fileReader.nextLine();
                if (addNewLine)
                    returnString += "\n";
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        System.out.println(returnString);
    }

    public String readFromFile(String fileName) throws Exception {
        Scanner fileReader = null;
        fileName=fileName+".csv";
        String verifiedFileName="";
        try {
            verifiedFileName=verifyFile(fileName);
            File myFile = new File(verifiedFileName);
            fileReader = new Scanner(myFile);
            String thisLine;
            while (fileReader.hasNextLine()) {
                thisLine = fileReader.nextLine();
                Person person = new Person(thisLine);
                personList.add(person);
            }


        } catch (
                Exception e) {
            throw new Exception(e);

        }
        return verifiedFileName;
    }


    public String verifyFile(String fileName) throws Exception {
        String verifiedFileName = fileName;
            File myFile = new File(fileName);
            while (!myFile.exists()) {
                String newFileName=userInputService.getUserInput("Enter a different file name since that file is not found.");
                verifiedFileName=newFileName;
                myFile=new File(verifiedFileName);
            }

        return verifiedFileName;
    }

    void addPersonToList(String fileName) throws IOException {
        String firstName = userInputService.getUserInput("What is their first name?");
        String lastName = userInputService.getUserInput("What is their last name?");
        int yearBorn = userInputService.getUserInputInt("What year were they born?");
        int monthBorn = userInputService.getUserInputInt("What month were they born?");
        int dateBorn = userInputService.getUserInputInt("What date were they born?");
        Person person = new Person(firstName, lastName, yearBorn, monthBorn, dateBorn);
        personList.add(person);
        writeToFile(fileName, personList);
    }



    void writeToFile(String fileName, List<Person> personList) throws IOException {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
            for (Person person : personList) {
                fileWriter.write(person.formatAsCSV() + "\n");
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (fileWriter != null)
                fileWriter.close();
        }
    }

}
