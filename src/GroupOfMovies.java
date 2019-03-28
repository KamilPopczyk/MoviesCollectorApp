import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.swing.text.html.HTMLDocument;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;




enum GroupType {
    VECTOR("Lista   (klasa Vector)"),
    ARRAY_LIST("Lista   (klasa ArrayList)"),
    LINKED_LIST("Lista   (klasa LinkedList)"),
    HASH_SET("Zbiór   (klasa HashSet)"),
    TREE_SET("Zbiór   (klasa TreeSet)");

    String typeName;

    private GroupType(String type_name) {
        typeName = type_name;
    }

    @Override
    public String toString() {
        return typeName;
    }
    public static GroupType find(String type_name) {
        for(GroupType type : values()){
            if(type.typeName.equals(type_name)){
                return type;
            }
        }
        return null;
    }

    public Collection<Movie> createCollection() throws MovieException {
        switch (this) {
            case VECTOR: return new Vector<Movie>();
            case ARRAY_LIST: return new ArrayList<Movie>();
            case HASH_SET: return new HashSet<Movie>();
            case TREE_SET: return new TreeSet<Movie>();
            case LINKED_LIST: return new LinkedList<Movie>();
            default: throw new MovieException("Nie ma implementacji podanego typu kolekcji.");
        }
    }
} // end Group type



