import java.util.Scanner;
public class Task1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //task1

        //int num1 = sc.nextInt();
        //int num2 = sc.nextInt();
        //int result = remainder(num1, num2);
        //System.out.println("Остаток от деления равен " + result);

        //task2

        //int a = sc.nextInt();
        //int height = sc.nextInt();
        //double result = triArea(a, height);
        //System.out.println("Площадь треугольника равна " + result);

        //task3

        //int chickenAmount = sc.nextInt();
        //int cowsAmount = sc.nextInt();
        //int pigsAmount = sc.nextInt();
        //int result = animals(chickenAmount, cowsAmount, pigsAmount);
        //System.out.println("Всего ног " + result);

        //task4

        //double prob = sc.nextInt();
        //int  prize = sc.nextInt();
        //int pay = sc.nextInt();
        //boolean result = profitableGamble(prob,prize,pay);
        //System.out.println("ans is " + result);

        //task5

        //int n = sc.nextInt();
        //int a = sc.nextInt();
        //int b = sc.nextInt();
        //String result = operation(n, a, b);
        //System.out.println("ans is " + result);

        //task6

        //char sym = sc.next().charAt(0);
        //int result = ctoa(sym);
        //System.out.println(result);

        //task7

        //int amount = sc.nextInt();
        //int result = addUpTo(amount);
        //System.out.println(result);

        //task8

        //int side1 = sc.nextInt();
        //int side2 = sc.nextInt();
        //int result = nextEdge(side1, side2);
        //System.out.println(result);

        //task9

        int lenArray = sc.nextInt();
        int[] array = new int[lenArray];

        for (int i = 0; i < array.length; i++) {
            array[i] = sc.nextInt();
        }
        int result = sumOfCubes(array);
        System.out.println(result);

        //task10

        //int a = sc.nextInt();
        //int b = sc.nextInt();
        //int c = sc.nextInt();
        //boolean result = abcmath(a, b, c);
        //System.out.println(result);
    }

    //task1

    public static int remainder(int num1, int num2) {
        return num1 % num2;
    }
    //task2

    public static Double triArea(int a, int height) {
        return a * height * 0.5;
    }
    //task3

    public static int animals(int chickenAmount, int cowsAmount, int pigsAmount) {
        int chickensLegs = 2;
        int cowsLegs = 4;
        int pigsLegs = 4;
        return chickenAmount * chickensLegs + cowsAmount * cowsLegs + pigsAmount * pigsLegs;
    }
    //task4

    public static boolean profitableGamble(double prob, int prize, int pay) {
        return (prob * prize > pay);
    }
    //task5

    public static String operation(int n, int a, int b) {
        if (a + b == n) {
            return "added";
        } else if (a - b == n) {
            return "substracted";
        } else if (a * b == n) {
            return "multiply";
        } else if (a / b == n) {
            return "divided";
        } else {
            return "none";
        }
    }
    //task6

    public static int ctoa(char sym) {
        return sym;
    }
    //task7

    public static int addUpTo(int amount) {
        int sum = 0;
        for (int i = 1; i <= amount; i++) {
            sum += i;
        }
        return sum;
    }
    //task8

    public static int nextEdge(int side1, int side2) {
        return side1 + side2 - 1;
    }
    //task9

    public static int sumOfCubes(int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i] * array[i] * array[i];
        }
        return sum;
    }
    //task10

    public static boolean abcmath(int a, int b, int c) {
        for (int i = 0; i <= b; i++) {
            a += a;
        }
        return a % c == 0;
    }
}