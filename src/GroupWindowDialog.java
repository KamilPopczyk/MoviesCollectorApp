import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GroupWindowDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;


    private String groupName;
    private GroupOfMovies groupOfMovies;

    // Etykiety
    JLabel groupNameLabel = new JLabel("Podaj nazwę nowej grupy: ");

    // Pola tekstowe
    JTextField groupNameField = new JTextField(10);


    // Przyciski
    JButton OKButton = new JButton("  OK  ");
    JButton CancelButton = new JButton("Anuluj");


    private GroupWindowDialog(Window parent, GroupOfMovies groupOfMovies) {
        super(parent, ModalityType.DOCUMENT_MODAL);

        this.groupOfMovies = groupOfMovies;

        // Konfiguracja okna dialogowego
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(250, 220);
        setLocationRelativeTo(parent);

        OKButton.addActionListener( this );
        CancelButton.addActionListener( this );


        JPanel panel = new JPanel();

        // Dodanie i rozmieszczenie na panelu wszystkich komponent�w GUI.
        panel.add(groupNameLabel);
        panel.add(groupNameField);


        panel.add(OKButton);
        panel.add(CancelButton);

        // Umieszczenie Panelu w oknie dialogowym.
        setContentPane(panel);


        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        Object source = event.getSource();

        if (source == OKButton) {
            try {
                if(groupOfMovies == null){
                    groupName = groupNameField.getText();
                }
                else {
                    groupOfMovies.setName(groupNameField.getText());
                }
                dispose();
            } catch (Exception e) {

                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
//            groupName = groupNameField.getText();
            dispose();
        }

        if (source == CancelButton) {
            dispose();
        }
    }


    public static void changeGroupName(Window parent, GroupOfMovies groupOfMovies) {
        new GroupWindowDialog(parent, groupOfMovies);
    }

    public static String newGroupName(Window parent) {
        GroupWindowDialog dialog = new GroupWindowDialog(parent, null);
        return dialog.groupName;
    }


}
