
// Defines the package where the class is located
package Std;

/*
	This Java code creates a GUI-based configuration editor for an application that generates ADIF (Amateur Data Interchange Format) files. Let me break down what it does and how it works:

	What it does:
		Reads configuration from a JSON file (or creates default if none exists)
		Provides a GUI interface to edit configuration parameters
		Saves the modified configuration back to JSON
		Specifically designed for amateur radio logging (Log4OM2 integration)
	How it works:
	Key Components:
		Config class: Data structure to hold configuration values
		Configuration class: Main GUI window extending JFrame
		JSON handling: Uses Google Gson library for serialization/deserialization
		Swing GUI: Table-based interface for editing parameters
	Flow:
		Startup: Reads config.json from user's AppData directory
		Display: Shows configuration in editable table format
		Edit: User can modify values in the table and text fields
	Save: Writes updated configuration back to JSON file
 
 */



import com.google.gson.Gson;
//Imports the GsonBuilder class to build Gson objects with custom options
import com.google.gson.GsonBuilder;

//Imports classes for file I/O handling
import java.io.*;

//Imports Swing classes for creating the graphical interface
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

//Imports Swing classes for interface components
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTextArea;

//Class to store the application configuration
class Config {
    // Field for UDP port number
    String nUDP;
    // Field for directory path
    String dir;
    // Fields for various configuration parameters
    String udp1;
    String udp2;
    String udp3;
    String udp4;
    String udp5;
    String udp6;
    String udp7;
    String udp8;
    String udp9;
}

// Main class that extends JFrame to create the application window
public class Configuration extends JFrame {
    // Declaration of the table to display data
    private JTable table;
    // Table model that manages data
    private DefaultTableModel model;
    // Object to store the configuration
    private Config config;
    // Text field for UDP port number
    private JTextField numUDPText;
    
    // Constructor of the Configuration class
    public Configuration() {
        
    	// Sets the window title
        setTitle("Edit your parameters");
        
        // Sets the window dimensions
        setSize(800,  493);
        
        // Defines the window closing operation
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Centers the window on screen
        setLocationRelativeTo(null);
       
        // Sets the layout of the main container
        getContentPane().setLayout(null);
       
        // Positions the window based on the platform
        setLocationByPlatform(true);

        // Reads configuration from JSON file
        config = leggiConfig();   // <-- reads JSONGsonBuilder(

        // Creates the IP input panel
        createInputPanel();  
        // Creates the table to display data
        createTable();
        // Creates the save configuration button
        createSaveButton();
    }

    /**
     * 
     */
    // Serial version identifier for serialization
    private static final long serialVersionUID = 1L;
    // Configuration file path
    private static String FILE_PATH;
    // Default path for configuration directory
    private static String path = "/AppData/Roaming/UDPMultiBridge/";
    // Gson object configured for JSON formatting
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // Text field for directory path
    private JTextField directoryText;

    // Method to read configuration from JSON file
    public static Config leggiConfig() {
        // Determines the user's home directory
        String userHome = System.getProperty("user.home");
        //define file name
    	String configFilePath = userHome + path;
    	File configPath = new File(configFilePath);

    	// check if the directory where to store config.json exists, if not create it.
    	if (configPath != null) {
    	    boolean created = configPath.mkdirs();
    	    System.out.println(
    	            "Directories created: " + created
    	    );
    	}
        // Builds the complete configuration file path
        FILE_PATH = userHome + path + "config.json";
        // Prints the configuration file path
        System.out.println("File_path configuration:  " + FILE_PATH);

        // Attempts to read the configuration file
        try (FileReader reader = new FileReader(FILE_PATH)) {
            // Converts JSON content to a Config object
            return gson.fromJson(reader,  Config.class);
        } catch (IOException e) {
            // If file doesn't exist, prints a message and creates default configuration
            System.out.println("File not found, creating default configuration");
            return creaDefault();
        }
    }
    
    // Method to create default configuration
    private static Config creaDefault() {

    	// Creates a new Config object
        Config config = new Config();
        // Sets default values for configuration
        config.nUDP = "2237";
        config.dir = "/AppData/Roaming/UDPMultiBridge/";
        config.udp1 = "22377";
        config.udp2 = "22378";
        config.udp3 = "";
        config.udp4 = "";
        config.udp5 = "";
        config.udp6 = "";
        config.udp7 = "";
        config.udp8 = "";
        config.udp9 = "";
        
        // Saves default configuration to file
        scriviConfig(config);
 
        // Returns Config object with default values
        return config;
    }

