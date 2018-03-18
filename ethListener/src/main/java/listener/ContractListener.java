package listener;

import ChaincodeIntegration.FabricConnection;
import ChaincodeIntegration.FabricUser;
import network.NetworkException;
import network.RinkebyNetwork;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

public class ContractListener {

    public void triggerChaincode() throws NetworkException, InterruptedException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, MalformedURLException, InvalidArgumentException, org.hyperledger.fabric.sdk.exception.InvalidArgumentException, EnrollmentException, CryptoException, ClassNotFoundException, TransactionException, ProposalException {
        FabricUser admin = new FabricUser("admin", "adminpw", true);

        FabricConnection fabricConnection = new FabricConnection(admin.getCaClient());
        fabricConnection.connect(admin);

        RinkebyNetwork network = new RinkebyNetwork();
        for (String address : network.getContractEvents()){
            System.out.println("Event detected for address " + address);
            fabricConnection.activateChaincode();
        }
    }

}
