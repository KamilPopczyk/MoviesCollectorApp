import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Iterator;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;



public class GroupOfMoviesWindowDialog extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String GREETING_MESSAGE =
                    "Program Movie - GUI\n" +
                    "Autor: Kamil Popczyk\n";


    private Movie currentMovie;

    private GroupOfMovies groupOfMovies;


    // Etykiety
    JLabel groupNameLabel = new JLabel("Nazwa grupy: ");
    JLabel collectionNameLabel = new JLabel("Rodzaj kolekcji: ");

    // Pola tekstowe
    JTextField groupNameField = new JTextField(10);
    JTextField collectionNameField = new JTextField(10);

    // Tabela
    JTable table;
    String[] columnNames = {"Tytuł", "Reżyser", "Rok premiery", "Gatunek"};

    // Menu
    JMenu menu;
    JMenuItem newButton, editButton, saveButton, loadButton;
    JMenuItem saveSerializableButton, loadSerializableButton, deleteButton;

    JMenu menuAbout;
    JMenuItem infoButton, exitButton;

    JMenu menuSort;
    JMenuItem sortTitleButton, sortPremiereYearButton, sortGenreButton;

    JMenu menuProperties;
    JMenuItem changeGroupNameButton, changeCollectionButton;

    //
    // Konfiguracja
    public GroupOfMoviesWindowDialog(Window parent, GroupOfMovies groupOfMovies){

        this.groupOfMovies = groupOfMovies;

        // Konfiguracja parametrow
        setTitle("MovieWindowApp");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(parent);

        // Pola nieedytowalne
        groupNameField.setEditable(false);
        collectionNameField.setEditable(false);


        JPanel panel = new JPanel();

        // Menu rozwijane
        JMenuBar menubar=new JMenuBar();
        menu = new JMenu("Lista filmów");
        // Tworzenie przyciskow menu
        newButton = new JMenuItem("Nowy film");
        editButton = new JMenuItem("Edycja danych");
        saveButton = new JMenuItem("Zapisz do pliku");
        loadButton = new JMenuItem("Wczytaj z pliku");
        saveSerializableButton = new JMenuItem("Zapisz do pliku binarnego");
        loadSerializableButton = new JMenuItem("Wczytaj z pliku binarnego");
        deleteButton = new JMenuItem("Usuń film");

        // Dodawanie przyciskow do menu
        menu.add(newButton); menu.add(editButton);  menu.add(deleteButton);
        menu.addSeparator();
        menu.add(saveButton); menu.add(loadButton);
        menu.add(saveSerializableButton); menu.add(loadSerializableButton);

        // Wlasciwosci
        menuProperties = new JMenu("Właściwości");
        changeGroupNameButton = new JMenuItem("Zmień nazwę grupy");
        changeCollectionButton = new JMenuItem("Zmień typ kolekcji");
        menuProperties.add(changeGroupNameButton); menuProperties.add(changeCollectionButton);

        // Sortowanie
        menuSort = new JMenu("Sortowanie");
        sortTitleButton = new JMenuItem("Sortuj według tytułu");
        sortPremiereYearButton = new JMenuItem("Sortuj według daty premiery");
        sortGenreButton = new JMenuItem("Sortuj według gatunku");
        menuSort.add(sortTitleButton); menuSort.add(sortPremiereYearButton); menuSort.add(sortGenreButton);

        // O programie
        menuAbout = new JMenu("O programie");
        infoButton = new JMenuItem("O programie");
        menuAbout.add(infoButton);

        menubar.add(menu);
        menubar.add(menuSort);
        menubar.add(menuProperties);
        menubar.add(menuAbout);
        // Zmiana layoutu panelu
        panel.setLayout(new BorderLayout());
        panel.add(menubar, BorderLayout.NORTH);

        // Sluchacze przyciskow
        newButton.addActionListener(this);
        editButton.addActionListener(this);
        saveButton.addActionListener(this);
        loadButton.addActionListener(this);
        saveSerializableButton.addActionListener(this);
        loadSerializableButton.addActionListener(this);
        deleteButton.addActionListener(this);
        infoButton.addActionListener(this);
        changeGroupNameButton.addActionListener(this);
        changeCollectionButton.addActionListener(this);
        sortTitleButton.addActionListener(this);
        sortPremiereYearButton.addActionListener(this);
        sortGenreButton.addActionListener(this);
//        exitButton.addActionListener(this);

        // Dodanie i rozmieszczenie na panelu wszystkich kompnentow
        // subPanele sluza do kolekcjonowania elementow w jeden 'element'
        JPanel subPanel = new JPanel();
        JPanel subPanelGroupName = new JPanel();
        JPanel subPanelCollectionName = new JPanel();

        subPanelGroupName.add(groupNameLabel);
        subPanelGroupName.add(groupNameField);
        subPanelCollectionName.add(collectionNameLabel);
        subPanelCollectionName.add(collectionNameField);
        // Dodawanie podpaneli w jeden
        subPanel.add(subPanelGroupName);
        subPanel.add(subPanelCollectionName);
        panel.add(subPanel, BorderLayout.CENTER);

        // tabela danych
        DefaultTableModel defaultTableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(defaultTableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.SOUTH);


        // Umieszczenie Panelu , glowne okno
        setContentPane(panel);

        // Zapelnienie pol tekstowych
        showCurrentData();

        setVisible(true);
    }



    void showCurrentData() {
        // uzupelnianie form
        groupNameField.setText(groupOfMovies.getName());
        collectionNameField.setText(groupOfMovies.getType().toString());

        // aktualizacja tabeli
        Iterator it = groupOfMovies.iterator();
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        while (it.hasNext()) {
            Movie movie = (Movie)it.next();
            String[] newMovieRow = {movie.getTitle(),
                                    movie.getDirectorName(),
                                    Integer.toString(movie.getPremiereYear()),
                                    movie.getGenre().toString() };
            model.addRow(newMovieRow);
        }
        table.setModel(model);
    }

    private Movie getCurrentMovie() {
        int selectedIndex = table.getSelectedRow();
        Iterator id = groupOfMovies.iterator();
        int i = 0;
        Movie selectedMovie;
        while (id.hasNext()){
            selectedMovie = (Movie)id.next();
            if(i == selectedIndex) return selectedMovie;
            i++;
        }
        return null;
    }


    // Implementacja interfejsu
    @Override
    public void actionPerformed(ActionEvent event) {
        Object eventSource = event.getSource();

        try {
            if (eventSource == newButton) {
                groupOfMovies.add(MovieWindowDialog.createNewMovie(this));
            }
            if (eventSource == deleteButton) {
                currentMovie = getCurrentMovie();
                Iterator id = groupOfMovies.iterator();
                while (id.hasNext()){
                   if(currentMovie.equals(id.next())) id.remove();
                }
            }
            if (eventSource == saveButton) {
                currentMovie = getCurrentMovie();

                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Zapisywanie pliku.");
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int returnValue = jfc.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    Movie.printToFile(selectedFile.getAbsolutePath(), currentMovie);
                }

            }
            if (eventSource == loadButton) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Odczytywanie pliku.");
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    currentMovie = Movie.readFromFile(selectedFile.getAbsolutePath()); // sciezka do pliku
                    groupOfMovies.add(currentMovie);
                }
            }
            if (eventSource == saveSerializableButton) {
                currentMovie = getCurrentMovie();

                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Zapisywanie pliku binarnego.");
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int returnValue = jfc.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    Movie.writeObject(selectedFile.getAbsolutePath(), currentMovie);
//						System.out.println(selectedFile.getAbsolutePath());
                }

            }
            if (eventSource == loadSerializableButton) {
                currentMovie = getCurrentMovie();

                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Odczytywanie pliku binarnego.");
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    currentMovie = Movie.readObject(selectedFile.getAbsolutePath());
                    groupOfMovies.add(currentMovie);
                }
            }
            if (eventSource == editButton) {
                currentMovie = getCurrentMovie();
                if (currentMovie == null) throw new MovieException("Zadna osoba nie zostala utworzona.");
                MovieWindowDialog.changeMovieData(this, currentMovie);
            }
            if (eventSource == infoButton) {
                JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
            }
            if (eventSource == exitButton) {
                System.exit(0);
            }

            // group change
            if (eventSource == changeGroupNameButton) {
                GroupWindowDialog.changeGroupName(this, groupOfMovies);
            }

            if(eventSource == changeCollectionButton) {
                CollectionTypeWindowDialog.changeGroupType(this, groupOfMovies);
            }

            // sort
            if(eventSource == sortTitleButton) {
                groupOfMovies.sortTitle();
            }

            if(eventSource == sortPremiereYearButton) {
                groupOfMovies.sortPremiereYear();
            }

            if(eventSource == sortGenreButton) {
                groupOfMovies.sortGenre();
            }


        } catch (MovieException e) {

            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Aktualizacja pol tekstowych
        showCurrentData();
    }

    public static GroupOfMovies createNewGroupOfMovies(Window parent) {
        String groupName = GroupWindowDialog.newGroupName(parent);
        GroupType groupType = CollectionTypeWindowDialog.newGroupType(parent);
        try{
            GroupOfMovies groupOfMovies = new GroupOfMovies(groupType, groupName);
            GroupOfMoviesWindowDialog dialog = new GroupOfMoviesWindowDialog(parent, groupOfMovies);
            return dialog.groupOfMovies;
        }
        catch (MovieException e){
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Blad podczas tworzenia obiektu klasy GroupOfMovies.", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }




}
