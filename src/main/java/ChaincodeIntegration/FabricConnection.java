package ChaincodeIntegration;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.net.MalformedURLException;

public class FabricConnection {
    private Channel channel;
    private HFClient client;
    private FabricUser user;
    private CryptoSuite cs;
    private final HFCAClient caClient;

    private final String ordererURL = "grpc://localhost:7050";
    private final String peerURL = "grpc://localhost:7051";

    public FabricConnection(HFCAClient caclient){
        this.caClient = caclient;
        client = HFClient.createNewInstance();
        cs = caclient.getCryptoSuite();
    }

    public void connect(FabricUser user) throws MalformedURLException, CryptoException, InvalidArgumentException, TransactionException {
        //cs.init();
        client.setCryptoSuite(cs);

        caClient.setCryptoSuite(cs);

        client.setUserContext(user);

        // Instantiate channel
        channel = client.newChannel("mychannel");
        Peer peer0 = client.newPeer("peer0.org1.example.com", peerURL);

        channel.addPeer(peer0);
        // It always wants orderer, otherwise even query does not work
        channel.addOrderer(client.newOrderer("orderer.example.com", ordererURL));
        channel.initialize();
    }
}
