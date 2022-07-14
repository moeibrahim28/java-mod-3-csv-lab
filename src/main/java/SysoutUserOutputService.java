public class SysoutUserOutputService implements UserOutputService {

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

}