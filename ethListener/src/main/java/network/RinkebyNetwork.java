package network;


import contracts.ETHBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;




/**
 * Created by rodrigo on 14/02/18.
 * Represents the Ethereum test network that will be used to connect and send transactions.
 */

public class Rinkeby {
    private static final Logger log = LoggerFactory.getLogger(Rinkeby.class);

    // the Bridge contract
    private ETHBridge contract;


    private Credentials ethCredentials;

    // my own infura network url !! Test Network !!
    private final String INFURA_NETWORK = "https://rinkeby.infura.io/iDxnNnHTWuNN9INbkvXv";

    // web3j client that will be used to connect and send transactions
    private Web3j web3j;
    private String clientVersion;

    /**
     * Connects to the Rinkeby test network.
     * @throws NetworkException
     */
    public void connect() throws NetworkException {
        web3j = Web3j.build(new HttpService(INFURA_NETWORK));

        // let's request the client version to make sure we are connected.
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3j.web3ClientVersion().send();
            this.clientVersion = web3ClientVersion.getWeb3ClientVersion();
            log.info("Connected to Rinkeby at " + this.clientVersion);
        } catch (IOException e) {
            this.clientVersion = null;
            log.error("Couldn't connect to Rinkeby!");
            throw new NetworkException("There was an error connecting to the ethereum blockchain");
        }
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
     * Loads from Eth blockchain our super contract
     * @param credentials the credentials that have eth balance to pay the fee
     */
    private void loadContract(Credentials credentials) throws NetworkException {
        // let's connect if we are not.
        if (this.clientVersion == null){
            this.connect();
        }

        // this token was published by me on tx 0x256775feb4e672012a3cf7b2fe502ca2ffad0a2f1fca921f7224ee33981ef2f4
        // https://rinkeby.etherscan.io/tx/0x256775feb4e672012a3cf7b2fe502ca2ffad0a2f1fca921f7224ee33981ef2f4
        contract = ETHBridge.load(ETHBridge.ADDRESS,this.web3j,credentials,ETHBridge.GAS_PRICE, ETHBridge.GAS_LIMIT);
        this.ethCredentials = credentials;
    }
}
