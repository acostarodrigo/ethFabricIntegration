import BridgeIntegration.eth.ETHNetworkException;
import BridgeIntegration.eth.EthereumNetwork;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class EthereumNetworkTest {

    @Test
    public void connectToRinkeby() throws ETHNetworkException {
        EthereumNetwork network = new EthereumNetwork();
        network.connect();

        assertNotNull(network.getClientVersion());
    }
}
