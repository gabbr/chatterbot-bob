package it.unibz.lib.bob.bbcheck;

/*************************************************************************
 *  Compilation:  javac In.java
 *  Execution:    java In
 *
 *  Reads in data of various types from: stdin, file, URL.
 *
 *  % java In
 *
 *  Remarks
 *  -------
 *    - isEmpty() returns true if there is no more input or
 *      it is all whitespace. This might lead to surprising behavior
 *      when used with readChar()
 *
 *************************************************************************/


import java.net.URLConnection;
import java.net.URL;
import java.net.Socket;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public final class ReadIn {
    private Scanner scanner;

    // assume Unicode UTF-8 encoding
    //private String charsetName = "UTF-8";

    //private String charsetName = "ISO-8859-1";
 
    
    // for stdin
    public ReadIn(String charsetName) { scanner = new Scanner(System.in, charsetName);  }

    // for socket
    public ReadIn(String charsetName, Socket socket) {
        try {
            InputStream is = socket.getInputStream();
            scanner = new Scanner(is, charsetName);
        } catch (IOException ioe) { System.err.println("Could not open " + socket); }
    }

    // for URLs
    public ReadIn(String charsetName, URL url) {
        try {
            URLConnection site = url.openConnection();
            InputStream is     = site.getInputStream();
            scanner            = new Scanner(is, charsetName);
        } catch (IOException ioe) { System.err.println("Could not open " + url); }
    }


    // for files and web pages
    public ReadIn(String charsetName, String s) {

        try {
            // first try to read file from local file system
            File file = new File(s);
            if (file.exists()) {
                scanner = new Scanner(file, charsetName);
                return;
            }

            // next try for files included in jar
            URL url = getClass().getResource(s);

            // or URL from web
            if (url == null) { url = new URL(s); }

            URLConnection site = url.openConnection();
            InputStream is     = site.getInputStream();
            scanner            = new Scanner(is, charsetName);
        } catch (IOException ioe) { System.err.println("Could not open " + s); }
    }

    // does input stream exists
    public boolean exists()  { return scanner != null; }

    // return true if only whitespace left
    public boolean isEmpty() { return !scanner.hasNext();  }

    public boolean hasNextLine() {
        boolean value = true;
        try                 { value = !scanner.hasNextLine(); }
        catch (Exception e) {                                 }
        return value;
    }

    // return next line, or null if no such line
    public String readLine() {
        String line = null;
        try                 { line = scanner.nextLine(); }
        catch (Exception e) {                            }
        return line;
    }

    // next character
    public char readChar() {
        // (?s) for DOTALL mode so . matches any character, including a line termination character
        // 1 says look only one character ahead
        // consider precompiling the pattern
        String s = scanner.findWithinHorizon("(?s).", 1);
        return s.charAt(0);
    }



    // return rest of input as string
    public String readAll() {
        if (!scanner.hasNextLine()) { return null; }

        // reference: http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return scanner.useDelimiter("\\A").next();
    }



    // next string, int, double, long, byte, boolean
    public String  readString()   { return scanner.next();        }
    public int     readInt()      { return scanner.nextInt();     }
    public double  readDouble()   { return scanner.nextDouble();  }
    public double  readFloat()    { return scanner.nextFloat();   }
    public long    readLong()     { return scanner.nextLong();    }
    public byte    readByte()     { return scanner.nextByte();    }

    // read in a boolean, allowing "true" or "1" for true and "false" or "0" for false
    public boolean readBoolean() {
        String s = readString();
        if (s.equalsIgnoreCase("true"))  return true;
        if (s.equalsIgnoreCase("false")) return false;
        if (s.equals("1"))               return true;
        if (s.equals("0"))               return false;
        throw new java.util.InputMismatchException();
    }


    // close the scanner
    public void close() { scanner.close();  }



    // for testing
    public static void main(String[] args) {
        ReadIn in;
        String s;

        // read from a URL
        System.out.println("readAll() from a URL");
        System.out.println("----------------------------------");
        in = new ReadIn("ISO-8859-1", "http://www.cs.princeton.edu/IntroCS/15inout/InTest.txt");
        System.out.println(in.readAll());

        // read one line at a time from URL
        System.out.println("readLine() from a URL");
        System.out.println("----------------------------------");
        in = new ReadIn("ISO-8859-1", "http://www.cs.princeton.edu/IntroCS/15inout/InTest.txt");
        while (!in.isEmpty()) {
            s = in.readLine();
            System.out.println(s);
        }
        System.out.println();

        // read one string at a time from URL
        System.out.println("readString() from a URL");
        System.out.println("----------------------------------");
        in = new ReadIn("ISO-8859-1", "http://www.cs.princeton.edu/IntroCS/15inout/InTest.txt");
        while (!in.isEmpty()) {
            s = in.readString();
            System.out.println(s);
        }
        System.out.println();


        // read one line at a time from file in current directory
        System.out.println("readLine() from current directory");
        System.out.println("----------------------------------");
        in = new ReadIn("ISO-8859-1", "./InTest.txt");
        while (!in.isEmpty()) {
            s = in.readLine();
            System.out.println(s);
        }
        System.out.println();


        // read one line at a time from file using relative path
        System.out.println("readLine() from relative path");
        System.out.println("----------------------------------");
        in = new ReadIn("ISO-8859-1", "../15inout/InTest.txt");
        while (!in.isEmpty()) {
            s = in.readLine();
            System.out.println(s);
        }
        System.out.println();

        // read one char at a time
        System.out.println("readChar() from file");
        System.out.println("----------------------------------");
        in = new ReadIn("ISO-8859-1", "InTest.txt");
        while (!in.isEmpty()) {
            char c = in.readChar();
            System.out.print(c);
        }
        System.out.println();
        System.out.println();

        // read one line at a time from absolute path
        System.out.println("readLine() from absolute path");
        System.out.println("----------------------------------");
        in = new ReadIn("ISO-8859-1", "/n/fs/csweb/introcs/15inout/InTest.txt");
        //// in = new In("G:\\www\\introcs\\15inout\\InTest.txt");  // Windows
        while (!in.isEmpty()) {
            s = in.readLine();
            System.out.println(s);
        }
        System.out.println();

    }

}