import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FloorHandlerThread extends Thread{

    private final PrintWriter out;
    private final Integer floorNumber;
    private final BufferedReader in;
    private final ElevatorDistributor elevatorDistributor;

    public FloorHandlerThread(Socket clientSocket, ElevatorDistributor elevatorDistributor) throws IOException {
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.elevatorDistributor = elevatorDistributor;

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        floorNumber = Integer.parseInt(in.readLine());
        ElevatorPanel.floorHandlerThreadMap.put(floorNumber, this);
    }


    void sendMessage(String serverMessage){
        try{
            out.println(serverMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            String floorDestination;
            while((floorDestination = in.readLine()) != null){
                System.out.println("["+floorNumber+"]: "+floorDestination);

                if(!ElevatorPanel.floorHandlerThreadMap.containsKey(Integer.parseInt(floorDestination))) {
                    sendMessage("NO FLOOR IN THE BUILDING WITH SUCH NUMBER");
                } else if(floorNumber.equals(Integer.parseInt(floorDestination))){
                    sendMessage("YOU ARE ON THE DESTINATION FLOOR");
                } else {
                    sendMessage("PLEASE, WAIT FOR THE ELEVATOR NUMBER: " + elevatorDistributor.getNumber(floorNumber, Integer.parseInt(floorDestination)));
                }
            }
        } catch (IOException e) { e.printStackTrace();
        }finally {
            if(out != null) out.close();
        }
    }
}
