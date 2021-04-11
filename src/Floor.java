import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Floor {

    public static void main(String[] args) throws IOException {
        System.out.println("FLOOR");
        String hostName = "localhost";
        Scanner scanner = new Scanner(System.in);

        System.out.print("Floor number: ");
        int number = Integer.parseInt(scanner.nextLine());
        int serverPortNumber = 12345;


        Socket socket = new Socket(hostName, serverPortNumber);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(number);

        Thread sendRequest = sendRequest(out);
        Thread getResponse = getResponse(in);

        sendRequest.start();
        getResponse.start();

    }

    private static Thread sendRequest(PrintWriter out){
        return new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while(true) {
                String destination;
                destination = scanner.nextLine();
                out.println(destination);
            }
        });
    }

    private static Thread getResponse(BufferedReader in){
        return new Thread(() -> {
            try{
                String serverMessage;
                while((serverMessage = in.readLine()) != null){
                        System.out.println(serverMessage);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
