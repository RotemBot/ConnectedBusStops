import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by rotem on 12/06/2017.
 */
public class BusClientWin extends JFrame implements ActionListener
{

    private JTextPane paneTextUp;
    private StyledDocument doc;
    private JTextArea textAreaDown;
    private Style base = StyleContext.getDefaultStyleContext().
            getStyle(StyleContext.DEFAULT_STYLE);
    private Style myStyle, otherStyle;
    private Style myHeaderStyle, otherHeaderStyle;
    public JButton send;
    private String myName = "Bus";
    private String otherName = "Server (Dialog):";
    private BusClient myClient;

    public BusClientWin(String header, BusClient myClient)
    {
        super(header);
        this.myClient = myClient;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(350,350);

        paneTextUp = new JTextPane();
        paneTextUp.setEditable(false);

        doc = paneTextUp.getStyledDocument();

        myStyle = doc.addStyle("myStyle", base);
        StyleConstants.setFontSize(myStyle, 14);
        StyleConstants.setForeground(myStyle, Color.BLUE);

        myHeaderStyle = doc.addStyle("myHeaderStyle", myStyle);
        StyleConstants.setBold(myHeaderStyle, true);

        otherStyle = doc.addStyle("otherStyle", base);
        StyleConstants.setFontSize(otherStyle, 16);
        StyleConstants.setForeground(otherStyle, Color.RED);

        otherHeaderStyle = doc.addStyle("otherHeaderStyle", otherStyle);
        StyleConstants.setBold(otherHeaderStyle, true);

        JScrollPane scrollPaneUp = new JScrollPane(paneTextUp);
        scrollPaneUp.setPreferredSize(new Dimension(100, 100));

        textAreaDown = new JTextArea(5, 25);
        textAreaDown.setEditable(true);
        JScrollPane scrollPaneDown = new JScrollPane(textAreaDown);

        send = new JButton("Send");
        send.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(send, BorderLayout.CENTER);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(scrollPaneUp);
        mainPanel.add(scrollPaneDown);
        mainPanel.add(buttonPanel);
        add(mainPanel);
        pack();
        setVisible(true);
    }

    public void printMe(String str)
    {
        try
        {
            doc.insertString(doc.getLength(), myName + ':' + '\n', myHeaderStyle);
            doc.insertString(doc.getLength(), "   " + str + '\n', myStyle);
        } catch (BadLocationException e)
        {
            e.printStackTrace();
        }
        paneTextUp.setCaretPosition(paneTextUp.getDocument().getLength());
    }

    public void printOther(String str)
    {
        try
        {
            doc.insertString(doc.getLength(), otherName + ':' + '\n', otherHeaderStyle);
            doc.insertString(doc.getLength(), "   " + str + '\n', otherStyle);
        } catch (BadLocationException e)
        {
            e.printStackTrace();
        }
        paneTextUp.setCaretPosition(paneTextUp.getDocument().getLength());
    }

    public void actionPerformed(ActionEvent arg0)
    {
        if (((JButton) arg0.getSource()).getText().equals("Close"))
        {
            System.exit(1);
        }
        printMe(textAreaDown.getText());
        myClient.bufferSocketOut.println(textAreaDown.getText());
        textAreaDown.setText("");
    }
}

