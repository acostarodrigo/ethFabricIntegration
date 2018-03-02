package ChaincodeIntegration.assets;

import java.io.Serializable;

/**
 * The asset metadata is all the asset information that needs to be hashed.
 * It is all the information that makes it unique.
 */
public interface AssetMetadata extends Serializable {
    /**
     * Serializes the metadata class so that it can be hashed to generate the ir
     * @return the serialized class.
     */
    public byte[] serialize();

    /**
     * Information to be consumed on chaincode contracts.
     * @return the list of arguments that will be used when calling a chaincode contract.
     */
    public String[] getArgs();
}
