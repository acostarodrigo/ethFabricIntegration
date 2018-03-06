package BridgeIntegration.eth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class EthereumNetwork {
    private static final Logger log = LoggerFactory.getLogger(EthereumNetwork.class);

    private Credentials ethCredentials;

    // my own infura network url !! Test Network !!
    private final String INFURA_NETWORK = "https://rinkeby.infura.io/iDxnNnHTWuNN9INbkvXv";

    // web3j client that will be used to connect and send transactions
    private Web3j web3j;
    private String clientVersion;

    /**
     * Connects to the Rinkeby test network.
     * @throws ETHNetworkException
     */
    public void connect() throws ETHNetworkException {
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
            throw new ETHNetworkException("There was an error connecting to the ethereum blockchain");
        }
    }

    /**
     * retrieves the client version to wich we are connected, or null if we are not connected.
     * @return client version to the node we are connected to
     */
    public String getClientVersion() {
        return clientVersion;
    }
}
