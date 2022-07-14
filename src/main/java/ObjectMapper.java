import java.util.List;

public class ObjectMapper {
    public String writeValueAsString(List<Person> personList) {

        StringBuffer personStringJSON = new StringBuffer();
        personStringJSON.append("[\n");
        for (Person person : personList) {
            personStringJSON.append("\t{\n");
            personStringJSON.append("\t\tfirstName: \""+ person.getFirstName()+"\"");
            personStringJSON.append(",\n");
            personStringJSON.append("\t\tlastName: \""+ person.getLastName()+"\"");
            personStringJSON.append(",\n");
            personStringJSON.append("\t\tbirthYear: "+ person.getBirthYear());
            personStringJSON.append(",\n");
            personStringJSON.append("\t\tbirthMonth: "+ person.getBirthMonth());
            personStringJSON.append(",\n");
            personStringJSON.append("\t\tbirthDay: "+ person.getBirthDay());
            personStringJSON.append("\n\t},\n");
        }
        personStringJSON.append("];");
            return personStringJSON.toString();
        }
    }

