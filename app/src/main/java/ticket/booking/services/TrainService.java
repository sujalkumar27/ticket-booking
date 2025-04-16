package ticket.booking.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class TrainService {
    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAIN_DB_PATH = "src/main/java/ticket/booking/localdb/trains.json";
    public TrainService() throws IOException {
        File trainFile = new File(TRAIN_DB_PATH);
        trainList = objectMapper.readValue(trainFile, new TypeReference<List<Train>>() {});
    }
    public List<Train> searchTrains(String source, String destination) {
       return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }
    private boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();
        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());
        return (sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex);
    }
    public void addTrain(Train newrTrain)  {
        //check if train already exists
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainNo().equalsIgnoreCase(newrTrain.getTrainNo()))
                .findFirst();
        if (existingTrain.isPresent()) {
            System.out.println("Train with this train number already exists");
            updateTrain(newrTrain);

        }else {
            trainList.add(newrTrain);
            saveTrainListToFile();
        }
    }
    public void updateTrain(Train updatedTrain) {
        //find the index of train with same train id
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainNo().equalsIgnoreCase(updatedTrain.getTrainNo()))
                .findFirst();
        if (existingTrain.isPresent()) {
            //if found replace the existing train with new train
            trainList.remove(existingTrain.get());
            trainList.add(updatedTrain);
            saveTrainListToFile();
        } else {
            //if not found treat it as adding a new train
            addTrain(updatedTrain);
        }
    }
    private void saveTrainListToFile() {
        try {
            objectMapper.writeValue(new File(TRAIN_DB_PATH), trainList);
        } catch (IOException e) {
            System.out.println("Error saving train list to file: " + e.getMessage());
            e.printStackTrace();//handle the exception based on your applications requirements
        }
    }
}
