import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 * ClientClass. This is the client class which handles all creation of client threads
 *
 * @author Jason Klimaseski
 */


public class ChatClient {
	private BufferedReader in;
	private PrintWriter out;
	private JFrame frame = new JFrame("Chat Client");
	private JTextField textField = new JTextField(40);
	private JTextArea messageArea = new JTextArea(8, 40);
	private JButton sendButton = new JButton("SEND");
	JPanel sendPanel;
	
	
	public ChatClient() {
		textField.setEditable(false);
		messageArea.setEditable(false);
		sendPanel = new JPanel();
		sendPanel.add(textField);
		sendPanel.add(sendButton);
		
		
		frame.getContentPane().add(sendPanel, "North");
		frame.getContentPane().add(new JScrollPane(messageArea), "South");
		frame.pack();
		
		textField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				out.println(textField.getText());
				textField.setText("");
			}
			
		});
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				out.println(textField.getText());
				textField.setText("");
			}
			
		});
	}
	
	private void run() throws IOException {
		String serverAddress = getServerAddress();
		Socket socket = new Socket(serverAddress, 4000);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		
		while(true) {
			String line = in.readLine();
			if(line.startsWith("SENDMENAME")) {
				out.println(getName());
			} else if (line.startsWith("NAMEACCEPTED")) {
				textField.setEditable(true);
			} else if(line.startsWith("MESSAGE")) {
				messageArea.append(line.substring(8) + "\n");
			}
		}
	}
	
	private String getServerAddress() {
		return JOptionPane.showInputDialog(frame, 
				"Enter Server Address", "Server Address", 
				JOptionPane.QUESTION_MESSAGE);
	}
	
	private String getName() {
		return JOptionPane.showInputDialog(frame, "Pick a Screen Name", 
				"Welcome to chat room", JOptionPane.PLAIN_MESSAGE);
	}
	
	public static void main(String[] args)  {
		try {
			ChatClient client = new ChatClient();
			client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			client.frame.setVisible(true);
			client.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	

}
