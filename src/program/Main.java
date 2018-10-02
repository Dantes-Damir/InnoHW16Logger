package program;

public class Main {
    public static void main(String[] args) {
        OccurrencesRealization occurrencesRealization = new OccurrencesRealization();
        String[] sources = {"http://www.gutenberg.org/files/4280/4280-h/4280-h.htm"};
        String[] words = {"Produced by Charles Aldarondo and David Widger"};
        try {
            occurrencesRealization.getOccurrences(sources, words, "FileExample.txt");
        } catch (InterruptedException e) {
            System.out.println("Программа завершила работу.");
        }
    }
}