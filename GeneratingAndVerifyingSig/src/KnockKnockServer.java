
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class KnockKnockServer {
	
    public static void main(String[] args) throws IOException {
    	System.setProperty("javax.net.ssl.keyStore", "knockknockkeystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "mypass");
        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        
        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try {
        	SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(portNumber);
        	System.out.println("Listening for connections on port" + portNumber);
        	SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        
            String inputLine, outputLine;
            
            // Initiate conversation with client
            KnockKnockProtocol kP = new KnockKnockProtocol();
            outputLine = kP.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = kP.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}


