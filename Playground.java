import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Playground {
    public static void main(String[] args) {
        System.out.println("Welcome to the Playground!");
        List<Integer> nums = List.of(1, 2, 3, 4);

        List<Integer> doubled = nums.stream()
                .peek(n -> System.out.println("Before map: " + n))
                .map(n -> n * 2)
                .peek(n -> System.out.println("After map: " + n))
                .toList();

        System.out.println(doubled);

    }

}