    // Method to write configuration to JSON file
    public static void scriviConfig(Config config) {
        // Attempts to write configuration to file
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            // Converts Config object to JSON format and writes to file
            gson.toJson(config,  writer);
            // Prints confirmation message
            System.out.println("Configuration saved");
        } catch (IOException e) {
            // Prints stack trace in case of error
            e.printStackTrace();
        }
    }

 
    // Method to create table with configuration udps
    public void createTable() {
        // Defines table column names
        String[] columnNames = {"UDP",  "Description"};

        // Defines data to display in table
        Object[][] data = {
                {"udp1",  config.udp1},  
                {"udp2",  config.udp2},  
                {"udp3",  config.udp3},  
                {"udp4",  config.udp4},  
                {"udp5",  config.udp5},  
                {"udp6",  config.udp6},  
                {"udp7",  config.udp7},  
                {"udp8",  config.udp8},  
                {"udp9",  config.udp9}
        };

        // Creates table model with data and column names
        model = new DefaultTableModel(data,  columnNames) {
            @Override
            // Overrides method to make only second column editable
            public boolean isCellEditable(int row,  int column) {
                return column != 0;
            }   
        };

        // Creates table with defined model
        table = new JTable(model);
 
        table.putClientProperty(
                "terminateEditOnFocusLost",
                Boolean.TRUE
        );

        // Creates scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);

        // Sets scroll pane position and dimensions
        scrollPane.setBounds(10,  55,  764,  186);
        // Sets table auto-resize mode
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Sets preferred column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(660);

        // Adds scroll pane to main container
        getContentPane().add(scrollPane);
    }
    
    // Method to save configuration to JSON file
    private void saveConfiguration() {
        // Shows dialog to confirm save
        int scelta = javax.swing.JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to save the configuration?", 
                "Confirm Save", 
                javax.swing.JOptionPane.YES_NO_OPTION, 
                javax.swing.JOptionPane.QUESTION_MESSAGE
        );
        
        // Exits method if user chooses NO
        if (scelta != javax.swing.JOptionPane.YES_OPTION) {
            return; // exits if user presses NO
        }

        // if you forget to press enter, the editing is stopped anyway
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        
        // Attempts to save data
        try {
            // Updates UDP port number from text box
            config.nUDP = numUDPText.getText();  
            // Updates directory path from text box
            config.dir = directoryText.getText();
            
            // Updates configuration udps from table values
            config.udp1 = model.getValueAt(0,  1).toString();
            config.udp2 = model.getValueAt(1,  1).toString();
            config.udp3 = model.getValueAt(2,  1).toString();
            config.udp4 = model.getValueAt(3,  1).toString();
            config.udp5 = model.getValueAt(4,  1).toString();
            config.udp6 = model.getValueAt(5,  1).toString();
            config.udp7 = model.getValueAt(6,  1).toString();
            config.udp8 = model.getValueAt(7,  1).toString();
            config.udp9 = model.getValueAt(8,  1).toString();

            // Writes updated configuration to JSON file
            scriviConfig(config);

            // Shows save confirmation message
            javax.swing.JOptionPane.showMessageDialog(this,  "Configuration saved!");

        } catch (Exception ex) {
            // Shows error message in case of problems
            javax.swing.JOptionPane.showMessageDialog(this,  "Error in data!");
        }
    }
    
    // Method to create save button
    private void createSaveButton() {
        // Creates new button with "SAVE" label
        javax.swing.JButton btnSave = new javax.swing.JButton("SAVE & EXIT");

        // Sets button position and dimensions
        btnSave.setBounds(337,  252,  120,  30);

        // Adds listener for button click event
        btnSave.addActionListener(e -> {

            // Commit last edited JTable cell
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }

            // Save config
            saveConfiguration();

            // Close ONLY this window
            Configuration.this.dispose();
        });

        // Adds button to main container
        getContentPane().add(btnSave);
        
        // Creates text area for instructions
        JTextArea instructionsTextArea = new JTextArea();
        // Sets text area as non-editable
        instructionsTextArea.setEditable(false);
        // Sets text area position and dimensions
        instructionsTextArea.setBounds(10,  295,  764,  148);
        // Adds text area to main container
        getContentPane().add(instructionsTextArea);
        // Adds instructions to text area
        instructionsTextArea.append("INSTRUCTIONS: \nThe program repeats a message received on a UDP port to other UDP ports.");
        instructionsTextArea.append("\nPort number must be included in the range from 1 to 65535.");
        instructionsTextArea.append("\nTHE NUMBER OF PORTS OUTSIDE RANGE WILL BE IGNORED.");

    }
    
    // Method to create IP input panel
    private void createInputPanel() {
        // Creates label for UDP udp
        JLabel lblUDP = new JLabel("UDP port: ");
        // Sets label position and dimensions
        lblUDP.setBounds(10,  11,  59,  25);

        // Creates text field for UDP port number
        numUDPText = new JTextField(7);
        // Sets text field position and dimensions
        numUDPText.setBounds(64,  11,  80,  25);

        // Creates label for directory field
        JLabel lblDir = new JLabel("Dir.Path: ");
        // Sets label position and dimensions
        lblDir.setBounds(158,  11,  52,  25);

        // Creates text field for directory path
        directoryText = new JTextField(40);
        // Sets text field position and dimensions
        directoryText.setBounds(207,  11,  567,  25);

        // Sets UDP field text with value from configuration
        numUDPText.setText(config.nUDP);

        // Sets directory field text with value from configuration
        directoryText.setText(config.dir);

        // Adds components to main container
        getContentPane().add(lblUDP);
        getContentPane().add(numUDPText);
        getContentPane().add(lblDir);
        getContentPane().add(directoryText);
    }
    
/*    public static void main(String[] args) {
        // Avvio GUI in un thread sicuro
        SwingUtilities.invokeLater(() -> {
            new Configuration().setVisible(true);
            
        });
    }
*/
}
