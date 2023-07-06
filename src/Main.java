import javax.swing.*;
import java.sql.*;
import java.time.LocalTime;
import java.util.Random;

public class Main {

    public static LocalTime timeTrack;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JDialog.setDefaultLookAndFeelDecorated(true);
        JOptionPane.showMessageDialog(null, "--- Welcome To Fly Mars --- ",
                "Welcome", JOptionPane.INFORMATION_MESSAGE);
        boolean isAppRunning = true;
        while (isAppRunning) {
            int chosenOption = Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Please choose an option :\n 1. Log-In\n 2. Sign-Up\n 3. Exit", "Log-In/Sign-Up Page"
                    , JOptionPane.QUESTION_MESSAGE));
            switch (chosenOption) {
                case 1:
                    Human currentHuman = Human.logIn();
                    Role currentRole = currentHuman.getRole();

                    switch (currentRole){
                        case PASSENGER:
                            Human.passengerPanel(currentHuman);
                            break;
                        case AGENT:
                            Human.agentPanel(currentHuman);
                            break;
                        case SUPERADMIN:
                            Human.superAdminPanel(currentHuman);
                            break;
                    }
                    break;
                case 2:
                    Human.signUp();
                    continue;
                case 3:
                    JOptionPane.showMessageDialog(null, "Thank you ! \uD83D\uDE09",
                            "Exit Page", JOptionPane.INFORMATION_MESSAGE);
                    isAppRunning = false;
                    break;
            }

        }
    }

    public static String generateOTPCode(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        return String.format("%06d", number);
    }

}
