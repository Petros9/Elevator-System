import java.util.LinkedList;
import java.util.Queue;

public class Elevator extends Thread{

    private final int number;
    private int currentFloor;
    private final Queue<Integer> destinationsQueue;
    private boolean justArrived;

    public Elevator(int number, int currentFloor){
        this.number = number;
        this.currentFloor = currentFloor;
        this.destinationsQueue = new LinkedList<>();
        this.justArrived = false;
    }

    public void setDestination(int destination) {
        this.destinationsQueue.add(destination);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getDestination() {
        if (destinationsQueue.isEmpty()) {
            return currentFloor;
        }
        return this.destinationsQueue.peek();
    }

    public int getNumber() {
        return number;
    }

    public boolean isFreeOfTasks(){
        return this.destinationsQueue.isEmpty();
    }

    @Override
    public void run(){

        while(true){
            try {
                if(justArrived) {
                    System.out.println("QUEUE WITH ID " + number + " ARRIVED AT FLOOR NUMBER " + destinationsQueue.peek());
                    justArrived = false;
                    destinationsQueue.poll();
                    int ARRIVAL_TIME = 8000;
                    Thread.sleep(ARRIVAL_TIME);
                } else if(destinationsQueue.isEmpty()) {
                    int WAITING_TIME = 2000;
                    Thread.sleep(WAITING_TIME);
                } else {
                    System.out.println("MOVING QUEUE WITH ID: " + number + " TO FLOOR NUMBER: "+ destinationsQueue.peek() + " CURRENT FLOOR " + currentFloor);
                    int MOVE_TIME = 5000;
                    Thread.sleep(MOVE_TIME);

                    if(destinationsQueue.peek() > currentFloor){
                        currentFloor++;
                    } else {
                        currentFloor --;
                    }

                    if(destinationsQueue.peek() == currentFloor){
                        justArrived = true;
                    }
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}
