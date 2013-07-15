package com;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ProcedureCallback;

public class basket {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		
			/*
			 * Get the file from the path 
			 */
		  	File file = new File("/opt/poc/main_execute/basket_dump.txt");
		  	
		  	int vCounter = 0;
		  	/*
		  	 *Get the argument from the command line 
		  	 */
		  /*	if(args[0].length() > 0){
		  		vCounter = Integer.parseInt(args[0].toString()); 
		  	} else {
		  		vCounter = 1000;
		  	}*/

	        int ch;
	        StringBuffer strContent = new StringBuffer("");
	        FileReader fileInSt = null;
	        String readLine; 
	        
	        boolean firstLine = true;

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
	            int count = 0;
	            /*
	             * Skip the first line
	             */
	            while ((readLine = br.readLine()) != null) {   
	            	if (curLineNr++ <= skipLines) {
	            		System.out.println("ReadLine "+readLine.toString());
	            		
	            		break;
	            	}
	        	 }
	            
	            while ((readLine = br.readLine()) != null)   {
	               String[] values = readLine.toString().split("\\|");
	              
	               System.out.println(values[0]+"|"+values[1]+"|"+values[2]+"|"+values[3]+"|"+values[4]);
	               
	               client.callProcedure(new ProcedureCallback() {
		  				@Override
		  					public void clientCallback(ClientResponse clientResponse) throws Exception { 
				  				  if (clientResponse.getStatus() != ClientResponse.SUCCESS) {
				  			           System.err.println(clientResponse.getStatusString());
				  			    } 
				  					
		  					}
		  		   		},"BasketInsert", 
		  		   						values[0].toString(),
		  		   						values[1].toString(),
		  		   						Float.parseFloat(values[2].toString()),
		  		   						values[3].toString(),
		  		   						values[4].toString()
	            	);
	               
	               count++;
	               System.out.println("count "+count);
	            }
	            /*
	             * File close
	             */
	            fileInSt.close();

	        }catch (FileNotFoundException e)
	        {
	            System.out.println("file" + file.getAbsolutePath()+ "could not be found");
	        } 
	        catch (IOException ioe) {
	            System.out.println("problem reading the file" + ioe);
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
      
	}

}
