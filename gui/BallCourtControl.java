package gui;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.*;

import javax.swing.*; 

import ballcourt.*;

/**
 * Class implementing the control GUI of the BouncingBall game.
 *
 * @author Jan Peleska
 * @version 2020-01-25
 */
public class BallCourtControl
{

    /** Maximal number of ball court frames that are simultaneously active */
    private static final int MAX_FRAMES = 4;

    /** 
     * Array where ball court frames are registered.
     * The index is also used as the number of the ball court frame.
     * If no ball court frame is associated with an index i,
     * bcframes[i] == null holds.
     */
    private BallCourtFrame[] bcframes = new BallCourtFrame[MAX_FRAMES];

    /** 
     * Reference to the ball court frame which is currently active.
     * If no ball court frame is active, currentBc == null holds.
     */
    private BallCourtFrame currentBc;

    /** 
     * Index of the ball court frame which is currently active.
     * If no ball court frame is active, currentBcNo == -1 holds.
     * Otherwise currentBcNo in 0..MAX_FRAMES-1
     */
    private int currentBcNo = -1;

    /** File chooser object */
    private static final JFileChooser filechooser = new JFileChooser(System.getProperty("user.dir"));

    /** Main user interface frame */
    private JFrame frame;
    /** Label of the file selection text field */
    private JLabel  outputTextLabel;
    /** Target file for the created files */
    private JLabel outputTextField;
    /** A button element controlling the selection of ball court frame from serialization file */
    private JButton loadFromFileButton;
    /** A button controlling the creation of a new BC frame */
    private JButton createBcButton;
    /** A button controlling the destruction of a BC frame */
    private JButton destroyBcButton;
    /** A button controlling the serialization and destruction of a BC frame */
    private JButton serializeBcButton;
    /** The EXIT button */
    private JButton exitButton;
    /** Radio button 0 */
    JRadioButton radioButton0;
    /** Radio button 1 */
    JRadioButton radioButton1;
    /** Radio button 2 */
    JRadioButton radioButton2;
    /** Radio button 3 */
    JRadioButton radioButton3;

    /** Inner class for WindowAdapters of BallCourtFrames */
    class BallCourtWindowAdapter extends WindowAdapter  {
        /** 
         * This WindowAdapter instance manages ball court number myBcNo in 0..MAX_FRAMES-1 
         */
        private int myBcNo;
        BallCourtWindowAdapter() {
            super();
            myBcNo = currentBcNo;
            System.out.println("BallCourtWindowAdapter created for frame " + myBcNo);
        }

        /** 
         * Adapter for the windowClosing event
         * @param e WindowEvent - cal only be the WindowClosing event,
         *          the adapter is only registered with this event.
         */
        @Override
        public void windowClosing(final WindowEvent e) {

            System.out.println("WindowClosing Event for frame " + myBcNo);

            BallCourtFrame myFrame = bcframes[myBcNo];
            myFrame.setVisible(false);
            myFrame.dispose();
            bcframes[myBcNo] = null;
            if ( currentBcNo == myBcNo ) {
                currentBc = null;
                makeButtonsVisible(true,true,false,false);
            }
        }

    }

    /**
     * Initialise the current ball court frame 
     * after de-serialization by performing the following actions.
     * <ul>
     * <li> assign currentBc to the bcframes array at index  currentBcNo
     * <li> insert the frame number into the window title
     * <li> Register a new BallCourtWindowAdapter-instance as window listener
     * <li> Enable only the destroy buttons
     * <li> Make the window frame and its content visible;
     * </ul>
     */
    private void initialiseCurrentFrame() {
        bcframes[currentBcNo] = currentBc;
        currentBc.setTitle("BC " + Integer.toString(currentBcNo));
        currentBc.addWindowListener(new BallCourtWindowAdapter());
        makeButtonsVisible(false,false,true,true);
        currentBc.setVisible(true);
    }

    /**
     * Activate ball court frame for selected ball court number 
     * by means of de-serialisation from file. The following actions are performed.
     * <ul>
     * <li> Show open-file dialogue to user
     * <li> If valid file is selected, de-serialize object from file and
     *      assign it to currentBc in the ball court which is currently selected
     * <li> Set the GUI button state accordingly and make the frame visible
     *      by calling method initialiseCurrentFrame
     * 
     * </ul>
     */
    private void deserializeFrame() {

        final int result = filechooser.showOpenDialog(frame);
        if(result != JFileChooser.APPROVE_OPTION) { 
            // no proper file has been specified - return with
            return;  
        }
        final File selectedFile = filechooser.getSelectedFile();

        try (FileInputStream file = new FileInputStream(selectedFile);
        ObjectInputStream in = new ObjectInputStream(file))
        {    
            // Method for deserialization of object 
            currentBc = (BallCourtFrame)in.readObject();  
            System.out.println("Frame has been deserialized "); 
            initialiseCurrentFrame();
        } 
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught: " + ex); 
        } 

