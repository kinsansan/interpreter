package newlang8;

import java.io.File;
import java.io.FileInputStream;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	       FileInputStream fin = null;
	        LexicalAnalyzer lex;
	        LexicalUnit		first;
	        Environment		env;
	        Node			prog;

                System.out.println("basic parser");
	        try {
	            fin = new FileInputStream("./src/newlang8/test.txt");
	        }
	        catch(Exception e) {
	            System.out.println("file not found");
	            System.exit(-1);
	        }
	        lex = new LexicalAnalyzerImpl(fin);
	        env = new Environment(lex);
	        
	        first = lex.get();
                prog = Program.isMatch(env, first);
                lex.unget(first);
	        if (prog != null && prog.Parse()) {
                System.out.println(prog.toString());
	        	prog.getValue();
	        }
	        else System.out.println("syntax error");
	}

}
