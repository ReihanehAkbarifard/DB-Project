//import javax.swing.*;
//import java.sql.*;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class Main {
//    public static void main(String[] args) throws SQLException {
//        boolean isInMenu = true;
//        while (isInMenu) {
//
//            int whereToGo = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Go?\n1. Agent\n2. SuperAdmin",
//                    "Sign-Up page", JOptionPane.QUESTION_MESSAGE));
//
//            if (whereToGo == 1) {
//                int agentWhatToDo = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Do?\n1. Create new Travel\n" +
//                                "2. Filter \n3. Stats\n4. Tops\n5. Exit",
//                        "Sign-Up page", JOptionPane.QUESTION_MESSAGE));
//
//                boolean isInHomePageAgent = true;
//
//                while (isInHomePageAgent) {
//                    switch (agentWhatToDo) {
//                        case 1: //create new travel OK --
//                            boolean isAddingNewTravel = true;
//                            while (isAddingNewTravel) {
//                                StringBuilder stringBuilder = new StringBuilder();
//
//                                String travelName = JOptionPane.showInputDialog(null, "Please enter Travel Name",
//                                        "Agent Page", JOptionPane.QUESTION_MESSAGE);
//
//                                String travelDateString = JOptionPane.showInputDialog(null, "Please enter Travel Date like this (2021-08-14)",
//                                        "Agent Page", JOptionPane.QUESTION_MESSAGE);
//
//                                LocalDate travelDate = LocalDate.parse(travelDateString, DateTimeFormatter.ISO_DATE);
//
//                                String travelTime = JOptionPane.showInputDialog(null, "Please enter Travel Time (like 2AM or 17 PM)",
//                                        "Agent Page", JOptionPane.QUESTION_MESSAGE);
//
//                                String travelNumberOfPeople = JOptionPane.showInputDialog(null, "Please enter Travel Number of People can join",
//                                        "Agent Page", JOptionPane.QUESTION_MESSAGE);
//
//                                stringBuilder = getTheAllCities();
//                                int numberOfCityForOrigin = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter Travel Origin from list\n" + stringBuilder,
//                                        "Agent Page", JOptionPane.QUESTION_MESSAGE));
//
//                                StringBuilder stringBuilder1 = new StringBuilder();
//                                stringBuilder1 = getTheAllCities(numberOfCityForOrigin);
//                                System.out.println(numberOfCityForOrigin);
//                                int numberOfCityForDestination = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter Travel Destination from list\n" + stringBuilder1,
//                                        "Agent Page", JOptionPane.QUESTION_MESSAGE));
//
//                                JDialog.setDefaultLookAndFeelDecorated(true);
//                                Object[] selectionValues = {"Airplane", "Bus", "Train"};
//                                String initialSelection = "Airplane";
//                                Object type = JOptionPane.showInputDialog(null, "Please enter Travel Vehicle",
//                                        "Create Travel", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
//
//                                float price = Float.parseFloat(JOptionPane.showInputDialog(null, "Enter Price for Ticket",
//                                        "Tickets Create Page", JOptionPane.INFORMATION_MESSAGE));
//
//                                int whereDoesAgentWorking = whereDoesAgentWork(22);
//
//                                Boolean isCreatedTravel = CreateTravel(travelName, travelDate, travelTime, travelNumberOfPeople, type, whereDoesAgentWorking, numberOfCityForOrigin, numberOfCityForDestination, price);
//                                if (isCreatedTravel) isAddingNewTravel = false;
//                                break;
//                            }
//                            isAddingNewTravel = false;
//                            break;
//                        case 2: //filter
//                            boolean isFiltering = true;
//                            while (isFiltering) {
//                                int agentWhatToFilter = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Filter?\n1. See all Tickets\n2. Filter By Price\n" +
//                                                "3. Filter By Time\n4. Filter By Rate\n5. Exit",
//                                        "Filter page", JOptionPane.QUESTION_MESSAGE));
//                                StringBuilder stringBuilder = new StringBuilder();
//                                switch (agentWhatToFilter) {
//                                    case 1:
//                                        //see all tickets ok --
//                                        stringBuilder = seeAllRelativeTickets(22);
//                                        JOptionPane.showInputDialog(null, "These are all your relative tickets\n" + stringBuilder, "All Tickets", JOptionPane.INFORMATION_MESSAGE);
//                                        break;
//                                    case 2:
//                                        //filter ok
//                                        stringBuilder = filterTicketsByPrice(22);
//                                        JOptionPane.showInputDialog(null, "These are all your tickets order by price\n" + stringBuilder, "All Tickets", JOptionPane.INFORMATION_MESSAGE);
//                                        break;
//                                    case 3:
//                                        //filter by time
//                                        String startTimeString = JOptionPane.showInputDialog(null, "Please enter Start Time like (2023-09-09)",
//                                                "Filter page", JOptionPane.INFORMATION_MESSAGE);
//
//                                        String endTimeString = JOptionPane.showInputDialog(null, "Please enter end Time like (2023-09-09)",
//                                                "Filter page", JOptionPane.INFORMATION_MESSAGE);
//                                        LocalDate startDate = LocalDate.parse(startTimeString, DateTimeFormatter.ISO_DATE);
//                                        LocalDate endDate = LocalDate.parse(endTimeString, DateTimeFormatter.ISO_DATE);
//
//                                        stringBuilder = filterTicketsByTime(startDate, endDate, 22);
//                                        JOptionPane.showInputDialog(null, "These are all your tickets order by time\n" + stringBuilder, "All Tickets", JOptionPane.INFORMATION_MESSAGE);
//                                        break;
//                                    case 4:
//                                        //filter by rate ok
//                                        boolean isInFilteringTravels = true;
//                                        while (isInFilteringTravels) {
//                                            int agentWhatToDoForTravels = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Filter?\n1. See all Travel and Rates\n2. Filter By Rate ASC\n" +
//                                                            "3. Filter By Rate DSC \n4. Exit",
//                                                    "Filter page", JOptionPane.QUESTION_MESSAGE));
//
//                                            switch (agentWhatToDoForTravels) {
//                                                case 1:
//                                                    stringBuilder = seeTheAllTravelsAndTheirRate(22);
//                                                    JOptionPane.showInputDialog(null, "These are all your travels and rates \n" + stringBuilder,
//                                                            "Filter page", JOptionPane.INFORMATION_MESSAGE);
//                                                    break;
//                                                case 2:
//                                                    stringBuilder = seeTheAllTravelsAndTheirRateASC(22);
//                                                    JOptionPane.showInputDialog(null, "These are all your travels and rates sorted ASC \n" + stringBuilder,
//                                                            "Filter page", JOptionPane.INFORMATION_MESSAGE);
//                                                    break;
//                                                case 3:
//                                                    stringBuilder = seeTheAllTravelsAndTheirRateDESC(22);
//                                                    JOptionPane.showInputDialog(null, "These are all your travels and rates sorted DESC \n" + stringBuilder,
//                                                            "Filter page", JOptionPane.INFORMATION_MESSAGE);
//                                                    break;
//                                                case 4:
//                                                    isInFilteringTravels = false;
//                                                    break;
//                                            }
//                                        }
//                                        break;
//                                    case 5:
//                                        isFiltering = false;
//                                        break;
//                                }
//                            }
//                            break;
//                        case 3: //stats
//                            StringBuilder stringBuilder = new StringBuilder();
//                            boolean isGettingStats = true;
//                            while (isGettingStats) {
//                                int agentWhatToDoInStats = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Do?\n1. Most Expensive Travel Ever\n" +
//                                                "2. See the Cheapest Travel Ever\n3. See the All Income\n4. See the Income in Range Rating\n5. See the Most Popular Destination\n6. Exit",
//                                        "Stats Page", JOptionPane.INFORMATION_MESSAGE));
//
//                                switch (agentWhatToDoInStats) {
//                                    case 1:
//                                        //most expensive travel ok
//                                        stringBuilder = seeTheMostExpensiveTravelPrice(22);
//                                        JOptionPane.showInputDialog(null, "This is your Most Expensive Travel Ever\n " + stringBuilder, "Travel Stats", JOptionPane.INFORMATION_MESSAGE);
//                                        break;
//                                    case 2:
//                                        //cheapest travel ok
//                                        stringBuilder = seeTheCheapestTravel(22);
//                                        JOptionPane.showInputDialog(null, "This is your Cheapest Travel Ever\n " + stringBuilder, "Travel Stats", JOptionPane.INFORMATION_MESSAGE);
//                                        break;
//                                    case 3:
//                                        //all the incomes ok
//                                        stringBuilder = seeTheAllIncome(22);
//                                        JOptionPane.showInputDialog(null, "This is your All Income Ever\n " + stringBuilder, "Travel Stats", JOptionPane.INFORMATION_MESSAGE);
//                                        break;
//                                    case 4:
//                                        //income in range ok
//                                        String startTimeString = JOptionPane.showInputDialog(null, "Please enter Start Time like (2023-09-09)",
//                                                "Filter page", JOptionPane.INFORMATION_MESSAGE);
//
//                                        String endTimeString = JOptionPane.showInputDialog(null, "Please enter end Time like (2023-09-09)",
//                                                "Filter page", JOptionPane.INFORMATION_MESSAGE);
//                                        LocalDate startDate = LocalDate.parse(startTimeString, DateTimeFormatter.ISO_DATE);
//                                        LocalDate endDate = LocalDate.parse(endTimeString, DateTimeFormatter.ISO_DATE);
//                                        stringBuilder = seeTheIncomeBetweenTwoDate(22, startDate, endDate);
//                                        JOptionPane.showInputDialog(null, "This is your All Income in range of these days\n " + stringBuilder, "Travel Stats", JOptionPane.INFORMATION_MESSAGE);
//                                        break;
//                                    case 5:
//                                        //get the Most Popular  ok --
//                                        stringBuilder = seeTheMostPopularDestination(22);
//                                        JOptionPane.showInputDialog(null, "This is your Most Popolar destination Ever\n " + stringBuilder, "Travel Stats", JOptionPane.INFORMATION_MESSAGE);
//                                        break;
//                                    case 6:
//                                        isGettingStats = false;
//                                        break;
//                                }
//                                //break;
//                            }
//                            break;
//                        case 4: //tops
//                            stringBuilder = seeTheTop5Passenger(22);
//                            int seeCity = Integer.parseInt(JOptionPane.showInputDialog(null, "These are Top 5 passenger in payment\n Enter 1 to see the name of the cities \n \n" + stringBuilder, "Agence Stats", JOptionPane.INFORMATION_MESSAGE));
//
//                            if (seeCity == 1){
//                                stringBuilder = seeTheTop5PassengerWithCityName(22);
//                                JOptionPane.showInputDialog(null, "These are Top  passenger Destination Name\n " + stringBuilder, "Agence Stats", JOptionPane.INFORMATION_MESSAGE);
//                            }
//                            break;
//                        case 5:
//                            isInHomePageAgent = false;
//                            break;
//                    }
//                    isInHomePageAgent = false;
//                }
//                isInMenu = false;
//            } else if (whereToGo == 2) {
//                StringBuilder stringBuilder = new StringBuilder();
//                boolean isInHomePageSuperAdmin = true;
//                int superAdminWhatToDo = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Do?\n1. Create new Discount Code\n" +
//                                "2. Messages\n3. Exit",
//                        "Sign-Up page", JOptionPane.QUESTION_MESSAGE));
//
//                while (isInHomePageSuperAdmin) {
//                    switch (superAdminWhatToDo) {
//                        case 1:
//                            //create offer
//                            stringBuilder = seeAllPassengers();
//                            int whoToGiveOffer = Integer.parseInt(JOptionPane.showInputDialog(null, "These are all Passengers who do you want to send an offer\n" + stringBuilder,
//                                    "Create Offer page", JOptionPane.QUESTION_MESSAGE));
//                            String offerCode = JOptionPane.showInputDialog(null, "Please enter an offer Code (Maximum 6 Character)",
//                                    "Create Offer page", JOptionPane.QUESTION_MESSAGE);
//                            float offerAmount = Float.parseFloat(JOptionPane.showInputDialog(null, "Please enter amount of offer ( 0.5 % )",
//                                    "Create Offer page", JOptionPane.QUESTION_MESSAGE));
//                            float offerlimitation = Float.parseFloat(JOptionPane.showInputDialog(null, "Please enter limit of offer ( 100000 )",
//                                    "Create Offer page", JOptionPane.QUESTION_MESSAGE));
//                            sendUPassengerOffer(whoToGiveOffer, offerCode, offerAmount, offerlimitation);
//                            break;
//                        case 2:
//                            //answer message
//                            boolean isInAnswerPage = true;
//                            while (isInAnswerPage) {
//                                int superAdminWhatToDoInAnswerPage = Integer.parseInt(JOptionPane.showInputDialog(null, "What do you want to Do?\n1. See All my Messages as Receiver\n" +
//                                                "2. See All Messages as Sender\n3. See the Chat by a specific user\n4. Exit",
//                                        "Sign-Up page", JOptionPane.QUESTION_MESSAGE));
//                                switch (superAdminWhatToDoInAnswerPage) {
//                                    case 1:
//                                        //see as receiver
//                                        stringBuilder = seeAllMessagesAsReceiver(27);
//                                        int whichToSeen = Integer.parseInt(JOptionPane.showInputDialog(null, "These are all your Messages as Receiver and you did not seen \t choose an option to seen\n" + stringBuilder,
//                                                "Answer Page", JOptionPane.QUESTION_MESSAGE));
//                                        int idSender = findThatWhoSendThisMessage(whichToSeen);
//                                        boolean isOk = updateSeenStatus(whichToSeen);
//                                        System.out.println("ki");
//                                        if (isOk) {
//                                            System.out.println("injo");
//                                            sendMessageReplyToUser(idSender, 27);
//                                        }
//                                        break;
//                                    case 2:
//                                        //see as sender
//                                        stringBuilder = seeAllMessagesAsSender(27);
//                                        JOptionPane.showInputDialog(null, "These are all your Messages as Sender\n" + stringBuilder,
//                                                "Answer Page", JOptionPane.QUESTION_MESSAGE);
//                                        break;
//                                    case 3:
//                                        //see chat by specific user
//                                        stringBuilder = seeAllUsers();
//                                        int whoToSeeChatWith = Integer.parseInt(JOptionPane.showInputDialog(null, "These are all Users\n" + stringBuilder,
//                                                "Answer page", JOptionPane.QUESTION_MESSAGE));
//
//                                        stringBuilder = seeChatWithUser(whoToSeeChatWith);
//                                        JOptionPane.showInputDialog(null, "These are all your Messages in Chat\n" + stringBuilder,
//                                                "Answer Page", JOptionPane.QUESTION_MESSAGE);
//                                        break;
//                                    case 4:
//                                        isInAnswerPage = false;
//                                        break;
//                                }
//                                isInAnswerPage = false;
//                            }
//                            break;
//                        case 3:
//                            isInHomePageSuperAdmin = false;
//                            break;
//                    }
//                }
//            }
//        }
//    }
//
//
//    //****************************************************************************************************************************
//
//    //Tested ok
//    public static Boolean CreateTravel(String travelName, LocalDate travelDate, String travelTime, String
//            travelNumberOfPeople, Object type, int idAgency, int origin, int destination, float price) {
//        int nums = Integer.parseInt(travelNumberOfPeople);
//        boolean isTravelCreated = false;
//        boolean isTravelAgencyCreated = false;
//        Vehicle vehicle = null;
//        switch (type.toString()) {
//            case "Airplane":
//                vehicle = Vehicle.Airplane;
//                break;
//            case "Bus":
//                vehicle = Vehicle.Bus;
//                break;
//            case "Train":
//                vehicle = Vehicle.Train;
//                break;
//        }
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "INSERT INTO travel (date, time, numberOfPeople, vehicle, name, origin, destination, price, seatsRemain) " +
//                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setDate(1, Date.valueOf(travelDate));
//            preparedStatement.setString(2, travelTime);
//            preparedStatement.setInt(3, nums);
//            preparedStatement.setObject(4, vehicle, Types.OTHER);
//            preparedStatement.setString(5, travelName);
//            preparedStatement.setInt(6, origin);
//            preparedStatement.setInt(7, destination);
//            preparedStatement.setFloat(8, price);
//            preparedStatement.setInt(9, nums);
//
//            int rows = preparedStatement.executeUpdate();
//            if (rows > 0) {
//                isTravelCreated = true;
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        int idTravel = getTheTravelId(travelName);
//
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "INSERT INTO agency_travel (idAgency, idTravel) " +
//                    "VALUES (?, ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, idAgency);
//            preparedStatement.setInt(2, idTravel);
//
//            int rows2 = preparedStatement.executeUpdate();
//            if (rows2 > 0) {
//                isTravelAgencyCreated = true;
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        return isTravelCreated && isTravelAgencyCreated;
//    }
//
//    //Tested ok
//    private static StringBuilder seeAllRelativeTickets(int agentID) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//
//            String sqlQuery = "select price, ticketStatus, travelId from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ?";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentID);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                Float price = resultSet.getFloat(1);
//                String ticketStatus = resultSet.getString(2);
//                String travelName = getTheTravelName(resultSet.getInt(3));
//                stringBuilder.append("Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested ok
//    private static StringBuilder filterTicketsByPrice(int idAgent) {
//        StringBuilder stringBuilder = new StringBuilder();
//
//        boolean isFilteringByPrice = true;
//        while (isFilteringByPrice) {
//            int agentHowWantToFilter = Integer.parseInt(JOptionPane.showInputDialog(null, "How do you want to Filter?\n1. Filter ASC\n" +
//                            "2. Filter DESC\n3. Get from a value\n4. Exit",
//                    "Filter Page Ticket", JOptionPane.QUESTION_MESSAGE));
//
//            switch (agentHowWantToFilter) {
//                case 1:
//                    //ASC ok --
//                    try {
//                        Class.forName("org.postgresql.Driver");
//                        Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                                "aliiiw", "ali123");
//
//                        //String sqlQuery = "SELECT source, destination, price, status, travelId FROM ticket t join travel tv ON t.travelId = tv.id join agency_travel ON tv.id = agency_travel.idTravel where idAgency = ? and status = 'Paid' order by price";
//                        String sqlQuery = "select price, status, travelId from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ? order by price";
//                        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//                        preparedStatement.setInt(1, idAgent);
//                        ResultSet resultSet = preparedStatement.executeQuery();
//
//                        while (resultSet.next()) {
//
//                            Float price = resultSet.getFloat(1);
//                            String ticketStatus = resultSet.getString(2);
//                            String travelName = getTheTravelName(resultSet.getInt(3));
//                            //stringBuilder.append("This is Ticket from " + cityNameSource + " to " + cityNameDestination + " with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName);
//                            //stringBuilder.append("\n");
//                            stringBuilder.append("Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + "\n");
//                        }
//                    } catch (SQLException | ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
//                    return stringBuilder;
//                case 2: //DESC ok --
//                    try {
//                        Class.forName("org.postgresql.Driver");
//                        Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                                "aliiiw", "ali123");
//                        //String sqlQuery = "SELECT source, destination, price, status, travelId FROM ticket t join travel tv ON t.travelId = tv.id join agency_travel ON tv.id = agency_travel.idTravel where idAgency = ? and status = 'Paid' order by price desc";
//                        String sqlQuery = "select price, status, travelId from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ? order by price desc ";
//                        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//                        preparedStatement.setInt(1, idAgent);
//                        ResultSet resultSet = preparedStatement.executeQuery();
//
//                        while (resultSet.next()) {
//                            Float price = resultSet.getFloat(1);
//                            String ticketStatus = resultSet.getString(2);
//                            String travelName = getTheTravelName(resultSet.getInt(3));
////                            stringBuilder.append("This is Ticket from " + cityNameSource + " to " + cityNameDestination + " with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName);
//                            //stringBuilder.append("\n");
//                            stringBuilder.append("Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + "\n");
//                        }
//                    } catch (SQLException | ClassNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
//                    return stringBuilder;
//                case 3:
//                    //get from and until ok
//                    int howToGetPrice = Integer.parseInt(JOptionPane.showInputDialog(null, "How do you want to Get Value Until or From??\n1. Until\n" +
//                                    "2. From\n3. Exit",
//                            "Filter Page Ticket", JOptionPane.QUESTION_MESSAGE));
//                    switch (howToGetPrice) {
//                        case 1:
//                            stringBuilder = new StringBuilder();
//                            Float value = Float.parseFloat(JOptionPane.showInputDialog(null, "Please enter your value",
//                                    "Filter Page Ticket", JOptionPane.QUESTION_MESSAGE));
//                            try {
//                                Class.forName("org.postgresql.Driver");
//                                Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                                        "aliiiw", "ali123");
//
//                                //String sqlQuery = "SELECT source, destination, price, status, travelId FROM ticket t join travel tv ON t.travelId = tv.id join agency_travel ON tv.id = agency_travel.idTravel where idAgency = ? and  status = 'Paid' and price < ?";
//                                String sqlQuery = "select price, status, travelId from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ? and price < ?";
//                                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//                                preparedStatement.setInt(1, idAgent);
//                                preparedStatement.setFloat(2, value);
//                                ResultSet resultSet = preparedStatement.executeQuery();
//
//                                while (resultSet.next()) {
//                                    Float price = resultSet.getFloat(1);
//                                    String ticketStatus = resultSet.getString(2);
//                                    String travelName = getTheTravelName(resultSet.getInt(3));
//                                    stringBuilder.append("Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + "\n");
////                                    printTicket(cityNameSource, cityNameDestination, price, ticketStatus, travelName);
//                                }
//                                JOptionPane.showInputDialog(null, "This is what you want" + stringBuilder,
//                                        "Filter Page Ticket", JOptionPane.INFORMATION_MESSAGE);
//                            } catch (SQLException | ClassNotFoundException e) {
//                                throw new RuntimeException(e);
//                            }
//                            break;
//                        case 2:
//                            stringBuilder = new StringBuilder();
//                            Float valueDSC = Float.parseFloat(JOptionPane.showInputDialog(null, "Please enter your value",
//                                    "Filter Page Ticket", JOptionPane.QUESTION_MESSAGE));
//                            try {
//                                Class.forName("org.postgresql.Driver");
//                                Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                                        "aliiiw", "ali123");
//
////                                String sqlQuery = "SELECT source, destination, price, status, travelId FROM ticket t join travel tv ON t.travelId = tv.id join agency_travel ON tv.id = agency_travel.idTravel where idAgency = ? and  status = 'Paid' and price > ?";
//                                String sqlQuery = "select price, status, travelId from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ? and price > ?";
//                                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//                                preparedStatement.setInt(1, idAgent);
//                                preparedStatement.setFloat(2, valueDSC);
//                                ResultSet resultSet = preparedStatement.executeQuery();
//
//                                while (resultSet.next()) {
//                                    Float price = resultSet.getFloat(1);
//                                    String ticketStatus = resultSet.getString(2);
//                                    String travelName = getTheTravelName(resultSet.getInt(3));
//                                    //printTicket(cityNameSource, cityNameDestination, price, ticketStatus, travelName);
//                                    stringBuilder.append("Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + "\n");
//                                }
//                                JOptionPane.showInputDialog(null, "This is what you want" + stringBuilder,
//                                        "Filter Page Ticket", JOptionPane.INFORMATION_MESSAGE);
//                            } catch (SQLException | ClassNotFoundException e) {
//                                throw new RuntimeException(e);
//                            }
//                            System.out.println();
//                            break;
//                        case 3:
//                            break;
//                    }
//                    break;
//                case 4:
//                    isFilteringByPrice = false;
//                    break;
//            }
//        }
//        return stringBuilder;
//    }
//
//    //Tested ok
//    private static StringBuilder filterTicketsByTime(LocalDate startTime, LocalDate endTime, int agentId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            //String sqlQuery = "SELECT source, destination, price, status, name,  date, time FROM ticket t join travel tv ON t.travelId = tv.id join agency_travel ON tv.id = agency_travel.idTravel WHERE idAgency = ? and (date between ? and ?) and status = 'Paid'";
//            String sqlQuery = "select price, ticketStatus, travelId, date from ticket join Travel on Ticket.travelId = Travel.id join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin on a2.id = agency_admin.idAgency where agency_admin.idAdmin = ? and date between ? and ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            preparedStatement.setDate(2, Date.valueOf(startTime));
//            preparedStatement.setDate(3, Date.valueOf(endTime));
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                Float price = resultSet.getFloat(1);
//                String ticketStatus = resultSet.getString(2);
//                String travelName = getTheTravelName(resultSet.getInt(3));
//                String date = resultSet.getString(4);
//                stringBuilder.append("This is Ticket with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName + " At Date " + date + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested ok
//    private static StringBuilder seeTheAllTravelsAndTheirRate(int agentId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "select avg(Ticket.rate) average_rate, Travel.name from Ticket join Travel on Travel.id = Ticket.travelId join Agency_Travel on Travel.id = Agency_Travel.idTravel join agency on agency.id = Agency_Travel.idAgency join agency_admin on Agency_Travel.idAgency = agency_admin.idAgency where idAdmin = ? group by Travel.name";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                float averate_rate = resultSet.getFloat(1);
//                String travelName = resultSet.getString(2);
//                stringBuilder.append("Your Travel named -> " + travelName + " has Rate -> " + averate_rate + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested ok
//    private static StringBuilder seeTheAllTravelsAndTheirRateASC(int agentId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "select avg(Ticket.rate) average_rate, Travel.name from Ticket join Travel on Travel.id = Ticket.travelId join Agency_Travel on Travel.id = Agency_Travel.idTravel join agency on agency.id = Agency_Travel.idAgency join agency_admin on Agency_Travel.idAgency = agency_admin.idAgency where idAdmin = ? group by Travel.name order by average_rate asc";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                float averate_rate = resultSet.getFloat(1);
//                String travelName = resultSet.getString(2);
//                stringBuilder.append("Your Travel named -> " + travelName + " has Rate -> " + averate_rate + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested ok
//    private static StringBuilder seeTheAllTravelsAndTheirRateDESC(int agentId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "select avg(Ticket.rate) average_rate, Travel.name from Ticket join Travel on Travel.id = Ticket.travelId join Agency_Travel on Travel.id = Agency_Travel.idTravel join agency on agency.id = Agency_Travel.idAgency join agency_admin on Agency_Travel.idAgency = agency_admin.idAgency where idAdmin = ? group by Travel.name order by average_rate desc";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                float averate_rate = resultSet.getFloat(1);
//                String travelName = resultSet.getString(2);
//                stringBuilder.append("Your Travel named -> " + travelName + " has Rate -> " + averate_rate + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested new
//    private static StringBuilder seeTheAllIncome(int agentId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "SELECT SUM(income_times_nums) AS total_income_times_nums FROM (SELECT Ticket.finalPrice * COUNT(*) AS income_times_nums FROM ticket JOIN travel ON Ticket.travelId = Travel.id JOIN Agency_Travel A ON Travel.id = A.idTravel JOIN agency a2 ON a2.id = A.idAgency join agency_admin aa on A.idAgency = aa.idAgency where idAdmin = ? GROUP BY Travel.name, Ticket.finalPrice) AS subquery";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                Float total = resultSet.getFloat(1);
//                stringBuilder.append(" Your Total Income is -> " + total + " \n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested ok
//    private static StringBuilder seeTheMostExpensiveTravelPrice(int agentId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "select Travel.name,price from travel join agency_travel on Travel.id = Agency_Travel.idTravel join agency on Agency_Travel.idAgency = agency.id join agency_admin on Agency_Travel.idAgency = agency_admin.idAgency where idAdmin = ? order by price desc limit 1";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String travelName = resultSet.getString(1);
//                Float total = resultSet.getFloat(2);
//                stringBuilder.append(travelName + " " + total + " \n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static StringBuilder seeTheCheapestTravel(int agentId) {
//
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "select Travel.name,price from travel join agency_travel on Travel.id = Agency_Travel.idTravel join agency on Agency_Travel.idAgency = agency.id join agency_admin on Agency_Travel.idAgency = agency_admin.idAgency where idAdmin = ? order by price limit 1";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String travelName = resultSet.getString(1);
//                Float total = resultSet.getFloat(2);
//                stringBuilder.append(travelName + " " + total + " \n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested New
//    private static StringBuilder seeTheIncomeBetweenTwoDate(int agentId, LocalDate startTime, LocalDate endTime) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "SELECT SUM(income_times_nums) AS total_income_times_nums FROM (SELECT Ticket.finalPrice * COUNT(*) AS income_times_nums FROM ticket JOIN travel ON Ticket.travelId = Travel.id JOIN Agency_Travel A ON Travel.id = A.idTravel JOIN agency a2 ON a2.id = A.idAgency join agency_admin aa on A.idAgency = aa.idAgency where idAdmin = ? and date between ? and ? GROUP BY Travel.name, Ticket.finalPrice ) AS subquery";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            preparedStatement.setDate(2, Date.valueOf(startTime));
//            preparedStatement.setDate(3, Date.valueOf(endTime));
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                Float total = resultSet.getFloat(1);
//                stringBuilder.append(" Your Total Income in this range is -> " + total + " \n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested new
//    private static StringBuilder seeTheMostPopularDestination(int agentId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "select Travel.destination, count(Travel.destination) as num from Travel join Agency_Travel A on Travel.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin aa on A.idAgency = aa.idAgency where idAdmin = ? group by Travel.destination order by num desc limit 1;";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String destination = getTheCityName(resultSet.getInt(1));
//                stringBuilder.append(" Your Popular Destination is -> " + destination + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static StringBuilder seeTheTop5Passenger(int agentId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "SELECT h.firstname || ' ' || h.lastname AS full_name, h.email, h.phonenumber, SUM(t.finalprice) AS total_paid_price, COUNT(DISTINCT t.travelid) AS num_destinations FROM human h JOIN passenger_ticket pt ON h.id = pt.idpassenger JOIN ticket t ON pt.idticket = t.id JOIN travel tr ON t.travelid = tr.id join Agency_Travel A on tr.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin aa on h.id = aa.idAdmin WHERE idAdmin = ? group by h.firstname, h.lastname, h.email, h.phonenumber LIMIT 5";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String fullName = resultSet.getString(1);
//                String email = resultSet.getString(2);
//                String phoneNumber = resultSet.getString(3);
//                float totalPrice = resultSet.getFloat(4);
//                int numsDestination = resultSet.getInt(5);
//                stringBuilder.append("Passenger -> " + fullName + " with Email -> " + email + " and with PhoneNumber -> " + phoneNumber + "\n" +
//                        "Total Price -> " + totalPrice + " and Distinct Destination Number -> " + numsDestination + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static StringBuilder seeTheTop5PassengerInSpecificMonth(int agentId, LocalDate startDate , LocalDate endDate) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "SELECT h.firstname || ' ' || h.lastname AS full_name, h.email, h.phonenumber, SUM(t.finalprice) AS total_paid_price, COUNT(DISTINCT t.travelid) AS num_destinations FROM human h JOIN passenger_ticket pt ON h.id = pt.idpassenger JOIN ticket t ON pt.idticket = t.id JOIN travel tr ON t.travelid = tr.id join Agency_Travel A on tr.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin aa on h.id = aa.idAdmin WHERE idAdmin = ? group by h.firstname, h.lastname, h.email, h.phonenumber LIMIT 5";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String fullName = resultSet.getString(1);
//                String email = resultSet.getString(2);
//                String phoneNumber = resultSet.getString(3);
//                float totalPrice = resultSet.getFloat(4);
//                int numsDestination = resultSet.getInt(5);
//                stringBuilder.append("Passenger -> " + fullName + " with Email -> " + email + " and with PhoneNumber -> " + phoneNumber + "\n" +
//                        "Total Price -> " + totalPrice + " and Distinct Destination Number -> " + numsDestination + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static StringBuilder seeTheTop5PassengerWithCityName(int agentId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "SELECT h.firstname || ' ' || h.lastname AS full_name, h.email, h.phonenumber, SUM(t.finalprice) AS total_paid_price, t.travelid AS num_destinations FROM human h JOIN passenger_ticket pt ON h.id = pt.idpassenger JOIN ticket t ON pt.idticket = t.id JOIN travel tr ON t.travelid = tr.id join Agency_Travel A on tr.id = A.idTravel join agency a2 on a2.id = A.idAgency join agency_admin aa on h.id = aa.idAdmin WHERE idAdmin = ? group by h.firstname, h.lastname, h.email, h.phonenumber, t.travelId LIMIT 5";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agentId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String cityName = getTheCityName(resultSet.getInt(5));
//                stringBuilder.append("City -> " + cityName + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static StringBuilder seeAllMessagesAsReceiver(int adminId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//
//            String sqlQuery = "select support_ticket.id, message_text, message_title, time, chat.idSender from support_ticket join chat on support_ticket.id = chat.messageId join human on idReceiver = human.id where human.id = ?  and message_status = false";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, adminId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                int idMessage = resultSet.getInt(1);
//                String message_text = resultSet.getString(2);
//                String message_title = resultSet.getString(3);
//                String time = resultSet.getString(4);
//                String senderName = findUserById(resultSet.getInt(5));
//                stringBuilder.append("Support Ticket " + "with ID -> " + idMessage + "  Sends from -> (" + senderName + ") at time " + time + "\nTitle: " + message_title + "\n" + "Text: " + message_text + "\n \n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static StringBuilder seeAllMessagesAsSender(int adminId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//
//            String sqlQuery = "select message_text, message_title, time, chat.idReceiver from support_ticket join chat on support_ticket.id = chat.messageId join human on idSender = human.id where human.id = ?";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, adminId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String message_text = resultSet.getString(1);
//                String message_title = resultSet.getString(2);
//                String time = resultSet.getString(3);
//                String receiverName = findUserById(resultSet.getInt(4));
//                stringBuilder.append("Support Ticket Sends to -> (" + receiverName + ") at time " + time + "\nTitle: " + message_title + "\n" + "Text: " + message_text + "\n \n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static StringBuilder seeChatWithUser(int whoToSeeChatWith) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "select message_text from support_ticket  join chat on support_ticket.id = chat.messageId where (idSender = ? and idReceiver = 27) or (idSender = 27 and idReceiver = ?) order by time";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, whoToSeeChatWith);
//            preparedStatement.setInt(2, whoToSeeChatWith);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String message_text = resultSet.getString(1);
//                stringBuilder.append("Text: " + message_text + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static int findThatWhoSendThisMessage(int whichToSeen) {
//        int idSender = -1;
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "select idSender from chat join support_ticket on chat.messageId = support_ticket.id where support_ticket.id = ?";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, whichToSeen);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                idSender = resultSet.getInt(1);
//
//            }
//            return idSender;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static void sendMessageReplyToUser(int idHuman, int idSuperAdmin) {
//
//        String messageTitle = JOptionPane.showInputDialog(null, "Please enter Message title",
//                "Answer Page", JOptionPane.QUESTION_MESSAGE);
//
//        String messageText = JOptionPane.showInputDialog(null, "Please enter Message text",
//                "Answer Page", JOptionPane.QUESTION_MESSAGE);
//
//        int idMessage = -1;
//
//        boolean isOk = false;
//
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "INSERT INTO support_ticket(message_text, message_title) values (?, ?)";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
//            preparedStatement.setString(1, messageTitle);
//            preparedStatement.setString(2, messageText);
//            int rows = preparedStatement.executeUpdate();
//
//            ResultSet resultSet = preparedStatement.getGeneratedKeys();
//
//            while (resultSet.next()) {
//                idMessage = resultSet.getInt(1);
//                break;
//            }
//
//            System.out.println(idMessage);
//            while (rows > 0) {
//                System.out.println("rafte too messages");
//                break;
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "INSERT INTO Chat (idSender, idReceiver, messageId) values (?, ?, ?)";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, idSuperAdmin);
//            preparedStatement.setInt(2, idHuman);
//            preparedStatement.setInt(3, idMessage);
//            int rows = preparedStatement.executeUpdate();
//
//            while (rows > 0) {
//                System.out.println("rafte too chat");
//                break;
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static boolean updateSeenStatus(int whichToSeen) {
//        boolean isOk = false;
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "update support_ticket set message_status = true where support_ticket.id = ?";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, whichToSeen);
//            int rows = preparedStatement.executeUpdate();
//
//            while (rows > 0) {
//                System.out.println("Updated Successfully");
//                isOk = true;
//                return isOk;
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        return isOk;
//    }
//
//    //Tested
//    private static void sendUPassengerOffer(int passengerId, String offerCode, float offerAmount, float offerLimitation) {
//        int offerId = -1;
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "INSERT INTO offer(code, discountAmount, limitation) values (?, ?, ?)";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
//            preparedStatement.setString(1, offerCode);
//            preparedStatement.setFloat(2, offerAmount);
//            preparedStatement.setFloat(3, offerLimitation);
//            int rows = preparedStatement.executeUpdate();
//
//            ResultSet resultSet = preparedStatement.getGeneratedKeys();
//
//            while (resultSet.next()) {
//                offerId = resultSet.getInt(1);
//                break;
//            }
//
//            while (rows > 0) {
//                System.out.println("rafte too offer");
//                break;
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "INSERT INTO offer_passenger (offerId, userId) values (?, ?)";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, offerId);
//            preparedStatement.setInt(2, passengerId);
//
//            int rows = preparedStatement.executeUpdate();
//
//            while (rows > 0) {
//                System.out.println("rafte too Offer Passenger");
//                break;
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//
//    //Helpers
//    //------------------------------------------------------------------------------------------------------------------------------------
//
//    //Tested
//    private static StringBuilder seeAllPassengers() {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//
//            String sqlQuery = "select id, firstName || lastName as fullName from human where userRole in ('PASSENGER')";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                int id = resultSet.getInt(1);
//                String fullName = resultSet.getString(2);
//                stringBuilder.append("User id: " + id + "\t" + " fullName: " + fullName + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static StringBuilder seeAllUsers() {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//
//            String sqlQuery = "select id, firstName || lastName as fullName from human where userRole in ('AGENT', 'PASSENGER')";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                int id = resultSet.getInt(1);
//                String fullName = resultSet.getString(2);
//                stringBuilder.append("User id: " + id + "\t" + " fullName: " + fullName + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static void printTicket(String cityNameSource, String cityNameDestination, Float price, String
//            ticketStatus, String travelName) {
//        System.out.println("This is Ticket from " + cityNameSource + " to " + cityNameDestination + " with price " + price + " and Status is " + ticketStatus + " for Travel named " + travelName);
//    }
//
//    //Tested
//    public static String findUserById(int userId) {
//        String userFullName = "";
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//
//            String sqlQuery = "select firstName || lastName as fullName from human where id = ?";
//
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, userId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                userFullName = resultSet.getString(1);
//            }
//            return userFullName;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static StringBuilder getTheAgencyTravels(int agencyId) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "SELECT idTravel FROM agency_travel where idAgency = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, agencyId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String travelName = getTheTravelName(resultSet.getInt(1));
//
//                stringBuilder.append(travelName + " \n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    private static int getTheTravelId(String name) {
//        int travelId = -1;
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "SELECT id FROM travel WHERE name = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setString(1, name);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                travelId = resultSet.getInt(1);
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        return travelId;
//
//    }
//
//    //Tested
//    private static String getTheTravelName(int id) {
//        String travelName = null;
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "SELECT name FROM travel WHERE id = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, id);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                travelName = resultSet.getString(1);
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        return travelName;
//    }
//
//    //Tested
//    private static String getTheCityName(int id) {
//        String cityName = null;
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//            String sqlQuery = "SELECT cityName FROM city WHERE id = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                cityName = resultSet.getString(1);
//            }
//
//            return cityName;
//
//
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    public static int whereDoesAgentWork(int idAgent) {
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "SELECT idAgency FROM agency_admin WHERE idAdmin = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, idAgent);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                return resultSet.getInt(1);
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        return -1;
//    }
//
//    //Tested
//    public static StringBuilder getTheAllCities() {
//        StringBuilder stringBuilder = new StringBuilder();
//
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "SELECT * FROM city";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            int id;
//            String cityName, country;
//            while (resultSet.next()) {
//                id = resultSet.getInt(1);
//                cityName = resultSet.getString(2);
//                country = resultSet.getString(3);
//                stringBuilder.append(id + ". " + "cityName: " + cityName + " " + "Country: " + country + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    public static StringBuilder getTheAllCities(int idToNotEnvolve) {
//        StringBuilder stringBuilder = new StringBuilder();
//
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
//                    "aliiiw", "ali123");
//
//            String sqlQuery = "SELECT * FROM city WHERE id != ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setInt(1, idToNotEnvolve);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            int id;
//            String cityName, country;
//            while (resultSet.next()) {
//                id = resultSet.getInt(1);
//                cityName = resultSet.getString(2);
//                country = resultSet.getString(3);
//                stringBuilder.append(id + ". " + "cityName: " + cityName + " " + "Country: " + country + "\n");
//            }
//            return stringBuilder;
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //Tested
//    public static String makeTicketPattern(int idTravel, float price) {
//        return "INSERT INTO Ticket (travelId, price, status) VALUES (" + idTravel + ", " + price + ", '" + TicketStatus.Start + "')";
//    }
//
//
//    //create tickets --ok
////                    int agencyId = whereDoesAgentWork(22);
////                    stringBuilder = getTheAgencyTravels(agencyId);
////                    String agentTravels = JOptionPane.showInputDialog(null, "These are your Travels Choose One\n" + stringBuilder,
////                            "Tickets Create Page", JOptionPane.INFORMATION_MESSAGE);
////                    int idTravel = getTheTravelId(agentTravels);
//    //createTicketsForTravel(idTravel);
//
////    private static StringBuilder seeTheTops(int whereDoesAgentWork) {
////        StringBuilder stringBuilder = new StringBuilder();
////
////    }
//
//    //    private static boolean createTicketsForTravel(int idTravel) throws SQLException {
////        boolean isGettingNums = false;
////        try {
////            Class.forName("org.postgresql.Driver");
////            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
////                    "aliiiw", "ali123");
////
////            String sqlQuery = "SELECT numberOfPeople FROM Travel WHERE id = ?";
////            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
////            preparedStatement.setInt(1, idTravel);
////            ResultSet resultSet = preparedStatement.executeQuery();
////
////            int numberOfTickets = 0;
////            while (resultSet.next()) {
////                numberOfTickets = resultSet.getInt(1);
////                isGettingNums = true;
////            }
////            float price = Float.parseFloat(JOptionPane.showInputDialog(null, "Enter Price for Ticket",
////                    "Tickets Create Page", JOptionPane.INFORMATION_MESSAGE));
////
////            ArrayList<String> tickets = new ArrayList<>();
////            for (int i = 1; i <= numberOfTickets; i++) {
////                tickets.add(makeTicketPattern(idTravel, price));
////            }
////
////            createTicket(tickets);
////
////            return isGettingNums;
////        } catch (ClassNotFoundException e) {
////            throw new RuntimeException(e);
////        }
////    }
//
//    //    //Tested New
////    public static void createTicket(ArrayList<String> tickets) throws ClassNotFoundException {
////        try {
////            Class.forName("org.postgresql.Driver");
////            Connection connection = DriverManager.getConnection("jdbc:postgresql://185.135.229.14:5432/dbproject",
////                    "aliiiw", "ali123");
////
////            TicketStatus ticketStatus = TicketStatus.Start;
////
////            String sqlQuery = "INSERT INTO Ticket (travelId, price, status) VALUES (?, ?, ?)";
////            Statement statement = connection.createStatement();
////            for (String str : tickets) {
////                statement.addBatch(str);
////            }
////            statement.executeBatch();
////
////            int numberOfTickets = 0;
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }
////    }
//}