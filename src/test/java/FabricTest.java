import ChaincodeIntegration.FabricConnection;
import ChaincodeIntegration.FabricUser;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.HFCAIdentity;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.UUID;

import static org.junit.Assert.*;

public class FabricTest {

    private FabricUser admin;
    private FabricConnection connection;

    @Test
    public void enrollAdmin() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, MalformedURLException, InvalidArgumentException, org.hyperledger.fabric.sdk.exception.InvalidArgumentException, EnrollmentException, CryptoException, ClassNotFoundException {
        admin = new FabricUser("admin", "adminpw", true);
        assertNotNull(admin.getEnrollment());
    }

    @Test (expected = EnrollmentException.class)
    public void enrollInvalidUser() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, MalformedURLException, InvalidArgumentException, org.hyperledger.fabric.sdk.exception.InvalidArgumentException, EnrollmentException, CryptoException, ClassNotFoundException {
        FabricUser badUser = new FabricUser("bad", "user", true);
        assertNull(badUser.getEnrollment());
    }

    @Test
    public void ConnectTest() throws InstantiationException, InvocationTargetException, NoSuchMethodException, MalformedURLException, InvalidArgumentException, org.hyperledger.fabric.sdk.exception.InvalidArgumentException, EnrollmentException, CryptoException, ClassNotFoundException, TransactionException, ProposalException, IllegalAccessException {
        if (admin == null)
            enrollAdmin();

        connection = new FabricConnection(admin.getCaClient());
        connection.connect(admin);
    }

    @Test
    public void registerNewUser() throws Exception {
        if (admin == null)
            enrollAdmin();

        // admin must be enrolled
        assertNotNull(admin.getEnrollment());
        HFCAClient caClient = admin.getCaClient();

        // this will be the random user we are creating
        String randomUser = UUID.randomUUID().toString();
        FabricUser newUser = new FabricUser(randomUser, "password", false);

        // user has not been enrolled yet
        assertNull(newUser.getEnrollment());

        // admin will register new user
        RegistrationRequest registrationRequest = new RegistrationRequest(newUser.getName());
        registrationRequest.setType("User");
        registrationRequest.setSecret(newUser.getSecret());

        // user is registered
        caClient.register(registrationRequest, admin);

        // let's enroll it
        newUser.setEnrollment(caClient.enroll(newUser.getName(), newUser.getSecret()));
        assertNotNull(newUser.getEnrollment());

        // lets leave the admin enrolled, just in case.
        admin.setEnrollment(caClient.enroll(admin.getName(), admin.getSecret()));
    }
}
