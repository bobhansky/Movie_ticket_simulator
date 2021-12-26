package myMovie.bean;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String director;
    private double score;
    private double time;
    private double price;
    private int tickets;
    private Date start;

    public Movie(String name, String director, double score, double time, double price, int tickets, Date start) {
        this.name = name;
        this.director = director;
        this.score = score;
        this.time = time;
        this.price = price;
        this.tickets = tickets;
        this.start = start;
    }

    public Movie() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", director='" + director + '\'' +
                ", score=" + score +
                ", time=" + time +
                ", price=" + price +
                ", tickets=" + tickets +
                ", start=" + start +
                '}';
    }
}
