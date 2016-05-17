package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread{

	 private String address;
	    private int port;
	    private String command;
	    private String value;
	    private TextView infoTextView;

	    private Socket socket;

	    public ClientThread(
	            String address,
	            int port,
	            String city,
	            String informationType,
	            TextView infoTextView) {
	        this.address = address;
	        this.port = port;
	        this.command = command;
	        this.value = value;
	        this.infoTextView = infoTextView;
	    }

	    @Override
	    public void run() {
	        try {
	            socket = new Socket(address, port);
	            if (socket == null) {
	                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
	            }

	            BufferedReader bufferedReader = Utilities.getReader(socket);
	            PrintWriter printWriter = Utilities.getWriter(socket);
	            if (bufferedReader != null && printWriter != null) {
	                printWriter.println(command);
	                printWriter.flush();
	                printWriter.println(value);
	                printWriter.flush();
	                String weatherInformation;
	                while ((weatherInformation = bufferedReader.readLine()) != null) {
	                    final String finalizedWeatherInformation = weatherInformation;
	                    infoTextView.post(new Runnable() {
	                        @Override
	                        public void run() {
	                            infoTextView.append(finalizedWeatherInformation + "\n");
	                        }
	                    });
	                }
	            } else {
	                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
	            }
	            socket.close();
	        } catch (IOException ioException) {
	            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
	            if (Constants.DEBUG) {
	                ioException.printStackTrace();
	            }
	        }
	    }
	}
