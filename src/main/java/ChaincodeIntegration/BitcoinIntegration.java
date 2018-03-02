package ChaincodeIntegration;

import ChaincodeIntegration.assets.Car;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;

public class BitcoinIntegration {
    private Channel channel;
    private HFClient client;
    private FabricUser user;
    private CryptoSuite cs;
    private final HFCAClient caClient;

    private final String ordererURL = "grpc://localhost:7050";
    private final String peerURL = "grpc://localhost:7051";

    public BitcoinIntegration(HFCAClient caclient){
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

    public ArrayList<Car> getCars() throws ProposalException, InvalidArgumentException {
        ArrayList<Car> carList = new ArrayList<>();
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

        req.setChaincodeID(cid);

        req.setFcn("queryAllCars");
        req.setArgs(new String[] { ""});
        Collection<ProposalResponse> resps = channel.queryByChaincode(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            System.out.println("response: " + payload);
        }
        return null;
    }

    public void changeCarOwner(Car car, String newOwner) throws InvalidArgumentException, ProposalException {
        TransactionProposalRequest req = client.newTransactionProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("bitcoin").build();
        req.setChaincodeID(cid);
        req.setFcn("changeCarOwner");
        req.setArgs(new String[] { car.getId(), newOwner });
        System.out.println("Executing for " + car.getId());
        Collection<ProposalResponse> resps = channel.sendTransactionProposal(req);

        channel.sendTransaction(resps);
    }
}
