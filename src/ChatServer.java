import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 1699;
    private static List<PrintWriter> writers = new ArrayList<PrintWriter>();
    private static ServerSocket listener;
    private static Scanner scanner;
    public static void main(String[] args) throws IOException {
        System.out.println("The chat server is running. Port is " + PORT);
        listener = new ServerSocket(PORT);
        scanner = new Scanner(System.in);
        while(true) {
            Socket socket = listener.accept();
            Handler h = new Handler();
            h.socket = socket;
            h.setPriority(Thread.NORM_PRIORITY);
            h.start();
        }
    }
    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String input;
        public void run() {
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                writers.add(out);
                while (true) {
                    input = in.readLine();
                    if (input.startsWith("NAME")) {
                        name = input.substring(4);
                        sendData("New user has connected, his name: " + name);
                    } else
                        sendData(input);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        private static void sendData(String s){
            int i = 0;
            for(PrintWriter pw: writers){
                i++;
                pw.println(s);
            }
            System.out.println(s);
        }
    }
}