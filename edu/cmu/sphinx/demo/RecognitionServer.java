package edu.cmu.sphinx.demo;
 
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.*;
import edu.cmu.sphinx.util.props.ConfigurationManager;

import java.net.*;
import java.io.*;


/**
 * A simple HelloWorld demo showing a simple speech application built using Sphinx-4. This application uses the Sphinx-4
 * endpointer, which automatically segments incoming audio into utterances and silences.
 */

public class RecognitionServer extends Thread { 
    public static void main(String[] args) {
        ConfigurationManager cm;
      //String serverName = args[0];
  	   String serverName = "localhost";
        //int port = Integer.parseInt(args[1]);
  	   int port = 5555;
  
        if (args.length > 0) {
            cm = new ConfigurationManager(args[0]);
        } else {
            cm = new ConfigurationManager(RecognitionServer.class.getResource("helloworld.config.xml"));
        }
        Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();
              
        // start the microphone or exit if the programm if this is not possible
        Microphone microphone = (Microphone) cm.lookup("microphone");
        if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            System.exit(1);
        }
        System.out.println("Say: (Light) (On|Off|High|Low)");
        // loop the recognition until the programm exits.
        System.out.println("Start speaking. Press Ctrl-C to quit.\n");
        while (true) {
            /* decode the audio file */
            Result result = recognizer.recognize();
            if (result != null) {
            	String todo = result.getBestFinalResultNoFiller();
                System.out.println(result.getBestFinalResultNoFiller());
           
                try
                {
                   System.out.println("Connecting to " + serverName
                                       + " on port " + port);
                   Socket client = new Socket(serverName, port);
                   System.out.println("Just connected to "
                                + client.getRemoteSocketAddress());
                   OutputStream outToServer = client.getOutputStream();
                   DataOutputStream out =
                                 new DataOutputStream(outToServer);

                   out.writeUTF(todo
                                + client.getLocalSocketAddress());
                   InputStream inFromServer = client.getInputStream();
                   DataInputStream in =
                                  new DataInputStream(inFromServer);
                   System.out.println("Server says " + in.readUTF());
                   client.close();
                }catch(IOException e)
                {
                   e.printStackTrace();
                }
            
            }
       }
   }
}