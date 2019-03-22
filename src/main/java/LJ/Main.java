package LJ;

public class Main {
    public static void main(String... args) throws Exception {
        String input = null;
        String output = null;

        if (args.length < 1) {
            throw new Exception("how to compile:\n" +
                    "lj <input filename> <output filename>");
        } else if (args.length == 2) {
            input = args[0];
            output = args[1];

            System.out.println("input filename: " + input);
            System.out.println("output filename: " + output);
        } else if (args.length == 1) {
            input = args[0];
            output = "out_" + input;

            System.out.println("input filename: " + input);
            System.out.println("output filename: " + output);
        }

        Compiler.compile(input, output);
    }
}
