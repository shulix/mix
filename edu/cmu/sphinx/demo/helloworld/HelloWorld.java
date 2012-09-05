package edu.cmu.sphinx.demo.helloworld;
 
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.*;
import edu.cmu.sphinx.util.props.ConfigurationManager;



/**
 * A simple HelloWorld demo showing a simple speech application built using Sphinx-4. This application uses the Sphinx-4
 * endpointer, which automatically segments incoming audio into utterances and silences.
 */
public class HelloWorld {
 
    public static void main(String[] args) {
        ConfigurationManager cm;
 
        if (args.length > 0) {
            cm = new ConfigurationManager(args[0]);
        } else {
            cm = new ConfigurationManager(HelloWorld.class.getResource("helloworld.config.xml"));
        }
 
        Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();
        ConfidenceScorer cs = (ConfidenceScorer) cm.lookup("confidenceScorer");
        
        // start the microphone or exit if the programm if this is not possible
        Microphone microphone = (Microphone) cm.lookup("microphone");
        if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            System.exit(1);
        }
 
        System.out.println("Say: (Light) (On|Off)");
 
        // loop the recognition until the programm exits.
        System.out.println("Start speaking. Press Ctrl-C to quit.\n");
        while (true) {
           
             Result result = recognizer.recognize();

             
            if (result != null) {
                String resultText = result.getBestFinalResultNoFiller();
                System.out.println("i got " + result.getBestPronunciationResult() + '\n');
                
          
           	    ConfidenceResult cr = cs.score(result);
        	    Path best = cr.getBestHypothesis();
        	    System.out.println(best.getTranscription());
        		String resultTextcl = result.getTimedBestResult(false,true);
                System.out.println(resultTextcl);
                /* 
                 * print out confidence of individual words  
                 * in the best path 
                 */  
                  //System.out.println(); 


                //System.out.println("You said: " + resultText + '\n');
            } else {
                System.out.println("I can't hear what you said.\n");
            }
        }
    }
    

}