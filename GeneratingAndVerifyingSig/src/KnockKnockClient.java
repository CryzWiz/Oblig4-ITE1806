
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class KnockKnockClient {
    public static void main(String[] args) throws IOException {
    	System.setProperty("javax.net.ssl.trustStore", "keystore");
    	SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
        if (args.length != 2) {
            System.err.println(
                "Usage: java KnockKnockClient <host name> <port number>");
            System.exit(1);
        }
 
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
 
        try {
        	SSLSocket kkSocket = (SSLSocket)f.createSocket(hostName, portNumber);
        	kkSocket.startHandshake();
        	PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
        	BufferedReader in = new BufferedReader(
            new InputStreamReader(kkSocket.getInputStream()));
        
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;
 
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye.")){
                	System.out.println("Status: Disconnected");
                    break;
                }
                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
}

