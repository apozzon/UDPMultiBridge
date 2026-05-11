package Std;

import java.awt.Font;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UdpMultiBridge {

	// Reference to the configuration window
	private static Configuration secondFrame;
	// Main application window
	private static JFrame frame;
	// Configuration object to hold settings
	public static Config config;
	// suspend or resume activity
	public static boolean suspendResume = true;

	public static void main(String[] args) {

		String destinationHost = "127.0.0.1";
		int destinationPort = 9999;

		// Create the main application window
		frame = new JFrame();
		// Set the title of the window
		frame.setTitle("UDP messeges relauncher by I2TPY");
		// Load an icon image from resources
		ImageIcon icon = new ImageIcon(UdpMultiBridge.class.getResource("/img/I2TPY.png"));

		// Set the window icon
		frame.setIconImage(icon.getImage());
		// Set window size and position
		frame.setBounds(100, 100, 900, 550);
		// Set default close operation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set layout manager to null for absolute positioning
		frame.getContentPane().setLayout(null);

		// Create a settings button
		JButton btnSetting = new JButton("...");

		// Add action listener to the settings button
		btnSetting.addActionListener(e -> {

			// Check if the configuration window is null or not displayed
			if (secondFrame == null || !secondFrame.isDisplayable()) {
				// Create and show the configuration window
				secondFrame = new Configuration();
				secondFrame.setVisible(true);
			} else {
				// Bring the configuration window to the front
				secondFrame.toFront();
				secondFrame.requestFocus();
			}

		});
		// Set button position and size
		btnSetting.setBounds(849, 11, 25, 23);
		// Add button to the window
		frame.getContentPane().add(btnSetting);

		// Create a label for the message area
		JLabel lblNewLabel_1 = new JLabel("ADIF CREATED  AREA");
		// Set horizontal alignment
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		// Set font
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 24));
		// Set position and size
		lblNewLabel_1.setBounds(10, 31, 282, 31);
		// Add label to the window
		frame.getContentPane().add(lblNewLabel_1);

		// Create a new button to close the application and related listener
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExit.setBounds(785, 463, 89, 23);
		frame.getContentPane().add(btnExit);
		btnExit.addActionListener(e -> {

			// close sockets if needed

			System.exit(0);
		});
		// Create a label for configuration
		JLabel lblNewLabel = new JLabel("Configuration");
		// Set position and size
		lblNewLabel.setBounds(768, 13, 85, 19);
		// Add label to the window
		frame.getContentPane().add(lblNewLabel);
		// create label for showing port in use
		JLabel lblUdpInUse = new JLabel("Listening UDP");
		lblUdpInUse.setBounds(264, 41, 202, 23);
		frame.getContentPane().add(lblUdpInUse);

		// create suspend-resume button
		JButton btnSuspend = new JButton("Suspend/Resume");
		btnSuspend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSuspend.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSuspend.setBounds(568, 465, 179, 23);
		btnSuspend.setText("Suspend");
		frame.getContentPane().add(btnSuspend);
		btnSuspend.addActionListener(e -> {
			if (suspendResume) {
				btnSuspend.setText("Resume");
				suspendResume = false;
			} else {
				btnSuspend.setText("Suspend");
				suspendResume = true;
			}
		});

		// Create a text area for displaying messages
		JTextArea textArea = new JTextArea();
		// Enable line wrapping
		textArea.setLineWrap(true);
		// Make text area non-editable
		textArea.setEditable(false);

		// Create a scroll pane for the text area
		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Set scroll pane position and size
		scroll.setBounds(10, 64, 864, 386);

		// Add scroll pane to the window
		frame.getContentPane().add(scroll);

		// Make the window visible
		frame.setVisible(true);

		// Load configuration settings
		config = Configuration.leggiConfig();
		// memorize in a vector to resed on all valid UDP number
		String[] udpArray = { config.udp1, config.udp2, config.udp3, config.udp4, config.udp5, config.udp6, config.udp7,
				config.udp8, config.udp9 };

		// listen port
		int listenPort;
		// Parse UDP port from configuration
		try {
			listenPort = Integer.parseInt(config.nUDP);
		}
		// If parsing fails, use default port 9999
		catch (NumberFormatException e) {
			listenPort = 9999;
		}

		try (DatagramSocket receiveSocket = new DatagramSocket(listenPort);
				DatagramSocket sendSocket = new DatagramSocket()) {
			System.out.println("Listening on UDP port " + listenPort);
			lblUdpInUse.setText("Listening on UDP port " + listenPort);

			byte[] buffer = new byte[2048];

			while (true) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				receiveSocket.receive(packet);

				// Extract exact data length
				byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());

				String dt = new String(data);

				System.out.println("\n\nRX: lenght" + packet.getLength()
				+ "--------------------------------------------\n" + (dt));

				if (suspendResume) {


					for (String udp : udpArray) {
						if (udp != null) {
							try {
								int port = Integer.parseInt(udp);
								if (port >= 1 && port <= 65535) {
									destinationPort = port;
									DatagramPacket outPacket = new DatagramPacket(data, data.length,
											InetAddress.getByName(destinationHost), destinationPort);
									sendSocket.send(outPacket);
									textArea.append("\nTX: lenght " + outPacket.getLength() + " to port  " + udp );
									// auto-scroll
									textArea.setCaretPosition(textArea.getDocument().getLength());
								} else {
									textArea.append("port  " + udp + " out of range 0-65535, PLEASE STOP AND CORRECT\n");
									// auto-scroll
									textArea.setCaretPosition(textArea.getDocument().getLength());
								}
							} catch (NumberFormatException ex) {
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}