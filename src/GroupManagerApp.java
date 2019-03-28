
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;



public class GroupManagerApp extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String GREETING_MESSAGE =
                    "Program do zarządzania grupami filmów \n" +
                    "Autor: Kamil Popczyk \n";

    // save & load file
    private static final String ALL_GROUPS_FILE = "LISTA_GRUP.BIN";


    public static void main(String[] args) {
        new GroupManagerApp();
    }


    WindowAdapter windowListener = new WindowAdapter() {

        @Override
        public void windowClosed(WindowEvent e) {

            JOptionPane.showMessageDialog(null, "Koniec programu.");
        }


        @Override
        public void windowClosing(WindowEvent e) {
            windowClosed(e);
        }

    };


    private List<GroupOfMovies> currentList = new ArrayList<GroupOfMovies>();

    JMenuBar menuBar        = new JMenuBar();
    JMenu menuGroups        = new JMenu("Grupy");
    JMenu menuSpecialGroups = new JMenu("Grupy specjalne");
    JMenu menuAbout         = new JMenu("O programie");

    JMenuItem menuNewGroup           = new JMenuItem("Utwórz grupę");
    JMenuItem menuEditGroup          = new JMenuItem("Edytuj grupę");
    JMenuItem menuDeleteGroup        = new JMenuItem("Usuń grupę");
    JMenuItem menuLoadGroup          = new JMenuItem("Załaduj grupę z pliku");
    JMenuItem menuSaveGroup          = new JMenuItem("Zapisz grupę do pliku");

    JMenuItem menuGroupUnion         = new JMenuItem("Połączenie grup");
    JMenuItem menuGroupIntersection  = new JMenuItem("Cześć wspólna grup");
    JMenuItem menuGroupDifference    = new JMenuItem("Różnica grup");
    JMenuItem menuGroupSymmetricDiff = new JMenuItem("Różnica symetryczna grup");

    JMenuItem menuAuthor             = new JMenuItem("Autor");

    // main buttons
    JButton buttonNewGroup = new JButton("Utwórz");
    JButton buttonEditGroup = new JButton("Edytuj");
    JButton buttonDeleteGroup = new JButton(" Usuń ");
    JButton buttonLoadGroup = new JButton("Otwórz");
    JButton buttonSavegroup = new JButton("Zapisz");

    JButton buttonUnion = new JButton("Suma");
    JButton buttonIntersection = new JButton("Iloczyn");
    JButton buttonDifference = new JButton("Różnica");
    JButton buttonSymmetricDiff = new JButton("Różnica symetryczna");


    ViewGroupList viewList;


    public GroupManagerApp() {
        setTitle("GroupManager");
        setSize(450, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

                              @Override
                              public void windowClosed(WindowEvent event) {
                                  try {
                                      saveGroupListToFile(ALL_GROUPS_FILE);
                                      JOptionPane.showMessageDialog(null, "Zapis danych do pliku " + ALL_GROUPS_FILE);
                                  } catch (MovieException e) {
                                      JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                  }
                              }

                              @Override
                              public void windowClosing(WindowEvent e) {
                                  windowClosed(e);
                              }

                          }
        );

        try {

            loadGroupListFromFile(ALL_GROUPS_FILE);
            JOptionPane.showMessageDialog(null, "Dane zostały wczytane z pliku " + ALL_GROUPS_FILE);
        } catch (MovieException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


        // Utworzenie i konfiguracja menu aplikacji
        setJMenuBar(menuBar);
        menuBar.add(menuGroups);
        menuBar.add(menuSpecialGroups);
        menuBar.add(menuAbout);

        menuGroups.add(menuNewGroup);
        menuGroups.add(menuEditGroup);
        menuGroups.add(menuDeleteGroup);
        menuGroups.addSeparator();
        menuGroups.add(menuLoadGroup);
        menuGroups.add(menuSaveGroup);

        menuSpecialGroups.add(menuGroupUnion);
        menuSpecialGroups.add(menuGroupIntersection);
        menuSpecialGroups.add(menuGroupDifference);
        menuSpecialGroups.add(menuGroupSymmetricDiff);

        menuAbout.add(menuAuthor);

        // Listeners
        menuNewGroup.addActionListener(this);
        menuEditGroup.addActionListener(this);
        menuDeleteGroup.addActionListener(this);
        menuLoadGroup.addActionListener(this);
        menuSaveGroup.addActionListener(this);
        menuGroupUnion.addActionListener(this);
        menuGroupIntersection.addActionListener(this);
        menuGroupDifference.addActionListener(this);
        menuGroupSymmetricDiff.addActionListener(this);
        menuAuthor.addActionListener(this);

        buttonNewGroup.addActionListener(this);
        buttonEditGroup.addActionListener(this);
        buttonDeleteGroup.addActionListener(this);
        buttonLoadGroup.addActionListener(this);
        buttonSavegroup.addActionListener(this);
        buttonUnion.addActionListener(this);
        buttonIntersection.addActionListener(this);
        buttonDifference.addActionListener(this);
        buttonSymmetricDiff.addActionListener(this);

        viewList = new ViewGroupList(currentList, 400, 250);
        viewList.refreshView();

        JPanel panel = new JPanel();



        panel.add(viewList);
        panel.add(buttonNewGroup);
        panel.add(buttonEditGroup);
        panel.add(buttonDeleteGroup);
        panel.add(buttonLoadGroup);
        panel.add(buttonSavegroup);
        panel.add(buttonUnion);
        panel.add(buttonIntersection);
        panel.add(buttonDifference);
        panel.add(buttonSymmetricDiff);

        setContentPane(panel);

        setVisible(true);
    }



    @SuppressWarnings("unchecked")
    void loadGroupListFromFile(String file_name) throws MovieException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file_name))) {
            currentList = (List<GroupOfMovies>)in.readObject();
        } catch (FileNotFoundException e) {
            throw new MovieException("Nie odnaleziono pliku " + file_name);
        } catch (Exception e) {
            throw new MovieException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
        }
    }


    void saveGroupListToFile(String file_name) throws MovieException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file_name))) {
            out.writeObject(currentList);
        } catch (FileNotFoundException e) {
            throw new MovieException("Nie odnaleziono pliku " + file_name);
        } catch (IOException e) {
            throw new MovieException("Wystąpił błąd podczas zapisu danych do pliku.");
        }
    }

    private  GroupOfMovies chooseGroup(Window parent, String message){
        Object[] groups = currentList.toArray();
        GroupOfMovies group = (GroupOfMovies) JOptionPane.showInputDialog(
                parent, message,
                "Wybierz grupę.",
                JOptionPane.QUESTION_MESSAGE,
                null,
                groups,
                null);
        return group;
    }



    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        try {
            if (source == menuNewGroup || source == buttonNewGroup) {
                GroupOfMovies group = GroupOfMoviesWindowDialog.createNewGroupOfMovies(this);
                if (group != null) {
                    currentList.add(group);
                }
            }

            if (source == menuEditGroup || source == buttonEditGroup) {
                int index = viewList.getSelectedIndex();
                if (index >= 0) {
                    Iterator<GroupOfMovies> iterator = currentList.iterator();
                    while (index-- > 0)
                        iterator.next();
                    new GroupOfMoviesWindowDialog(this, iterator.next());
                }
            }

            if (source == menuDeleteGroup || source == buttonDeleteGroup) {
                int index = viewList.getSelectedIndex();
                if (index >= 0) {
                    Iterator<GroupOfMovies> iterator = currentList.iterator();
                    while (index-- >= 0)
                        iterator.next();
                    iterator.remove();
                }
            }

            if (source == menuLoadGroup || source == buttonLoadGroup) {
                JFileChooser chooser = new JFileChooser(".");
                int returnVal = chooser.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    GroupOfMovies group = GroupOfMovies.readFromFile(chooser.getSelectedFile().getName());
                    currentList.add(group);
                }
            }

            if (source == menuSaveGroup || source == buttonSavegroup) {
                int index = viewList.getSelectedIndex();
                if (index >= 0) {
                    Iterator<GroupOfMovies> iterator = currentList.iterator();
                    while (index-- > 0)
                        iterator.next();
                    GroupOfMovies group = iterator.next();

                    JFileChooser chooser = new JFileChooser(".");
                    int returnVal = chooser.showSaveDialog(this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        GroupOfMovies.printToFile( chooser.getSelectedFile().getName(), group );
                    }
                }
            }

            if (source == menuGroupUnion || source == buttonUnion) {
                String message1 =
                        "SUMA GRUP\n\n" +
                                "Tworzenie grupy zawieraj¹cej wszystkie osoby z grupy pierwszej\n" +
                                "oraz wszystkie osoby z grupy drugiej.\n" +
                                "Wybierz pierwsz¹ grupê:";
                String message2 =
                        "SUMA GRUP\n\n" +
                                "Tworzenie grupy zawieraj¹cej wszystkie osoby z grupy pierwszej\n" +
                                "oraz wszystkie osoby z grupy drugiej.\n" +
                                "Wybierz drug¹ grupê:";
                GroupOfMovies group1 = chooseGroup(this, message1);
                if (group1 == null)
                    return;
                GroupOfMovies group2 = chooseGroup(this, message2);
                if (group2 == null)
                    return;
                currentList.add( GroupOfMovies.createGroupUnion(group1, group2) );
            }

            if (source == menuGroupIntersection || source == buttonIntersection) {
                String message1 =
                        "ILOCZYN GRUP\n\n" +
                                "Tworzenie grupy osób, które nale¿¹ zarówno do grupy pierwszej,\n" +
                                "jak i do grupy drugiej.\n" +
                                "Wybierz pierwsz¹ grupê:";
                String message2 =
                        "ILOCZYN GRUP\n\n" +
                                "Tworzenie grupy osób, które nale¿¹ zarówno do grupy pierwszej,\n" +
                                "jak i do grupy drugiej.\n" +
                                "Wybierz drug¹ grupê:";
                GroupOfMovies group1 = chooseGroup(this, message1);
                if (group1 == null)
                    return;
                GroupOfMovies group2 = chooseGroup(this, message2);
                if (group2 == null)
                    return;
                currentList.add( GroupOfMovies.createGroupIntersection(group1, group2) );
            }

            if (source == menuGroupDifference || source == buttonDifference) {
                String message1 =
                        "RÓ¯NICA GRUP\n\n" +
                                "Tworzenie grupy osób, które nale¿¹ do grupy pierwszej\n" +
                                "i nie ma ich w grupie drugiej.\n" +
                                "Wybierz pierwsz¹ grupê:";
                String message2 =
                        "RÓ¯NICA GRUP\n\n" +
                                "Tworzenie grupy osób, które nale¿¹ do grupy pierwszej\n" +
                                "i nie ma ich w grupie drugiej.\n" +
                                "Wybierz drug¹ grupê:";
                GroupOfMovies group1 = chooseGroup(this, message1);
                if (group1 == null)
                    return;
                GroupOfMovies group2 = chooseGroup(this, message2);
                if (group2 == null)
                    return;
                currentList.add( GroupOfMovies.createGroupDifference(group1, group2) );
            }

            if (source == menuGroupSymmetricDiff || source == buttonSymmetricDiff) {
                String message1 = "RÓ¯NICA SYMETRYCZNA GRUP\n\n"
                        + "Tworzenie grupy zawieraj¹cej osoby nale¿¹ce tylko do jednej z dwóch grup,\n"
                        + "Wybierz pierwsz¹ grupê:";
                String message2 = "RÓ¯NICA SYMETRYCZNA GRUP\n\n"
                        + "Tworzenie grupy zawieraj¹cej osoby nale¿¹ce tylko do jednej z dwóch grup,\n"
                        + "Wybierz drug¹ grupê:";
                GroupOfMovies group1 = chooseGroup(this, message1);
                if (group1 == null)
                    return;
                GroupOfMovies group2 = chooseGroup(this, message2);
                if (group2 == null)
                    return;
                currentList.add( GroupOfMovies.createGroupSymmetricDiff(group1, group2) );
            }

            if (source == menuAuthor) {
                JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
            }

        } catch (MovieException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        viewList.refreshView();
    }

}



class ViewGroupList extends JScrollPane {
    private static final long serialVersionUID = 1L;

    private List<GroupOfMovies> list;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewGroupList(List<GroupOfMovies> list, int width, int height){
        this.list = list;
        setPreferredSize(new Dimension(width, height));
        setBorder(BorderFactory.createTitledBorder("Lista grup:"));

        String[] tableHeader = { "Nazwa grupy", "Typ kolekcji", "Liczba filmów" };
        tableModel = new DefaultTableModel(tableHeader, 0);
        table = new JTable(tableModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        setViewportView(table);
    }

    void refreshView(){
        tableModel.setRowCount(0);
        for (GroupOfMovies group : list) {
            if (group != null) {
                String[] row = { group.getName(), group.getType().toString(), "" + group.size() };
                tableModel.addRow(row);
            }
        }
    }

    int getSelectedIndex(){
        int index = table.getSelectedRow();
        if (index<0) {
            JOptionPane.showMessageDialog(this, "Żadana grupa nie jest zaznaczona.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return index;
    }

} // koniec klasy ViewGroupList