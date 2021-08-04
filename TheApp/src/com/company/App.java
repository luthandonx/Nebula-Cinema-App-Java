package com.company;

import javax.naming.directory.SearchResult;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.ListView;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class App extends JPanel {
    Panel IntroPanel;
    Panel TicPanel;
    Panel PicturesPanel;
    Panel MoviePanel;
    Panel SearchPanel;
    Panel ResultsPanel;

    Label ClickMovielbl;

    Button BeginBtn;
    Button EnterMemberShipbtn;

    Label MovieNamelbl ;
    Label PatronIdlbl ;
    Label TicketNumberlbl;
    Label Discountlbl;
    Label Pricelbl;
    JSpinner NumberOfTickets ;
    Label MemberNamelbl;
    Button Paybtn;
    Button DeleteMemberShipbtn;
    Label Pointslbl;

    Label Movielbl;
    Label Directorlbl;
    Label Lengthlbl;
    Label Typelbl;
    Label RoomNumberlbl;
    Label AgeRestrictionlbl;
    Label Genrelbl;
    Label Languagelbl;

    double ImaxPrice = 150; double threeDPrice = 80 ; double twoDPrice = 60; double totalPrice; int discount;
    String ID;String TicketID; int Points;int RoomNumber;String MovieID; String MemberNum;String name;String MemberPatronID;
    String Movie;String MemberName;

    int PatronCounter;
    Label SearchLengthlbl;TextField inputlengthtxt;
    Label SearchDirectorlbl; TextField inputDirectortxt;
    Label SearchGenrelbl; TextField inputGenretxt;
    Label Searchlbl;Button Searchbtn;

    List Resultslst ;

    Pictures  [] pics = new Pictures[6];
    frames[] frame = new frames[6];
    Boolean member;
    Connection connection;

    Button CloseExit ;

    public App()
    {
        PatronCounter = 0;
        RoomNumber = 0;//just by default;
        Random randDiscount = new Random();
        discount = randDiscount.nextInt(15)+5;
        totalPrice = 0;
        this.setLayout(new GridLayout(3,2));
        IntroPanel = new Panel();
        PicturesPanel = new Panel();
        TicPanel = new Panel();
        MoviePanel = new Panel();
        SearchPanel = new Panel();
        ResultsPanel = new Panel();
        PicturesPanel.addMouseListener(new mouse());
        EstablishConnection();
        PlacePictures();
        Inputsetup();
        this.add(IntroPanel);

        PictureSetup();
        this.add(PicturesPanel);

        PicturesPanel.setVisible(false);
        TicketSetup();
        this.add(TicPanel);
        TicPanel.setVisible(false);
        MovieSetup();
        this.add(MoviePanel);
        MoviePanel.setVisible(false);
        SearchSetup();
        this.add(SearchPanel);
        SearchPanel.setVisible(false);
        SearchResults();
        this.add(ResultsPanel);
        ResultsPanel.setVisible(false);
    }

    void Inputsetup ()
    {
        Label introlbl = new Label("Welcome to Nebula Cinemas");
        BeginBtn = new Button("Begin");
        EnterMemberShipbtn = new Button("Enter MemberShip ID");
        ID = "Pat";
        ClickMovielbl = new Label();
        IntroPanel.setLayout(new GridLayout(5,1));
        IntroPanel.add(introlbl);
        IntroPanel.add(BeginBtn);
        IntroPanel.add(EnterMemberShipbtn);
        EnterMemberShipbtn.setVisible(false);
        EnterMemberShipbtn.addActionListener(new MemberShips());
        BeginBtn.addActionListener(new intro());
        IntroPanel.add(ClickMovielbl);



        //IntroPanel.setBackground(new Color(0,0,0));
    }
    public class intro implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String w ;
            boolean set = false;
            w= JOptionPane.showInputDialog("Are you a member Enter Y for Yes of N for No");
            try {
                Statement stmt = null;
                stmt = connection.createStatement();
                String qry = "Select * from Patron";
                ResultSet results = stmt.executeQuery(qry);
                while(results.next()){PatronCounter = PatronCounter + 1;}
               // System.out.println(PatronCounter+"");
                ID= "Pat"+PatronCounter;
                PatronIdlbl.setText("Patron ID : "+ID);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            if (w.contains("Y")| w.contains("y"))
            {
                member = true;set = true;
                PatronIdlbl.setText("Membership Number: ");
                DeleteMemberShipbtn.setVisible(true);
                Pointslbl.setVisible(true);
                EnterMemberShipbtn.setVisible(true);
            }
            else if (w.contains("N")| w.contains("n") )
            {
                member = false;set = true;
            }
            else
            {
                JFrame f= new JFrame();
                JOptionPane.showMessageDialog(f,"Please only enter N or Y","Error", JOptionPane.WARNING_MESSAGE );
            }
            if (set)
            {
                PicturesPanel.setVisible(true);
                TicPanel.setVisible(true);
                MoviePanel.setVisible(true);
                SearchPanel.setVisible(true);
                ResultsPanel.setVisible(true);
                ClickMovielbl.setText("Click the image of the movie you want to watch");
                BeginBtn.setVisible(false);
            }
        }
    }

    public class MemberShips extends Component implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            MemberNum = JOptionPane.showInputDialog("Enter MemberShip Code");
            try {
                Statement stmt = connection.createStatement();
                String qry = "Select * from Member where MemberNumber = \""+MemberNum+"\"";
                ResultSet results = stmt.executeQuery(qry);

                while(results.next())
                {
                    Pointslbl.setText("Points: "+results.getInt("Points"));Points = results.getInt("Points");
                    PatronIdlbl.setText("Membership : "+results.getString("MemberNumber"));
                    MemberPatronID = results.getString("PatronID");
                }

                qry = "Select * from Member , Patron where Member.PatronID = Patron.PatronID and Member.MemberNumber = \""+MemberNum+"\"";
                results = stmt.executeQuery(qry);
                while(results.next())
                {
                    MemberName = results.getString("Patron.Name");
                }
                MemberNamelbl.setVisible(true);
                MemberNamelbl.setText("Welcome Back: "+ MemberName);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                JOptionPane.showMessageDialog(this,"Sorry we could not find your details  try enter you ID again");
            }

        }
    }

    public class Pay implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int ticketcounter = 0;

            String qry = "";
            try {
                Statement stmt = connection.createStatement();
                qry = "Select * from Ticket";
                ResultSet results = stmt.executeQuery(qry);
                while(results.next()){ticketcounter=ticketcounter+1;}//count the number of tickects
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                JOptionPane.showMessageDialog(new JPanel() , "Something went wrong in counting tickets");
            }


            if (!member)
            {
                qry = "insert into Patron values(?,?,?)";
                try {
                    PreparedStatement prstmt = connection.prepareStatement(qry);
                    prstmt.setString(1,ID);
                    name = JOptionPane.showInputDialog("Enter your name");
                    prstmt.setString(2, name);
                    prstmt.setInt(3,RoomNumber);
                    int row = prstmt.executeUpdate();
                    System.out.println(row + " Patron Account has been created");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    JOptionPane.showMessageDialog(new JPanel() , "Something went wrong in inserting Patron");
                }

                qry = "insert into NonMember values (?,?)";

                try {
                    PreparedStatement ps = connection.prepareStatement(qry);
                    ps.setString(1,ID);
                    ps.setInt(2 ,discount);
                    int row = ps.executeUpdate();
                    System.out.println(row + " NonMember Account has been created");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            //Insert Details for the ticket
            TicketID = "Tick"+ticketcounter;
            String PatronID = ID;
            float price = (float) totalPrice;
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
            java.util.Date date = new java.util.Date();
            //dateformat.format(date);

            int seats = 0;
            String time = "";
            try {
                //Room

                qry = "Select * from room where RoomNumber = "+RoomNumber+"";
                Statement prestmt1 = connection.createStatement();
                ResultSet rs = prestmt1.executeQuery(qry);
                while(rs.next()){seats = rs.getInt("NumberOfSeats"); time = rs.getTime("OperationTime").toString();}
                seats = seats + (int)NumberOfTickets.getValue();
                qry = "Update room set NumberOfSeats = ? where RoomNumber = "+RoomNumber+"";
                PreparedStatement pstmt = connection.prepareStatement(qry);
                pstmt.setInt(1,seats);
                int rows = pstmt.executeUpdate();
                System.out.println(rows + " Room has been updated");


                // Ticket
                qry = "insert into Ticket values (?,?,?,?,?)";
                PreparedStatement prestmt = connection.prepareStatement(qry);
                prestmt.setString(1,TicketID);
                prestmt.setString(2,MovieID);
                if (member){prestmt.setString(3,MemberPatronID);}
                else{prestmt.setString(3,PatronID);;}
                prestmt.setFloat(4,price);
                prestmt.setDate(5, new java.sql.Date(date.getTime()));
                int row = prestmt.executeUpdate();
                System.out.println(row + "  Ticket has been added");
                }
            catch (SQLException throwables) {
                throwables.printStackTrace();
                JOptionPane.showMessageDialog(new JPanel() , "Something went wrong in inserting ticket");
            }

            if(member)
            {
                name = MemberName;
                Points = Points + 5;
                qry = "Update Member set Points = ? where MemberNumber = \""+MemberNum+"\"";
                try {
                    PreparedStatement ps = connection.prepareStatement(qry);
                    ps.setInt(1,Points);
                    int row = ps.executeUpdate();
                    System.out.println(row + " Member Account has been updated");
                    Resultslst.add("Points Now: "+Points);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    JOptionPane.showMessageDialog(new JPanel() , "Something went wrong updating members");
                }
            }
            Resultslst.removeAll();
            Resultslst.add("Welcome : "+ name);
            Resultslst.add("Movie: "+Movie);
            Resultslst.add("Go to Room : "+RoomNumber);
            Resultslst.add("Number of Tickets: "+ NumberOfTickets.getValue() + " Total: R"+totalPrice);
            Resultslst.add("Date: "+ date.toString());
            Resultslst.add("Movie Starts at : "+ time);

            if (member)
            {
                Resultslst.add("You have "+Points+" Points Now");
            }
        }
    }
    void TicketSetup()
    {
        TicPanel.setLayout(new GridLayout(9,2));
        MovieNamelbl = new Label("Movie: ");
        DeleteMemberShipbtn = new Button("Delete Membership");
        PatronIdlbl = new Label("Patron ID: "+ID);
        MemberNamelbl = new Label("Ticket Number");
        MemberNamelbl.setVisible(false);
        Discountlbl = new Label("Discount Awarded: "+discount+"%");
        NumberOfTickets = new JSpinner(new SpinnerNumberModel(1,1,20,1));
        int x = (int) NumberOfTickets.getValue();
        Pricelbl = new Label("Price: "+totalPrice);
        Paybtn = new Button("Pay");
        DeleteMemberShipbtn.setVisible(false);
        DeleteMemberShipbtn.addActionListener(new deleteMemberShip());
        Pointslbl = new Label("Points: ");
        Pointslbl.setVisible(false);
        TicPanel.add(Pointslbl);
        Paybtn.addActionListener(new Pay());
        TicPanel.add(MovieNamelbl);TicPanel.add(PatronIdlbl);TicPanel.add(MemberNamelbl);
        TicPanel.add(Discountlbl);TicPanel.add(NumberOfTickets);TicPanel.add(Pricelbl);
        TicPanel.add(Paybtn); TicPanel.add(DeleteMemberShipbtn);
    }

    void PictureSetup()
    {
        PicturesPanel.setLayout(new GridLayout(2,3));
        for (int i = 0 ; i <= 5 ; i++)
        {
           PicturesPanel.add(new frames(pics[i]));
        }

    }

    void MovieSetup()
    {
         Movielbl = new Label();
         Directorlbl = new Label();
         Lengthlbl = new Label();
         Typelbl= new Label();
         RoomNumberlbl= new Label();
         AgeRestrictionlbl= new Label();
         Genrelbl= new Label();
         Languagelbl = new Label();

         MoviePanel.setLayout(new GridLayout(4 , 2));
         MoviePanel.add(Movielbl);MoviePanel.add(Lengthlbl);
         MoviePanel.add(Directorlbl);MoviePanel.add(Genrelbl);
         MoviePanel.add(RoomNumberlbl);MoviePanel.add(Typelbl);
         MoviePanel.add(AgeRestrictionlbl);MoviePanel.add(Languagelbl);
    }

    public class deleteMemberShip implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String w = JOptionPane.showInputDialog("Are you sure you want to delete your account y|Y for yes and any other key for no ");
            if (w.contains("Y")| w.contains("y"))
            {
                String qry = "delete from Member where MemberNumber = \""+MemberNum+"\"";
                try {
                    Statement stmt = connection.createStatement();
                    int row = stmt.executeUpdate(qry);
                    System.out.println(row +" Accounts have been deleted");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    JOptionPane.showMessageDialog(new Panel(),"Had trouble deleting account","Error", JOptionPane.WARNING_MESSAGE );
                }
                Resultslst.removeAll();
                Resultslst.add("Your account has been deleted succesfully");
            }
        }
    }
    
    void SearchSetup()
    {
        SearchLengthlbl = new Label("Enter Movie Name");
        inputlengthtxt = new TextField();
        SearchDirectorlbl= new Label("Search Director");
        inputDirectortxt = new TextField();
        SearchGenrelbl= new Label("Search Genre");
        inputGenretxt = new TextField();
        Searchlbl= new Label("Press to Search");
        Searchbtn= new Button ("Search");
        Searchbtn.addActionListener(new Search());

         SearchPanel.setLayout(new GridLayout(2,4));
         SearchPanel.add(SearchLengthlbl);SearchPanel.add(inputlengthtxt);
         SearchPanel.add(SearchDirectorlbl);SearchPanel.add(inputDirectortxt);
         SearchPanel.add(SearchGenrelbl); SearchPanel.add(inputGenretxt);
         SearchPanel.add(Searchlbl);SearchPanel.add(Searchbtn);
    }
    public class Search implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Resultslst.removeAll();
            String genre = inputGenretxt.getText();
            String director = inputDirectortxt.getText();
            String movie = inputlengthtxt.getText();
            String qry = "";
            qry = "Select * from Movie where ";
            int x = 0 ;
            if (movie.length() > 1 ) {
                 qry = qry + " Name Like \"%"+movie+"%\"";
                 x = + 1;
            }
            if (director.length()> 1)
            {
                if (x != 0){qry = qry + " and ";}
                qry = qry + " Director Like \"%"+director+"%\"";
                x = x +1;
            }
            if (genre.length()> 1)
            {
                if (x != 0){qry = qry + " and ";}
                qry = qry + "Genre Like \""+genre+"%\"";
            }

            System.out.println(qry);

           if (x > 0)
           {
               try {
                   Statement stmt = connection.createStatement();
                   ResultSet rs = stmt.executeQuery(qry);
                   while(rs.next())
                   {
                       Resultslst.add(rs.getString("Name"));
                   }
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               }
           }
        }
    }


    void SearchResults() {
            Resultslst = new List();
            ResultsPanel.setLayout(new BorderLayout());
            ResultsPanel.add(Resultslst, BorderLayout.CENTER);
            CloseExit = new Button("Exit");
            CloseExit.addActionListener(new exit());
            ResultsPanel.add(CloseExit, BorderLayout.SOUTH);
        }

        void EstablishConnection() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Nebula", "root", "password");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                System.out.println("Something is up");
            }
        }

        void PlacePictures() {
            try {
                Statement state = connection.createStatement();
                String qry = "Select * from Movie";
                ResultSet results = state.executeQuery(qry);
                int counter = 0;
                while (results.next()) {
                    String name = results.getString("Name");
                    // System.out.println(name);
                    pics[counter] = new Pictures(name);
                    counter = counter + 1;
                }
            } catch (SQLException s) {
                System.out.println("Something is wrong with PlacePictures");
            }
        }

        String GetMouseXY(int x, int y) {
            if ((x > 0) & (x < 101) & (y > 0) & (y < 101)) {
                return "Ghost in a shell(1995)";
            }
            if ((x > 130) & (x < 240) & (y > 0) & (y < 101)) {
                return "Laurance Anyways(2012)";
            }
            if ((x > 260) & (x < 370) & (y > 0) & (y < 151)) {
                return "Lost in Translation(2003)";
            }
            if ((x > 0) & (x < 101) & (y > 110) & (y < 210)) {
                return "Pulp Fiction(1994)";
            }
            if ((x > 130) & (x < 240) & (y > 110) & (y < 210)) {
                return "Predestination(2014)";
            }
            if ((x > 260) & (x < 370) & (y > 110) & (y < 210)) {
                return "The Revenant(2015)";
            } else {
                return "";
            }
        }

        public class mouse implements MouseListener {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int x = mouseEvent.getX();
                int y = mouseEvent.getY();
                String name = GetMouseXY(x, y);
                //System.out.println(name);
                String Type = "";
                if (name.length() > 2) {
                    try {
                        Statement state = connection.createStatement();
                        String qry = "Select * from Movie where Name = \"" + name + "\"";
                        ResultSet results = state.executeQuery(qry);
                        while (results.next()) {
                            MovieID = results.getString("MovieID");
                            Movielbl.setText("Name: " + results.getString("Name"));
                            Movie = results.getString("Name");
                            Directorlbl.setText("Director: " + results.getString("Director"));
                            Lengthlbl.setText("Length_Minutes: " + results.getInt("Length_Minutes"));
                            Genrelbl.setText("Genre: " + results.getString("Genre"));
                            AgeRestrictionlbl.setText("AgeRestriction: " + results.getString("AgeRestriction"));
                            Typelbl.setText(results.getString("Type"));
                            Type = results.getString("Type");
                            RoomNumberlbl.setText("RoomNumber: " + results.getInt("RoomNumber"));
                            RoomNumber = results.getInt("RoomNumber");
                            Languagelbl.setText("Language: " + results.getString("Language"));
                            MovieNamelbl.setText("Movie: " + results.getString("Name"));
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        JOptionPane.showMessageDialog(new JPanel(), "Something went wrong in getting Movie Details ");
                    }

                    totalPrice = 0;
                    if (Type.contains("3")) {
                        totalPrice = totalPrice + threeDPrice;
                    }
                    if (Type.contains("2")) {
                        totalPrice = totalPrice + twoDPrice;
                    } else {
                        totalPrice = totalPrice + ImaxPrice;
                    }
                    int n = (int) NumberOfTickets.getValue();
                    totalPrice = totalPrice * n - (totalPrice * n * ((double)discount / 100));
                    Pricelbl.setText("Price: R" + totalPrice);
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        }

        public class exit implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        }
}

