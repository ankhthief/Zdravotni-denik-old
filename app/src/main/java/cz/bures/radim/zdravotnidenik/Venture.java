package cz.bures.radim.zdravotnidenik;

public class Venture {

    private long id;
    private String name_of_venture;
    private String place_of_venture;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName_of_venture() {
        return name_of_venture;
    }

    public void setName_of_venture(String name_of_venture) {
        this.name_of_venture = name_of_venture;
    }

    public String getPlace_of_venture() {
        return place_of_venture;
    }

    public void setPlace_of_venture(String place_of_venture) {
        this.place_of_venture = place_of_venture;
    }
}
