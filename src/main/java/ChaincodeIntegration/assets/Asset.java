package ChaincodeIntegration.assets;

/**
 * An Asset is its metadata and the id (SHA256 of that metadata)
 */
public interface Asset {
    /**
     * The {@link AssetMetadata} is the information to be hashed from the asset. Are all the properties
     * that form the asset. It is encouraged that this is defined with the class constructor.
     * @param assetMetadata
     */
    public void setAssetMetadata(final AssetMetadata assetMetadata);

    /**
     * Gets the {@link AssetMetadata} defined for this asset.
     * Required to get the metadata that will be hashed to get the id of the asset
     * @return the {@link AssetMetadata} that forms the properties of the asset.
     */
    public AssetMetadata getAssetMetadata();

    /**
     * Id of the asset is the SHA256 of the {@link AssetMetadata} defined for this asset
     * @return a 64 int long string with the SHA256 of the {@link AssetMetadata}.toString() value
     */
    public String getId();

    /**
     * Information to be consumed on chaincode contracts.
     * @return the list of arguments that will be used when calling a chaincode contract.
     */
    public String[] getArgs();
}
