import java.net.*;
import java.util.Scanner;

public class UDPSender {
	
	private final static int PACKETSIZE = 100 ;

	public static void main(String[] args) 
   {
	      // Check the arguments
	      if( args.length != 3 )
	      {
	         System.out.println( "usage: java UDPSender host port n messages" ) ;
	         return ;
	      }
	      DatagramSocket socket = null ;
	      DatagramSocket ackSocket = null;
	      try
	      {
	         // Convert the arguments first, to ensure that they are valid
	         InetAddress host = InetAddress.getByName( args[0] ) ;
	         int port         = Integer.parseInt( args[1] ) ;
	         int nMsgs		  = Integer.parseInt(args[2]);
	         socket = new DatagramSocket() ;
	         
	         // Prepare ACK receiving
	         ackSocket = new DatagramSocket( port + 1 ) ;
     
	         String message = null;

    		 System.out.println("Sending messages:");
	         boolean exit = true;
    		 while (exit) {
	    		 for (int i = 0; i < nMsgs ; i++) {
	    			 message = "Message " + Integer.toString(i  + 1);
	    			 byte [] data = message.getBytes() ;
	        		 DatagramPacket packet = new DatagramPacket( data, data.length, host, port ) ;
	        		 socket.send( packet ) ;
	        		 System.out.println(new String(packet.getData()).trim());
	        		 
	        		 DatagramPacket ackPacket = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE ) ;
	 	             ackSocket.receive( ackPacket ) ;
	 	             System.out.println( ackPacket.getAddress() + " " + ackPacket.getPort() + ": " + new String(ackPacket.getData()).trim() ) ;

	 	             if ( !new String(ackPacket.getData()).trim().equals("ACK: " + message ) ) {
	 	            	System.out.println("Packet dropped, closing connection");
	 		            socket.close() ;
	 		            ackSocket.close();
	 	            	exit = false;
	 	             }
	        		 
	    		 }
	    		 exit = false;
    		 }
    		 
	      }
	      catch( Exception e )
	      {
	         System.out.println( e ) ;
	      }
	      finally
	      {
	         if( socket != null )
	        	System.out.println("Script finished, closing connection");
	            socket.close() ;
	            ackSocket.close();
      }
   }
}

