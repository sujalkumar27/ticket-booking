package ticket.booking.entities;

import java.util.List;

public class User {
    private String userId;
    private String name;
    private List<Ticket> ticketsBooked;

    private String password;
    private String hashedPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashPassword() {
        return hashedPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashedPassword = hashPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void printTickets(){
        for(int i=0;i<ticketsBooked.size();i++){
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }

    public User(String userId, String name, List<Ticket> ticketsBooked, String password, String hashedPassword) {
        this.userId = userId;
        this.name = name;
        this.ticketsBooked = ticketsBooked;
        this.password = password;
        this.hashedPassword = hashedPassword;
    }
}
