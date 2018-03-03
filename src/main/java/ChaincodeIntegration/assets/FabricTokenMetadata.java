package ChaincodeIntegration.assets;

public class FabricTokenMetadata implements AssetMetadata{
    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public String[] getArgs() {
        return new String[0];
    }
}
