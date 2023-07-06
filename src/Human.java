import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Human {

    private int primaryKey;
    private String email;
    private String phoneNumber;
    private String passWord;
    private boolean verifyStatus;
    private String firstName;
    private String lastName;
    private Role role;
    private String nationalCode;
    private Date birthOfDate;


    public Human(String email, String phoneNumber, String passWord, Role role, String firstName,
                 String lastName, int primaryKey) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passWord = passWord;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.primaryKey = primaryKey;
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public Date getBirthOfDate() {
        return birthOfDate;
    }

    public void setBirthOfDate(Date birthOfDate) {
        this.birthOfDate = birthOfDate;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public static void signUp() throws SQLException {
        String email = null;
        String passWord;
        String phoneNumber = null;
        boolean verifyStatus = false;
        Role role = Role.PASSENGER;
        String firstName;
        String lastName;

        boolean inSignedUp = true;
        while (inSignedUp) {

            switch (Integer.parseInt(JOptionPane.showInputDialog(null, "Please choose " +
                    "your role :\n1. Passenger\n2. Super Admin \n3. Agent"))) {
                case 1:
                    role = Role.PASSENGER;
                    break;
                case 2:
                    role = Role.SUPERADMIN;
                    break;
                case 3:
                    role = Role.AGENT;
            }

            boolean isNotEmailPhoneTrue = true;
            while (isNotEmailPhoneTrue) {
                email = JOptionPane.showInputDialog(null, "Please enter your Email Address:",
                        "Sign-Up page", JOptionPane.QUESTION_MESSAGE);


                if (email.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill out all parts!");
                    continue;
                }

                phoneNumber = JOptionPane.showInputDialog(null, "Please enter your phoneNumber:",
                        "Sign-Up page", JOptionPane.QUESTION_MESSAGE);

                if (phoneNumber.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill out all parts!");
                    continue;
                }

                try {
                    Class.forName("org.postgresql.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                            "aliiiw", "ali123");

                    String sqlQuery = "SELECT email, phoneNumber FROM human WHERE email = ?" +
                            " or phoneNumber = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, phoneNumber);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(null, "This email or phone number " +
                                        "have been chosen before. try another one!!!",
                                "Alert", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        isNotEmailPhoneTrue = false;
                    }
                    

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }


            while (true) {
                firstName = JOptionPane.showInputDialog(null, "Please Enter your firstName :",
                        "Sign-Up Page", JOptionPane.QUESTION_MESSAGE);
                lastName = JOptionPane.showInputDialog(null, "Please Enter your lastName :",
                        "Sign-Up Page", JOptionPane.QUESTION_MESSAGE);

                passWord = JOptionPane.showInputDialog(null, "Please Enter your passWord :",
                        "Sign-Up Page", JOptionPane.QUESTION_MESSAGE);
                if ((passWord != null) && ((passWord.length() > 4))) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Not accepted ! Please try another one\nYour password must have at least 5 characters ",
                            "Sign-Up page", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            try {
                Class.forName("org.postgresql.Driver");
                Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                        "aliiiw", "ali123");

                String kPassword = "passWord";
                StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                encryptor.setPassword(kPassword);
                String encrypted= encryptor.encrypt(passWord);

                String sqlQuery = "INSERT INTO human (email, phoneNumber, password, verifyStatus, userRole," +
                        " firstName, lastName) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, phoneNumber);
                preparedStatement.setString(3, encrypted);
                preparedStatement.setBoolean(4, verifyStatus);
                preparedStatement.setObject(5, role, Types.OTHER);
                preparedStatement.setString(6, firstName);
                preparedStatement.setString(7, lastName);


                int rows = preparedStatement.executeUpdate();

                boolean isNotDone = true;
                while (isNotDone) {
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(null, "Your account has been created ,but it " +
                                        "has not still verified. please verify it.",
                                "Alert", JOptionPane.INFORMATION_MESSAGE);
                    }


                    String OTPCode = Main.generateOTPCode();
                    sqlQuery = "INSERT INTO human_otp (email, otp) " +
                            "VALUES (?, ?)";
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, OTPCode);

                    preparedStatement.executeUpdate();

                    SendEmail.SendEmailToUser(email, OTPCode);


                    String otpByHuman = JOptionPane.showInputDialog(null, "Please Enter the OTP Code :",
                            "Sign-Up Page", JOptionPane.QUESTION_MESSAGE);

                    if (LocalDateTime.now().getMinute() - Main.timeTrack.getMinute() < 2) {

                        sqlQuery = "SELECT otp FROM human_otp WHERE email = ?";
                        preparedStatement = connection.prepareStatement(sqlQuery);
                        preparedStatement.setString(1, email);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        while (resultSet.next()) {
                            if (resultSet.getString(1).equals(otpByHuman)) {
                                JOptionPane.showMessageDialog(null, "You have signed in successfully",
                                        "verification", JOptionPane.INFORMATION_MESSAGE);
                                sqlQuery = "UPDATE human SET verifyStatus = ? WHERE email = ?";
                                preparedStatement = connection.prepareStatement(sqlQuery);
                                preparedStatement.setBoolean(1, true);
                                preparedStatement.setString(2, email);
                                preparedStatement.executeUpdate();

                                break;
                            } else {
                                JOptionPane.showMessageDialog(null, "You entered wrong verification code",
                                        "verification", JOptionPane.INFORMATION_MESSAGE);
                            }

                        }
                        isNotDone = false;

                    } else {
                        JOptionPane.showMessageDialog(null, "verification time is over",
                                "verification", JOptionPane.INFORMATION_MESSAGE);
                    }
                    System.out.println(LocalDateTime.now().getMinute() - Main.timeTrack.getMinute());
                    sqlQuery = "DELETE FROM human_otp WHERE email = ? ";
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, email);

                    preparedStatement.executeUpdate();

                }


            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            inSignedUp = false;
        }
    }


    public static Human logIn() throws SQLException {
        String email = null;
        String passWord = null;
        String phoneNumber = null;
        try {


            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");
            do {

                ArrayList<String> options = new ArrayList<>();
                options.add("email");
                options.add("phoneNumber");

                int chosen = JOptionPane.showOptionDialog(null, "How do you want to log in?\n" +
                                "Your email or your phoneNumber?" + "\n",
                        "logIn page",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options.toArray(), options.get(0));
                ResultSet resultSet = null;
                if (chosen == 0) {
                    email = JOptionPane.showInputDialog(null, "Please Enter your email :",
                            "logIn Page", JOptionPane.QUESTION_MESSAGE);

                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from human where email = ? ");
                    preparedStatement.setString(1, email);
                    resultSet = preparedStatement.executeQuery();


                    boolean isTruePassword = true;
                    ResultSet resultSet1 = null;
                    if (resultSet.next()) {
                        while (isTruePassword) {
                            if (resultSet.getBoolean("verifyStatus")) {
                                passWord = JOptionPane.showInputDialog(null, "Please Enter your passWord :",
                                        "logIn Page", JOptionPane.QUESTION_MESSAGE);
                                preparedStatement = connection.prepareStatement("SELECT passWord from human" +
                                        " where email = ? ");
                                preparedStatement.setString(1, email);
                                resultSet1 = preparedStatement.executeQuery();
                            } else {
                                JOptionPane.showMessageDialog(null, "Your account has not verified yet",
                                        "logIn Page", JOptionPane.INFORMATION_MESSAGE);
                            }


                            while (resultSet1.next()) {
                                String kPassWord = "passWord";
                                StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                                encryptor.setPassword(kPassWord);
                                String decrypted = encryptor.decrypt(resultSet1.getString(1));
                                System.out.println(decrypted);
                                if (decrypted.equals(passWord)) {
                                    JOptionPane.showMessageDialog(null, "Welcome Dear ",
                                            "logIn Page", JOptionPane.INFORMATION_MESSAGE);
                                    isTruePassword = false;

                                } else {
                                    JOptionPane.showMessageDialog(null, "Your password is incorrect" +
                                                    "\nplease try again",
                                            "logIn Page", JOptionPane.INFORMATION_MESSAGE);


                                }
                            }

                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "There is no email" +
                                        " such that, Please try again", "logIn Page",
                                JOptionPane.INFORMATION_MESSAGE);
                        Human.logIn();
                    }


                } else if (chosen == 1) {
                    phoneNumber = JOptionPane.showInputDialog(null, "Please Enter your phoneNumber :",
                            "logIn Page", JOptionPane.QUESTION_MESSAGE);

                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from human where phoneNumber = ? ");
                    preparedStatement.setString(1, phoneNumber);
                    resultSet = preparedStatement.executeQuery();


                    if (resultSet.next()) {
                        if (resultSet.getBoolean("verifyStatus")) {

                            passWord = JOptionPane.showInputDialog(null, "Please Enter your passWord :",
                                    "logIn Page", JOptionPane.QUESTION_MESSAGE);
                            preparedStatement = connection.prepareStatement("SELECT passWord from human where phoneNumber = ? ");
                            preparedStatement.setString(1, phoneNumber);
                            resultSet = preparedStatement.executeQuery();
                        } else {
                            JOptionPane.showMessageDialog(null, "Your account has not verified yet",
                                    "logIn Page", JOptionPane.INFORMATION_MESSAGE);
                        }

                        while (resultSet.next()) {
                            if (resultSet.getString(1).equals(passWord)) {
                                JOptionPane.showMessageDialog(null, "Welcome Dear",
                                        "logIn Page", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Your password is incorrect" +
                                                "\nplease try again",
                                        "logIn Page", JOptionPane.INFORMATION_MESSAGE);

                            }

                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "There is no phone number" +
                                        " such that, Please try again", "Sign-Up page",
                                JOptionPane.INFORMATION_MESSAGE);
                        Human.logIn();
                    }


                }

                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                        "from human where phoneNumber = ? or email = ? ");
                preparedStatement.setString(1, phoneNumber);
                preparedStatement.setString(2, email);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    Human human = new Human(resultSet.getString("email"),
                            resultSet.getString("phoneNumber"),
                            resultSet.getString("passWord"),
                            Role.valueOf(resultSet.getString("userRole")), resultSet.getString("firstName"),
                            resultSet.getString("lastName"), resultSet.getInt("id"));
                    return human;
                }


            }
            while (true);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void passengerPanel(Human passenger) throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                "aliiiw", "ali123");

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ticketStatus, date, ticket.id " +
                "from passenger_ticket join ticket on passenger_ticket.idTicket = ticket.id join travel on travel.id = " +
                "ticket.travelId where idPassenger = ?");
        preparedStatement.setInt(1, passenger.getPrimaryKey());
        ResultSet resultSetFirst = preparedStatement.executeQuery();

        String ticketStatus = null;
        String travelDate;
        int ticketId;
        while (resultSetFirst.next()) {
            ticketStatus = resultSetFirst.getString(1);
            travelDate = resultSetFirst.getString(2);
            ticketId = resultSetFirst.getInt(3);

            int isDone = LocalDate.now().compareTo(LocalDate.parse(travelDate));
            boolean isDoneBool;

            if (isDone >= 0) {
                isDoneBool = true;
            } else {
                isDoneBool = false;
            }

            if (ticketStatus.equals(TicketStatus.Paid.toString()) && isDoneBool) {

                preparedStatement = connection.prepareStatement("UPDATE ticket SET ticketStatus = ?" +
                        " WHERE id = ?");
                preparedStatement.setObject(1, TicketStatus.Done, Types.OTHER);
                preparedStatement.setInt(2, ticketId);
                preparedStatement.executeUpdate();
            }

        }


        boolean isInHomePage = true;
        while (isInHomePage) {
            int chosenOption = Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Please choose an option :\n1. buy ticket\n2. See Profile\n3. Log Out",
                    "Home page", JOptionPane.QUESTION_MESSAGE));
            switch (chosenOption) {
                case 1:
                    switch (Integer.parseInt(JOptionPane.showInputDialog(null, "Please choose " +
                            "an option :\n1. search for a ticket \n2. my reserved tickets\n" +
                            "3. my purchased tickets\n4. cancel my ticket\n5. rate the travel\n6. back"))) {
                        case 1:
                            boolean isInSearchForTicketWithFilter = true;
                            while (isInSearchForTicketWithFilter) {
                                switch (Integer.parseInt(JOptionPane.showInputDialog(null, "Please choose " +
                                        "your travel type :\n1. domestic flight \n2. abroad flight\n" +
                                        "3. train\n4. bus\n5. back"))) {
                                    case 1:
                                        searchForFlight(passenger, 1);
                                        isInSearchForTicketWithFilter = false;
                                        break;
                                    case 2:
                                        searchForFlight(passenger, 2);
                                        isInSearchForTicketWithFilter = false;
                                        break;
                                    case 3:
                                        searchForTrainOrBus(passenger, 1);
                                        isInSearchForTicketWithFilter = false;
                                        break;
                                    case 4:
                                        searchForTrainOrBus(passenger, 2);
                                        isInSearchForTicketWithFilter = false;
                                        break;
                                    case 5:
                                        isInSearchForTicketWithFilter = false;
                                        break;
                                }
                            }
                        case 2:
                            passenger.showAllMyReservedTickets(passenger);
                            break;

                        case 3:
                            passenger.showAllMyPurchasedTickets();
                            break;
                        case 4:
                            passenger.cancelTheTravel();
                            break;
                        case 5:
                            passenger.rateToTravel();
                            break;
                        case 6:
                            break;

                    }

                case 2:
                    switch (Integer.parseInt(JOptionPane.showInputDialog(null, "Please choose " +
                            "an option :\n1. Show all my detail\n2. Edit Profile\n3. back"))) {
                        case 1:
                            passenger.seeProfile();
                            break;
                        case 2:
                            passenger.editProfile();
                            break;
                        case 3:
                            break;
                    }
                    break;


            }
        }
    }


    public void editProfile() throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");
            int chosenOpt = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Choose an Option Which You" +
                            " Want to edit :\n1. Name\n2. Last Name\n3 .National code\n4. back", "Edit Profile",
                    JOptionPane.QUESTION_MESSAGE));
            switch (chosenOpt) {
                case 1:
                    String newFirstName = JOptionPane.showInputDialog(null, "Please Enter your new " +
                            "first name", "Edit firstName", JOptionPane.QUESTION_MESSAGE);
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE human SET firstName = ? WHERE " +
                            "email = ?");
                    preparedStatement.setString(1, newFirstName);
                    preparedStatement.setString(2, this.getEmail());
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Your fistName has edited " +
                            "successfully", "Edit username", JOptionPane.INFORMATION_MESSAGE);
                    setFirstName(newFirstName);
                    break;
                case 2:
                    String newLastName = JOptionPane.showInputDialog(null, "Please Enter your new " +
                            "last name", "Edit last name", JOptionPane.QUESTION_MESSAGE);
                    preparedStatement = connection.prepareStatement("UPDATE human SET lastName = ? WHERE " +
                            "email = ?");
                    preparedStatement.setString(1, newLastName);
                    preparedStatement.setString(2, getEmail());
                    JOptionPane.showMessageDialog(null, "Your LastName has edited " +
                            "successfully", "Edit last name", JOptionPane.INFORMATION_MESSAGE);
                    setPassWord(newLastName);
                    break;
                case 3:
                    String newPassWord = JOptionPane.showInputDialog(null, "Please Enter your new " +
                            "PassWord", "Edit PassWord", JOptionPane.QUESTION_MESSAGE);
                    preparedStatement = connection.prepareStatement("UPDATE human SET passWord = ? WHERE " +
                            "email = ?");
                    preparedStatement.setString(1, newPassWord);
                    preparedStatement.setString(2, getEmail());
                    JOptionPane.showMessageDialog(null, "Your PassWord has edited " +
                            "successfully", "Edit password", JOptionPane.INFORMATION_MESSAGE);
                    setPassWord(newPassWord);
                    break;
                case 4:
                    break;

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void seeProfile() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            StringBuilder allInformation = new StringBuilder();

            allInformation.append("first name : " + this.firstName + "\n").append("last name : " + this.lastName + "\n")
                    .append("email : " + this.email + "\n").append("phone number : " + this.phoneNumber + "\n")
                    .append("logged in as " + this.role + "\n").append("date of birth : " + this.birthOfDate + "\n")
                    .append("national code : " + this.nationalCode + "\n");
            JOptionPane.showMessageDialog(null, allInformation, "See Profile",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void searchForFlight(Human passenger, int domesticAbroad) throws SQLException {
        ArrayList<Integer> pkOfTravels = new ArrayList<>();
        StringBuilder allTravels = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            int startRate = 0;
            int endRate = 0;

            ArrayList<String> options = new ArrayList<>();

            options.add("yes");
            options.add("no");

            int chosen = JOptionPane.showOptionDialog(null, "do you want to determine the rate of " +
                            "agency?" + "\n", "ticket reservation",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options.toArray(), options.get(0));

            if(chosen == 0){
                startRate = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the start of rate range",
                        "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                endRate = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the end of rate range",
                        "ticket reservation", JOptionPane.QUESTION_MESSAGE));


            }
            else if (chosen == 1){
                startRate = 1;
                endRate = 5;

            }

            switch (Integer.parseInt(JOptionPane.showInputDialog(null,
                    "On what basis you want to filter it? :\n1. origin and destination \n" +
                            "2. origin and destination and price\n3. origin and destination and date\n" +
                            "4. origin and destination and price and date\n5. back"))) {
                case 1:
                    String origin = JOptionPane.showInputDialog(null, "Please Enter your origin",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    ;String destination = JOptionPane.showInputDialog(null, "Please Enter your destination",
                        "ticket reservation", JOptionPane.QUESTION_MESSAGE);


                    int idOriginCity1 = 0;
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, origin);
                    ResultSet resultSetCity1 = preparedStatement.executeQuery();
                    if (resultSetCity1.next()) {
                        idOriginCity1 = resultSetCity1.getInt("id");
                    }
                    int idOriginCity2 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, destination);
                    ResultSet resultSetCity2 = preparedStatement.executeQuery();
                    if (resultSetCity2.next()) {
                        idOriginCity2 = resultSetCity2.getInt("id");
                    }


                    preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = " +
                            "idTravel join agency on agency.id = idAgency where origin = ?" +
                            "and destination = ? and vehicle = ? and agency.rate between ? and ?");
                    preparedStatement.setInt(1, idOriginCity1);
                    preparedStatement.setInt(2, idOriginCity2);
                    preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                    preparedStatement.setInt(4, startRate);
                    preparedStatement.setInt(5, endRate);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {

                        boolean isNullOrigin = getCountryFromCity(origin) == null;
                        boolean isNullDestination = getCountryFromCity(destination) == null;

                        if (!isNullOrigin && !isNullDestination) {
                            if (!getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination)) && domesticAbroad == 1) {
                                JOptionPane.showMessageDialog(null, "It's a domestic flight," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            } else if (getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination)) && domesticAbroad == 2) {
                                JOptionPane.showMessageDialog(null, "It's an abroad flight," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            } else {

                                preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = " +
                                        "idTravel join agency on agency.id = idAgency where origin = ?" +
                                        "and destination = ? and vehicle = ? and agency.rate between ? and ?");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                                preparedStatement.setInt(4, startRate);
                                preparedStatement.setInt(5, endRate);
                                resultSet = preparedStatement.executeQuery();

                                int counter = 1;
                                while (resultSet.next()) {


                                    pkOfTravels.add(resultSet.getInt("travel.id"));
                                    allTravels = allTravels.append(counter + " - " + "Date : " + resultSet.getString("date") + "\n").
                                            append("time : " + resultSet.getString("time") + "\n").append("origin : " +
                                                    origin + "\n").
                                            append("destination : " + destination + "\n").
                                            append("remaining seats : " + resultSet.getString("seatsRemain") + "\n");
                                    counter++;

                                }

                                int myTravel = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the travel" +
                                                " you want to reserve a ticket for it\n0 - Back\n" + allTravels,
                                        "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                                if (myTravel > 0) {
                                    passenger.reserveTicket(pkOfTravels.get(myTravel - 1));
                                } else if (myTravel == 0) {
                                    return;
                                }
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "there is no origin or destination such that " +
                                            "please try again", "ticket reservation",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    break;
                case 2:
                    origin = JOptionPane.showInputDialog(null, "Please Enter your origin",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    destination = JOptionPane.showInputDialog(null, "Please Enter your destination",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    int startPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the price floor"
                            , JOptionPane.QUESTION_MESSAGE));
                    int endPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the price ceiling",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));


                    idOriginCity1 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, origin);
                    resultSetCity1 = preparedStatement.executeQuery();
                    if (resultSetCity1.next()) {
                        idOriginCity1 = resultSetCity1.getInt("id");
                    }
                    idOriginCity2 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, destination);
                    resultSetCity2 = preparedStatement.executeQuery();
                    if (resultSetCity2.next()) {
                        idOriginCity2 = resultSetCity2.getInt("id");
                    }


                    preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = idTravel" +
                            " join agency on agency.id = idAgency where origin = ?" +
                            "and destination = ? and vehicle = ? and price between ? and ? and agency.rate between ? and ?");
                    preparedStatement.setInt(1, idOriginCity1);
                    preparedStatement.setInt(2, idOriginCity2);
                    preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                    preparedStatement.setInt(4, startPrice);
                    preparedStatement.setInt(5, endPrice);
                    preparedStatement.setInt(6, startRate);
                    preparedStatement.setInt(7, endRate);
                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {

                        boolean isNullOrigin = getCountryFromCity(origin) == null;
                        boolean isNullDestination = getCountryFromCity(destination) == null;

                        if (!isNullOrigin && !isNullDestination) {
                            if (!getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination)) && domesticAbroad == 1) {
                                JOptionPane.showMessageDialog(null, "It's a domestic flight," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            } else if (getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination)) && domesticAbroad == 2) {
                                JOptionPane.showMessageDialog(null, "It's an abroad flight," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            } else {

                                preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = idTravel" +
                                        " join agency on agency.id = idAgency where origin = ?" +
                                        "and destination = ? and vehicle = ? and price between ? and ? and agency.rate between ? and ?");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                                preparedStatement.setInt(4, startPrice);
                                preparedStatement.setInt(5, endPrice);
                                preparedStatement.setInt(6, startRate);
                                preparedStatement.setInt(7, endRate);
                                resultSet = preparedStatement.executeQuery();

                                int counter = 1;
                                while (resultSet.next()) {


                                    pkOfTravels.add(resultSet.getInt("travel.id"));
                                    allTravels = allTravels.append(counter + " - " + "Date : " + resultSet.getString("date") + "\n").
                                            append("time : " + resultSet.getString("time") + "\n").append("origin : " +
                                                    origin + "\n").
                                            append("destination : " + destination + "\n").
                                            append("remaining seats : " + resultSet.getString("seatsRemain") + "\n");
                                    counter++;

                                }

                                int myTravel = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the travel" +
                                                " you want to reserve a ticket for it\n0 - Back\n" + allTravels,
                                        "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                                if (myTravel > 0) {
                                    passenger.reserveTicket(pkOfTravels.get(myTravel - 1));
                                } else if (myTravel == 0) {
                                    return;
                                }
                            }


                        } else {
                            JOptionPane.showMessageDialog(null, "there is no origin or destination such that " +
                                            "please try again", "ticket reservation",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    break;
                case 3:
                    origin = JOptionPane.showInputDialog(null, "Please Enter your origin",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    destination = JOptionPane.showInputDialog(null, "Please Enter your destination",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    LocalDate startDate = LocalDate.parse(JOptionPane.showInputDialog(null, "Please Enter start date",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                    LocalDate endDate = LocalDate.parse(JOptionPane.showInputDialog(null, "Please Enter the end date",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));


                    idOriginCity1 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, origin);
                    resultSetCity1 = preparedStatement.executeQuery();
                    if (resultSetCity1.next()) {
                        idOriginCity1 = resultSetCity1.getInt("id");
                    }
                    idOriginCity2 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, destination);
                    resultSetCity2 = preparedStatement.executeQuery();
                    if (resultSetCity2.next()) {
                        idOriginCity2 = resultSetCity2.getInt("id");
                    }


                    preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = \" +\n" +
                            "                            \"idTravel join agency on agency.id = idAgency where origin = ?" +
                            "and destination = ? and vehicle = ? and price between ? and ? and agency.rate between ? and ? ");
                    preparedStatement.setInt(1, idOriginCity1);
                    preparedStatement.setInt(2, idOriginCity2);
                    preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                    preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
                    preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
                    preparedStatement.setInt(6, startRate);
                    preparedStatement.setInt(7, endRate);
                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {

                        boolean isNullOrigin = getCountryFromCity(origin) == null;
                        boolean isNullDestination = getCountryFromCity(destination) == null;

                        if (!isNullOrigin && !isNullDestination) {
                            if (!getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination)) && domesticAbroad == 1) {
                                JOptionPane.showMessageDialog(null, "It's a domestic flight," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            } else if (getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination)) && domesticAbroad == 2) {
                                JOptionPane.showMessageDialog(null, "It's an abroad flight," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            } else {

                                preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = \" +\n" +
                                        "                            \"idTravel join agency on agency.id = idAgency where origin = ?" +
                                        "and destination = ? and vehicle = ? and price between ? and ? and agency.rate between ? and ? ");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                                preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
                                preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
                                preparedStatement.setInt(6, startRate);
                                preparedStatement.setInt(7, endRate);
                                resultSet = preparedStatement.executeQuery();

                                int counter = 1;
                                while (resultSet.next()) {


                                    pkOfTravels.add(resultSet.getInt("travel.id"));
                                    allTravels = allTravels.append(counter + " - " + "Date : " + resultSet.getString("date") + "\n").
                                            append("time : " + resultSet.getString("time") + "\n").append("origin : " +
                                                    origin + "\n").
                                            append("destination : " + destination + "\n").
                                            append("remaining seats : " + resultSet.getString("seatsRemain") + "\n");
                                    counter++;

                                }

                                int myTravel = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the travel" +
                                                " you want to reserve a ticket for it\n0 - Back\n" + allTravels,
                                        "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                                if (myTravel > 0) {
                                    passenger.reserveTicket(pkOfTravels.get(myTravel - 1));
                                } else if (myTravel == 0) {
                                    return;
                                }
                            }


                        }


                    }
                case 4:
                    origin = JOptionPane.showInputDialog(null, "Please Enter your origin",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    destination = JOptionPane.showInputDialog(null, "Please Enter your destination",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    startPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the price floor"
                            , JOptionPane.QUESTION_MESSAGE));
                    endPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the price ceiling",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                    startDate = LocalDate.parse(JOptionPane.showInputDialog(null, "Please Enter start date",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                    endDate = LocalDate.parse(JOptionPane.showInputDialog(null, "Please Enter the end date",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));


                    idOriginCity1 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, origin);
                    resultSetCity1 = preparedStatement.executeQuery();
                    if (resultSetCity1.next()) {
                        idOriginCity1 = resultSetCity1.getInt("id");
                    }
                    idOriginCity2 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, destination);
                    resultSetCity2 = preparedStatement.executeQuery();
                    if (resultSetCity2.next()) {
                        idOriginCity2 = resultSetCity2.getInt("id");
                    }


                    preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = idTravel" +
                            " join agency on agency.id = idAgency where origin = ?" +
                            "and destination = ? and vehicle = ? and price between ? and ? and date between ? and ? and agency.rate between ? and ? ");
                    preparedStatement.setInt(1, idOriginCity1);
                    preparedStatement.setInt(2, idOriginCity2);
                    preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                    preparedStatement.setInt(4, startPrice);
                    preparedStatement.setInt(5, endPrice);
                    preparedStatement.setDate(6, java.sql.Date.valueOf(startDate));
                    preparedStatement.setDate(7, java.sql.Date.valueOf(endDate));
                    preparedStatement.setInt(8, startRate);
                    preparedStatement.setInt(9, endRate);
                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {

                        boolean isNullOrigin = getCountryFromCity(origin) == null;
                        boolean isNullDestination = getCountryFromCity(destination) == null;

                        if (!isNullOrigin && !isNullDestination) {
                            if (!getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination)) && domesticAbroad == 1) {
                                JOptionPane.showMessageDialog(null, "It's a domestic flight," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else if (getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination)) && domesticAbroad == 2) {
                                JOptionPane.showMessageDialog(null, "It's an abroad flight," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {

                                preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = idTravel" +
                                        " join agency on agency.id = idAgency where origin = ?" +
                                        "and destination = ? and vehicle = ? and price between ? and ? and date between ? and ? and agency.rate between ? and ? ");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                                preparedStatement.setInt(4, startPrice);
                                preparedStatement.setInt(5, endPrice);
                                preparedStatement.setDate(6, java.sql.Date.valueOf(startDate));
                                preparedStatement.setDate(7, java.sql.Date.valueOf(endDate));
                                preparedStatement.setInt(8, startRate);
                                preparedStatement.setInt(9, endRate);
                                resultSet = preparedStatement.executeQuery();

                                int counter = 1;
                                while (resultSet.next()) {


                                    pkOfTravels.add(resultSet.getInt("travel.id"));
                                    allTravels = allTravels.append(counter + " - " + "Date : " + resultSet.getString("date") + "\n").
                                            append("time : " + resultSet.getString("time") + "\n").append("origin : " +
                                                    origin + "\n").
                                            append("destination : " + destination + "\n").
                                            append("remaining seats : " + resultSet.getString("seatsRemain") + "\n");
                                    counter++;

                                }

                                int myTravel = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the travel" +
                                                " you want to reserve a ticket for it\n0 - Back\n" + allTravels,
                                        "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                                if (myTravel > 0) {
                                    passenger.reserveTicket(pkOfTravels.get(myTravel - 1));
                                } else if (myTravel == 0) {
                                    return;
                                }
                            }


                        } else {
                            JOptionPane.showMessageDialog(null, "there is no origin or destination such that " +
                                            "please try again", "ticket reservation",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static String getCountryFromCity(String city) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT country from city where cityName = ?");
            preparedStatement.setString(1, city);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String country = resultSet.getString("country");
                return country;
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void searchForTrainOrBus(Human passenger, int trainOrBus) throws SQLException {
        ArrayList<Integer> pkOfTravels = new ArrayList<>();
        StringBuilder allTravels = new StringBuilder();


        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            int startRate = 0;
            int endRate = 0;

            ArrayList<String> options = new ArrayList<>();

            options.add("yes");
            options.add("no");

            int chosen = JOptionPane.showOptionDialog(null, "do you want to determine the rate of " +
                            "agency?" + "\n", "ticket reservation",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options.toArray(), options.get(0));

            if(chosen == 0){
                startRate = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the start of rate range",
                        "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                endRate = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the end of rate range",
                        "ticket reservation", JOptionPane.QUESTION_MESSAGE));


            }
            else if (chosen == 1){
                startRate = 1;
                endRate = 5;

            }

            switch (Integer.parseInt(JOptionPane.showInputDialog(null,
                    "On what basis do you want to filter it? :\n1. origin and destination \n" +
                            "2. origin and destination and price\n3. origin and destination and date\n" +
                            "4. origin and destination and price and date\n5. rate according to agency\n6. back"))) {
                case 1:
                    String origin = JOptionPane.showInputDialog(null, "Please Enter your origin",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    String destination = JOptionPane.showInputDialog(null, "Please Enter your destination",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);


                    int idOriginCity1 = 0;
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, origin);
                    ResultSet resultSetCity1 = preparedStatement.executeQuery();
                    if (resultSetCity1.next()) {
                        idOriginCity1 = resultSetCity1.getInt("id");
                    }
                    int idOriginCity2 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, destination);
                    ResultSet resultSetCity2 = preparedStatement.executeQuery();
                    if (resultSetCity2.next()) {
                        idOriginCity2 = resultSetCity2.getInt("id");
                    }


                    ResultSet resultSet = null;
                    if (trainOrBus == 1) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = " +
                                "idTravel join agency on agency.id = idAgency where origin = ?" +
                                "and destination = ? and vehicle = ? and agency.rate between ? and ?");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                        preparedStatement.setInt(4, startRate);
                        preparedStatement.setInt(5, endRate);
                        resultSet = preparedStatement.executeQuery();
                    } else if (trainOrBus == 2) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = " +
                                "idTravel join agency on agency.id = idAgency where origin = ?" +
                                "and destination = ? and vehicle = ? and agency.rate between ? and ?");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
                        preparedStatement.setInt(4, startRate);
                        preparedStatement.setInt(5, endRate);
                        resultSet = preparedStatement.executeQuery();
                    }

                    if (resultSet.next()) {

                        boolean isNullOrigin = getCountryFromCity(origin) == null;
                        boolean isNullDestination = getCountryFromCity(destination) == null;

                        if (!isNullOrigin && !isNullDestination) {
                            if (!getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination))) {
                                JOptionPane.showMessageDialog(null, "It's a domestic travel," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                        } else {

                            resultSet = null;
                            if (trainOrBus == 1) {
                                preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = " +
                                        "idTravel join agency on agency.id = idAgency where origin = ?" +
                                        "and destination = ? and vehicle = ? and agency.rate between ? and ?");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                                preparedStatement.setInt(4, startRate);
                                preparedStatement.setInt(5, endRate);
                                resultSet = preparedStatement.executeQuery();
                            } else if (trainOrBus == 2) {
                                preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = " +
                                        "idTravel join agency on agency.id = idAgency where origin = ?" +
                                        "and destination = ? and vehicle = ? and agency.rate between ? and ?");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
                                preparedStatement.setInt(4, startRate);
                                preparedStatement.setInt(5, endRate);
                                resultSet = preparedStatement.executeQuery();
                            }

                            int counter = 1;
                            while (true) {
                                assert resultSet != null;
                                if (!resultSet.next()) break;


                                pkOfTravels.add(resultSet.getInt("travel.id"));
                                allTravels = allTravels.append(counter + " - " + "Date : " + resultSet.getString("date") + "\n").
                                        append("time : " + resultSet.getString("time") + "\n").append("origin : " +
                                                origin + "\n").
                                        append("destination : " + destination + "\n").
                                        append("remaining seats : " + resultSet.getString("seatsRemain") + "\n");
                                counter++;

                            }

                            int myTravel = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the travel" +
                                            " you want to reserve a ticket for it\n0 - Back\n" + allTravels,
                                    "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                            if (myTravel > 0) {
                                passenger.reserveTicket(pkOfTravels.get(myTravel - 1));
                            } else if (myTravel == 0) {
                                return;
                            }
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "there is no origin or destination such that " +
                                        "please try again", "ticket reservation",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    break;
                case 2:
                    origin = JOptionPane.showInputDialog(null, "Please Enter your origin",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    destination = JOptionPane.showInputDialog(null, "Please Enter your destination",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    int startPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the price floor"
                            , JOptionPane.QUESTION_MESSAGE));
                    int endPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the price ceiling",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));


                    idOriginCity1 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, origin);
                    resultSetCity1 = preparedStatement.executeQuery();
                    if (resultSetCity1.next()) {
                        idOriginCity1 = resultSetCity1.getInt("id");
                    }
                    idOriginCity2 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, destination);
                    resultSetCity2 = preparedStatement.executeQuery();
                    if (resultSetCity2.next()) {
                        idOriginCity2 = resultSetCity2.getInt("id");
                    }


                    resultSet = null;
                    if (trainOrBus == 1) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = " +
                                "idTravel join agency on agency.id = idAgency where origin = ?" +
                                "and destination = ? and vehicle = ? and price between ? and ? and agency.rate between ? and ?  ");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                        preparedStatement.setInt(4, startPrice);
                        preparedStatement.setInt(5, endPrice);
                        preparedStatement.setInt(6, startRate);
                        preparedStatement.setInt(7, endRate);

                        resultSet = preparedStatement.executeQuery();
                    } else if (trainOrBus == 2) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = " +
                                "idTravel join agency on agency.id = idAgency where origin = ?" +
                                "and destination = ? and vehicle = ? and price between ? and ? and agency.rate between ? and ?  ");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
                        preparedStatement.setInt(4, startPrice);
                        preparedStatement.setInt(5, endPrice);
                        preparedStatement.setInt(6, startRate);
                        preparedStatement.setInt(7, endRate);
                    }

                    if (resultSet.next()) {

                        boolean isNullOrigin = getCountryFromCity(origin) == null;
                        boolean isNullDestination = getCountryFromCity(destination) == null;

                        if (!isNullOrigin && !isNullDestination) {
                            if (!getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination))) {
                                JOptionPane.showMessageDialog(null, "It's a domestic travel," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            } else {

                                if (trainOrBus == 1) {
                                    preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = " +
                                            "idTravel join agency on agency.id = idAgency where origin = ?" +
                                            "and destination = ? and vehicle = ? and price between ? and ? and agency.rate between ? and ?  ");
                                    preparedStatement.setInt(1, idOriginCity1);
                                    preparedStatement.setInt(2, idOriginCity2);
                                    preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                                    preparedStatement.setInt(4, startPrice);
                                    preparedStatement.setInt(5, endPrice);
                                    preparedStatement.setInt(6, startRate);
                                    preparedStatement.setInt(7, endRate);

                                    resultSet = preparedStatement.executeQuery();
                                } else if (trainOrBus == 2) {
                                    preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on travel.id = " +
                                            "idTravel join agency on agency.id = idAgency where origin = ?" +
                                            "and destination = ? and vehicle = ? and price between ? and ? and agency.rate between ? and ?  ");
                                    preparedStatement.setInt(1, idOriginCity1);
                                    preparedStatement.setInt(2, idOriginCity2);
                                    preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
                                    preparedStatement.setInt(4, startPrice);
                                    preparedStatement.setInt(5, endPrice);
                                    preparedStatement.setInt(6, startRate);
                                    preparedStatement.setInt(7, endRate);
                                }

                                int counter = 1;
                                while (resultSet.next()) {


                                    pkOfTravels.add(resultSet.getInt("travel.id"));
                                    allTravels = allTravels.append(counter + " - " + "Date : " + resultSet.getString("date") + "\n").
                                            append("time : " + resultSet.getString("time") + "\n").append("origin : " +
                                                    origin + "\n").
                                            append("destination : " + destination + "\n").
                                            append("remaining seats : " + resultSet.getString("seatsRemain") + "\n");
                                    counter++;

                                }

                                int myTravel = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the travel" +
                                                " you want to reserve a ticket for it\n0 - Back\n" + allTravels,
                                        "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                                if (myTravel > 0) {
                                    passenger.reserveTicket(pkOfTravels.get(myTravel - 1));
                                } else if (myTravel == 0) {
                                    return;
                                }
                            }


                        } else {
                            JOptionPane.showMessageDialog(null, "there is no origin or destination such that " +
                                            "please try again", "ticket reservation",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    break;
                case 3:
                    origin = JOptionPane.showInputDialog(null, "Please Enter your origin",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    destination = JOptionPane.showInputDialog(null, "Please Enter your destination",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    LocalDate startDate = LocalDate.parse(JOptionPane.showInputDialog(null, "Please Enter start date",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                    LocalDate endDate = LocalDate.parse(JOptionPane.showInputDialog(null, "Please Enter the end date",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));


                    idOriginCity1 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, origin);
                    resultSetCity1 = preparedStatement.executeQuery();
                    if (resultSetCity1.next()) {
                        idOriginCity1 = resultSetCity1.getInt("id");
                    }
                    idOriginCity2 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, destination);
                    resultSetCity2 = preparedStatement.executeQuery();
                    if (resultSetCity2.next()) {
                        idOriginCity2 = resultSetCity2.getInt("id");
                    }

                    resultSet = null;
                    if (trainOrBus == 1) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on" +
                                " travel.id = idTravel join agency on agency.id = idAgency where origin = ?" +
                                "and destination = ? and vehicle = ? and price between ? and ? and agency.rate between ? and ? ");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                        preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
                        preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
                        preparedStatement.setInt(6, startRate);
                        preparedStatement.setInt(7, endRate);
                        resultSet = preparedStatement.executeQuery();
                    } else if (trainOrBus == 2) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel join agency_travel on" +
                                " travel.id = idTravel join agency on agency.id = idAgency where origin = ?" +
                                "and destination = ? and vehicle = ? and price between ? and ? and agency.rate between ? and ? ");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
                        preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
                        preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
                        preparedStatement.setInt(6, startRate);
                        preparedStatement.setInt(7, endRate);
                    }

                    if (resultSet.next()) {

                        boolean isNullOrigin = getCountryFromCity(origin) == null;
                        boolean isNullDestination = getCountryFromCity(destination) == null;

                        if (!isNullOrigin && !isNullDestination) {
                            if (!getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination))) {
                                JOptionPane.showMessageDialog(null, "It's a domestic travel," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            } else {

                                if (trainOrBus == 1) {
                                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                            "and destination = ? and vehicle = ? and price between ? and ? ");
                                    preparedStatement.setInt(1, idOriginCity1);
                                    preparedStatement.setInt(2, idOriginCity2);
                                    preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                                    preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
                                    preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
                                    resultSet = preparedStatement.executeQuery();
                                } else if (trainOrBus == 2) {
                                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                            "and destination = ? and vehicle = ? and price between ? and ? ");
                                    preparedStatement.setInt(1, idOriginCity1);
                                    preparedStatement.setInt(2, idOriginCity2);
                                    preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
                                    preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
                                    preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
                                    resultSet = preparedStatement.executeQuery();
                                }

                                int counter = 1;
                                while (resultSet.next()) {


                                    pkOfTravels.add(resultSet.getInt("id"));
                                    allTravels = allTravels.append(counter + " - " + "Date : " + resultSet.getString("date") + "\n").
                                            append("time : " + resultSet.getString("time") + "\n").append("origin : " +
                                                    origin + "\n").
                                            append("destination : " + destination + "\n").
                                            append("remaining seats : " + resultSet.getString("seatsRemain") + "\n");
                                    counter++;

                                }

                                int myTravel = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the travel" +
                                                " you want to reserve a ticket for it\n0 - Back\n" + allTravels,
                                        "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                                if (myTravel > 0) {
                                    passenger.reserveTicket(pkOfTravels.get(myTravel - 1));
                                } else if (myTravel == 0) {
                                    return;
                                }
                            }


                        }


                    }
                    break;
                case 4:
                    origin = JOptionPane.showInputDialog(null, "Please Enter your origin",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    destination = JOptionPane.showInputDialog(null, "Please Enter your destination",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE);
                    startPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the price floor"
                            , JOptionPane.QUESTION_MESSAGE));
                    endPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the price ceiling",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                    startDate = LocalDate.parse(JOptionPane.showInputDialog(null, "Please Enter start date",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                    endDate = LocalDate.parse(JOptionPane.showInputDialog(null, "Please Enter the end date",
                            "ticket reservation", JOptionPane.QUESTION_MESSAGE));


                    idOriginCity1 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, origin);
                    resultSetCity1 = preparedStatement.executeQuery();
                    if (resultSetCity1.next()) {
                        idOriginCity1 = resultSetCity1.getInt("id");
                    }
                    idOriginCity2 = 0;
                    preparedStatement = connection.prepareStatement("SELECT * FROM city where cityName = ?");
                    preparedStatement.setString(1, destination);
                    resultSetCity2 = preparedStatement.executeQuery();
                    if (resultSetCity2.next()) {
                        idOriginCity2 = resultSetCity2.getInt("id");
                    }

                    resultSet = null;
                    if (trainOrBus == 1) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                "and destination = ? and vehicle = ? and price between ? and ? and date between ? and ? ");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                        preparedStatement.setInt(4, startPrice);
                        preparedStatement.setInt(5, endPrice);
                        preparedStatement.setDate(6, java.sql.Date.valueOf(startDate));
                        preparedStatement.setDate(7, java.sql.Date.valueOf(endDate));
                        resultSet = preparedStatement.executeQuery();
                    } else if (trainOrBus == 2) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                "and destination = ? and vehicle = ? and price between ? and ? and date between ? and ? ");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
                        preparedStatement.setInt(4, startPrice);
                        preparedStatement.setInt(5, endPrice);
                        preparedStatement.setDate(6, java.sql.Date.valueOf(startDate));
                        preparedStatement.setDate(7, java.sql.Date.valueOf(endDate));
                        resultSet = preparedStatement.executeQuery();
                    }

                    if (resultSet.next()) {

                        boolean isNullOrigin = getCountryFromCity(origin) == null;
                        boolean isNullDestination = getCountryFromCity(destination) == null;

                        if (!isNullOrigin && !isNullDestination) {
                            if (!getCountryFromCity(origin).
                                    equals(getCountryFromCity(destination))) {
                                JOptionPane.showMessageDialog(null, "It's a domestic travel," +
                                                " please enter another origin and destination", "ticket reservation",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            } else {

                                if (trainOrBus == 1) {
                                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                            "and destination = ? and vehicle = ? and price between ? and ? and date between ? and ? ");
                                    preparedStatement.setInt(1, idOriginCity1);
                                    preparedStatement.setInt(2, idOriginCity2);
                                    preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                                    preparedStatement.setInt(4, startPrice);
                                    preparedStatement.setInt(5, endPrice);
                                    preparedStatement.setDate(6, java.sql.Date.valueOf(startDate));
                                    preparedStatement.setDate(7, java.sql.Date.valueOf(endDate));
                                    resultSet = preparedStatement.executeQuery();
                                } else if (trainOrBus == 2) {
                                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                            "and destination = ? and vehicle = ? and price between ? and ? and date between ? and ? ");
                                    preparedStatement.setInt(1, idOriginCity1);
                                    preparedStatement.setInt(2, idOriginCity2);
                                    preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
                                    preparedStatement.setInt(4, startPrice);
                                    preparedStatement.setInt(5, endPrice);
                                    preparedStatement.setDate(6, java.sql.Date.valueOf(startDate));
                                    preparedStatement.setDate(7, java.sql.Date.valueOf(endDate));
                                    resultSet = preparedStatement.executeQuery();
                                }

                                int counter = 1;
                                while (resultSet.next()) {


                                    pkOfTravels.add(resultSet.getInt("travel.id"));
                                    allTravels = allTravels.append(counter + " - " + "Date : " + resultSet.getString("date") + "\n").
                                            append("time : " + resultSet.getString("time") + "\n").append("origin : " +
                                                    origin + "\n").
                                            append("destination : " + destination + "\n").
                                            append("remaining seats : " + resultSet.getString("seatsRemain") + "\n");
                                    counter++;

                                }

                                int myTravel = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the travel" +
                                                " you want to reserve a ticket for it\n0 - Back\n" + allTravels,
                                        "ticket reservation", JOptionPane.QUESTION_MESSAGE));
                                if (myTravel > 0) {
                                    passenger.reserveTicket(pkOfTravels.get(myTravel - 1));
                                } else if (myTravel == 0) {
                                }
                            }


                        } else {
                            JOptionPane.showMessageDialog(null, "there is no origin or destination such that " +
                                            "please try again", "ticket reservation",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

            }

        } catch (
                ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void reserveTicket(int idOfTravel) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            PreparedStatement preparedStatement = connection.prepareStatement("SELECT price from travel" +
                    " where id = ?");
            preparedStatement.setInt(1, idOfTravel);
            ResultSet resultSet = preparedStatement.executeQuery();
            int price = 0;
            if (resultSet.next()) {
                price = resultSet.getInt("price");
            }


            preparedStatement = connection.prepareStatement("INSERT INTO ticket" +
                    "(travelId, finalPrice, ticketStatus) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, idOfTravel);
            preparedStatement.setInt(2, price);
            preparedStatement.setObject(3, TicketStatus.Reserve, Types.OTHER);
            preparedStatement.executeUpdate();

            ResultSet primaryKey = preparedStatement.getGeneratedKeys();

            int ticketId = 0;
            if (primaryKey.next()) {
                ticketId = primaryKey.getInt(1);
            }


            preparedStatement = connection.prepareStatement("INSERT INTO passenger_ticket" +
                    "(idPassenger, idTicket) VALUES (?, ?)");
            preparedStatement.setInt(1, getPrimaryKey());
            preparedStatement.setInt(2, ticketId);
            preparedStatement.executeUpdate();


            JOptionPane.showMessageDialog(null, "you reserved the ticket successfully"
                    , "ticket reservation", JOptionPane.INFORMATION_MESSAGE);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showAllMyReservedTickets(Human passenger) throws SQLException {
        StringBuilder allTravels = new StringBuilder();
        ArrayList<Integer> pkOfTickets = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


//            PreparedStatement preparedStatement = connection.prepareStatement("SELECT idTicket from passenger_ticket" +
//                    " where idPassenger = ?");
//            preparedStatement.setInt(1, getPrimaryKey());
//            ResultSet resultSet = preparedStatement.executeQuery();
//
            int idTicket;

            int counter = 1;
//            while (resultSet.next()) {


            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM travel join ticket on ticket.travelId = " +
                    "travel.id join passenger_ticket on passenger_ticket.idTicket = ticket.id where idPassenger = ? and ticketStatus = ? ");
            preparedStatement.setInt(1, getPrimaryKey());
            preparedStatement.setObject(2, TicketStatus.Reserve, Types.OTHER);
            ResultSet travelOfTicket = preparedStatement.executeQuery();


            if (travelOfTicket.next()) {

                idTicket = travelOfTicket.getInt("idTicket");
                pkOfTickets.add(idTicket);


                allTravels = allTravels.append(counter + " - " + "Date :" + travelOfTicket.getString("date") + "\n").
                        append("time :" + travelOfTicket.getString("time") + "\n").append("origin :" +
                                getCityByItsPrimaryKey(travelOfTicket.getInt("origin")) + "\n").
                        append("destination :" + getCityByItsPrimaryKey(travelOfTicket.getInt("destination")) + "\n").
                        append("remaining seats :" + travelOfTicket.getString("seatsRemain") + "\n");
                counter++;
            }


            int myTravel = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the travel" +
                            "you want to buy a ticket for it\n0 - Back\n" + allTravels,
                    "buy ticket", JOptionPane.QUESTION_MESSAGE));

            if (myTravel > 0) {
                passenger.buyTicket(pkOfTickets.get(myTravel - 1));
            } else if (myTravel == 0) {
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public String getCityByItsPrimaryKey(int primaryKey) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT cityName from city" +
                    " where id = ?");
            preparedStatement.setInt(1, primaryKey);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void buyTicket(int idOfTicket) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from offer_passenger" +
                    " where userId = ?");
            preparedStatement.setInt(1, getPrimaryKey());
            ResultSet resultSet = preparedStatement.executeQuery();

            int offerPassengerId = 0;
            int offerId = 0;
            if (resultSet.next()) {
                offerPassengerId = resultSet.getInt("id");
                offerId = resultSet.getInt("offerId");
                preparedStatement = connection.prepareStatement("UPDATE ticket SET offerId = ? where id = ?");
                preparedStatement.setInt(1, offerPassengerId);
                preparedStatement.setInt(2, idOfTicket);
                preparedStatement.executeUpdate();
            }

            preparedStatement = connection.prepareStatement("SELECT seatsRemain, price, travelId from travel join ticket on travel.id = " +
                    "ticket.travelId where ticket.id = ?");
            preparedStatement.setInt(1, idOfTicket);
            ResultSet resultSet1 = preparedStatement.executeQuery();

            float price = 0;
            int seatsRemain = 0;
            int travelId = 0;

            while (resultSet1.next()) {
                seatsRemain = resultSet1.getInt("seatsRemain");
                price = resultSet1.getFloat("price");
                travelId = resultSet1.getInt("travelId");
            }
            System.out.println(seatsRemain);
            System.out.println(price);

            if (seatsRemain > 0) {

                ArrayList<String> options = new ArrayList<>();
                options.add("Yes");
                options.add("No");

                int chosen = JOptionPane.showOptionDialog(null, "the price of this travel is: " +
                                price + "\ndo you have any discount code?"
                        , "buy ticket",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options.toArray(), options.get(0));
                switch (chosen) {
                    case 0:
                        String discountCode = JOptionPane.showInputDialog(null, "Please Enter the " +
                                        "discount code",
                                "buy ticket", JOptionPane.QUESTION_MESSAGE);
                        preparedStatement = connection.prepareStatement("SELECT * from offer" +
                                " where id = ?");
                        preparedStatement.setInt(1, offerId);
                        ResultSet offer = preparedStatement.executeQuery();


                        float finalPrice;
                        if (offer.next()) {

                            if (!offer.getString("code").equals(discountCode)) {
                                return;
                            }

                            int limitation = offer.getInt("limitation");
                            int discountAmount = offer.getInt("discountAmount");
                            if (offer.getInt("limitation") < price) {
                                finalPrice = limitation * discountAmount;

                            } else {
                                finalPrice = price * discountAmount;


                            }

                            preparedStatement = connection.prepareStatement("UPDATE ticket SET finalPrice = ? " +
                                    " where offerId = ?");
                            preparedStatement.setFloat(1, finalPrice);
                            preparedStatement.setInt(2, idOfTicket);
                            preparedStatement.executeQuery();
                            JOptionPane.showMessageDialog(null, "your discount has " +
                                    "the new price is :" + finalPrice, "buy ticket", JOptionPane.INFORMATION_MESSAGE);


                        } else {
                            JOptionPane.showMessageDialog(null, "there is no discount code such that"
                                    , "buy ticket", JOptionPane.INFORMATION_MESSAGE);
                        }

                    case 1:
                        return;

                }

                int chosen1 = JOptionPane.showOptionDialog(null, "do you want to buy it?"
                        , "buy ticket",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options.toArray(), options.get(0));
                switch (chosen1) {
                    case 0:
                        preparedStatement = connection.prepareStatement("UPDATE ticket SET ticketStatus = ? " +
                                " where offerId = ?");
                        preparedStatement.setObject(1, TicketStatus.Paid, Types.OTHER);
                        preparedStatement.setInt(2, idOfTicket);
                        preparedStatement.executeQuery();

                        preparedStatement = connection.prepareStatement("UPDATE travel SET seatsRemain = ? " +
                                " where id = ?");
                        preparedStatement.setInt(1, seatsRemain - 1);
                        preparedStatement.setInt(2, travelId);
                        preparedStatement.executeQuery();


                        JOptionPane.showMessageDialog(null, "your purchase has been done successfully"
                                , "buy ticket", JOptionPane.INFORMATION_MESSAGE);
                        break;


                    case 1:
                        break;
                }

            } else {
                JOptionPane.showMessageDialog(null, "this travel is full, you cant buy a " +
                                "ticket for this trip.", "buy ticket",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public StringBuilder allMyPurchasedTickets() throws SQLException {
        StringBuilder allTravels = new StringBuilder();
        ArrayList<Integer> pkOfTickets = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            int counter = 1;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM travel join ticket on ticket.travelId = " +
                    "travel.id join passenger_ticket on passenger_ticket.idTicket = ticket.id where idPassenger = ? and ticketStatus = ? ");
            preparedStatement.setInt(1, getPrimaryKey());
            preparedStatement.setObject(2, TicketStatus.Paid, Types.OTHER);
            ResultSet travelOfTicket = preparedStatement.executeQuery();


            if (travelOfTicket.next()) {

                allTravels = allTravels.append(counter + " - " + "Date :" + travelOfTicket.getString("date") + "\n").
                        append("time :" + travelOfTicket.getString("time") + "\n").append("origin :" +
                                getCityByItsPrimaryKey(travelOfTicket.getInt("origin")) + "\n").
                        append("destination :" + getCityByItsPrimaryKey(travelOfTicket.getInt("destination")) + "\n").
                        append("remaining seats :" + travelOfTicket.getString("seatsRemain") + "\n");
                counter++;
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return allTravels;
    }

    public void showAllMyPurchasedTickets() throws SQLException {
        StringBuilder allTravels = allMyPurchasedTickets();
        JOptionPane.showInputDialog(null, allTravels,
                "All My Purchased Tickets", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cancelTheTravel() throws SQLException {

        StringBuilder allTravels = new StringBuilder();
        ArrayList<Integer> pkOfTickets = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            int counter = 1;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM travel join ticket on ticket.travelId = " +
                    "travel.id join passenger_ticket on passenger_ticket.idTicket = ticket.id where idPassenger = ? and ticketStatus = ? ");
            preparedStatement.setInt(1, getPrimaryKey());
            preparedStatement.setObject(2, TicketStatus.Paid, Types.OTHER);
            ResultSet travelOfTicket = preparedStatement.executeQuery();

            int idTicket;
            if (travelOfTicket.next()) {
                idTicket = travelOfTicket.getInt("idTicket");
                pkOfTickets.add(idTicket);

                allTravels = allTravels.append(counter + " - " + "Date :" + travelOfTicket.getString("date") + "\n").
                        append("time :" + travelOfTicket.getString("time") + "\n").append("origin :" +
                                getCityByItsPrimaryKey(travelOfTicket.getInt("origin")) + "\n").
                        append("destination :" + getCityByItsPrimaryKey(travelOfTicket.getInt("destination")) + "\n").
                        append("remaining seats :" + travelOfTicket.getString("seatsRemain") + "\n");
                counter++;
            }


            int myTravel = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the travel" +
                            "you want to cancel the ticket of it\n0 - Back\n" + allTravels,
                    "cancel ticket", JOptionPane.QUESTION_MESSAGE));

            if (myTravel > 0) {
                idTicket = pkOfTickets.get(myTravel - 1);

                preparedStatement = connection.prepareStatement("SELECT seatsRemain, travelId FROM travel join ticket on ticket.travelId = " +
                        "travel.id where ticket.id = ?");
                preparedStatement.setInt(1, idTicket);
                ResultSet travelDetailsResultSet = preparedStatement.executeQuery();

                int seatsRemain = 0;
                int travelId = 0;
                if (travelDetailsResultSet.next()) {
                    seatsRemain = travelDetailsResultSet.getInt(1);
                    travelId = travelDetailsResultSet.getInt(2);
                }


                preparedStatement = connection.prepareStatement("UPDATE ticket SET ticketStatus = ?" +
                        " WHERE id = ?");
                preparedStatement.setObject(1, TicketStatus.Canceled, Types.OTHER);
                preparedStatement.setInt(2, idTicket);
                preparedStatement.executeQuery();

                preparedStatement = connection.prepareStatement("UPDATE travel SET seatsRemain = ?" +
                        " WHERE id = ?");
                preparedStatement.setInt(1, seatsRemain + 1);
                preparedStatement.setInt(2, travelId);
                preparedStatement.executeQuery();

                JOptionPane.showInputDialog(null, "travel wes canceled successfully",
                        "cancel ticket", JOptionPane.INFORMATION_MESSAGE);

            } else if (myTravel == 0) {
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void rateToTravel() throws SQLException {
        StringBuilder allTravels = new StringBuilder();
        ArrayList<Integer> pkOfTickets = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            int counter = 1;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM travel join ticket on ticket.travelId = " +
                    "travel.id join passenger_ticket on passenger_ticket.idTicket = ticket.id where idPassenger = ? and ticketStatus = ? ");
            preparedStatement.setInt(1, getPrimaryKey());
            preparedStatement.setObject(2, TicketStatus.Done, Types.OTHER);
            ResultSet travelOfTicket = preparedStatement.executeQuery();

            int idTicket;
            if (travelOfTicket.next()) {
                idTicket = travelOfTicket.getInt("idTicket");
                pkOfTickets.add(idTicket);

                allTravels = allTravels.append(counter + " - " + "Date :" + travelOfTicket.getString("date") + "\n").
                        append("time :" + travelOfTicket.getString("time") + "\n").append("origin :" +
                                getCityByItsPrimaryKey(travelOfTicket.getInt("origin")) + "\n").
                        append("destination :" + getCityByItsPrimaryKey(travelOfTicket.getInt("destination")) + "\n").
                        append("remaining seats :" + travelOfTicket.getString("seatsRemain") + "\n");
                counter++;
            }


            int myTravel = Integer.parseInt(JOptionPane.showInputDialog(null, "Please Enter the travel" +
                            "you want to rate it\n0 - Back\n" + allTravels,
                    "rate travel", JOptionPane.QUESTION_MESSAGE));

            if (myTravel > 0) {
                idTicket = pkOfTickets.get(myTravel - 1);

                int rate = Integer.parseInt(JOptionPane.showInputDialog(null, "Please rate this trip\n" +
                                "enter a number between 1 to 5",
                        "rate travel", JOptionPane.QUESTION_MESSAGE));

                preparedStatement = connection.prepareStatement("UPDATE ticket SET rate = ?" +
                        " WHERE id = ?");
                preparedStatement.setInt(1, rate);
                preparedStatement.setInt(2, idTicket);
                preparedStatement.executeQuery();

                JOptionPane.showInputDialog(null, "you rate has submitted successfully",
                        "rate travel", JOptionPane.INFORMATION_MESSAGE);


            } else if (myTravel == 0) {
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void selectBaseOnRate() throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT AVG(travel.rate), agency.name FROM agency_travel join " +
                    "travel on idTravel = travel.id join agency on agency.id = idAgency GROUP BY agency.name WHERE " +
                    "travel.vehicle = ?");
            preparedStatement.setObject(1, Vehicle.Bus, Types.OTHER);


            ResultSet agencyRate = preparedStatement.executeQuery();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //superAdmin//////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void superAdminPanel(Human superAdmin) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isInHomePageSuperAdmin = true;
        int superAdminWhatToDo = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Do?\n1. Create new Discount Code\n" +
                        "2. Messages\n3. Exit",
                "Sign-Up page", JOptionPane.QUESTION_MESSAGE));

        while (isInHomePageSuperAdmin) {
            switch (superAdminWhatToDo) {
                case 1:
                    //create offer
                    stringBuilder = seeAllPassengers();
                    int whoToGiveOffer = Integer.parseInt(JOptionPane.showInputDialog(null, "These are all Passengers who do you want to send an offer\n" + stringBuilder,
                            "Create Offer page", JOptionPane.QUESTION_MESSAGE));
                    String offerCode = JOptionPane.showInputDialog(null, "Please enter an offer Code (Maximum 6 Character)",
                            "Create Offer page", JOptionPane.QUESTION_MESSAGE);
                    float offerAmount = Float.parseFloat(JOptionPane.showInputDialog(null, "Please enter amount of offer ( 0.5 % )",
                            "Create Offer page", JOptionPane.QUESTION_MESSAGE));
                    float offerlimitation = Float.parseFloat(JOptionPane.showInputDialog(null, "Please enter limit of offer ( 100000 )",
                            "Create Offer page", JOptionPane.QUESTION_MESSAGE));
                    sendUPassengerOffer(whoToGiveOffer, offerCode, offerAmount, offerlimitation);
                    break;
                case 2:
                    //answer message
                    boolean isInAnswerPage = true;
                    while (isInAnswerPage) {
                        int superAdminWhatToDoInAnswerPage = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Do?\n1. See All my Messages as Receiver\n" +
                                        "2. See All Messages as Sender\n3. See the Chat by a specific user\n4. Exit",
                                "Sign-Up page", JOptionPane.QUESTION_MESSAGE));
                        switch (superAdminWhatToDoInAnswerPage) {
                            case 1:
                                //see as receiver
                                stringBuilder = seeAllMessagesAsReceiver(superAdmin.getPrimaryKey());
                                int whichToSeen = Integer.parseInt(JOptionPane.showInputDialog(null, "These are all your Messages as Receiver and you did not seen \t choose an option to seen\n" + stringBuilder,
                                        "Answer Page", JOptionPane.QUESTION_MESSAGE));
                                int idSender = findThatWhoSendThisMessage(whichToSeen);
                                boolean isOk = updateSeenStatus(whichToSeen);
                                if (isOk) {
                                    sendMessageReplyToUser(idSender, superAdmin.getPrimaryKey());
                                }
                                break;
                            case 2:
                                //see as sender
                                stringBuilder = seeAllMessagesAsSender(superAdmin.getPrimaryKey());
                                JOptionPane.showInputDialog(null, "These are all your Messages as Sender\n" + stringBuilder,
                                        "Answer Page", JOptionPane.QUESTION_MESSAGE);
                                break;
                            case 3:
                                //see chat by specific user
                                stringBuilder = seeAllUsers();
                                int whoToSeeChatWith = Integer.parseInt(JOptionPane.showInputDialog(null, "These are all Users\n" + stringBuilder,
                                        "Answer page", JOptionPane.QUESTION_MESSAGE));

                                stringBuilder = seeChatWithUser(whoToSeeChatWith, superAdmin.getPrimaryKey());
                                JOptionPane.showInputDialog(null, "These are all your Messages in Chat\n" + stringBuilder,
                                        "Answer Page", JOptionPane.QUESTION_MESSAGE);
                                break;
                            case 4:
                                isInAnswerPage = false;
                                break;
                        }
                        isInAnswerPage = false;
                    }
                    break;
                case 3:
                    isInHomePageSuperAdmin = false;
                    break;
            }
        }
    }

    //agent///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void agentPanel(Human agent) throws SQLException{
        int agentWhatToDo = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Do?\n1. Create new Travel\n" +
                        "2. Filter \n3. Stats\n4. Tops\n5. Exit",
                "Sign-Up page", JOptionPane.QUESTION_MESSAGE));

        boolean isInHomePageAgent = true;

        while (isInHomePageAgent) {
            switch (agentWhatToDo) {
                case 1: //create new travel OK --
                    boolean isAddingNewTravel = true;
                    while (isAddingNewTravel) {
                        StringBuilder stringBuilder = new StringBuilder();

                        String travelName = JOptionPane.showInputDialog(null, "Please enter Travel Name",
                                "Agent Page", JOptionPane.QUESTION_MESSAGE);

                        String travelDateString = JOptionPane.showInputDialog(null, "Please enter Travel Date like this (2021-08-14)",
                                "Agent Page", JOptionPane.QUESTION_MESSAGE);

                        LocalDate travelDate = LocalDate.parse(travelDateString, DateTimeFormatter.ISO_DATE);

                        String travelTime = JOptionPane.showInputDialog(null, "Please enter Travel Time (like 2AM or 17 PM)",
                                "Agent Page", JOptionPane.QUESTION_MESSAGE);

                        String travelNumberOfPeople = JOptionPane.showInputDialog(null, "Please enter Travel Number of People can join",
                                "Agent Page", JOptionPane.QUESTION_MESSAGE);

                        stringBuilder = getTheAllCities();
                        int numberOfCityForOrigin = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter Travel Origin from list\n" + stringBuilder,
                                "Agent Page", JOptionPane.QUESTION_MESSAGE));

                        StringBuilder stringBuilder1 = new StringBuilder();
                        stringBuilder1 = getTheAllCities(numberOfCityForOrigin);
                        System.out.println(numberOfCityForOrigin);
                        int numberOfCityForDestination = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter Travel Destination from list\n" + stringBuilder1,
                                "Agent Page", JOptionPane.QUESTION_MESSAGE));

                        JDialog.setDefaultLookAndFeelDecorated(true);
                        Object[] selectionValues = {"Airplane", "Bus", "Train"};
                        String initialSelection = "Airplane";
                        Object type = JOptionPane.showInputDialog(null, "Please enter Travel Vehicle",
                                "Create Travel", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);

                        float price = Float.parseFloat(JOptionPane.showInputDialog(null, "Enter Price for Ticket",
                                "Tickets Create Page", JOptionPane.INFORMATION_MESSAGE));

                        int whereDoesAgentWorking = whereDoesAgentWork(agent.getPrimaryKey());

                        Boolean isCreatedTravel = CreateTravel(travelName, travelDate, travelTime, travelNumberOfPeople, type, whereDoesAgentWorking, numberOfCityForOrigin, numberOfCityForDestination, price);
                        if (isCreatedTravel) isAddingNewTravel = false;
                        break;
                    }
                    isAddingNewTravel = false;
                    break;
                case 2: //filter
                    boolean isFiltering = true;
                    while (isFiltering) {
                        int agentWhatToFilter = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Filter?\n1. See all Tickets\n2. Filter By Price\n" +
                                        "3. Filter By Time\n4. Filter By Rate\n5. Exit",
                                "Filter page", JOptionPane.QUESTION_MESSAGE));
                        StringBuilder stringBuilder = new StringBuilder();
                        switch (agentWhatToFilter) {
                            case 1:
                                //see all tickets ok --
                                stringBuilder = seeAllRelativeTickets(agent.getPrimaryKey());
                                JOptionPane.showInputDialog(null, "These are all your relative tickets\n" + stringBuilder, "All Tickets", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case 2:
                                //filter ok
                                stringBuilder = filterTicketsByPrice(agent.getPrimaryKey());
                                JOptionPane.showInputDialog(null, "These are all your tickets order by price\n" + stringBuilder, "All Tickets", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case 3:
                                //filter by time
                                String startTimeString = JOptionPane.showInputDialog(null, "Please enter Start Time like (2023-09-09)",
                                        "Filter page", JOptionPane.INFORMATION_MESSAGE);

                                String endTimeString = JOptionPane.showInputDialog(null, "Please enter end Time like (2023-09-09)",
                                        "Filter page", JOptionPane.INFORMATION_MESSAGE);
                                LocalDate startDate = LocalDate.parse(startTimeString, DateTimeFormatter.ISO_DATE);
                                LocalDate endDate = LocalDate.parse(endTimeString, DateTimeFormatter.ISO_DATE);

                                stringBuilder = filterTicketsByTime(startDate, endDate, agent.getPrimaryKey());
                                JOptionPane.showInputDialog(null, "These are all your tickets order by time\n" + stringBuilder, "All Tickets", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case 4:
                                //filter by rate ok
                                boolean isInFilteringTravels = true;
                                while (isInFilteringTravels) {
                                    int agentWhatToDoForTravels = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Filter?\n1. See all Travel and Rates\n2. Filter By Rate ASC\n" +
                                                    "3. Filter By Rate DSC \n4. Exit",
                                            "Filter page", JOptionPane.QUESTION_MESSAGE));

                                    switch (agentWhatToDoForTravels) {
                                        case 1:
                                            stringBuilder = seeTheAllTravelsAndTheirRate(agent.getPrimaryKey());
                                            JOptionPane.showInputDialog(null, "These are all your travels and rates \n" + stringBuilder,
                                                    "Filter page", JOptionPane.INFORMATION_MESSAGE);
                                            break;
                                        case 2:
                                            stringBuilder = seeTheAllTravelsAndTheirRateASC(agent.getPrimaryKey());
                                            JOptionPane.showInputDialog(null, "These are all your travels and rates sorted ASC \n" + stringBuilder,
                                                    "Filter page", JOptionPane.INFORMATION_MESSAGE);
                                            break;
                                        case 3:
                                            stringBuilder = seeTheAllTravelsAndTheirRateDESC(agent.getPrimaryKey());
                                            JOptionPane.showInputDialog(null, "These are all your travels and rates sorted DESC \n" + stringBuilder,
                                                    "Filter page", JOptionPane.INFORMATION_MESSAGE);
                                            break;
                                        case 4:
                                            isInFilteringTravels = false;
                                            break;
                                    }
                                }
                                break;
                            case 5:
                                isFiltering = false;
                                break;
                        }
                    }
                    break;
                case 3: //stats
                    StringBuilder stringBuilder = new StringBuilder();
                    boolean isGettingStats = true;
                    while (isGettingStats) {
                        int agentWhatToDoInStats = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Do?\n1. Most Expensive Travel Ever\n" +
                                        "2. See the Cheapest Travel Ever\n3. See the All Income\n4. See the Income in Range Rating\n5. See the Most Popular Destination\n6. Exit",
                                "Stats Page", JOptionPane.INFORMATION_MESSAGE));

                        switch (agentWhatToDoInStats) {
                            case 1:
                                //most expensive travel ok
                                stringBuilder = seeTheMostExpensiveTravelPrice(agent.getPrimaryKey());
                                JOptionPane.showInputDialog(null, "This is your Most Expensive Travel Ever\n " + stringBuilder, "Travel Stats", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case 2:
                                //cheapest travel ok
                                stringBuilder = seeTheCheapestTravel(agent.getPrimaryKey());
                                JOptionPane.showInputDialog(null, "This is your Cheapest Travel Ever\n " + stringBuilder, "Travel Stats", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case 3:
                                //all the incomes ok
                                stringBuilder = seeTheAllIncome(agent.getPrimaryKey());
                                JOptionPane.showInputDialog(null, "This is your All Income Ever\n " + stringBuilder, "Travel Stats", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case 4:
                                //income in range ok
                                String startTimeString = JOptionPane.showInputDialog(null, "Please enter Start Time like (2023-09-09)",
                                        "Filter page", JOptionPane.INFORMATION_MESSAGE);

                                String endTimeString = JOptionPane.showInputDialog(null, "Please enter end Time like (2023-09-09)",
                                        "Filter page", JOptionPane.INFORMATION_MESSAGE);
                                LocalDate startDate = LocalDate.parse(startTimeString, DateTimeFormatter.ISO_DATE);
                                LocalDate endDate = LocalDate.parse(endTimeString, DateTimeFormatter.ISO_DATE);
                                stringBuilder = seeTheIncomeBetweenTwoDate(agent.getPrimaryKey(), startDate, endDate);
                                JOptionPane.showInputDialog(null, "This is your All Income in range of these days\n " + stringBuilder, "Travel Stats", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case 5:
                                //get the Most Popular  ok --
                                stringBuilder = seeTheMostPopularDestination(agent.getPrimaryKey());
                                JOptionPane.showInputDialog(null, "This is your Most Popolar destination Ever\n " + stringBuilder, "Travel Stats", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            case 6:
                                isGettingStats = false;
                                break;
                        }
                        //break;
                    }
                    break;
                case 4: //tops
                    stringBuilder = seeTheTop5Passenger(agent.getPrimaryKey());
                    int seeCity = Integer.parseInt(JOptionPane.showInputDialog(null, "These are Top 5 passenger in payment\n Enter 1 to see the name of the cities \n \n" + stringBuilder, "Agence Stats", JOptionPane.INFORMATION_MESSAGE));

                    if (seeCity == 1){
                        stringBuilder = seeTheTop5PassengerWithCityName(agent.getPrimaryKey());
                        JOptionPane.showInputDialog(null, "These are Top  passenger Destination Name\n " + stringBuilder, "Agence Stats", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case 5:
                    isInHomePageAgent = false;
                    break;
            }
            isInHomePageAgent = false;
        }

    }

    //****************************************************************************************************************************

    //Tested ok
    public static Boolean CreateTravel(String travelName, LocalDate travelDate, String travelTime, String
            travelNumberOfPeople, Object type, int idAgency, int origin, int destination, float price) {
        int nums = Integer.parseInt(travelNumberOfPeople);
        boolean isTravelCreated = false;
        boolean isTravelAgencyCreated = false;
        Vehicle vehicle = null;
        switch (type.toString()) {
            case "Airplane":
                vehicle = Vehicle.Airplane;
                break;
            case "Bus":
                vehicle = Vehicle.Bus;
                break;
            case "Train":
                vehicle = Vehicle.Train;
                break;
        }
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "INSERT INTO travel (date, time, numberOfPeople, vehicle, name, origin, destination, price, seatsRemain) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setDate(1, java.sql.Date.valueOf(travelDate));
            preparedStatement.setString(2, travelTime);
            preparedStatement.setInt(3, nums);
            preparedStatement.setObject(4, vehicle, Types.OTHER);
            preparedStatement.setString(5, travelName);
            preparedStatement.setInt(6, origin);
            preparedStatement.setInt(7, destination);
            preparedStatement.setFloat(8, price);
            preparedStatement.setInt(9, nums);

            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                isTravelCreated = true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        int idTravel = getTheTravelId(travelName);

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "INSERT INTO agency_travel (idAgency, idTravel) " +
                    "VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, idAgency);
            preparedStatement.setInt(2, idTravel);

            int rows2 = preparedStatement.executeUpdate();
            if (rows2 > 0) {
                isTravelAgencyCreated = true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return isTravelCreated && isTravelAgencyCreated;
    }

    //Tested ok
    private static StringBuilder seeAllRelativeTickets(int agentID) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            String sqlQuery = "select price, ticketStatus, travelId from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Float price = resultSet.getFloat(1);
                String ticketStatus = resultSet.getString(2);
                String travelName = getTheTravelName(resultSet.getInt(3));
                stringBuilder.append("Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested ok
    private static StringBuilder filterTicketsByPrice(int idAgent) {
        StringBuilder stringBuilder = new StringBuilder();

        boolean isFilteringByPrice = true;
        while (isFilteringByPrice) {
            int agentHowWantToFilter = Integer.parseInt(JOptionPane.showInputDialog(null, "How do you want to Filter?\n1. Filter ASC\n" +
                            "2. Filter DESC\n3. Get from a value\n4. Exit",
                    "Filter Page Ticket", JOptionPane.QUESTION_MESSAGE));

            switch (agentHowWantToFilter) {
                case 1:
                    //ASC ok --
                    try {
                        Class.forName("org.postgresql.Driver");
                        Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                                "aliiiw", "ali123");

                        //String sqlQuery = "SELECT source, destination, price, status, travelId FROM ticket t join travel tv ON t.travelId = tv.id join agency_travel ON tv.id = agency_travel.idTravel where idAgency = ? and status = 'Paid' order by price";
                        String sqlQuery = "select price, status, travelId from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ? order by price";
                        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                        preparedStatement.setInt(1, idAgent);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        while (resultSet.next()) {

                            Float price = resultSet.getFloat(1);
                            String ticketStatus = resultSet.getString(2);
                            String travelName = getTheTravelName(resultSet.getInt(3));
                            //stringBuilder.append("This is Ticket from " + cityNameSource + " to " + cityNameDestination + " with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName);
                            //stringBuilder.append("\n");
                            stringBuilder.append("Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + "\n");
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return stringBuilder;
                case 2: //DESC ok --
                    try {
                        Class.forName("org.postgresql.Driver");
                        Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                                "aliiiw", "ali123");
                        //String sqlQuery = "SELECT source, destination, price, status, travelId FROM ticket t join travel tv ON t.travelId = tv.id join agency_travel ON tv.id = agency_travel.idTravel where idAgency = ? and status = 'Paid' order by price desc";
                        String sqlQuery = "select price, status, travelId from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ? order by price desc ";
                        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                        preparedStatement.setInt(1, idAgent);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        while (resultSet.next()) {
                            Float price = resultSet.getFloat(1);
                            String ticketStatus = resultSet.getString(2);
                            String travelName = getTheTravelName(resultSet.getInt(3));
//                            stringBuilder.append("This is Ticket from " + cityNameSource + " to " + cityNameDestination + " with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName);
                            //stringBuilder.append("\n");
                            stringBuilder.append("Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + "\n");
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return stringBuilder;
                case 3:
                    //get from and until ok
                    int howToGetPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "How do you want to Get Value Until or From??\n1. Until\n" +
                                    "2. From\n3. Exit",
                            "Filter Page Ticket", JOptionPane.QUESTION_MESSAGE));
                    switch (howToGetPrice) {
                        case 1:
                            stringBuilder = new StringBuilder();
                            Float value = Float.parseFloat(JOptionPane.showInputDialog(null, "Please enter your value",
                                    "Filter Page Ticket", JOptionPane.QUESTION_MESSAGE));
                            try {
                                Class.forName("org.postgresql.Driver");
                                Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                                        "aliiiw", "ali123");

                                //String sqlQuery = "SELECT source, destination, price, status, travelId FROM ticket t join travel tv ON t.travelId = tv.id join agency_travel ON tv.id = agency_travel.idTravel where idAgency = ? and  status = 'Paid' and price < ?";
                                String sqlQuery = "select price, status, travelId from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ? and price < ?";
                                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                                preparedStatement.setInt(1, idAgent);
                                preparedStatement.setFloat(2, value);
                                ResultSet resultSet = preparedStatement.executeQuery();

                                while (resultSet.next()) {
                                    Float price = resultSet.getFloat(1);
                                    String ticketStatus = resultSet.getString(2);
                                    String travelName = getTheTravelName(resultSet.getInt(3));
                                    stringBuilder.append("Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + "\n");
//                                    printTicket(cityNameSource, cityNameDestination, price, ticketStatus, travelName);
                                }
                                JOptionPane.showInputDialog(null, "This is what you want" + stringBuilder,
                                        "Filter Page Ticket", JOptionPane.INFORMATION_MESSAGE);
                            } catch (SQLException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case 2:
                            stringBuilder = new StringBuilder();
                            Float valueDSC = Float.parseFloat(JOptionPane.showInputDialog(null, "Please enter your value",
                                    "Filter Page Ticket", JOptionPane.QUESTION_MESSAGE));
                            try {
                                Class.forName("org.postgresql.Driver");
                                Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                                        "aliiiw", "ali123");

//                                String sqlQuery = "SELECT source, destination, price, status, travelId FROM ticket t join travel tv ON t.travelId = tv.id join agency_travel ON tv.id = agency_travel.idTravel where idAgency = ? and  status = 'Paid' and price > ?";
                                String sqlQuery = "select price, status, travelId from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ? and price > ?";
                                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                                preparedStatement.setInt(1, idAgent);
                                preparedStatement.setFloat(2, valueDSC);
                                ResultSet resultSet = preparedStatement.executeQuery();

                                while (resultSet.next()) {
                                    Float price = resultSet.getFloat(1);
                                    String ticketStatus = resultSet.getString(2);
                                    String travelName = getTheTravelName(resultSet.getInt(3));
                                    //printTicket(cityNameSource, cityNameDestination, price, ticketStatus, travelName);
                                    stringBuilder.append("Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + "\n");
                                }
                                JOptionPane.showInputDialog(null, "This is what you want" + stringBuilder,
                                        "Filter Page Ticket", JOptionPane.INFORMATION_MESSAGE);
                            } catch (SQLException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println();
                            break;
                        case 3:
                            break;
                    }
                    break;
                case 4:
                    isFilteringByPrice = false;
                    break;
            }
        }
        return stringBuilder;
    }

    //Tested ok
    private static StringBuilder filterTicketsByTime(LocalDate startTime, LocalDate endTime, int agentId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            //String sqlQuery = "SELECT source, destination, price, status, name,  date, time FROM ticket t join travel tv ON t.travelId = tv.id join agency_travel ON tv.id = agency_travel.idTravel WHERE idAgency = ? and (date between ? and ?) and status = 'Paid'";
            String sqlQuery = "select price, ticketStatus, travelId, date from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ? and date between ? and ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            preparedStatement.setDate(2, java.sql.Date.valueOf(startTime));
            preparedStatement.setDate(3, java.sql.Date.valueOf(endTime));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Float price = resultSet.getFloat(1);
                String ticketStatus = resultSet.getString(2);
                String travelName = getTheTravelName(resultSet.getInt(3));
                String date = resultSet.getString(4);
                stringBuilder.append("This is Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + " At Date " + date + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested ok
    private static StringBuilder seeTheAllTravelsAndTheirRate(int agentId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "select avg(Ticket.rate) average_rate, Travel.name from Ticket join Travel on Travel.id = Ticket.travelId join Agency_Travel on Travel.id = Agency_Travel.idTravel join agency on agency.id = Agency_Travel.idAgency join agency_admin on Agency_Travel.idAgency = agency_admin.idAgency where idAdmin = ? group by Travel.name";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                float averate_rate = resultSet.getFloat(1);
                String travelName = resultSet.getString(2);
                stringBuilder.append("Your Travel named -> " + travelName + " has Rate -> " + averate_rate + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested ok
    private static StringBuilder seeTheAllTravelsAndTheirRateASC(int agentId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "select avg(Ticket.rate) average_rate, Travel.name from Ticket join Travel on Travel.id = Ticket.travelId join Agency_Travel on Travel.id = Agency_Travel.idTravel join agency on agency.id = Agency_Travel.idAgency join agency_admin on Agency_Travel.idAgency = agency_admin.idAgency where idAdmin = ? group by Travel.name order by average_rate asc";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                float averate_rate = resultSet.getFloat(1);
                String travelName = resultSet.getString(2);
                stringBuilder.append("Your Travel named -> " + travelName + " has Rate -> " + averate_rate + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested ok
    private static StringBuilder seeTheAllTravelsAndTheirRateDESC(int agentId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "select avg(Ticket.rate) average_rate, Travel.name from Ticket join Travel on Travel.id = Ticket.travelId join Agency_Travel on Travel.id = Agency_Travel.idTravel join agency on agency.id = Agency_Travel.idAgency join agency_admin on Agency_Travel.idAgency = agency_admin.idAgency where idAdmin = ? group by Travel.name order by average_rate desc";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                float averate_rate = resultSet.getFloat(1);
                String travelName = resultSet.getString(2);
                stringBuilder.append("Your Travel named -> " + travelName + " has Rate -> " + averate_rate + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested new
    private static StringBuilder seeTheAllIncome(int agentId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "SELECT SUM(income_times_nums) AS total_income_times_nums FROM (SELECT Ticket.finalPrice * COUNT(*) AS income_times_nums FROM ticket JOIN travel ON Ticket.travelId = Travel.id JOIN Agency_Travel A ON Travel.id = A.idTravel JOIN agency a2 ON a2.id = A.idAgency join agency_admin aa on A.idAgency = aa.idAgency where idAdmin = ? GROUP BY Travel.name, Ticket.finalPrice) AS subquery";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Float total = resultSet.getFloat(1);
                stringBuilder.append(" Your Total Income is -> " + total + " \n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested ok
    private static StringBuilder seeTheMostExpensiveTravelPrice(int agentId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "select Travel.name,price from travel join agency_travel on Travel.id = Agency_Travel.idTravel join agency on Agency_Travel.idAgency = agency.id join agency_admin on Agency_Travel.idAgency = agency_admin.idAgency where idAdmin = ? order by price desc limit 1";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String travelName = resultSet.getString(1);
                Float total = resultSet.getFloat(2);
                stringBuilder.append(travelName + " " + total + " \n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static StringBuilder seeTheCheapestTravel(int agentId) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "select Travel.name,price from travel join agency_travel on Travel.id = Agency_Travel.idTravel join agency on Agency_Travel.idAgency = agency.id join agency_admin on Agency_Travel.idAgency = agency_admin.idAgency where idAdmin = ? order by price limit 1";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String travelName = resultSet.getString(1);
                Float total = resultSet.getFloat(2);
                stringBuilder.append(travelName + " " + total + " \n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested New
    private static StringBuilder seeTheIncomeBetweenTwoDate(int agentId, LocalDate startTime, LocalDate endTime) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "SELECT SUM(income_times_nums) AS total_income_times_nums FROM (SELECT Ticket.finalPrice * COUNT(*) AS income_times_nums FROM ticket JOIN travel ON Ticket.travelId = Travel.id JOIN Agency_Travel A ON Travel.id = A.idTravel JOIN agency a2 ON a2.id = A.idAgency join agency_admin aa on A.idAgency = aa.idAgency where idAdmin = ? and date between ? and ? GROUP BY Travel.name, Ticket.finalPrice ) AS subquery";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            preparedStatement.setDate(2, java.sql.Date.valueOf(startTime));
            preparedStatement.setDate(3, java.sql.Date.valueOf(endTime));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Float total = resultSet.getFloat(1);
                stringBuilder.append(" Your Total Income in this range is -> " + total + " \n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested new
    private static StringBuilder seeTheMostPopularDestination(int agentId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "select Travel.destination, count(Travel.destination) as num from Travel join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin aa on A.idAgency = aa.idAgency where idAdmin = ? group by Travel.destination order by num desc limit 1;";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String destination = getTheCityName(resultSet.getInt(1));
                stringBuilder.append(" Your Popular Destination is -> " + destination + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static StringBuilder seeTheTop5Passenger(int agentId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "SELECT h.firstname || ' ' || h.lastname AS full_name, h.email, h.phonenumber, SUM(t.finalprice) AS total_paid_price, COUNT(DISTINCT t.travelid) AS num_destinations FROM human h JOIN passenger_ticket pt ON h.id = pt.idpassenger JOIN ticket t ON pt.idticket = t.id JOIN travel tr ON t.travelid = tr.id join Agency_Travel A on tr.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin aa on h.id = aa.idAdmin WHERE idAdmin = ? group by h.firstname, h.lastname, h.email, h.phonenumber LIMIT 5";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String fullName = resultSet.getString(1);
                String email = resultSet.getString(2);
                String phoneNumber = resultSet.getString(3);
                float totalPrice = resultSet.getFloat(4);
                int numsDestination = resultSet.getInt(5);
                stringBuilder.append("Passenger -> " + fullName + " with Email -> " + email + " and with PhoneNumber -> " + phoneNumber + "\n" +
                        "Total Price -> " + totalPrice + " and Distinct Destination Number -> " + numsDestination + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static StringBuilder seeTheTop5PassengerInSpecificMonth(int agentId, LocalDate startDate , LocalDate endDate) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "SELECT h.firstname || ' ' || h.lastname AS full_name, h.email, h.phonenumber, SUM(t.finalprice) AS total_paid_price, COUNT(DISTINCT t.travelid) AS num_destinations FROM human h JOIN passenger_ticket pt ON h.id = pt.idpassenger JOIN ticket t ON pt.idticket = t.id JOIN travel tr ON t.travelid = tr.id join Agency_Travel A on tr.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin aa on h.id = aa.idAdmin WHERE idAdmin = ? group by h.firstname, h.lastname, h.email, h.phonenumber LIMIT 5";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String fullName = resultSet.getString(1);
                String email = resultSet.getString(2);
                String phoneNumber = resultSet.getString(3);
                float totalPrice = resultSet.getFloat(4);
                int numsDestination = resultSet.getInt(5);
                stringBuilder.append("Passenger -> " + fullName + " with Email -> " + email + " and with PhoneNumber -> " + phoneNumber + "\n" +
                        "Total Price -> " + totalPrice + " and Distinct Destination Number -> " + numsDestination + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static StringBuilder seeTheTop5PassengerWithCityName(int agentId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "SELECT h.firstname || ' ' || h.lastname AS full_name, h.email, h.phonenumber, SUM(t.finalprice) AS total_paid_price, t.travelid AS num_destinations FROM human h JOIN passenger_ticket pt ON h.id = pt.idpassenger JOIN ticket t ON pt.idticket = t.id JOIN travel tr ON t.travelid = tr.id join Agency_Travel A on tr.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin aa on h.id = aa.idAdmin WHERE idAdmin = ? group by h.firstname, h.lastname, h.email, h.phonenumber, t.travelId LIMIT 5";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String cityName = getTheCityName(resultSet.getInt(5));
                stringBuilder.append("City -> " + cityName + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static StringBuilder seeAllMessagesAsReceiver(int adminId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            String sqlQuery = "select support_ticket.id, message_text, message_title, time, chat.idSender from support_ticket join chat on support_ticket.id = chat.messageId join human on idReceiver = human.id where human.id = ?  and message_status = false";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, adminId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int idMessage = resultSet.getInt(1);
                String message_text = resultSet.getString(2);
                String message_title = resultSet.getString(3);
                String time = resultSet.getString(4);
                String senderName = findUserById(resultSet.getInt(5));
                stringBuilder.append("Support Ticket " + "with ID -> " + idMessage + "  Sends from -> (" + senderName + ") at time " + time + "\nTitle: " + message_title + "\n" + "Text: " + message_text + "\n \n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static StringBuilder seeAllMessagesAsSender(int adminId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            String sqlQuery = "select message_text, message_title, time, chat.idReceiver from support_ticket join chat on support_ticket.id = chat.messageId join human on idSender = human.id where human.id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, adminId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String message_text = resultSet.getString(1);
                String message_title = resultSet.getString(2);
                String time = resultSet.getString(3);
                String receiverName = findUserById(resultSet.getInt(4));
                stringBuilder.append("Support Ticket Sends to -> (" + receiverName + ") at time " + time + "\nTitle: " + message_title + "\n" + "Text: " + message_text + "\n \n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static StringBuilder seeChatWithUser(int whoToSeeChatWith, int superAdminId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "select message_text from support_ticket  join chat on support_ticket.id = chat.messageId where (idSender = ? and idReceiver = ?) or (idSender = ? and idReceiver = ?) order by time";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, whoToSeeChatWith);
            preparedStatement.setInt(2, superAdminId );
            preparedStatement.setInt(3, whoToSeeChatWith);
            preparedStatement.setInt(4, superAdminId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String message_text = resultSet.getString(1);
                stringBuilder.append("Text: " + message_text + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static int findThatWhoSendThisMessage(int whichToSeen) {
        int idSender = -1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "select idSender from chat join support_ticket on chat.messageId = support_ticket.id where support_ticket.id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, whichToSeen);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                idSender = resultSet.getInt(1);

            }
            return idSender;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static void sendMessageReplyToUser(int idHuman, int idSuperAdmin) {

        String messageTitle = JOptionPane.showInputDialog(null, "Please enter Message title",
                "Answer Page", JOptionPane.QUESTION_MESSAGE);

        String messageText = JOptionPane.showInputDialog(null, "Please enter Message text",
                "Answer Page", JOptionPane.QUESTION_MESSAGE);

        int idMessage = -1;

        boolean isOk = false;

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "INSERT INTO support_ticket(message_text, message_title) values (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, messageTitle);
            preparedStatement.setString(2, messageText);
            int rows = preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                idMessage = resultSet.getInt(1);
                break;
            }

            System.out.println(idMessage);
            while (rows > 0) {
                System.out.println("rafte too messages");
                break;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "INSERT INTO Chat (idSender, idReceiver, messageId) values (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, idSuperAdmin);
            preparedStatement.setInt(2, idHuman);
            preparedStatement.setInt(3, idMessage);
            int rows = preparedStatement.executeUpdate();

            while (rows > 0) {
                System.out.println("rafte too chat");
                break;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static boolean updateSeenStatus(int whichToSeen) {
        boolean isOk = false;
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "update support_ticket set message_status = true where support_ticket.id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, whichToSeen);
            int rows = preparedStatement.executeUpdate();

            while (rows > 0) {
                System.out.println("Updated Successfully");
                isOk = true;
                return isOk;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return isOk;
    }

    //Tested
    private static void sendUPassengerOffer(int passengerId, String offerCode, float offerAmount, float offerLimitation) {
        int offerId = -1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "INSERT INTO offer(code, discountAmount, limitation) values (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, offerCode);
            preparedStatement.setFloat(2, offerAmount);
            preparedStatement.setFloat(3, offerLimitation);
            int rows = preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                offerId = resultSet.getInt(1);
                break;
            }

            while (rows > 0) {
                System.out.println("rafte too offer");
                break;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "INSERT INTO offer_passenger (offerId, userId) values (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, offerId);
            preparedStatement.setInt(2, passengerId);

            int rows = preparedStatement.executeUpdate();

            while (rows > 0) {
                System.out.println("rafte too Offer Passenger");
                break;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    //Helpers
    //------------------------------------------------------------------------------------------------------------------------------------

    //Tested
    private static StringBuilder seeAllPassengers() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            String sqlQuery = "select id, firstName || lastName as fullName from human where userRole in ('PASSENGER')";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String fullName = resultSet.getString(2);
                stringBuilder.append("User id: " + id + "\t" + " fullName: " + fullName + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static StringBuilder seeAllUsers() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            String sqlQuery = "select id, firstName || lastName as fullName from human where userRole in ('AGENT', 'PASSENGER')";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String fullName = resultSet.getString(2);
                stringBuilder.append("User id: " + id + "\t" + " fullName: " + fullName + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static void printTicket(String cityNameSource, String cityNameDestination, Float price, String
            ticketStatus, String travelName) {
        System.out.println("This is Ticket from " + cityNameSource + " to " + cityNameDestination + " with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName);
    }

    //Tested
    public static String findUserById(int userId) {
        String userFullName = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");


            String sqlQuery = "select firstName || lastName as fullName from human where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                userFullName = resultSet.getString(1);
            }
            return userFullName;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static StringBuilder getTheAgencyTravels(int agencyId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "SELECT idTravel FROM agency_travel where idAgency = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, agencyId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String travelName = getTheTravelName(resultSet.getInt(1));

                stringBuilder.append(travelName + " \n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    private static int getTheTravelId(String name) {
        int travelId = -1;
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "SELECT id FROM travel WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                travelId = resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return travelId;

    }

    //Tested
    private static String getTheTravelName(int id) {
        String travelName = null;
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "SELECT name FROM travel WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                travelName = resultSet.getString(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return travelName;
    }

    //Tested
    private static String getTheCityName(int id) {
        String cityName = null;
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");
            String sqlQuery = "SELECT cityName FROM city WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cityName = resultSet.getString(1);
            }

            return cityName;


        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    public static int whereDoesAgentWork(int idAgent) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "SELECT idAgency FROM agency_admin WHERE idAdmin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, idAgent);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    //Tested
    public static StringBuilder getTheAllCities() {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "SELECT * FROM city";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            int id;
            String cityName, country;
            while (resultSet.next()) {
                id = resultSet.getInt(1);
                cityName = resultSet.getString(2);
                country = resultSet.getString(3);
                stringBuilder.append(id + ". " + "cityName: " + cityName + " " + "Country: " + country + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Tested
    public static StringBuilder getTheAllCities(int idToNotEnvolve) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
                    "aliiiw", "ali123");

            String sqlQuery = "SELECT * FROM city WHERE id != ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, idToNotEnvolve);
            ResultSet resultSet = preparedStatement.executeQuery();

            int id;
            String cityName, country;
            while (resultSet.next()) {
                id = resultSet.getInt(1);
                cityName = resultSet.getString(2);
                country = resultSet.getString(3);
                stringBuilder.append(id + ". " + "cityName: " + cityName + " " + "Country: " + country + "\n");
            }
            return stringBuilder;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}




