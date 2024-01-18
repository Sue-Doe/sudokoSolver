/**
* WaldnerMaxA1Q1
*
* COMP 2140 SECTION D01
* ASSIGNMENT Assignment 1, question 1
* @author Max Waldner, 7889322
* @version May 25, 2023
*
* PURPOSE: Library database to keep track of books added, loaned and returned to the library
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


class WaldnerMaxA1Q1 {
    

    //processes the file from user input
    public static void main(String[] args) {
        String line;
        String[] tokens;

        Library nlibrary = new Library();
        try {
            System.out.println("Please enter the input file name (.txt files only)");
            Scanner file = new Scanner(System.in);
            String fileName = file.nextLine();
            System.out.println();


            BufferedReader input;
            input = new BufferedReader(new FileReader(fileName));
            line = input.readLine();
            file.close();

            System.out.println("Processing file " + fileName +"...\n");


            while (line != null) {
                tokens = line.split(",");
                parseCommand(tokens, nlibrary);
                line = input.readLine();
                
            }

            input.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            
        }

        System.out.println("Program terminated normally.");

    }
    // processes the users file into commands
    public static void parseCommand(String[] tokens, Library lib) {

        //strips all whitespace
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].strip();
        }
        
        //sets unkown info about book to unkown
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].strip().equals("")) {
                tokens[i] = "unknown";
            }
        }

        //strips whitespace
        String[] command = tokens[0].split(" ");
        for (int i = 0; i < command.length; i++) {
            command[i] = command[i].strip();
        }
        
        //adds book to library
        if (command[0].equals("ADD")) {
            Book newBook = new Book(tokens[2], tokens[1], command[1]);
            lib.addBook(newBook);

        // search books by last name
        } else if (command[0].equals("SEARCHA")) {
            System.out.println("Books by " + command[1] +":" + lib.listByAuthor(command[1]));

        // tries rents book by all info
        } else if (command[0].equals("GETBOOK")) {

            if ( lib.loanBook(command[1]+", " + tokens[1] +", " + tokens[2])) {
                System.out.println("Book loaned:");
                System.out.println(command[1]+", " + tokens[1] +", " + tokens[2] + "\n");
            } else {
                System.out.println("Book already loaned.");
                System.out.println(command[1]+", " + tokens[1] +", " + tokens[2] + "\n");

            }

            //prints all books by title 
        }   else if (command[0].equals("SEARCHT")) {

            String title="";
            for (int i = 1; i < command.length; i++) {
                    title = title + command[i] + " ";
            }
            System.out.println("Books named " + title.strip()+":");
            System.out.println(lib.listByTitle(title) + "\n");



            //returns loaned book
        }   else if (command[0].equals("RETURNBOOK")) {
            //System.out.println("RETURN BOOK COMMAND");
            if (lib.returnBook(command[1]+", " + tokens[1] +", " + tokens[2])) {
                System.out.println("Book Returned.");

                System.out.println(command[1]+", " + tokens[1] +", " + tokens[2] +"\n");

            } else {
                System.out.println("Book is Already in library.");
            }
        }

        //prints if unknown command
        else {
            System.out.println("ERROR: Unknown command.");

        }

        


    }
}


// Creates book objects
class Book {
    private String title;
    private String firstName;
    private String lastName;
    private boolean rented;

    //constructor
    public Book(String title, String firstName, String lastName) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        rented = false;
    }

    //returns book title
    public String getTitle () {
        return title;
    }

    //returns Authors first name 
    public String getFirstName() {
        return firstName;
    }

    //returns Authers last name
    public String getLastName() {
        return lastName;
    }

    //checks if book is already rented
    public Boolean getRented() {
        return rented;
    }

    //rents book
    public void rent() {
        rented = true;
    }

    //returns book
    public void returnBook() {
        rented = false;
    }

    //prints book
    public String toString() {
        return lastName+", " + firstName+", " + title;
    }

}


//Library class to store books in array
class Library {

    private Book[] libName;
    private int size;

    //constructor
    public Library() {
        this.libName = new Book[50];
        this.size = 0;
    }


    //adds book to library
    public void addBook(Book nameBook) { 
        boolean added = false;

        //doubles book array if it fills up
        if (libName.length <= size) {
            Book[] newArr = new Book[size*2];
            for (int i = 0; i < libName.length; i++) {
                newArr[i] = libName[i];
            }
            this.libName = newArr;
        }


        //adds book to array
        for (int i = 0; i < libName.length; i++) {
            if ( libName[i] == null && !added) {
                libName[i] = nameBook;
                added = true;
                size++;
            } 
        }
        
        orderList(size);
    } 


    //orders book array by lastname firstname and then title
    private void orderList(int size) {

        int largest;
        Book temp;

        for (int i=0; i<size-1; i++) {
            largest = i;

            for (int j=largest+1; j<size; j++) {

                if ((libName[largest].toString()).compareTo((libName[j].toString())) > 0) {
                    largest = j; 
                }
            }
            temp = libName[i]; 
            libName[i] = libName[largest]; 
            libName[largest] = temp;
        }
    }
  

    //Creates string of books by author
    public String listByAuthor(String author) {
        String authorList = "\n";
        for (int i = 0; i < libName.length; i++) {
            if (libName[i] != null) {
                if (libName[i].getLastName().strip().equals(author.strip())) {
                    authorList = authorList + libName[i] + "\n";
                }
            }
        }
        return authorList;
    }


    //creates string of books by title
    public String listByTitle(String title) {
        String titleList = "";
        for (int i = 0; i < libName.length; i++) {
            if (libName[i] != null) {
                if (libName[i].getTitle().equals(title.strip())) {
                    titleList = titleList + libName[i] + "\n";
                }
            }
        }
        return titleList;

    }


    //loans out a book if availiable 
    public boolean loanBook(String title) {
    
        String[] tokens = splitter(title);

        for (int i = 0; i < libName.length; i++) {
            if (libName[i] != null) {
                if(libName[i].getLastName().equals(tokens[0])) {
                    if(libName[i].getFirstName().equals(tokens[1])) {
                        if(libName[i].getTitle().equals(tokens[2])){
                            if (!libName[i].getRented()) {
                                libName[i].rent();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    //returns book to library
    public boolean returnBook(String title) {
    
        String[] tokens = splitter(title);

        for (int i = 0; i < libName.length; i++) {
            if (libName[i] != null) {
                if(libName[i].getLastName().equals(tokens[0])) {
                    if(libName[i].getFirstName().equals(tokens[1])) {
                        if(libName[i].getTitle().equals(tokens[2])){
                            if (libName[i].getRented()) {
                                libName[i].returnBook();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    //formats the Strings into array and gets rid of white space
    private String[] splitter(String string) {
        String[] tokens = string.split(",");
        for(int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].strip();
        }

        return tokens;
    }


}
