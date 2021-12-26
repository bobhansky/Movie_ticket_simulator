package myMovie.run;

import myMovie.bean.Business;
import myMovie.bean.Customer;
import myMovie.bean.Movie;
import myMovie.bean.User;
import myMovie.bean.User.gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class MovieSystem {
    public static final Logger LOGGER = LoggerFactory.getLogger("MovieSystem.class");

    /**
     * define data collection to store data
     * 1. Users
     * 2. movies
     */



    // used for customers and business
    private static List<User> ALL_USERS = new ArrayList<>();

    // store business and its movies
    // can only be traced by String,   if you use object as key, it varies everytime you launch the program
    // while the hashcode or something stored in file doesn't match
    // then can't find the value
    private static Map<String,List<Movie>> ALL_MOVIES = new HashMap<>();


    // Scanner
    public static final Scanner sc = new Scanner(System.in);

    // store Login User, which makes many function convenient
    public static User loginUser;

    // time formatter:
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    // initialize and test data
    static {
        File f1 = new File("projects/storedFiles/users.txt");
        if(f1.length() != 0){
            readUsers();
        }

        File f2 = new File("projects/storedFiles/movies.txt");
        if(f2.length() != 0){
            readMovies();
        }

        // if files is empty, load test data
        if (ALL_USERS.size() == 0 && ALL_MOVIES.size() ==0 ) {
            Customer c = new Customer();
            c.setLoginName("zyf888");
            c.setPassword("123456");
            c.setGender(gender.MALE);
            c.setMoney(10000);
            c.setPhone("110110");
            ALL_USERS.add(c);

            Customer c1 = new Customer();
            c1.setLoginName("gzl888");
            c1.setPassword("123456");
            c1.setGender(gender.FEMALE);
            c1.setMoney(2000);
            c1.setPhone("111111");
            ALL_USERS.add(c1);

            Business b = new Business();
            b.setLoginName("baozugong888");
            b.setPassword("123456");
            b.setMoney(0);
            b.setGender(gender.MALE);
            b.setPhone("110110");
            b.setLocation("火星6号2B二层");
            b.setShopName("甜甜圈国际影城");
            ALL_USERS.add(b);
            // 注意，商家一定需要加入到店铺排片信息中去
            ALL_MOVIES.put(b.getLoginName(), new ArrayList<Movie>()); // b = []

            Business b2 = new Business();
            b2.setLoginName("baozupo888");
            b2.setPassword("123456");
            b2.setMoney(0);
            b2.setGender(gender.FEMALE);
            b2.setPhone("110110");
            b2.setLocation("火星8号8B八层");
            b2.setShopName("巧克力国际影城");
            ALL_USERS.add(b2);
            // 注意，商家一定需要加入到店铺排片信息中去
            ALL_MOVIES.put(b2.getLoginName() , new ArrayList<Movie>()); // b2 = []
        }

    }



    public static void main(String[] args) {
        // show main menu
        showMain();
    }

    //*********************************FUNCTION IMPLEMENTATION****************************

    /**
     * mainMenu
     */
    public static void showMain(){
        while (true) {
            System.out.println("----------------Movie Menu-----------------------");
            System.out.println("1.Login");
            System.out.println("2.Customer Create Account");
            System.out.println("3.Business Create Account");
            System.out.println("4.Quit");
            System.out.println("please enter your operation:");
            String command = sc.nextLine();
            switch(command){
                case "1":
                    login();
                    break;
                case "2":
                    createCus();
                    break;
                case "3":
                    createBus();
                    break;
                case "4":
                    System.out.println("See you!");
                    storeUsers();
                    storeMovies();
                    LOGGER.info("Files Saved");
                    return;
                default:
                    System.out.println("Wrong Operation Number! Please Enter Again: ");
            }
        }
    }

    private static void createBus() {
        System.out.println("--------------------Business Account Creation-------------------------");
        Business c = new Business();
        // username
        while (true) {
            System.out.println("Please enter the username:  quit to quit ");
            String uname = sc.nextLine();
            if (uname.equals("quit")) return;
            if (getUserByName(uname) != null) {
                System.out.println("This name is occupied, try again: ");
                continue;
            }
            c.setLoginName(uname);
            break;
        }
        // password
        while (true) {
            System.out.println("Please enter the password: ");
            String upas = sc.nextLine();

            System.out.println("Please confirm the password: ");
            if (upas.equals(sc.nextLine())) {
                c.setPassword(upas);
                break;
            }
            System.out.println("Two passwords don't match, try again");
        }

        // gender
        while (true) {
            System.out.println("Please enter gender: m/f ");
            String gen = sc.nextLine();

            if (gen.equals("m") || gen.equals("f")) {
                c.setGender(gen.equals("m") ? gender.MALE : gender.FEMALE);
                break;
            }
            System.out.println("m for male, f for female, please try again");
        }

        // phone and money:
        while (true) {
            System.out.println("please enter your phone number:  11 digit");
            String phone = sc.nextLine();
            if (phone.length() != 11) {
                System.out.println("wrong phone length, try again");
                continue;
            }
            c.setPhone(phone);

            double m = 0;
            System.out.println("please enter your money:  positive double");
            try{
                m = Double.parseDouble(sc.nextLine());
                if(m<0) throw new Exception();
            }catch (Exception e){
                System.out.println("please enter digits >= 0!");
                continue;
            }

            c.setMoney(m);
            break;
        }

        // Shop name
        while (true) {
            System.out.println("Please enter the Shopname:  quit to quit ");
            String uname = sc.nextLine();
            if (uname.equals("quit")) return;
            if (getBusinessByName(uname) != null) {
                System.out.println("This name is occupied, try again: ");
                continue;
            }
            c.setShopName(uname);
            break;
        }

        // Location
        while (true) {
            System.out.println("Please enter the location:  quit to quit ");
            String uname = sc.nextLine();
            if (uname.equals("quit")) return;
            boolean valid = true;
            for(User u: ALL_USERS){
                if(u instanceof Business && ((Business) u).getLocation().equals(uname)){
                    System.out.println("this location is oppupied!");
                    valid = false;
                    break;
                }
            }
            if(!valid) continue;
            c.setLocation(uname);
            break;
        }
        ALL_USERS.add(c);
        ALL_MOVIES.put(c.getLoginName(),new ArrayList<Movie>());
        System.out.println("Account Create Successfully!");
    }


    /**
     *  Customer Account Creation
     */
    private static void createCus() {
        System.out.println("--------------------Customer Account Creation-------------------------");
        Customer c = new Customer();
        // username
        while (true) {
            System.out.println("Please enter the username:  quit to quit ");
            String uname = sc.nextLine();
            if(uname.equals("quit")) return;
            if(getUserByName(uname) != null){
                System.out.println("This name is occupied, try again: ");
                continue;
            }
            c.setLoginName(uname);
            break;
        }
        // password
        while (true) {
            System.out.println("Please enter the password: ");
            String upas = sc.nextLine();

            System.out.println("Please confirm the password: ");
            if(upas.equals(sc.nextLine())){
                c.setPassword(upas);
                break;
            }
            System.out.println("Two passwords don't match, try again");
        }

        // gender
        while (true) {
            System.out.println("Please enter gender: m/f ");
            String gen = sc.nextLine();

            if(gen.equals("m") || gen.equals("f")){
                c.setGender(gen.equals("m")? gender.MALE : gender.FEMALE);
                break;
            }
            System.out.println("m for male, f for female, please try again");
        }

        // phone and money:
        while(true){
            System.out.println("please enter your phone number:  11 digit");
            String phone = sc.nextLine();
            if(phone.length()!=11){
                System.out.println("wrong phone length, try again");
                continue;
            }
            c.setPhone(phone);

            System.out.println("please enter your money:  positive double");
            String m = sc.nextLine();

            try{
                double mon = Double.parseDouble(m);
                if(mon < 0){
                    System.out.println("money can't be negative, try again");
                    continue;
                }

                c.setMoney(mon);
            } catch(Exception e){
                System.out.println("enter a valid number!");
                continue;
            }

            break;
        }

        // make this object to the User List
        ALL_USERS.add(c);
        LOGGER.info(c.getLoginName() + " Account Created");
        System.out.println("Account Create Successfully!");
    }


    /**
     * Login
     */
    private static void login() {
        User target = null;
        while (true) {
            System.out.println("Please enter the username: ");
            String uname = sc.nextLine();
            // find this user name
            if(uname.equals("quit")) return;
            target = getUserByName(uname);
            if(target == null){
                System.out.println("User not found, try again: ");
                continue;
            }
            break;
        }

        // check password
        while (true) {
            System.out.println("Please enter the password: ");
            String upas = sc.nextLine();
            if(upas.equals("quit")) return;
            // check if password is correct
            if(upas.equals(target.getPassword())) break;
            System.out.println("Wrong password, please try again: ");
        }
        loginUser = target;

        // login successfully and check the class of User
        if(target instanceof Business){
            LOGGER.info(loginUser.getLoginName() + " Login as Business");
            showBusinessMenu();
        }

        if(target instanceof  Customer){
            LOGGER.info(loginUser.getLoginName() + " Login as Customer");
            showCustomerMenu();
        }
        LOGGER.info(loginUser.getLoginName() + " Log Out");
    }


    /**
     * customer menu
     */
    private static void showCustomerMenu() {
        while (true) {
            System.out.println("-------------CustomerMain--------------");
            System.out.println("Hello, "+(loginUser.getGender()==gender.MALE ? "Mr." : "Ms.")+ loginUser.getLoginName());
            System.out.println("1.Show all movies information");
            System.out.println("2.Find movie information by name");
            System.out.println("3.Rating");
            System.out.println("4.Purchasing tickets");
            System.out.println("5.Show Your Money");
            System.out.println("6.Quit");
            System.out.println("Enter your operation: ");
            String command = sc.nextLine();
            switch(command){
                case "1":
                    showCusMovies();
                    break;
                case "2":
                    findMovieInfo();
                    break;
                case "3":

                    break;
                case "4":
                    // buy ticket
                    buyTicket();
                    break;
                case "5":
                    System.out.println("You have "+ loginUser.getMoney());
                    break;
                case "6":
                    return;
            }
        }
    }

    /**
     * find movie information by movie Name
     */
    private static void findMovieInfo() {
        while (true) {
            System.out.println("-----------------Find Movie Information-------------------");
            System.out.println("please Enter the movie name: quit to quit");
            String moname = sc.nextLine();
            if(moname.equals("quit")) return;

            if(getMovieByName(moname) == null){
                System.out.println("there's no such movie!");
                continue;
            }


            ALL_MOVIES.forEach((businessName, movies) -> {
                for(Movie m: movies) {
                    if (m.getName().contains(moname)) {
                        Business b = getBusinessByOwnerName(businessName);
                        System.out.println(b.getShopName() + "    " + b.getLocation());
                        System.out.println(m);
                    }
                }
            });
        }
    }

    /**
     * purchase ticket   customer
     */
    private static void buyTicket() {
        System.out.println("--------------------Purchase Ticket-------------------------");

        // get movie
        Movie targetMovie;
        Business theaterOwner;
        while(true){
            // which shop to watch the movie
            System.out.println("Enter the Shop name:   enter quit to quit");
            String shop = sc.nextLine();
            if(shop.equals("quit")) return;
            Business sp = getBusinessByName(shop);
            if(sp == null){
                System.out.println("no such theater, please enter again");
                continue;
            }

            theaterOwner = sp;
            System.out.println("Enter the movie name:   enter quit to quit");
            String movieName = sc.nextLine();
            if(movieName.equals("quit")) return;

            targetMovie = null;
            for(Movie m : ALL_MOVIES.get(sp.getLoginName())){
                if(m.getName().contains(movieName)){
                    targetMovie = m;
                }
            }

            if(targetMovie == null){
                System.out.println("there's no such movie");
                continue;
            }
            break;
        }

        while(true){
            System.out.println("Enter the number of ticket you want to buy:  quit to quit");
            System.out.println("There are "+targetMovie.getTickets()+" tickets left");
            String nums = sc.nextLine();
            int numbers;
            if(nums.equals("quit")) return;
            try{
                numbers = Integer.parseInt(nums);
            }catch(Exception e){
                System.out.println("please enter digits!");
                continue;
            }

            // not enough tickets
            if(targetMovie.getTickets() < numbers){
                System.out.println("Sorry, there's no such many tickets available!");
                continue;
            }

            double cost = BigDecimal.valueOf(targetMovie.getPrice()).multiply(BigDecimal.valueOf(numbers)).doubleValue();
            // not enough money
            if(loginUser.getMoney() < cost){
                System.out.println("Purchase fails: You don't have enough money!");
                continue;
            }
            // time passed
            // after movie is played, customers can't buy tickets
            Date validDate = targetMovie.getStart();
            if(validDate.before(new Date())){
                System.out.println("Purchase fails because the movie is already began!");
            }
            else{
                targetMovie.setTickets(targetMovie.getTickets() - numbers);
                loginUser.setMoney(loginUser.getMoney() - cost);
                theaterOwner.setMoney(theaterOwner.getMoney() + cost);
                System.out.println("Purchase Successfully:");
                System.out.println("Movie name: "+ targetMovie.getName());
                System.out.println("Tickts: "+ numbers);
                System.out.println("Cost: "+ cost);
                break;
            }
        }
    }


    /**
     * get business obejct by the shop name
     * @param shop the name
     * @return the business with this name
     */
    private static Business getBusinessByName(String shop) {
        for( User u: ALL_USERS) {
            if (u instanceof Business && ((Business) u).getShopName().contains(shop)) return (Business) u;
        }
        return null;
    }

    /**
     * get business object by ownner name
     */
    private static Business getBusinessByOwnerName(String ownerName) {
        for( User u: ALL_USERS) {
            if (u instanceof Business && ((Business) u).getLoginName().equals(ownerName)) return (Business) u;
        }
        return null;
    }

    /**
     * show all movies that is online   (customer)
     */
    private static void showCusMovies() {
/*        Set<Entry<Business,List<Movie>>> entry = ALL_MOVIES.entrySet();
        for(Entry<Business,List<Movie>> e : entry){
            System.out.println("Shop Name: "+ e.getKey().getShopName() + ", Location: "+e.getKey().getLocation());
            if(e.getValue().size() == 0) System.out.println("there's no movie in this shop!");
            else System.out.println(e.getValue());
        }*/
        System.out.println("--------------------Show All Shop and online Movies-----------------------");

        ALL_MOVIES.forEach((businessName, movies) -> {
            Business b = getBusinessByOwnerName(businessName);
            System.out.println("Shop Name: "+ b.getShopName() + ", Location: "+b.getLocation() +", Phone: "+ b.getPhone());
            if(movies == null || movies.size() == 0) System.out.println("there's no movie in this shop!");
            else System.out.println(movies);
        });
    }

    /**
     * business Menu
     */
    private static void showBusinessMenu() {
        while (true) {
            System.out.println("------------BusinessMain--------------");
            System.out.println("Hello, "+(loginUser.getGender()==gender.MALE ? "Mr." : "Ms.")+ loginUser.getLoginName());
            System.out.println("0.ShowMovieInformation");
            System.out.println("1.Add movie");
            System.out.println("2.Delete movie");
            System.out.println("3.Change movies information");
            System.out.println("4.Show income");
            System.out.println("5.Quit");
            System.out.println("Enter your operation: ");
            String command = sc.nextLine();
            switch(command){
                case "0":
                    showBusMovie();
                    break;
                case "1":
                    addMovie();
                    break;
                case "2":
                    deleteMovie();
                    break;
                case "3":
                    updateMovie();
                    break;
                case "4":
                    System.out.println("Total Money: "+ loginUser.getMoney());
                    break;
                case "5":
                    return;

            }
        }
    }


    /**
     * business Movie in this theater
     */
    private static void showBusMovie() {
        Business b = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(b.getLoginName());
        if(movies.size() == 0){
            System.out.println("There's no movies in this theater!");
            return;
        }
        for( Movie m:movies) System.out.println(m);
    }

    /**
     * update movie information
     */
    private static void updateMovie() {
        System.out.println("---------------------Update Movie Information ---------------------");
        Business buser = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(buser.getLoginName());
        if(movies == null || movies.size() == 0){
            System.out.println("no movies there");
            return;
        }

        while (true) {
            // choose the movie to offline
            System.out.println("enter the name of the movie to update: enter quit to quit");
            String name = sc.nextLine();

            if(name.equals("quit")) return;
            // check if there's a movie called this
            Movie tarMovie = getMovieByName(name);
            if(tarMovie != null) {
                System.out.println("enter the new name of the movie：");
                String newname  = sc.nextLine();
                System.out.println("enter the new director：");
                String newDire  = sc.nextLine();
                System.out.println("enter the new duration time：");
                String newtime  = sc.nextLine();
                System.out.println("enter the new ticket price：");
                String newprice  = sc.nextLine();
                System.out.println("enter the new tickets numbers：");
                String newtotalNumber  = sc.nextLine();


                tarMovie.setName(newname);
                tarMovie.setDirector(newDire);
                tarMovie.setTime(Double.parseDouble(newtime));
                tarMovie.setPrice(Double.parseDouble(newtime));
                tarMovie.setTickets(Integer.parseInt(newtotalNumber));

                // time
                while(true){
                    try{
                        System.out.println("enter the start time:");
                        String start  = sc.nextLine();
                        tarMovie.setStart(sdf.parse(start));
                        break;
                    }catch (Exception e){
                        System.out.println("something goes wrong on Time format!");
                    }
                }

                System.out.println("you update "+ tarMovie.getName()+ " successfully");
                return;
            }else{
                System.out.println("there's no such movie!");
            }
        }

    }

    /**
     *  make movie offline
     */
    private static void deleteMovie() {
        System.out.println("---------------------offline Movie---------------------");
        Business buser = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(buser.getLoginName());
        if(movies == null || movies.size() == 0){
            System.out.println("no movies there");
            return;
        }

        while (true) {
            // choose the movie to offline
            System.out.println("enter the name of the movie: enter quit to quit");
            String name = sc.nextLine();

            // check if there's a movie called this
            Movie tarMovie = getMovieByName(name);
            if(tarMovie != null) {
                movies.remove(tarMovie);
                System.out.println("you offline "+ tarMovie.getName()+ " successfully");
                return;
            }else{
                System.out.println("there's no such movie!");
            }
        }
    }

    /**
     * get movie by its na,e
     * @param n name of the movie
     * @return the movie
     */
    public static Movie getMovieByName(String n){
        Set<Entry<String,List<Movie>>> entrySet = ALL_MOVIES.entrySet();
        for(Entry<String,List<Movie>> entry : entrySet){
            for(Movie m : entry.getValue()){
                if(m.getName().contains(n)) return m;
            }
        }
        return null;
    }

    /**
     * make a movie online
     */
    private static void addMovie() {
        while (true) {
            try{
                System.out.println("enter the name of the movie:");
                String name  = sc.nextLine();
                System.out.println("enter the director:");
                String dire  = sc.nextLine();
                System.out.println("enter the  duration time:");
                String time  = sc.nextLine();
                System.out.println("enter the ticket price:");
                String price  = sc.nextLine();
                System.out.println("enter the tickets numbers:");
                String totalNumber  = sc.nextLine();
                System.out.println("enter the start time:  yyyy-MM-dd hh:mm");
                String start  = sc.nextLine();

                Movie m = new Movie(name,dire,0.0,Double.parseDouble(time),Double.parseDouble(price),Integer.parseInt(totalNumber),sdf.parse(start));
                Business b = (Business)loginUser;
                if(ALL_MOVIES.get(b.getLoginName()) == null){

                }
                ALL_MOVIES.get(b.getLoginName()).add(m);
                System.out.println("make "+name +" online successfully!");
                return;
            }catch (Exception e){
                System.out.println("something wrong with input format!");
            }
        }

    }


    /**
     * find User by username
     *
     * @param name the name
     * @return target user
     */
    private static User getUserByName(String name) {
        User res = null;
        for (User user : ALL_USERS) {
            if (user.getLoginName().equals(name)) {
                res = user;
                break;
            }
        }
        return res;
    }


    //******************** Stream func*******************

    /**
     *  store user to file from program
     */
    private static void storeUsers(){
        try(
                ObjectOutputStream os =
                        new ObjectOutputStream(new FileOutputStream("projects/storedFiles/users.txt"));
                ) {

            os.writeObject(ALL_USERS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  read users from files to program
     */
    private static void readUsers(){
        try(

                ObjectInputStream is =
                        new ObjectInputStream(new FileInputStream("projects/storedFiles/users.txt"));
        ) {

            ALL_USERS = (List<User>) is.readObject();
        } catch (Exception e) {
            System.out.println(" Reading Users goes wrong");
            e.printStackTrace();
        }
    }

    /**
     * store movies from program to files
     */
    private static void storeMovies(){
        try(
                ObjectOutputStream os =
                        new ObjectOutputStream(new FileOutputStream("projects/storedFiles/movies.txt"));
                ){

            os.writeObject(ALL_MOVIES);

        }catch(Exception e){
            System.out.println("something wrong with storing movies");
            e.printStackTrace();
        }
    }

    /**
     * read movies from files to program
     */
    private static void readMovies(){
        try(

                ObjectInputStream is =
                        new ObjectInputStream(new FileInputStream("projects/storedFiles/movies.txt"));
        ) {

            ALL_MOVIES = (Map<String,List<Movie>>) is.readObject();
        } catch (Exception e) {
            System.out.println(" Reading movies goes wrong");
            e.printStackTrace();
        }
    }

    //********************Stream func end****************
}




