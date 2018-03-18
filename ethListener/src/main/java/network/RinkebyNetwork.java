package network;


import contracts.EventGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;


/**
 * Created by rodrigo on 14/02/18.
 * Represents the Ethereum test network that will be used to connect and send transactions.
 */

public class RinkebyNetwork {
    private static final Logger log = LoggerFactory.getLogger(RinkebyNetwork.class);

    // the Bridge contract
    private EventGenerator contract;


    private TransactionManager transactionManager;
    // the ETH address we are using to perform calls.
    private final String PROGRAM_ADDRESS = "0x0284B395EceFBbAb8d90b294c8dE7C5DD1E410a2";

    // my own infura network url !! Test Network !!
    private final String INFURA_NETWORK = "https://rinkeby.infura.io/iDxnNnHTWuNN9INbkvXv";


    // web3j client that will be used to connect and send transactions
    private Web3j web3j;
    private String clientVersion;

    /**
     * Connects to the RinkebyNetwork test network.
     * @throws NetworkException
     */
    public Web3j connect() throws NetworkException {
        web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));

        // let's request the client version to make sure we are connected.
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3j.web3ClientVersion().send();
            this.clientVersion = web3ClientVersion.getWeb3ClientVersion();
            log.info("Connected to RinkebyNetwork at " + this.clientVersion);
        } catch (IOException e) {
            this.clientVersion = null;
            log.error("Couldn't connect to RinkebyNetwork!");
            throw new NetworkException("There was an error connecting to the ethereum blockchain. ",e);
        }

        return web3j;
    }

    /**
     * retrieves the client version to wich we are connected, or null if we are not connected.
     * @return client version to the node we are connected to
     */
    public String getClientVersion() {
        return clientVersion;
    }

    /**
     * gets the eth balance of the passed address
     * @param address the address to check the balance for
     * @return the big integer eth this address has
     * @throws IOException
     */
    public BigInteger getBalanceForAddress(String address) throws IOException, NetworkException {
        connect();
        EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        return balance.getBalance();

    }

    /**
     * Loads the contract from the blockchain
     * @return
     */
    public EventGenerator loadContract(){
        if (contract != null)
            return contract;

        //transactionManager = new ReadonlyTransactionManager(this.web3j, this.PROGRAM_ADDRESS);
        transactionManager = new ClientTransactionManager(this.web3j, this.PROGRAM_ADDRESS);
        contract = EventGenerator.load(EventGenerator.ADDRESS, this.web3j, this.transactionManager,EventGenerator.GAS_PRICE, EventGenerator.GAS_LIMIT);

        return contract;
    }


    public ArrayList<String> getContractEvents() throws InterruptedException, NetworkException {
        this.getReady();
        ArrayList<String> addresses = new ArrayList<>();

        contract.depositEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST).subscribe((event -> addresses.add(event.recipient)),error -> System.out.println("Error: " + error.toString() ));

        return addresses;
    }

    /**
     * gets the amount of required signatures specified for the contract at creation time
     * We know this value, but we still will get it from blockchain.
     * @return the amount of required signatures to be able to withdrawn.
     */
    public int getRequiredSignatures() throws NetworkException {
        // let's make sure the contract is loaded.
        this.getReady();

        BigInteger requiredSignatures = BigInteger.ZERO;
        try {
            requiredSignatures = contract.requiredSignatures().send();
        } catch (Exception e) {
            return 0;
        }
        return requiredSignatures.intValue();
    }

    /**
     * connects and loads the contract, if necesary.
     * @throws NetworkException
     */
    private void getReady() throws NetworkException {
        if (web3j == null)
            this.connect();

        if (contract == null)
            this.loadContract();
    }

    public String getAuthorities() throws Exception {
        this.getReady();

        //return contract.requiredSignatures().send().toString();
        //return contract.authorities(BigInteger.ZERO).send()
        return contract.getAuthority(BigInteger.ZERO).send();

    }
}
