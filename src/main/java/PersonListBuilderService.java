import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    public void readFromCSV(String fileName, String fileType) throws Exception {
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


        setPersonList(personList);
    }

    public List<Person> readPersonListFromJSON(String fileName) {

        List<Person> restoredPersons = null;
        System.out.println("The user has chosen this file: " + fileName);
        // If the File of this file name does not exist, exit program
        if (!new File(fileName).exists()) {
            System.out.println("Can't open file.");

        }


        //Get the contents of the file from our File Reader, made by curriculum.
        String jsonFileContents = FileReader.readFromFile(fileName);
        try {
            restoredPersons = new ArrayList<>(Arrays.asList(new ObjectMapper().readValue(jsonFileContents, Person[].class)));
            setPersonList(restoredPersons);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return restoredPersons;


    }

    // make sure the file name belongs to an exisiting file
    public String verifyFile(String fileName, String fileType) throws Exception {
        String verifiedFileName = fileName;
        File myFile = new File(fileName);
        while (!myFile.exists()) {
            String newFileName = getNewFileName();
            String extension = getFileExtenion();

            verifiedFileName = newFileName + extension;
            myFile = new File(verifiedFileName);
        }

        return verifiedFileName;
    }

    public String getNewFileName(){
        return userInputService.getUserInput("Enter a new file name:");
    }

    // add person to list of persons
    void addPersonToList(String fileName, String fileExtension) throws IOException {
        String firstName = userInputService.getUserInput("What is their first name?");
        String lastName = userInputService.getUserInput("What is their last name?");
        int yearBorn = userInputService.getUserInputInt("What year were they born?");
        int monthBorn = userInputService.getUserInputInt("What month were they born?");
        int dateBorn = userInputService.getUserInputInt("What date were they born?");
        Person person = new Person(firstName, lastName, yearBorn, monthBorn, dateBorn);
        this.personList.add(person);
        writeToFile(fileName, ".csv", personList);
        writeJson(personList, fileName);

    }

    // write to files i.e save
    void writeToFile(String fileName, String extension, List<Person> personList) throws IOException {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName + extension);
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

    public void createPersonList(String fileName, String fileExtension) throws Exception {

        String name = userInputService.getUserInput("What's the file name? If one already exists in a json \nfile please enter the file name with the .json extension.");
        if (fileExtension.equals(".csv")) {

            readFromCSV(fileName, fileExtension);
            writeJson(getPersonList(), fileName + ".json");


        } else {

            //read from json file

            List<Person> personList1 = readPersonListFromJSON(fileName);
            writeJson(personList1, name);


        }

    }

    public void writeJson(List<Person> personList, String fileName) throws JsonProcessingException {
        //JSON
        // A student object will consist of
        // {"firstname":"", "lastName":"", "studentNumber":#}
        // Example:
        // Car
        // objectname: {"EngineName":Engine, "YearCreated":2022 ...... Motors: [                ]}

        //After importing 'com.fasterxml.jackson' using the Canvas tutorial
        //I can now use ObjectMapper to write our object as a JSON string:
        String json = new ObjectMapper().writeValueAsString(personList);

        try {
            FileReader.writeToFile(fileName + ".json", json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printJson(List<Person> personList) throws JsonProcessingException {
        //JSON
        // A student object will consist of
        // {"firstname":"", "lastName":"", "studentNumber":#}
        // Example:
        // Car
        // objectname: {"EngineName":Engine, "YearCreated":2022 ...... Motors: [                ]}

        //After importing 'com.fasterxml.jackson' using the Canvas tutorial
        //I can now use ObjectMapper to write our object as a JSON string:
        String json = new ObjectMapper().writeValueAsString(personList);
        System.out.println(json);
    }

    public String getFileExtenion() {
        String fileExtension = "";
        int extensionChoice = userInputService.getUserInputInt("What extension is your file?\n1. \".csv\"\n2. \".json\"");
        switch (extensionChoice) {
            case 1:
                fileExtension = ".csv";
                break;
            case 2:
                fileExtension = ".json";
                break;
            default:
                System.out.println("Invalid input. Choose one of the following options:");
                fileExtension = getFileExtenion();
        }

        return fileExtension;

    }
}
