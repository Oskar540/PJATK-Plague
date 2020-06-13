package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Country implements Externalizable {
    public String name;
    public String id;
    public ePopulationSize population;
    public Set<Country> neighbours;
    public int infected = 0;
    public int deaths = 0;
    public int posX;
    public int posY;

    public Country() {
        name = "";
        id = "";
        population = ePopulationSize.medium;
        neighbours = null;
        infected = 0;
        deaths = 0;
        posX = 0;
        posY = 0;
    }
    public Country(String name, ePopulationSize population){
        this.name = name;
        this.population = population;
        this.neighbours = new HashSet<Country>();
    }
    public Country(String name, String id, ePopulationSize population){
        this.name = name;
        this.id = id;
        this.population = population;
        this.neighbours = new HashSet<Country>();
    }
    public Country(String name, ePopulationSize population, int posX, int posY){
        this.name = name;
        this.population = population;
        this.neighbours = new HashSet<Country>();
        this.posX = posX;
        this.posY = posY;
    }
    public Country(String name, ePopulationSize population, ArrayList<Country> neighbours){
        this.name = name;
        this.population = population;
    }
    public Country(String name, ePopulationSize population, int posX, int posY, Set neighbours){
        this.name = name;
        this.population = population;
        this.neighbours = neighbours;
        this.posX = posX;
        this.posY = posY;
    }
    public Country(String name, String id, ePopulationSize population, Set neighbours, int infected, int deaths, int posX, int posY) {
        this.name = name;
        this.id = id;
        this.population = population;
        this.neighbours = neighbours;
        this.infected = infected;
        this.deaths = deaths;
        this.posX = posX;
        this.posY = posY;
    }
    @Override public int hashCode( ) {
        return id.hashCode( );
    }

    public void setNeighbours(Set<Country> neighbours)
    {
        this.neighbours = neighbours;
    }
    public void setNeighbours(Country neighbour)
    {
        this.neighbours.add(neighbour);
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeObject(id);
        out.writeObject(population);
        out.writeObject(neighbours);
        out.writeInt(infected);
        out.writeInt(deaths);
        out.writeInt(posX);
        out.writeInt(posY);
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        id = (String) in.readObject();
        population = (ePopulationSize) in.readObject();
        neighbours = (Set<Country>) in.readObject();
        infected = in.readInt();
        deaths = in.readInt();
        posX = in.readInt();
        posY = in.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return id.equals(country.id);
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    //in controller:
//    FileOutputStream f = new FileOutputStream("countries.txt");
//    ObjectOutputStream o = new ObjectOutputStream(f);
//    Country c = new Country();
//    o.writeObject(c);
//    o.close();
//    f.close();
//    Country c2 = (Country)o.readObject();
}
