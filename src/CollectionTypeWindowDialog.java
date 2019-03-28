import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class CollectionTypeWindowDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;


    private GroupOfMovies groupOfMovies;
    private GroupType groupType;


    Font font = new Font("MonoSpaced", Font.BOLD, 12);

    // Etykieta
    JLabel collectionTypeLabel = new JLabel("Typ kolekcji: ");

    // Pola tekstowe
    JComboBox<GroupType> genreBox = new JComboBox<GroupType>(GroupType.values());

    // Przyciski
    JButton OKButton = new JButton("  OK  ");
    JButton CancelButton = new JButton("Anuluj");


    private CollectionTypeWindowDialog(Window parent, GroupOfMovies groupOfMovies) {
        super(parent, ModalityType.DOCUMENT_MODAL);

        // Konfiguracja pokna dialogowego
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(250, 220);
        setLocationRelativeTo(parent);

        this.groupOfMovies = groupOfMovies;


        OKButton.addActionListener( this );
        CancelButton.addActionListener( this );


        JPanel panel = new JPanel();

        panel.setBackground(Color.GRAY);

        // Dodanie i rozmieszczenie na panelu wszystkich komponent�w GUI.

        panel.add(collectionTypeLabel);
        panel.add(genreBox);

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
                        groupType = (GroupType)genreBox.getSelectedItem();
                    }
                    else {
                        groupOfMovies.setType((GroupType) genreBox.getSelectedItem());
                    }
                    dispose();
            } catch (MovieException e) {

                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (source == CancelButton) {
            dispose();
        }
    }

    public static GroupType newGroupType(Window parent) {
        CollectionTypeWindowDialog dialog = new CollectionTypeWindowDialog(parent, null);
        return dialog.groupType;
    }


    public static void changeGroupType(Window parent, GroupOfMovies groupOfMovies) {
        new CollectionTypeWindowDialog(parent, groupOfMovies);
    }

}
