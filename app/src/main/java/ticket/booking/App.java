/*
 * This source file was generated by the Gradle 'init' task
 */
package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println("Running train booking system");
        Scanner sc= new Scanner(System.in);
        int option=0;
        UserBookingService userBookingService;
        try{
            userBookingService =new UserBookingService();
        }catch (IOException ex){
            System.out.println("There is someting wrong");
            return;
        }
        while (option!=7){
            System.out.println("1. Signup");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Train");
            System.out.println("5. Book a seat");
            System.out.println("6. Cancel my booking");
            System.out.println("7. Exit");
            option=sc.nextInt();
            Train trainSelectedForBooking = new Train();
            switch (option){
                case 1:
                    System.out.println("Enter username to signup");
                    String name=sc.next();
                    System.out.println("Enter password to signup");
                    String password=sc.next();
                    User user=new User(UUID.randomUUID().toString(), name, new ArrayList<>(), password, UserServiceUtil.hashPassword(password));
                    userBookingService.signup(user);
                    break;
                case 2:
                    System.out.println("Enter username to login");
                    String nameToLogin=sc.next();
                    System.out.println("Enter password to login");
                    String passwordToLogin=sc.next();
                    User user1=new User(UUID.randomUUID().toString(), nameToLogin, new ArrayList<>(), passwordToLogin, UserServiceUtil.hashPassword(passwordToLogin));
                    try {
                        userBookingService = new UserBookingService(user1);
                    } catch (IOException e) {
                        System.out.println("There is something wrong");
                        return;
                    }
                    break;
                case 3:
                    System.out.println("Fetching your bookings");
                    userBookingService.fetchBooking();
                    break;
                case 4:
                    System.out.println("Type your source station");
                    String source=sc.next();
                    System.out.println("Type your destination station");
                    String destination=sc.next();
                    List<Train> trains=userBookingService.getTrains(source, destination);
                    int index=1;
                    for(Train train:trains){
                        System.out.println(index+"Train Id : "+train.getTrainNo());
                        for(Map.Entry<String, String> entry:train.getStationTimes().entrySet()){
                            System.out.println("Station: "+entry.getKey()+" Time: "+entry.getValue());
                        }
                    }
                    System.out.println("Select a train by typing 1,2,3...");
                    trainSelectedForBooking=trains.get(sc.nextInt());
                    break;
                case 5:
                    System.out.println("select the seat by typing row and column");
                    System.out.println("Enter the row");
                    int row=sc.nextInt();
                    System.out.println("Enter the column");
                    int column=sc.nextInt();
                    System.out.println("Booking your seat....");
                    Boolean booked= userBookingService.bookTrainSeat(trainSelectedForBooking, row, column);
                    if(booked.equals(Boolean.TRUE)){
                        System.out.println("Booking successful!! Enjoy your journey");
                    }else{
                        System.out.println("Booking failed!! Cannot book this seat");
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
