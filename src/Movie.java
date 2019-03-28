import java.io.*;


enum MovieGenre {
    UNKNOWN("-------"),
    ACTION("Akcja"),
    ADVENTURE("Przygodowy"),
    COMEDY("Komedia"),
    CRIME("Kryminalny"),
    HORROR("Horror");

    String genreName;

    MovieGenre(String genre_name) {
        genreName = genre_name;
    }


    @Override
    public String toString() {
        return genreName;
    }


}


class MovieException extends Exception {

    private static final long serialVersionUID = 1L;

    public MovieException(String message) {
        super(message);
    }

}

public class Movie implements Serializable, Comparable<Movie>{
    private String title;
    private String directorName;
    private int premiereYear;
    private MovieGenre genre;

    public Movie() { }

    public Movie(String title) throws MovieException {
        setTitle(title);
    }

    @Override
    public String toString() { return title;}

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + title.hashCode();
        result = 31 * result + directorName.hashCode();
        result = 31 * result + premiereYear;
        result = 31 * result + genre.toString().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object x) {
        if(x == this) return true;
        if(!(x instanceof Movie)) {
            return false;
        }

        Movie movie = (Movie) x;

        return movie.title.equals(title) && movie.premiereYear == premiereYear && movie.directorName.equals(directorName) && movie.genre.equals(genre);
    }

    @Override
    public int compareTo(Movie movie) {
        return this.toString().compareTo(movie.toString());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws MovieException {
        if ((title == null) || title.equals(""))
            throw new MovieException("Pole tytul musi byc wypelnione! ");
        this.title = title;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) throws MovieException {
        if ((directorName == null) || directorName.equals(""))
            throw new MovieException("Pole rezyser musi byc wypelnione! ");
        this.directorName = directorName;
    }

    public int getPremiereYear() {
        return premiereYear;
    }

    public void setPremiereYear(int premiereYear) throws MovieException {
        if((premiereYear != 0) && (premiereYear < 1900 || premiereYear > 2020))
            throw new MovieException("Pole data premiery musi byc w przedziale [1900-2020]");
        this.premiereYear = premiereYear;
    }

    public void setPremiereYear(String premiereYear) throws MovieException {
        if(premiereYear == null || premiereYear.equals("")){
            setPremiereYear(0);
            return;
        }
        try {
            setPremiereYear(Integer.parseInt(premiereYear));
        }catch (NumberFormatException e) {
            throw new MovieException("Rok premiery musi byc liczba calkowita.");
        }
    }

    public MovieGenre getGenre() {
        return genre;
    }

    public void setGenre(MovieGenre genre){
        this.genre = genre;
    }

    public void setGenre(String genreName) throws MovieException {
        if (genreName == null || genreName.equals("")){
//            pusty lanuch znakow = gatunek niezdefioniowany
            return;
        }
        for (MovieGenre genre : MovieGenre.values()) {
            if(genre.genreName.equals(genreName)) {
                this.genre = genre;
                return;
            }
        }
        throw new MovieException("Nie ma takiego gatunku.");
    }

    public static void printToFile(PrintWriter writer, Movie movie){
        writer.println(movie.title + "#" + movie.directorName +
                "#" + movie.premiereYear + "#" + movie.genre);
    }

    public static void printToFile(String fileName, Movie movie) throws MovieException {
        try(PrintWriter writer = new PrintWriter(fileName)){
            printToFile(writer, movie);
        } catch (FileNotFoundException e){
            throw new MovieException("Nie odnaleziono pliku: " + fileName);
        }
    }

    public static Movie readFromFile(BufferedReader reader) throws MovieException {
        try {
            String line = reader.readLine();
            String[] txt = line.split("#");
            Movie movie = new Movie(txt[0]);
            movie.setDirectorName(txt[1]);
            movie.setPremiereYear(txt[2]);
            movie.setGenre(txt[3]);
            return movie;
        } catch(IOException e){
            throw new MovieException("Wystapil blad podczas odczytu danych z pliku.");
        }
    }

    public static Movie readFromFile(String file_name) throws MovieException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
            return Movie.readFromFile(reader);
        } catch (FileNotFoundException e){
            throw new MovieException("Nie odnaleziono pliku " + file_name);
        } catch(IOException e){
            throw new MovieException("Wystapil blad podczas odczytu danych z pliku.");
        }
    }

    public static void writeObject(String fileName, Movie movie) throws MovieException {
        if(!fileName.contains(".bin")) fileName = fileName + ".bin";
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(movie);
        } catch (FileNotFoundException e){
            throw new MovieException("Nie odnaleziono pliku " + fileName);
        } catch (IOException e){
            throw new MovieException("Wystapil blad podczas odczytu danych z pliku. :" + fileName);
        }
    }

    public static Movie readObject(String fileName) throws MovieException {
        if (!fileName.contains(".bin")) fileName = fileName + ".bin";
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Movie movie = (Movie) inputStream.readObject();
            return movie;
        } catch (FileNotFoundException e) {
            throw new MovieException("Nie odnaleziono pliku " + fileName);
        } catch (IOException e) {
            throw new MovieException("Wystapil blad podczas odczytu danych z pliku.");
        } catch (ClassNotFoundException e) {
            throw new MovieException("Error.");
        }
    }

}
