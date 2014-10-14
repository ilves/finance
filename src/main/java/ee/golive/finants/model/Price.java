package ee.golive.finants.model;

import javax.persistence.*;

@Entity
@Table(name="prices")
public class Price {

    @Id
    private String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

}
