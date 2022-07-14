public class PersonBuilderService {
    private final UserInputService userInputService;

    public PersonBuilderService(UserInputService userInputService) {
        this.userInputService = userInputService;
    }

    public Person createNewPerson() {
        String first_name = userInputService.getUserInput("What's the person's first name?");
        String last_name = userInputService.getUserInput("What's the person's last name?");
        int year_born = userInputService.getUserInputInt("What year were they born?");
        int month_born = userInputService.getUserInputInt("What month were they born?");
        int day_born = userInputService.getUserInputInt("What date were they born?");
        Person person = new Person(first_name, last_name, year_born, month_born, day_born);
        return person;
    }


}