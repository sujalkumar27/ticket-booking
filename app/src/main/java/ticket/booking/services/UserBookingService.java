package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserBookingService {
    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper= new ObjectMapper();
    public UserBookingService() throws IOException{
        loadUsers();
    }
    public List<User> loadUsers() throws IOException{
        File users=new File(USERS_PATH);
        return  objectMapper.readValue(users, new TypeReference<List<User>>(){});
    }

    private static final String  USERS_PATH="src/main/java/ticket.booking/localdb/users.json";
    public UserBookingService(User user) throws IOException {
        this.user=user;
        loadUsers();
    }
    public Boolean loginUser(){
        Optional<User> foundUser= userList.stream().filter(user1 -> {
            return user1.getName().equalsIgnoreCase(user.getName()) &&
                UserServiceUtil.checkPassword(user.getPassword(),user1.getHashPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signup(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch(IOException e){
            return Boolean.FALSE;
        }
    }

    private  void saveUserListToFile() throws IOException{
        File userFile= new File(USERS_PATH);
        objectMapper.writeValue(userFile,userList);
    }
    public void fetchBooking(){
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId){
        Scanner sc= new Scanner(System.in);
        System.out.println("Enter the ticket id to cancel");
        ticketId=sc.next();

        if(ticketId==null || ticketId.isEmpty()){
            System.out.println("Ticket id cannot be null or empty");
            return Boolean.FALSE;
        }
        String finalticketId1=ticketId;

        boolean removed= user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalticketId1));
        String finalTicketId = ticketId;
        user.getTicketsBooked().removeIf(Ticket -> Ticket.getTicketId().equals(finalTicketId));
        if(removed){
            System.out.println("Ticket with ID "+ticketId+ "has been cancelled");
            return Boolean.TRUE;
        }else{
            System.out.println("No ticket found with ticked Id "+ticketId);
            return Boolean.FALSE;
        }
    }
    public List<Train> getTrains(String source, String destination)  {
       try {
              TrainService trainService = new TrainService();
               return trainService.searchTrains(source, destination);
         } catch (IOException e) {
              System.out.println("Error fetching trains: " + e.getMessage());
              return new ArrayList<>();
       }
    }
    public Boolean bookTrainSeat(Train train,int row,int seat) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    System.out.println("Seat booked successfully");

                    return true;//Booking successful
                } else {
                    return false;//seat is already booked
                }
            } else {
                return false;//invalid seat number
            }
        } catch (IOException e) {
            return Boolean.FALSE;
        }

    }
}
