import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Playground {
    public static void main(String[] args) {
        System.out.println("Welcome to the Playground!");
    }

    List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry");
    Stream<String> filtered = fruits.stream()
            .filter(fruit -> fruit.startsWith("B"));

}
