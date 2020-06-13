package sample;

import com.sun.jmx.snmp.daemon.CommunicatorServer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.*;
import java.util.*;

public class GamePlayController {
    ArrayList<Country> countries;
    ArrayList<Button> countriesBtns;
    double multiplier = 1.2;
    int days = 0;
    boolean play = true;
    boolean isInfected = false;
    boolean flightsBlocked = false;
    boolean abroadingBlocked = false;
    String countryIdOnFocus = "wm_back";
    int worldPopulation;
    int worldInfected;
    int worldDeaths;
    @FXML
    private Label scoreDays;
    @FXML
    private AnchorPane wm_back;
    @FXML
    private Label populationLbl;
    @FXML
    private Label infectedLbl;
    @FXML
    private Label deathsLbl;
    @FXML
    private TitledPane statsTlt;
    @FXML
    private ImageView planeImg;
    @FXML
    private ImageView wallImg;
    @FXML
    public void initialize() {
        countries = new ArrayList<>();
//        try {
//            FileOutputStream f = null;
//            ObjectOutputStream o = null;
//            f = new FileOutputStream("countries.bin");
//            o = new ObjectOutputStream(f);
//            for (Country c : countries) {
//                o.writeObject(c);
//            }
//            o.close();
//            f.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            e.getCause();
//        } //writing
        try {
            FileInputStream f = null;
            ObjectInputStream o = null;
            f = new FileInputStream("countries.bin");
            o = new ObjectInputStream(f);
            Country tmp = (Country)o.readObject();
            while (tmp != null) {
                countries.add(tmp);
                tmp = (Country)o.readObject();
            }
            o.close();
            f.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } //reading
        for (Country c : countries) {
            System.out.println(c);
            for (Object neighbour : c.neighbours) {
                System.out.print("\t- ");
                System.out.println(neighbour);
            }
        }
//         displaying all countries

        countriesBtns = getAllNodes(wm_back);
        new Thread() {
            public void run() {
                while(play){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        public void run() {
                            if(isInfected)
                            scoreDays.setText("Days: " + days++);
                            worldPopulation = 0; worldInfected = 0; worldDeaths = 0;

                            for(Country c : countries) {
                                if (c.infected * multiplier >= c.population.amountInThousand)
                                    c.infected = c.population.amountInThousand;
                                else if(c.infected < 0) c.infected = 0;
                                else
                                    c.infected *= multiplier;
                                if (c.infected > 0) {
                                    int redColor = (int) (((double) c.infected / (double) (ePopulationSize.getMaxAmount(c.population).amountInThousand)) * 255);
                                    redColor = redColor < 0 ? 0 : (redColor > 255 ? 255 : redColor);
                                    int greenColor = (0x80 - redColor);
                                    greenColor = greenColor < 0 ? 0 : (greenColor > 255 ? 255 : greenColor);
                                    Color hex = new Color(redColor, greenColor, 0);
                                    String colorTohex = "#"+Integer.toHexString(hex.getRGB()).substring(2);
                                    countriesBtns.stream().filter(b -> b.getId().equals(c.id)).findFirst().get().setStyle("-fx-background-color: " + colorTohex);

                                    double chance = (double) ((double)c.infected / (double)c.population.amountInThousand);
                                    double maxChance = (double) ((double)c.infected / (double)ePopulationSize.getMaxAmount(c.population).amountInThousand);
                                    double rand = Math.random();
                                    if(rand < chance*4.0d) //umieranie
                                    {
                                        double amount = (double)c.infected * ((double)new Random().nextInt(3)/100.0d) + 1.0d;
                                        if(c.name.equals("Grenlandia")) System.out.println("umiera " +(int)amount +" osob");
                                        c.infected -= (int)amount;
                                        c.deaths += (int)amount;
                                        c.population.amountInThousand -= (int)amount;
                                    }
                                    if (!flightsBlocked && (rand < 0.01 + maxChance/4.0d) && (c.infected >= 10)){ //powietrzne
                                        int index = new Random().nextInt(countries.size());
                                        System.out.println(c.name +" powietrzne -> "+ countries.get(index).name);
                                        countries.get(index).infected += 11;
                                        countries.get(index).population.amountInThousand += 11;
//                                        countries.stream().filter(co -> co.id.equals(c.id)).findFirst().get().infected -= 10;
//                                        countries.stream().filter(co -> co.id.equals(c.id)).findFirst().get().population.amountInThousand -= 10;
                                        c.infected -= 10;
                                        c.population.amountInThousand -= 10;
                                    }
                                    else if (!abroadingBlocked && (rand < 0.05 + maxChance) && (c.neighbours.size() != 0) && (c.infected >= 10)) { //ladowe
                                        int index = new Random().nextInt(c.neighbours.size());
                                        System.out.println(c.name +" ladowe -> "+ c.neighbours.stream().skip(index).findFirst().get().name);
                                        c.neighbours.stream().skip(index).findFirst().get().infected += 12;
                                        c.neighbours.stream().skip(index).findFirst().get().population.amountInThousand += 12;
//                                        countries.stream().filter(co -> co.id.equals(c.id)).findFirst().get().neighbours.stream().skip(index).findFirst().get().infected += 10;
//                                        countries.stream().filter(co -> co.id.equals(c.id)).findFirst().get().neighbours.stream().skip(index).findFirst().get().population.amountInThousand += 10;
//                                        countries.stream().filter(co -> co.id.equals(c.id)).findFirst().get().infected -= 10;
//                                        countries.stream().filter(co -> co.id.equals(c.id)).findFirst().get().population.amountInThousand -= 10;
                                        c.infected -= 10;
                                        c.population.amountInThousand -= 10;
                                    }

                                }
                                worldPopulation += c.population.amountInThousand; worldInfected += c.infected; worldDeaths += c.deaths;
                            }

                            if(countryIdOnFocus.length() == 2){
                                statsTlt.setText(String.valueOf(countries.stream().filter(c -> c.id.equals(countryIdOnFocus)).findFirst().get().name));
                                populationLbl.setText(String.valueOf(countries.stream().filter(c -> c.id.equals(countryIdOnFocus)).findFirst().get().population.amountInThousand));
                                infectedLbl.setText(String.valueOf(countries.stream().filter(c -> c.id.equals(countryIdOnFocus)).findFirst().get().infected));
                                deathsLbl.setText(String.valueOf(countries.stream().filter(c -> c.id.equals(countryIdOnFocus)).findFirst().get().deaths));
                            }
                            else{
                                statsTlt.setText("World");
                                populationLbl.setText(String.valueOf(worldPopulation));
                                infectedLbl.setText(String.valueOf(worldInfected));
                                deathsLbl.setText(String.valueOf(worldDeaths));
                            }

                        }
                    });
                }
            }
        }.start();
    }

    @FXML
    public void worldClicked(Event event){
        Node target = (Node)event.getSource();
        if(target.getId().length() <= 2)
        {
            if(!isInfected){
                countries.stream().filter(c -> c.id.equals(target.getId())).findFirst().get().infected += 9;
                isInfected = true;
            }
            countryIdOnFocus = countries.stream().filter(c -> c.id.equals(target.getId())).findFirst().get().id;
            statsTlt.setText(String.valueOf(countries.stream().filter(c -> c.id.equals(countryIdOnFocus)).findFirst().get().name));
            populationLbl.setText(String.valueOf(countries.stream().filter(c -> c.id.equals(countryIdOnFocus)).findFirst().get().population.amountInThousand));
            infectedLbl.setText(String.valueOf(countries.stream().filter(c -> c.id.equals(countryIdOnFocus)).findFirst().get().infected));
            deathsLbl.setText(String.valueOf(countries.stream().filter(c -> c.id.equals(countryIdOnFocus)).findFirst().get().deaths));
        }
        else{
            countryIdOnFocus = "wm_back";
            statsTlt.setText("World");
            populationLbl.setText(String.valueOf(worldPopulation));
            infectedLbl.setText(String.valueOf(worldInfected));
            deathsLbl.setText(String.valueOf(worldDeaths));
            scoreDays.requestFocus();
        }
    }

    @FXML
    public void flightsBlocking(){
        flightsBlocked = !flightsBlocked;
        if(flightsBlocked)
            planeImg.setEffect(new BoxBlur(8, 8, 1));
        else
            planeImg.setEffect(new BoxBlur(0, 0, 1));
    }

    @FXML
    public void abroadingBlocking(){
        abroadingBlocked = !abroadingBlocked;
        if(abroadingBlocked)
            wallImg.setEffect(new BoxBlur(8, 8, 1));
        else
            wallImg.setEffect(new BoxBlur(0, 0, 1));
    }

    private final Object PAUSE_KEY = new Object();

    public static ArrayList<Button> getAllNodes(Parent root) {
        ArrayList<Button> nodes = new ArrayList<Button>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Button> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if(node.getId() != null && node.getId().length() == 2)
            nodes.add((Button)node);
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }
}
