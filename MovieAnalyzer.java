import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class is used to read and parse movies information from .csv file
 */
public class MovieAnalyzer {
  List<Movie> movies;

  /**
   * take in file path and return a list containing Movie class and its information.

   * @param dataset_path The file path of .csv
   *
  */
  public MovieAnalyzer(String dataset_path) {
    try {
      File inputF = new File(dataset_path);
      InputStream fs = new FileInputStream(inputF);
      BufferedReader br = new BufferedReader(new InputStreamReader(fs, StandardCharsets.UTF_8));
      Function<String, Movie> MapToMovies = (line) -> {
        String[] cols = combine(line.split(","));
        Movie movie = new Movie();
        movie.setSeries_Title(cols[1].equals("") ? null : cols[1]);
        movie.setReleased_Year(cols[2].equals("") ? 0 : Integer.parseInt(cols[2]));
        movie.setCertificate(cols[3].equals("") ? null : cols[3]);
        movie.setRuntime(cols[4].equals("") ? 0 : Integer.parseInt(cols[4]
                                                         .replaceAll("[^0-9]", "")));
        movie.setGenre(cols[5].equals("") ? null : cols[5].replace(" ", "")
                                                          .replace("\"", "").split(","));
        movie.setIMDB_Rating(cols[6].equals("") ? 0 : Float.parseFloat(cols[6]));
        movie.setOverview(cols[7].equals("") ? null : cols[7]);
        movie.setMeta_score(cols[8].equals("") ? 0 : Integer.parseInt(cols[8]));
        movie.setDirector(cols[9].equals("") ? null : cols[9]);
        String[] stars = new String[4];
        stars[0] = cols[10].equals("") ? null : cols[10];
        stars[1] = cols[11].equals("") ? null : cols[11];
        stars[2] = cols[12].equals("") ? null : cols[12];
        stars[3] = cols[13].equals("") ? null : cols[13];
        movie.setStar(stars);
        movie.setNoofvotes(cols[14].equals("") ? 0L :
                Long.parseLong(cols[14].replaceAll("[^0-9]", "")));
        movie.setGross(cols[15].equals("") ? 0L :
                Long.parseLong(cols[15].replaceAll("[^0-9]", "")));
        movie.StarCnt();
        return movie;
      };
      this.movies = br.lines().skip(1).map(MapToMovies).collect(Collectors.toList());
      br.close();
    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static int count(String text){
        int result = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i)=='"'){
                result++;
            }
        }
        return result%2;
        //return true if contains even number of quotes
    }
    public static String[] combine(String[] groups){
        String[] result = new String[16];
        StringBuilder sb;
        int index = 0;
        for(int i = 0; i < groups.length; i++){
            if (count(groups[i])%2==0){
                result[index++] = groups[i];
            }
            else{
                sb = new StringBuilder();
                sb.append(groups[i]).append(",").append(groups[++i]);
                while(count(groups[i])%2==0){
                    sb.append(",").append(groups[++i]);
                }
//                sb.append(groups[i-1]).append(",").append(groups[i]);
                result[index++] = String.valueOf(sb);
            }
        }
        result[15] = result[15]==null ? "":result[15];
        return result;
    }
    @Override
    public String toString() {
        return "MovieAnalyzer{" +
                "movies=" + movies +
                '}';
    }

    static class Movie{
        private String Series_Title;
        private int Released_Year;
        private String Certificate;
        private int Runtime;
        private final List<String> Genre = new ArrayList<>();
        private float IMDB_Rating;
        private String Overview;
        private int Meta_score;
        private String Director;
        private final List<Star> Star = new ArrayList<>();
        private long Noofvotes;
        private long Gross;

        public void setSeries_Title(String series_Title) {
            if (series_Title!=null && series_Title.startsWith("\"")){
                Series_Title = series_Title.substring(1,series_Title.length()-1);
            }
            else{
                Series_Title = series_Title;
            }
        }

        public void setReleased_Year(int released_Year) {
            Released_Year = released_Year;
        }

        public void setCertificate(String certificate) {
            Certificate = certificate;
        }

        public void setRuntime(int runtime) {
            this.Runtime = Math.max(runtime, 0);
        }

        public void setGenre(String[] genre) {
            this.Genre.addAll(List.of(genre));
        }

        public void setIMDB_Rating(float IMDB_Rating) {
            this.IMDB_Rating = (float) Math.max(IMDB_Rating, 0.0);
        }

        public void setOverview(String overview) {
            if (overview!=null && overview.startsWith("\"")){
                Overview = overview.substring(1,overview.length()-1);
            }
            else{
                Overview = overview;
            }
        }

        public void setMeta_score(int meta_score) {
            Meta_score = meta_score;
        }

        public void setDirector(String director) {
            Director = director;
        }

        public void setStar(String[] stars){
            for (String name: stars) {
                this.Star.add(new Star(name));
            }
        }

        public void setNoofvotes(long noofvotes) {
            Noofvotes = noofvotes;
        }

        public void setGross(long gross) {
            Gross = gross;
        }

        public String getSeries_Title() {
            return Series_Title;
        }

        public int getReleased_Year() {
            return Released_Year;
        }

        public String getCertificate() {
            return Certificate;
        }

        public int getRuntime() {
            return Runtime;
        }

        public List<String> getGenre() {
            return Genre;
        }

        public float getIMDB_Rating() {
            return IMDB_Rating;
        }

        public String getOverview() {
            return Overview;
        }

        public int getMeta_score() {
            return Meta_score;
        }

        public String getDirector() {
            return Director;
        }

        public List<Star> getStar(){return this.Star;}

        public long getNoofvotes() {return Noofvotes;}

        public long getGross() {
            return Gross;
        }

        public void StarCnt(){
            for (Star star : this.Star){
                star.setRating(this.getIMDB_Rating());
                star.setGross(this.getGross());
            }
        }

        @Override
        public String toString() {
            return "Movie{" +
                    "Series_Title='" + Series_Title + '\'' +
                    ", Released_Year=" + Released_Year +
                    ", Certificate='" + Certificate + '\'' +
                    ", Runtime=" + Runtime +
                    ", Genre=" + Genre +
                    ", IMDB_Rating=" + IMDB_Rating +
                    ", Overview='" + Overview + '\'' +
                    ", Meta_score=" + Meta_score +
                    ", Director='" + Director + '\'' +
                    ", Star=" + Star +
                    ", Noofvotes=" + Noofvotes +
                    ", Gross=" + Gross +
                    '}';
        }
    }

    static class Star{
        private double rating;
        private String name;
        private long gross;
        public Star(String name){
            this.name = name;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getGross() {
            return gross;
        }

        public void setGross(long gross) {
            this.gross = gross;
        }

        @Override
        public String toString() {
            return "Star{" +
                    "rating=" + rating +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public Map<Integer, Integer> getMovieCountByYear(){
        Comparator<Movie> comparator = Comparator.comparingInt(Movie::getReleased_Year);
        return this.movies.stream()
                .collect(Collectors.groupingBy(
                        Movie::getReleased_Year,
                        ()-> new TreeMap<Integer,Integer>(Comparator.comparingInt(e-> (int) e).reversed()),
                        Collectors.reducing(0, e->1,Integer::sum)));
    }

    public Map<String, Integer> getMovieCountByGenre(){
        return this.movies.stream().map(Movie::getGenre).flatMap(List::stream)
                .collect(Collectors.groupingBy(
                        String::toString,
                        HashMap::new,
                        Collectors.reducing(0, e->1,Integer::sum)
                )).entrySet().stream()
                .sorted((s1,s2)->{
                    if (s1.getValue()<s2.getValue()) return 1;
                    else if (s1.getValue()>s2.getValue()) return -1;
                    else{
                        return s1.getKey().compareTo(s2.getKey());
                    }
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Map<List<String>, Integer> getCoStarCount(){
        return this.movies.stream().map(Movie::getStar)
                .map(MovieAnalyzer::address1)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(
                        ArrayList::new,
                        HashMap::new,
                        Collectors.reducing(0,e->1,Integer::sum)
                ))
                .entrySet().stream()
                .sorted((o1,o2)-> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    public List<String> getTopMovies(int top_k, String by){
        if (by.equals("runtime")){
            return this.movies.stream()
                    .filter(s->s.getRuntime()>0)
                    .sorted((o1,o2)-> {
                        if (o1.getRuntime()>o2.getRuntime()) return -1;
                        else if (o1.getRuntime()<o2.getRuntime()) return 1;
                        else {
                            return o1.getSeries_Title().compareTo(o2.getSeries_Title());
                        }
                    })
                    .limit(top_k)
                    .map(Movie::getSeries_Title)
                    .collect(Collectors.toList());
        }
        else if (by.equals("overview")){
            return this.movies.stream()
                    .filter(s->s.getOverview().length()>0)
                    .sorted((o1,o2)-> {
                        if (o1.getOverview().length()>o2.getOverview().length()) return -1;
                        else if (o1.getOverview().length()<o2.getOverview().length()) return 1;
                        else {
                            return o1.getSeries_Title().compareTo(o2.getSeries_Title());
                        }
                    })
                    .limit(top_k)
                    .map(Movie::getSeries_Title)
                    .collect(Collectors.toList());
        }
        else{
            return null;
        }
    }

    public List<String> getTopStars(int top_k, String by){
        if (by.equals("rating")){
            return this.movies.stream()
                    .filter(s->s.getIMDB_Rating()>0)
                    .map(Movie::getStar)
                    .flatMap(List::stream)
                    .collect(Collectors.groupingBy(
                            Star::getName,
                            HashMap::new,
                            Collectors.averagingDouble(Star::getRating)
                    )).entrySet().stream()
                    .sorted((s1,s2)-> {
                        if (s1.getValue()>s2.getValue()) return -1;
                        else if (s1.getValue()<s2.getValue()) return 1;
                        else return s1.getKey().compareTo(s2.getKey());
                    })
                    .limit(top_k)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
        else if (by.equals("gross")){
            return this.movies.stream()
                    .filter(s->s.getGross()>0)
                    .map(Movie::getStar)
                    .flatMap(List::stream)
                    .collect(Collectors.groupingBy(
                            Star::getName,
                            HashMap::new,
                            Collectors.averagingLong(Star::getGross)
                    )).entrySet().stream()
                    .sorted((s1, s2) -> {
                        if (s1.getValue() > s2.getValue()) return -1;
                        else if (s1.getValue() < s2.getValue()) return 1;
                        else return s1.getKey().compareTo(s2.getKey());
                    })
                    .limit(top_k)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
        else {
            return null;
        }
    }

    public List<String> searchMovies(String genre, float min_rating, int max_runtime){
        return this.movies.stream()
                .filter(s->s.getGenre().contains(genre))
                .filter(s->s.getIMDB_Rating()>=min_rating)
                .filter(s->s.getRuntime()<max_runtime & s.getRuntime()>0)
                .map(Movie::getSeries_Title)
                .sorted()
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        MovieAnalyzer test = new MovieAnalyzer("resources/imdb_top_500.csv");
        test.getTopStars(15,"gross").forEach(System.out::println);
    }

    public static List<List<String>> address1(List<Star> list){
        List<List<String>> result = new ArrayList<>();
        List<String> temp;
        for (Star star : list) {
            if (star.getName() == null) {
                return result;
            }
        }
        list.sort(Comparator.comparing(s -> s.name));
        for (int i = 0; i < list.size()-1; i++) {
            for (int j = i+1; j < list.size(); j++) {
                temp = new ArrayList<>();
                temp.add(list.get(i).getName());
                temp.add(list.get(j).getName());
                result.add(temp);
            }
        }
        return result;
    }
}
