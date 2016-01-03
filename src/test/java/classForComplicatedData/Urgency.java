package classForComplicatedData;

/**
 * Created by dugancaii on 12/4/2014.
 */
public enum  Urgency {
    EMERGENCY (3),
    PRIOR (2),
    NORMAL (1),
    LEISURE (0);
    private final int urgency;
    private Urgency(int urgency) {
        this.urgency = urgency;
    }
    public int  getValue () {
        return  this.urgency;
    }
}
