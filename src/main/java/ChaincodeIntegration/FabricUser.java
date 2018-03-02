package ChaincodeIntegration;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

public class FabricUser implements User {
    private final String caURL = "http://localhost:7054";
    private String name;
    private HFCAClient caClient;
    private Enrollment enrollment;

    private String mspId;
    private String secret;


    public FabricUser(String name, String secret) throws EnrollmentException, InvalidArgumentException, MalformedURLException, IllegalAccessException, InvocationTargetException, org.hyperledger.fabric.sdk.exception.InvalidArgumentException, InstantiationException, NoSuchMethodException, CryptoException, ClassNotFoundException {
        caClient = HFCAClient.createNewInstance("ca.example.com",caURL, null);
        caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

        this.name = name;
        this.mspId = "Org1MSP";
        this.secret = secret;

        this.enrollment = caClient.enroll(this.name, this.secret);
    }

    public String getName() {
        return this.name;
    }

    public Set<String> getRoles() {
        return new HashSet<String>();
    }


    public String getAccount() {
        return "";
    }


    public String getAffiliation() {
        return "";
    }


    public Enrollment getEnrollment() {
        return this.enrollment;

    }

    public String getMspId() {
        return this.mspId;
    }

    public HFCAClient getCaClient() {
        return caClient;
    }
}
