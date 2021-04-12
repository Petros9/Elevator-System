import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ElevatorDistributor {
    private final Lock lock = new ReentrantLock();
    private final ArrayList<Elevator> elevators;
    private static final Integer INFINITY = 100;

    private static ArrayList<Elevator> generateElevators(int elevatorsQuantity){
        ArrayList<Elevator> elevators = new ArrayList<>();
        int currentNumber = 0;

        while(currentNumber < elevatorsQuantity){
            elevators.add(new Elevator(currentNumber, 0));
            currentNumber++;
        }

        for(Elevator elevator : elevators){
            elevator.start();
        }
        return elevators;
    }

    public ElevatorDistributor(int elevatorsQuantity){
        this.elevators = generateElevators(elevatorsQuantity);
    }

    private int calculateDistance(int passengerDestination, int passengerCurrentFloor, int elevatorLastDestination, int elevatorWholeDistance){
        int currentElevatorMovingVector = Math.abs(elevatorLastDestination - elevatorWholeDistance);
        int elevatorMovingToPassengerVector = Math.abs(elevatorLastDestination - passengerCurrentFloor);
        int passengerMovingVector = Math.abs(passengerDestination - passengerCurrentFloor);
        return currentElevatorMovingVector + elevatorMovingToPassengerVector + passengerMovingVector;
    }

    private Elevator chooseTransportingElevatorNumber(int passengerDestination, int passengerCurrentFloor){
        Elevator transportingElevator = null;
        int distance = INFINITY;

        for(Elevator elevator : elevators){
            if(elevator.isFreeOfTasks()){
                transportingElevator = elevator;
                break;
            } else if(elevator.getDestination() == passengerCurrentFloor){
                transportingElevator = elevator;
                break;
            } else {
                int elevatorDistance = calculateDistance(passengerDestination, passengerCurrentFloor,elevator.getLastDestination(), elevator.getWholeDistance());
                if(distance > elevatorDistance){
                    distance = elevatorDistance;
                    transportingElevator = elevator;
                }
            }
        }

        return transportingElevator;
    }

    public String getElevatorStatus(int elevatorNumber){
        Elevator resultElevator = null;
        for(Elevator elevator : elevators){
            if(elevator.getNumber() == elevatorNumber){
                resultElevator = elevator;
                break;
            }
        }

        if(resultElevator != null) {
            return ("ELEVATOR ID: " + elevatorNumber + " CURRENT FLOOR: " + resultElevator.getCurrentFloor() + " DESTINATION FLOOR: " + resultElevator.getDestination());
        } else {
            return "THERE IS NO ELEVATOR WITH SUCH ID";
        }
    }

    public int getNumber(int floorNumber, int destination){
        lock.lock();
        Elevator resultElevator;
        try{
            resultElevator = chooseTransportingElevatorNumber(destination, floorNumber);
            resultElevator.setDestination(floorNumber);
            resultElevator.setDestination(destination);
        } finally {
            lock.unlock();
        }
        return resultElevator.getNumber();
    }
}