public class GroupOfMovies implements Iterable<Movie>, Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private GroupType type;
    private Collection<Movie> collection;

    public GroupOfMovies(GroupType type, String name) throws MovieException {
        setName(name);
        if (type==null) {
            throw new MovieException("Nieprawidłowy typ kolekcji.");
        }
        this.type = type;
        collection = this.type.createCollection();
    }

    public GroupOfMovies(String type_name, String name) throws MovieException {
        setName(name);
        GroupType type = GroupType.find(type_name);
        if (type==null) {
            throw new MovieException("Nieprawidłowy typ kolekcji.");
        }
        this.type = type;
        collection = this.type.createCollection();
    }

    public String getName() {return name;}

    public void setName(String name) throws MovieException {
        if ((name == null) || name.equals(""))
            throw  new MovieException("Brak wpisanej nazwy grupy.");
        this.name = name;
    }

    public GroupType getType() { return type; }

    public void setType(GroupType type) throws MovieException {
        if(type == null) {
            throw new MovieException("Brak typu kolekcji.");
        }
        if(this.type == type) return;

        // zmiana typu kolekcji:
        Collection<Movie> previousCollection = collection;
        collection = type.createCollection();
        this.type = type;
        for(Movie movie : previousCollection)
            collection.add(movie);
    }

    public void setType(String type_name) throws MovieException {
        for(GroupType type : GroupType.values()) {
            if (type.toString().equals((type_name))) {
                setType(type);
                return;
            }
        }
        throw new MovieException("Brak typu kolekcji.");
    }

    public boolean add(Movie e) {
        return collection.add(e);
    }

    public Iterator<Movie> iterator() {
        return collection.iterator();
    }

    public int size() {
        return collection.size();
    }

    // sortowanie

    public void sortTitle() throws MovieException {
        if(type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
            throw new MovieException("Kolekcje typu set nie mogą być sortowane.");
        }

        Collections.sort((List<Movie>) collection);
    }

    public void sortPremiereYear() throws MovieException {
        if(type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
            throw new MovieException("Kolekcje typu set nie mogą być sortowane.");
        }

        Collections.sort((List<Movie>) collection, new Comparator<Movie>() {

            @Override
            public int compare(Movie o1, Movie o2) {
                if (o1.getPremiereYear() < o2.getPremiereYear()) return -1;
                if (o1.getPremiereYear() > o2.getPremiereYear()) return 1;
                return 0;
            }
        });

    }

    public void sortGenre() throws MovieException {
        if(type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
            throw new MovieException("Kolekcje typu set nie mogą być sortowane.");
        }

        Collections.sort((List<Movie>) collection, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                return o1.getGenre().toString().compareTo(o2.getGenre().toString());
            }
        });
    }

    @Override
    public String toString() {
        return name + " [" + type + "]";
    }

    public static void printToFile(PrintWriter writer, GroupOfMovies group) {
        writer.println(group.getName());
        writer.println(group.getType());
        for (Movie movie : group.collection)
            Movie.printToFile(writer, movie);
    }

    public static void printToFile(String file_name, GroupOfMovies group) throws MovieException {
        try (PrintWriter writer = new PrintWriter(file_name)) {
            printToFile(writer, group);
        } catch (FileNotFoundException e){
            throw new MovieException("Nie odnaleziono pliku " + file_name);
        }
    }

    public static GroupOfMovies readFromFile(BufferedReader reader) throws MovieException{
        try {
            String group_name = reader.readLine();
            String type_name = reader.readLine();
            GroupOfMovies groupOfMovies = new GroupOfMovies(type_name, group_name);

            Movie movie;
            while((movie = Movie.readFromFile(reader)) != null)
                groupOfMovies.collection.add(movie);
            return groupOfMovies;
        } catch(IOException e){
            throw new MovieException("Błąd podczas odczytu danych z pliku.");
        }
    }

    public static GroupOfMovies readFromFile(String file_name) throws MovieException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
            return GroupOfMovies.readFromFile(reader);
        } catch (FileNotFoundException e){
            throw new MovieException("Nie odnaleziono pliku " + file_name);
        } catch(IOException e){
            throw new MovieException("Błąd podczas odczytu danych z pliku.");
        }
    }

    public static GroupOfMovies createGroupUnion(GroupOfMovies g1,GroupOfMovies g2) throws MovieException {
        String name = "(" + g1.name + " OR " + g2.name +")";
        GroupType type;
        if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
            type = g2.type;
        } else {
            type = g1.type;
        }
        GroupOfMovies group = new GroupOfMovies(type, name);
        group.collection.addAll(g1.collection);
        group.collection.addAll(g2.collection);
        return group;
    }

    public static GroupOfMovies createGroupIntersection(GroupOfMovies g1,GroupOfMovies g2) throws MovieException {
        String name = "(" + g1.name + " AND " + g2.name +")";
        GroupType type;
        if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
            type = g2.type;
        } else {
            type = g1.type;
        }
        GroupOfMovies group = new GroupOfMovies(type, name);

        // wyznaczanie czesci wspolnej
        Iterator itG1 = g1.iterator();
        Movie currentMovie;
        while (itG1.hasNext()) {    // petla sprawdzajaca kazdy element
            currentMovie = (Movie)itG1.next();
            if(g2.collection.contains(currentMovie)) {
                if(!group.collection.contains(currentMovie)) group.collection.add(currentMovie); // czy juz nie byl dodany ?
            }
        }
        return group;
    }

    public static GroupOfMovies createGroupDifference(GroupOfMovies g1,GroupOfMovies g2) throws MovieException {
        String name = "(" + g1.name + " SUB " + g2.name +")";
        GroupType type;
        if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
            type = g2.type;
        } else {
            type = g1.type;
        }
        GroupOfMovies group = new GroupOfMovies(type, name);

        // roznica dwoch grup
        // filmy z pierwszej grupy jesli nie naleza do drugiej
        Iterator itG1 = g1.iterator();
        Movie currentMovie;
        while (itG1.hasNext()) {    // petla sprawdzajaca kazdy element
            currentMovie = (Movie)itG1.next();
            if(!g2.collection.contains(currentMovie)) {
                if(!group.collection.contains(currentMovie)) group.collection.add(currentMovie); // czy juz nie byl dodany ?
            }
        }

        return group;
    }

    public static GroupOfMovies createGroupSymmetricDiff(GroupOfMovies g1,GroupOfMovies g2) throws MovieException {
        String name = "(" + g1.name + " XOR " + g2.name +")";
        GroupType type;
        if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
            type = g2.type;
        } else {
            type = g1.type;
        }

        GroupOfMovies group = new GroupOfMovies(type, name);

        Iterator itG1 = g1.iterator();
        Movie currentMovie;
        while (itG1.hasNext()) {    // petla sprawdzajaca kazdy element
            currentMovie = (Movie)itG1.next();
            if(!g2.collection.contains(currentMovie)) {
                if(!group.collection.contains(currentMovie)) group.collection.add(currentMovie); // czy juz nie byl dodany ?
            }
        }

        Iterator itG2 = g2.iterator();
        while (itG2.hasNext()) {    // petla sprawdzajaca kazdy element
            currentMovie = (Movie)itG2.next();
            if(!g1.collection.contains(currentMovie)) {
                if(!group.collection.contains(currentMovie)) group.collection.add(currentMovie); // czy juz nie byl dodany ?
            }
        }

        return group;
    }


} // end GroupOfMovies
