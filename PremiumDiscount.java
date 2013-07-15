package acme;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.net.UnknownHostException;

import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ProcedureCallback;

public class PremiumDiscount {
	  public static void main(String[] args) throws UnknownHostException, IOException {
		  
		    File file = new File("/opt/poc/main_execute/premiums.csv");

	        int ch;
	        StringBuffer strContent = new StringBuffer("");
	        FileReader fileInSt = null;
	        String readLine; 
	        
	        ClientConfig clientConfig = new ClientConfig();
	      	org.voltdb.client.Client client = org.voltdb.client.ClientFactory.createClient(clientConfig);
	        
	        // Client instance connected to the database running on
	        // the specified IP address, in this case 127.0.0.1. The
	        // database always runs on TCP/IP port 21212.
	        client.createConnection("localhost");
	        
	        try{
	            FileInputStream fStream = new FileInputStream(file);
	            DataInputStream in = new DataInputStream(fStream);
	            BufferedReader br = new BufferedReader(new InputStreamReader(in));
	            fileInSt = new FileReader(file);
	            
	            int curLineNr = 1;
	            int skipLines = 1;

	            while ((readLine = br.readLine()) != null)   {
	            	if (curLineNr++ <= skipLines) {
	                    continue;
	                }
	               System.out.println("ReadLine "+readLine.length() );
	               System.out.println("ReadLine "+readLine.toString() );
	               String[] values = readLine.toString().split(",");
	               
	               client.callProcedure(new ProcedureCallback() {
		  				@Override
		  					public void clientCallback(ClientResponse clientResponse) throws Exception { 
				  				  if (clientResponse.getStatus() != ClientResponse.SUCCESS) {
				  			           System.err.println(clientResponse.getStatusString());
				  			    } 
				  					
		  					}
		  		   		},"InsertPremiumDiscount", 
		  		   						values[0].toString(),
		  		   						values[1].toString()
	            	);
	             
	               System.out.println(values[0]);
	               System.out.println(values[1]);
	            }
		  }catch(Exception e){
			  e.printStackTrace();
		  }
	  }
}
