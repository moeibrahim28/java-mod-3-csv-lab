import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public interface UserInputService extends AutoCloseable{
    String getUserInput(String prompt);
    int getUserInputInt(String prompt);
    String askUserAboutFile() throws IOException;
}