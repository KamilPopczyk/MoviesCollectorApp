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


public class MovieWindowDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;


	private Movie movie;


	Font font = new Font("MonoSpaced", Font.BOLD, 12);
	
	// Etykiety
	JLabel titleNameLabel = new JLabel("Tytul filmu: ");
	JLabel directorNameLabel  = new JLabel("Reżyser: ");
	JLabel premiereYearLabel      = new JLabel("Rok premiery: ");
	JLabel genreLabel       = new JLabel("Gatunek: ");

	// Pola tekstowe
	JTextField titleNameField = new JTextField(10);
	JTextField directorNameField = new JTextField(10);
	JTextField premiereYearField = new JTextField(10);
	JComboBox<MovieGenre> genreBox = new JComboBox<MovieGenre>(MovieGenre.values());

	// Przyciski
	JButton OKButton = new JButton("  OK  ");
	JButton CancelButton = new JButton("Anuluj");


	private MovieWindowDialog(Window parent, Movie movie) {
		super(parent, ModalityType.DOCUMENT_MODAL);
		
		// Konfiguracja pokna dialogowego
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(250, 220);
		setLocationRelativeTo(parent);

		this.movie = movie;

		if (movie==null){
			setTitle("Nowy film");
		} else{
			setTitle(movie.toString());
			titleNameField.setText(movie.getTitle());
			directorNameField.setText(movie.getDirectorName());
			premiereYearField.setText(""+movie.getPremiereYear());
			genreBox.setSelectedItem(movie.getGenre());
		}

		OKButton.addActionListener( this );
		CancelButton.addActionListener( this );
		

		JPanel panel = new JPanel();

		panel.setBackground(Color.GRAY);

		// Dodanie i rozmieszczenie na panelu wszystkich komponent�w GUI.
		panel.add(titleNameLabel);
		panel.add(titleNameField);
		
		panel.add(directorNameLabel);
		panel.add(directorNameField);
		
		panel.add(premiereYearLabel);
		panel.add(premiereYearField);
		
		panel.add(genreLabel);
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
				if (movie == null) { // Utworzenie nowego filmu
					movie = new Movie(titleNameField.getText());
					movie.setDirectorName(directorNameField.getText());
				} else { // Aktualizacja
					movie.setTitle(titleNameField.getText());
					movie.setDirectorName(directorNameField.getText());
				}
				movie.setPremiereYear(premiereYearField.getText());
				movie.setGenre((MovieGenre) genreBox.getSelectedItem());

				dispose();
			} catch (MovieException e) {

				JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		if (source == CancelButton) {
			dispose();
		}
	}
	

	public static Movie createNewMovie(Window parent) {
		MovieWindowDialog dialog = new MovieWindowDialog(parent, null);
		return dialog.movie;
	}


	public static void changeMovieData(Window parent, Movie movie) {
		new MovieWindowDialog(parent, movie);
	}

}
