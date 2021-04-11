import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ElevatorPanel {
    static Map<Integer, FloorHandlerThread> floorHandlerThreadMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        System.out.println("ELEVATOR PANEL");

        int ELEVATORS_QUANTITY = 2;

        ElevatorDistributor elevatorDistributor = new ElevatorDistributor(ELEVATORS_QUANTITY);
        int portNumber = 12345;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Thread statusChecker = statusChecker(elevatorDistributor);
        statusChecker.start();

        while(true){
            new FloorHandlerThread(serverSocket.accept(), elevatorDistributor).start();
        }
    }

    private static Thread statusChecker(ElevatorDistributor elevatorDistributor){
        return new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while(true) {
                int elevatorNumber = Integer.parseInt(scanner.next());
                System.out.println(elevatorDistributor.getElevatorStatus(elevatorNumber));
            }
        });
    }
}
