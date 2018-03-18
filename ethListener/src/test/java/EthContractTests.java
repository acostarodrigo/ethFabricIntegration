import contracts.ETHBridge;
import contracts.EventGenerator;
import listener.ContractListener;
import network.NetworkException;
import network.RinkebyNetwork;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import static org.junit.Assert.*;


public class EthContractTests {
    private RinkebyNetwork network;
    private Web3j web3j;
    private EventGenerator contract;

    /**
     * we need to connect to rinkeby blockchain to start tests
     * @throws NetworkException
     */
    @Test
    public void connectNetworkTest() throws NetworkException {
        network = new RinkebyNetwork();
        this.web3j = network.connect();

        assertNotNull(this.web3j);
        assertNotNull(network.getClientVersion());
    }

    /**
     * Let's make sure we can load the contract from the blockchain and that is valid
     * @throws NetworkException
     * @throws IOException
     */
    @Test
    public void loadContractTest() throws NetworkException, IOException {
        if (network == null)
            connectNetworkTest();


        TransactionManager transactionManager = new ClientTransactionManager(this.web3j, "");
        contract = EventGenerator.load(EventGenerator.ADDRESS,this.web3j,transactionManager,ETHBridge.GAS_PRICE, ETHBridge.GAS_LIMIT);
        assertNotNull(contract);

        contract.setContractAddress(EventGenerator.ADDRESS);
        assertFalse(contract.isValid());
    }

    @Test
    public void getContractBalance() throws IOException, NetworkException {
        if (contract == null)
            loadContractTest();

        EthGetBalance balance = web3j.ethGetBalance(contract.getContractAddress(), DefaultBlockParameterName.LATEST).send();
        assertNotNull(balance.getBalance());
    }

    @Test
    public void listenEvents() throws NetworkException, InterruptedException {
        if (network == null)
            connectNetworkTest();

        network.getContractEvents();
    }

    @Test
    public void getRequiredSignaturesTest() throws NetworkException {
        if (network == null)
            connectNetworkTest();
        int i = network.getRequiredSignatures();
        assertEquals(2,i);
    }

    @Test
    public void getAuthoritiesTest() throws Exception {
        if (network == null)
            connectNetworkTest();
        System.out.println(network.getAuthorities());
    }

    @Test
    public void triggerChaincodeTest() throws IllegalAccessException, InstantiationException, CryptoException, InvalidArgumentException, InvocationTargetException, NetworkException, MalformedURLException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException, ClassNotFoundException, ProposalException, NoSuchMethodException, TransactionException, EnrollmentException, InterruptedException {
        ContractListener listener = new ContractListener();
        listener.triggerChaincode();
    }


}
