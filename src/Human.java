import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        String email;
        String passWord;
        String phoneNumber;
        boolean verifyStatus = false;
        Role role = Role.PASSENGER;
        String firstName;
        String lastName;


        while (true) {

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


            email = JOptionPane.showInputDialog(null, "Please enter your Email Address:",
                    "Sign-Up page", JOptionPane.QUESTION_MESSAGE);


            if (email.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out all parts!");
            }

            phoneNumber = JOptionPane.showInputDialog(null, "Please enter your phoneNumber:",
                    "Sign-Up page", JOptionPane.QUESTION_MESSAGE);


            if (phoneNumber.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out all parts!");
            } else if (true) {
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

                    while (resultSet.next()) {
                        JOptionPane.showMessageDialog(null, "This email or phone number " +
                                        "have been chosen before. try another one!!!",
                                "Alert", JOptionPane.INFORMATION_MESSAGE);
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

                String sqlQuery = "INSERT INTO human (email, phoneNumber, password, verifyStatus, userRole," +
                        " firstName, lastName) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, phoneNumber);
                preparedStatement.setString(3, passWord);
                preparedStatement.setBoolean(4, verifyStatus);
                preparedStatement.setObject(5, role, Types.OTHER);
                preparedStatement.setString(6, firstName);
                preparedStatement.setString(7, lastName);


                int rows = preparedStatement.executeUpdate();


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

                while (true) {
                    if (LocalDateTime.now().getMinute() - Main.timeTrack.getMinute() < 2) {

                        sqlQuery = "SELECT otp FROM human_otp WHERE email = ?";
                        preparedStatement = connection.prepareStatement(sqlQuery);
                        preparedStatement.setString(1, email);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        while (resultSet.next()) {
                            String otpByHuman = JOptionPane.showInputDialog(null, "Please Enter the OTP Code :",
                                    "Sign-Up Page", JOptionPane.QUESTION_MESSAGE);
                            if (resultSet.getString(1).equals(otpByHuman)) {
                                JOptionPane.showMessageDialog(null, "You have signed in successfully",
                                        "verification", JOptionPane.INFORMATION_MESSAGE);
                                sqlQuery = "UPDATE human SET verifyStatus = ? WHERE email = ?";
                                preparedStatement = connection.prepareStatement(sqlQuery);
                                preparedStatement.setBoolean(1, true);
                                preparedStatement.setString(2, email);
                                preparedStatement.executeUpdate();

                                break;
                            }

                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "verification time is over",
                                "verification", JOptionPane.INFORMATION_MESSAGE);
                    }
                    sqlQuery = "DELETE FROM human_otp WHERE email = ? ";
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, email);

                    preparedStatement.executeUpdate();
                }


            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
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
                                if (resultSet1.getString(1).equals(passWord)) {
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


                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                            "and destination = ? and vehicle = ?");
                    preparedStatement.setInt(1, idOriginCity1);
                    preparedStatement.setInt(2, idOriginCity2);
                    preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
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

                                preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                        "and destination = ? and vehicle = ?");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                                resultSet = preparedStatement.executeQuery();

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


                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                            "and destination = ? and vehicle = ? and price between ? and ? ");
                    preparedStatement.setInt(1, idOriginCity1);
                    preparedStatement.setInt(2, idOriginCity2);
                    preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                    preparedStatement.setInt(4, startPrice);
                    preparedStatement.setInt(5, endPrice);
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

                                preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                        "and destination = ? and vehicle = ? and price between ? and ? ");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                                preparedStatement.setInt(4, startPrice);
                                preparedStatement.setInt(5, endPrice);
                                resultSet = preparedStatement.executeQuery();

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


                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                            "and destination = ? and vehicle = ? and price between ? and ? ");
                    preparedStatement.setInt(1, idOriginCity1);
                    preparedStatement.setInt(2, idOriginCity2);
                    preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                    preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
                    preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
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

                                preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                        "and destination = ? and vehicle = ? and price between ? and ? ");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                                preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
                                preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
                                resultSet = preparedStatement.executeQuery();

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


                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                            "and destination = ? and vehicle = ? and price between ? and ? and date between ? and ? ");
                    preparedStatement.setInt(1, idOriginCity1);
                    preparedStatement.setInt(2, idOriginCity2);
                    preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                    preparedStatement.setInt(4, startPrice);
                    preparedStatement.setInt(5, endPrice);
                    preparedStatement.setDate(6, java.sql.Date.valueOf(startDate));
                    preparedStatement.setDate(7, java.sql.Date.valueOf(endDate));
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

                                preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                        "and destination = ? and vehicle = ? and price between ? and ? and date between ? and ? ");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                                preparedStatement.setInt(4, startPrice);
                                preparedStatement.setInt(5, endPrice);
                                preparedStatement.setDate(6, java.sql.Date.valueOf(startDate));
                                preparedStatement.setDate(7, java.sql.Date.valueOf(endDate));
                                resultSet = preparedStatement.executeQuery();

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


                        } else {
                            JOptionPane.showMessageDialog(null, "there is no origin or destination such that " +
                                            "please try again", "ticket reservation",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
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
                    if(trainOrBus == 1){
                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                            "and destination = ? and vehicle = ?");
                    preparedStatement.setInt(1, idOriginCity1);
                    preparedStatement.setInt(2, idOriginCity2);
                    preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                    resultSet = preparedStatement.executeQuery();
                    }
                    else if(trainOrBus == 2){
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                "and destination = ? and vehicle = ?");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
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
                            if(trainOrBus == 1){
                                preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                        "and destination = ? and vehicle = ?");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                                resultSet = preparedStatement.executeQuery();
                            }
                            else if(trainOrBus == 2){
                                preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                        "and destination = ? and vehicle = ?");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
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
                    if(trainOrBus == 1){
                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                            "and destination = ? and vehicle = ? and price between ? and ? ");
                    preparedStatement.setInt(1, idOriginCity1);
                    preparedStatement.setInt(2, idOriginCity2);
                    preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                    preparedStatement.setInt(4, startPrice);
                    preparedStatement.setInt(5, endPrice);
                    resultSet = preparedStatement.executeQuery();
                    }
                    else if (trainOrBus == 2){
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                "and destination = ? and vehicle = ? and price between ? and ? ");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
                        preparedStatement.setInt(4, startPrice);
                        preparedStatement.setInt(5, endPrice);
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

                                if(trainOrBus == 1){
                                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                            "and destination = ? and vehicle = ? and price between ? and ? ");
                                    preparedStatement.setInt(1, idOriginCity1);
                                    preparedStatement.setInt(2, idOriginCity2);
                                    preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                                    preparedStatement.setInt(4, startPrice);
                                    preparedStatement.setInt(5, endPrice);
                                    resultSet = preparedStatement.executeQuery();
                                }
                                else if (trainOrBus == 2){
                                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                            "and destination = ? and vehicle = ? and price between ? and ? ");
                                    preparedStatement.setInt(1, idOriginCity1);
                                    preparedStatement.setInt(2, idOriginCity2);
                                    preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
                                    preparedStatement.setInt(4, startPrice);
                                    preparedStatement.setInt(5, endPrice);
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
                    if(trainOrBus == 1) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                "and destination = ? and vehicle = ? and price between ? and ? ");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                        preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
                        preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
                        resultSet = preparedStatement.executeQuery();
                    }
                    else if(trainOrBus == 2) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                "and destination = ? and vehicle = ? and price between ? and ? ");
                        preparedStatement.setInt(1, idOriginCity1);
                        preparedStatement.setInt(2, idOriginCity2);
                        preparedStatement.setObject(3, Vehicle.Bus, Types.OTHER);
                        preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
                        preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
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

                                if(trainOrBus == 1) {
                                    preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                            "and destination = ? and vehicle = ? and price between ? and ? ");
                                    preparedStatement.setInt(1, idOriginCity1);
                                    preparedStatement.setInt(2, idOriginCity2);
                                    preparedStatement.setObject(3, Vehicle.Train, Types.OTHER);
                                    preparedStatement.setDate(4, java.sql.Date.valueOf(startDate));
                                    preparedStatement.setDate(5, java.sql.Date.valueOf(endDate));
                                    resultSet = preparedStatement.executeQuery();
                                }
                                else if(trainOrBus == 2) {
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
                    if(trainOrBus == 1) {
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
                    }
                    else if(trainOrBus == 2) {
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

                                preparedStatement = connection.prepareStatement("SELECT * FROM travel where origin = ?" +
                                        "and destination = ? and vehicle = ? and price between ? and ? and date between ? and ? ");
                                preparedStatement.setInt(1, idOriginCity1);
                                preparedStatement.setInt(2, idOriginCity2);
                                preparedStatement.setObject(3, Vehicle.Airplane, Types.OTHER);
                                preparedStatement.setInt(4, startPrice);
                                preparedStatement.setInt(5, endPrice);
                                preparedStatement.setDate(6, java.sql.Date.valueOf(startDate));
                                preparedStatement.setDate(7, java.sql.Date.valueOf(endDate));
                                resultSet = preparedStatement.executeQuery();

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


                        } else {
                            JOptionPane.showMessageDialog(null, "there is no origin or destination such that " +
                                            "please try again", "ticket reservation",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
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


}