        catch(ClassNotFoundException ex) 
        { 
            System.out.println("ClassNotFoundException is caught: " + ex); 
        }    

    }

    /**
     * Create a new BallCourtFrame instance with a player and a ball
     * inside and initialise the frame as by calling initialiseCurrentFrame.
     */
    private void createBcFrame() {

        BallCourt bc = new BallCourt(10,10,300,300);

        Ball b = new Ball(bc,0,0);

        bc.addBall(b);

        Player p = new Player( bc, 150, 150, 5);
        bc.addPlayer(p);

        currentBc = new BallCourtFrame(bc,10,10,315,350);
        initialiseCurrentFrame();
    }

    /**
     * Serialize and destroy an active ball court frame by performing the
     * following actions.
     * <ul>
     * <li> Present save-to-file dialogue to user
     * <li> Serialize currentBc to the selected file 
     * <li> Make the current ball court frame invisible and release its resources
     *      as specified for methdd destroyBcFrame.
     * 
     * </ul>
     */
    private void serializeAndDestroyBcFrame() {

        final int result = filechooser.showSaveDialog(frame);
        if(result != JFileChooser.APPROVE_OPTION) {  
            // no proper file has been specified - return without further action
            return;  
        }
        final File selectedFile = filechooser.getSelectedFile();

        try(FileOutputStream file = new FileOutputStream(selectedFile);
        ObjectOutputStream out = new ObjectOutputStream(file))
        {    

            //Saving of object in a file 
            //FileOutputStream file = new FileOutputStream(selectedFile); 
            //ObjectOutputStream out = new ObjectOutputStream(file); 

            // Method for serialization of object 
            out.writeObject(currentBc); 
            System.out.println("BallCourt has been serialized"); 

        } 
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught: " + ex); 
        }
        catch(SecurityException ex)
        {
            System.out.println("SecurityException is caught: " + ex);
        }

        destroyBcFrame();
    }

    /**
     * Make selected ball court frame invisible and release its
     * window-related resources. Update bcframes, currentBc and button states
     * accordingly.
     */
    private void destroyBcFrame() {

        currentBc.setVisible(false);
        currentBc.dispose();

        bcframes[currentBcNo] = null;
        currentBc = null;
        makeButtonsVisible(true,true,false,false);
    }

    /** Display dialog containing information about tool version and developer */
    private void showAboutInformation() {

        String messageString = new String("Bouncing Ball GUI, Version 2.0");	 
        JOptionPane.showMessageDialog(frame, 
            messageString,
            new String("About Bouncing Ball GUI"),
            JOptionPane.INFORMATION_MESSAGE);

    }

    /** Closes the GUI -- Listener action for the EXIT button */
    private void doExit() {
        System.exit(0);
    }

    /**
     * Create the menu bar with the following menus
     * <ul> 
     * <li> File menue with EXIT command
     * <li> Help menue with About-command
     * </ul>
     */
    private void createMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        // Create File menu 
        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);

        JMenuItem exitEntry = new JMenuItem("Exit");
        exitEntry.addActionListener(e -> doExit());
        fileMenu.add(exitEntry);

        // Create the Help menu
        JMenu helpMenu = new JMenu("Help");
        menubar.add(helpMenu);

        JMenuItem aboutEntry = new JMenuItem("About");
        aboutEntry.addActionListener(e -> showAboutInformation());
        helpMenu.add(aboutEntry);

    }

    /**
     * Make command buttons visible or invisble
     * @param createButton Makes the CREATE BC button visible iff true
     * @param createButton Makes the LOAD BC FROM FILE button visible iff true     
     * @param createButton Makes the DESTROY BC button visible iff true     
     * @param createButton Makes the SAVE&DESTROY BC button visible iff true     
     */
    private void makeButtonsVisible(boolean createButton, boolean loadButton, 
    boolean destroyButton, boolean serializeButton) {
        createBcButton.setEnabled(createButton);
        loadFromFileButton.setEnabled(loadButton);
        destroyBcButton.setEnabled(destroyButton);
        serializeBcButton.setEnabled(serializeButton);
    }

    /**
     * Set the selected BC frame according to given number
     * @param frameNum Number in 0..MAX_FRAMES-1 for the 
     *        frame to be selected. The method expects that
     *        an active frame is already registered in bcframes[frameNum]
     *        and sets currentBc, the BC text field, and the command button state
     *        accordingly.
     */
    private void setCurrentFrame(int frameNum) {
        currentBc = bcframes[frameNum]; 
        currentBcNo = frameNum;
        outputTextField.setText(Integer.toString(currentBcNo));

        if ( currentBc == null ) {
            makeButtonsVisible(true,true,false,false);
        }
        else {
            makeButtonsVisible(false,false,true,true);
        }
    }

    /** 
     * Create the window frame with Grid Layout,
     * 7 lines, and 2 columns
     */
    private void createFrame() {
        frame = new JFrame("BallCourt Control GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridLayout theLayout = new GridLayout(7, 2);
        theLayout.setHgap(10);
        theLayout.setVgap(10);
        frame.setLayout(theLayout);
    }

    /**
     * Create the following widgets and associate action listeners.
     * <ul>
     * <li> EXIT button - action listener calls method doExit
     * <li> Load from File button - action listener calls method deserializeFrame
     * <li> Create BC button - action listener calls method createBcFrame
     * <li> Destroy BC - action listener calls method  destroyBcFrame      
     * <li> Save&destroy BC - action listener calls method   
     * <li> 4 Radio buttons for BC frame selection in range 0..MAX_FRAMES-1
     * <li> One text label indicating the selected BC (if any)
     * </ul>
     */
    private void createWidgets() {

        // Create EXIT button
        exitButton = new JButton("EXIT");
        exitButton.addActionListener(e -> doExit());

        // Create a button for loading frame from serialisation file
        loadFromFileButton = new JButton("Load From File");
        loadFromFileButton.addActionListener(e -> deserializeFrame());

        // Create a button for BallCourtFrame creation
        createBcButton = new JButton("Create BC");
        createBcButton.addActionListener(e -> createBcFrame());

        // Create a button for Frame creation
        destroyBcButton = new JButton("Destroy BC");
        destroyBcButton.addActionListener(e -> destroyBcFrame());

        // Create a button for Frame creation
        serializeBcButton = new JButton("Save&destroy BC");
        serializeBcButton.addActionListener(e -> serializeAndDestroyBcFrame());

        // Create a group for the radio buttons
        ButtonGroup bg = new ButtonGroup();

        // Create a radio button
        radioButton0 = new JRadioButton("BC 0");
        radioButton0.addActionListener(e -> setCurrentFrame(0));
        radioButton1 = new JRadioButton("BC 1");
        radioButton1.addActionListener(e -> setCurrentFrame(1));
        radioButton2 = new JRadioButton("BC 2");
        radioButton2.addActionListener(e -> setCurrentFrame(2));
        radioButton3 = new JRadioButton("BC 3");
        radioButton3.addActionListener(e -> setCurrentFrame(3));
        bg.add(radioButton0);
        bg.add(radioButton1);
        bg.add(radioButton2);
        bg.add(radioButton3);
        outputTextLabel = new JLabel("Selected Ball Court: BC ");
        outputTextField = new JLabel("not selected");

    }

    /**
     * Add widges created by method createWidgets to GUI frame
     */
    private void widgetsToFrame() {

        frame.add(outputTextLabel);
        frame.add(outputTextField);

        frame.add(radioButton0); 
        frame.add(radioButton1); 
        frame.add(radioButton2); 
        frame.add(radioButton3); 

        frame.add(createBcButton);
        frame.add(destroyBcButton);
        frame.add(loadFromFileButton);
        frame.add(serializeBcButton);
        frame.add(exitButton); 

    }

    /**
     * Setup the GUI by performing the following actions
     * <ul>
     * <li> Create the window frame
     * <li> Create the menue bar
     * <li> Create the widgets
     * <li> Add widgets to frame
     * <li> Make command buttons visible in de-activated state
     * <li> pack the frame and make it visible
     * </ul>
     */
    public void setup() {

        createFrame();
        createMenuBar();
        createWidgets();
        widgetsToFrame();

        makeButtonsVisible(false,false,false,false);
        frame.pack();
        frame.setVisible(true);

    }

    
    /**
     * Update all existing ball court frames by calling their run-method
     */
    public void update() {
        for ( BallCourtFrame f : bcframes ) {
            if ( f != null ) {
                f.run();    
            }
        }
    }

}
