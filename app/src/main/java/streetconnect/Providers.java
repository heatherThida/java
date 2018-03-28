package streetconnect;

// Meant to display service providers in a ListView in the Organizations Fragment.
// Ignore for the time being.

public class Providers
{
    private String location; // ideally latitude + longitude coordinates
    private String type; // type of service provider: shelter, food kitchen, etc.
    private String shortDescription; // short description of provider
    private String name; // name of service provider

    // constructors
    public Providers(String location, String type, String shortDescription, String name)
    {
        this.location = location;
        this.type = type;
        this.shortDescription = shortDescription;
        this.name = name;
    }

    // basic getter functions
    public String getLocation()
    {
        return location;
    }

    public String getType()
    {
        return type;
    }

    public String getShortDescription()
    {
        return shortDescription;
    }

    public String getName()
    {
        return name;
    }

    // basic setter functions
    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}