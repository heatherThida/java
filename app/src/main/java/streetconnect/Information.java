package streetconnect;

// This is a class I made to represent basic types of information that might be used in a notification.
// This class is flexible and can/will be altered depending on the types of information available in a notification.
// In my envisioning of the notification system, we'd put the notifications online in JSON format,
// then retrieve them in-app via HTTPGet, and parse through them. When a relevant notification for the user is found,
// the details inside will be loaded into an instance of the Information class. These instances would then be funnelled into
// a ListView to display in the Notifications fragment.

// This class currently isn't used. It's in the same boat as CustomAdapter. They were used for testing and now shouldn't be needed again.s

public class Information {

    private String location; // ideally latitude + longitude coordinates
    private String type; // type of service to be provided, similar to category
    private String shortDescription; // short description of service
    private String id; // maybe we'd like to implement some sort of numbering for notifications
    private String providerName; // name of service provider
    private String time; // time notification was received

    // constructors
    // initializes everything (pass in null if not initialized here?)
    public Information(String providerName, String location, String type, String shortDescription, String id, String time)
    {
        this.providerName = providerName;
        this.location = location;
        this.type = type;
        this.shortDescription = shortDescription;
        this.id = id;
        this.time = time;
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

    public String getId()
    {
        return id;
    }

    public String getProviderName()
    {
        return providerName;
    }

    public String getTime()
    {
        return time;
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

    public void setId(String id)
    {
        this.id = id;
    }

    public void setProviderName(String providerName)
    {
        this.providerName = providerName;
    }

    public void setTime(String time)
    {
        this.time = time;
    }
}
