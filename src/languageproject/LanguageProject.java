package languageproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LanguageProject {
    public static void main(String[] args) {
        // Default test file. You can change this file name when testing another file.
        String filePath = "examples/test1_assignment.txt";

        if (args.length > 0) {
            filePath = args[0];
        }

        File file = new File(filePath);

        if (!file.exists()) {
            file = new File("LanguageProject/" + filePath);
        }

        System.out.println("==============================================");
        System.out.println("   TAMGA LEXER AND RECURSIVE-DESCENT PARSER   ");
        System.out.println("==============================================");
        System.out.println("Analyzed File: " + file.getPath());
        System.out.println();

        if (!file.exists()) {
            System.out.println("ERROR: '" + filePath + "' file not found!");
            System.out.println("The program also checked: LanguageProject/" + filePath);
            System.out.println("Please check the 'examples' folder and the file name.");
            System.exit(1);
        }

        // 1. FILE READING PROCESS
        String codeContent = "";

        try {
            Scanner reader = new Scanner(file);

            // Read all text from the file line by line.
            while (reader.hasNextLine()) {
                codeContent += reader.nextLine() + "\n";
            }

            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: '" + file.getPath() + "' file not found!");
            System.out.println("Please check the 'examples' folder and the file name.");
            System.exit(1);
        }

        // 2. LEXICAL ANALYSIS STAGE
        Lexer lexer = new Lexer(codeContent);

        // 3. SYNTAX ANALYSIS STAGE
        Parser parser = new Parser(lexer);

        // 4. START PARSING FROM THE TOP GRAMMAR RULE
        parser.program();
    }
}

