package Mover3D;

// CommandsPanel.java
// Thana Konglikhit, October 2003, s4310170@maliwan.psu.ac.th
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* CommandsPanel holds a text field for entering limb and figure
 movement commands, and a "reset" button for reseting the
 figure's limbs to their original position.

 ------
 Limb commands:
 �
 (limbName | limbNo) (fwd|f|turn|t|side|s) [ angleChg ]

 fwd = positive rotation around x-axis
 turn = positive rotation around y-axis
 side = positive rotation around z-axis

 A limb name or number can be used. These are printed to
 standard output when Mover3D is started.

 If the angleChg is not included, a default angle of 5 degrees
 is used. angleChg can be negative.

 A angle which would take a limb outside of its specified angle
 range is ignored (an error message is printed).

 ------
 Figure commands:

 fwd | back | left | right | up | down | clock | cclock
 f  |   b  |  l   |   r   | u  |  d   |   c   | cc

 There are keyboard equivalents of these, processed by
 LocationBeh.

 -------

 Multiple commands can be entered into the text field, separated
 by ",'s.

 */
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class CommandsPanel extends JPanel {
    private final static int BACK = 1;
    private final static int CCLOCK = 1; // counter clockwise
    private final static int CLOCK = 0; // clockwise turn
    private final static int DOWN = 5;
    // figure movement constants
    private final static int FWD = 0;
    private final static int LEFT = 2;
    private final static int RIGHT = 3;
    /**
     * 
     */
    private static final long serialVersionUID = 6003436556651568738L;
    private final static int UP = 4;
    // axis constants
    private final static int X_AXIS = 0;
    private final static int Y_AXIS = 1;
    private final static int Z_AXIS = 2;
    private Figure figure;

    public CommandsPanel(Figure fig) {
        figure = fig;
        setLayout(new FlowLayout());
        add(new JLabel("Commands:"));
        // text field for entering commands
        JTextField commsTF = new JTextField(35);
        add(commsTF);
        commsTF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processComms(e.getActionCommand());
            }
        });
        // reset button
        JButton resetBut = new JButton("Reset");
        add(resetBut);
        resetBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                figure.reset();
            }
        });
    } // end of CommandsPanel()

    private void figCommand(String opStr)
    /*
     * Process a figure command of the form: fwd | back | left | right | up | down | clock | cclock f | b | l | r | u | d | c | cc
     */
    {
        if (opStr.equals("fwd") || opStr.equals("f")) {
            figure.doMove(FWD);
        } else if (opStr.equals("back") || opStr.equals("b")) {
            figure.doMove(BACK);
        } else if (opStr.equals("left") || opStr.equals("l")) {
            figure.doMove(LEFT);
        } else if (opStr.equals("right") || opStr.equals("r")) {
            figure.doMove(RIGHT);
        } else if (opStr.equals("up") || opStr.equals("u")) {
            figure.doMove(UP);
        } else if (opStr.equals("down") || opStr.equals("d")) {
            figure.doMove(DOWN);
        } else if (opStr.equals("clock") || opStr.equals("c")) {
            figure.doRotateY(CLOCK);
        } else if (opStr.equals("cclock") || opStr.equals("cc")) {
            figure.doRotateY(CCLOCK);
        } else {
            System.out.println("Unknown figure operation: " + opStr);
            return;
        }
    } // end of figCommand()

    private void limbCommand(String limbName, String opStr, String angleStr)
    /*
     * Process a limb command of the form: (limbName | limbNo) (fwd|f|turn|t|side|s) angleChg
     */
    { // get the limb number
        int limbNo = -1;
        try {
            limbNo = figure.checkLimbNo(Integer.parseInt(limbName));
        } catch (NumberFormatException e) {
            limbNo = figure.findLimbNo(limbName);
        } // map name to number
        if (limbNo == -1) {
            System.out.println("Illegal Limb name/no: " + limbName);
            return;
        }
        // get the angle change
        double angleChg = 0;
        try {
            angleChg = Double.parseDouble(angleStr);
        } catch (NumberFormatException e) {
            System.out.println("Illegal angle change: " + angleStr);
        }
        if (angleChg == 0) {
            System.out.println("Angle change is 0, so doing nothing");
            return;
        }
        // extract the axis of rotation from the limb operation
        int axis;
        if (opStr.equals("fwd") || opStr.equals("f")) {
            axis = X_AXIS;
        } else if (opStr.equals("turn") || opStr.equals("t")) {
            axis = Y_AXIS;
        } else if (opStr.equals("side") || opStr.equals("s")) {
            axis = Z_AXIS;
        } else {
            System.out.println("Unknown limb operation: " + opStr);
            return;
        }
        // apply the command to the limb
        // System.out.println("Command: limbNo & axis/angleChg: " +
        // limbNo + " & " + axis + "/" + angleChg );
        figure.updateLimb(limbNo, axis, angleChg);
    } // end of limbCommand()

    private void processComms(String input)
    // process a sequence of limb/figure commands
    {
        if (input == null) {
            return;
        }
        String[] commands = input.split(","); // split input into commands
        StringTokenizer toks;
        for (String element : commands) {
            toks = new StringTokenizer(element.trim());
            if (toks.countTokens() == 3) {
                limbCommand(toks.nextToken(), toks.nextToken(), toks.nextToken());
            } else if (toks.countTokens() == 2) {
                // default angle
                limbCommand(toks.nextToken(), toks.nextToken(), "5"); // inefficient
            } else if (toks.countTokens() == 1) {
                figCommand(toks.nextToken());
            } else {
                System.out.println("Illegal command: " + element);
            }
        }
    } // end of processComms()
} // end of CommandsPanel class
