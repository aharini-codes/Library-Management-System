package com.library;

import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {

    static final String URL = "jdbc:mysql://localhost:3306/library";
    static final String USER = "root";
    static final String PASSWORD = "root123";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. View Books");
            System.out.println("2. Add Book");
            System.out.println("3. Add Member");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1: viewBooks(); break;
                case 2: addBook(sc); break;
                case 3: addMember(sc); break;
                case 4: issueBook(sc); break;
                case 5: returnBook(sc); break;
                case 6: System.exit(0);
                default: System.out.println("Invalid choice!");
            }
        }
    }

    static void viewBooks() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {

            while (rs.next()) {
            	System.out.println(
            		    "ID: " + rs.getInt("book_id") +
            		    " | Title: " + rs.getString("title") +
            		    " | Copies: " + rs.getInt("available_copies")
            		);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void addBook(Scanner sc) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

            sc.nextLine();
            System.out.print("Title: ");
            String title = sc.nextLine();

            System.out.print("Author: ");
            String author = sc.nextLine();

            System.out.print("Genre: ");
            String genre = sc.nextLine();

            System.out.print("Year: ");
            int year = sc.nextInt();

            System.out.print("Copies: ");
            int copies = sc.nextInt();

            String query = "INSERT INTO books (title, author, genre, published_year, available_copies) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, genre);
            ps.setInt(4, year);
            ps.setInt(5, copies);

            ps.executeUpdate();
            System.out.println("Book added!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void addMember(Scanner sc) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

            sc.nextLine();
            System.out.print("First Name: ");
            String fn = sc.nextLine();

            System.out.print("Last Name: ");
            String ln = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Phone: ");
            String phone = sc.nextLine();

            System.out.print("Address: ");
            String addr = sc.nextLine();

            String query = "INSERT INTO members (first_name, last_name, email, phone_number, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, fn);
            ps.setString(2, ln);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, addr);

            ps.executeUpdate();
            System.out.println("Member added!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void issueBook(Scanner sc) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

            System.out.print("Member ID: ");
            int mid = sc.nextInt();

            System.out.print("Book ID: ");
            int bid = sc.nextInt();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO borrowing_records (member_id, book_id, borrow_date) VALUES (?, ?, CURDATE())");
            ps.setInt(1, mid);
            ps.setInt(2, bid);
            ps.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE books SET available_copies = available_copies - 1 WHERE book_id=?");
            ps2.setInt(1, bid);
            ps2.executeUpdate();

            System.out.println("Book issued!");

        } catch (Exception e) {
            System.out.println("Error issuing book!");
        }
    }

    static void returnBook(Scanner sc) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

            System.out.print("Member ID: ");
            int mid = sc.nextInt();

            System.out.print("Book ID: ");
            int bid = sc.nextInt();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE borrowing_records SET return_date=CURDATE() WHERE member_id=? AND book_id=? AND return_date IS NULL");
            ps.setInt(1, mid);
            ps.setInt(2, bid);
            ps.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE books SET available_copies = available_copies + 1 WHERE book_id=?");
            ps2.setInt(1, bid);
            ps2.executeUpdate();

            System.out.println("Book returned!");

        } catch (Exception e) {
            System.out.println("Error returning book!");
        }
    }
}
