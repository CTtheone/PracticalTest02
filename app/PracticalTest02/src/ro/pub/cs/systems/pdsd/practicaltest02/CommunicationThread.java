package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.util.Log;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;
	private Object Date;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
        Log.i(Constants.TAG, "A new communication thread is awake!");
    }

    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
                    
                    String command = bufferedReader.readLine();
                    String value = bufferedReader.readLine();
                    HashMap<String, Information> data = serverThread.getData();
                    Information information = new Information();
                    
                    Log.i(Constants.TAG, "Command:" + command);
                    Log.i(Constants.TAG, "Value:" + value);
                    
                    String result = null;
                    
                    if (command.equals("get")) {
                    	Information ret = data.get(value); 
                    	if (ret != null) {
                			Date date2 = Calendar.getInstance().getTime();
                	
                			if (date2.getMinutes() - ret.time.getMinutes() > 0 ) {
                				result = "Old entrance";
                			} else {
                				result = ret.info;
                			}
                			printWriter.println(result);
                			printWriter.flush();
                    	}
                    	
                    } else if (command.equals("put")) {
                    	information.info = new String(value);
                    	// trebuie sa ii pun time-stam-ul actual
                    	
                    	Log.i(Constants.TAG, "Get the date from the remote server!");
                    	
                    	try {
//	                    	HttpClient httpClient = new DefaultHttpClient();
//	                        HttpPost httpPost = new HttpPost(Constants.WEB_SERVICE_ADDRESS);
//	                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//	                        String pageSourceCode = httpClient.execute(httpPost, responseHandler);
//	                        
//	                        Log.e(Constants.TAG, "Informatia primita:" + pageSourceCode);
                    	} catch (Exception e) {
                    		e.printStackTrace();                    		
                    	}
                    	
                    	information.time = Calendar.getInstance().getTime();
                    			
                    }
                }
                socket.close();
            } catch (Exception ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
            
                    
                    
//                            printWriter.println(result);
//                            printWriter.flush();
           
    }
  }
}
