import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PersonListBuilderService {
    private final UserInputService userInputService;
    private List<Person> personList;

    public PersonListBuilderService(UserInputService userInputService) {
        this.userInputService = userInputService;
        personList = new ArrayList<>();
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    // print from file and add new lines
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

    // read from file if it exists and add people to the list
    public String readFromCSV(String fileName, String fileType) throws Exception {
        Scanner fileReader = null;
        fileName = fileName + fileType;
        String verifiedFileName = fileName;
        try {
            verifiedFileName = verifyFile(fileName, fileType);
            File myFile = new File(verifiedFileName);
            fileReader = new Scanner(myFile);
            String thisLine;
            while (fileReader.hasNextLine()) {
                thisLine = fileReader.nextLine();
                Person person = new Person(thisLine);
                personList.add(person);
            }

        } catch (Exception e) {
            throw new Exception(e);

        }
        return verifiedFileName;
    }

    public String readFromJSON(String fileName, String fileType, boolean addNewLine) throws Exception {

        String returnString = new String();
        fileName = fileName + fileType;
        String verifiedFileName = fileName;
        Scanner fileReader = null;
        try {

            verifiedFileName = verifyFile(fileName, fileType);
            File myFile = new File(verifiedFileName);
            fileReader = new Scanner(myFile);
            List<Person> restoredPersons = Arrays.asList(new ObjectMapper().readValue(myFile, Person[].class));
            this.personList = restoredPersons;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (fileReader != null)
                fileReader.close();
        }

        return returnString;
    }

    // make sure the file name belongs to an exisiting file
    public String verifyFile(String fileName, String fileType) throws Exception {
        String verifiedFileName = fileName;
        File myFile = new File(fileName);
        while (!myFile.exists()) {
            String newFileName = userInputService
                    .getUserInput("Enter a different file name since that file is not found.");
            verifiedFileName = newFileName;
            myFile = new File(verifiedFileName);
        }

        return verifiedFileName;
    }

    // add person to list of persons
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

    // write to files i.e save
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
