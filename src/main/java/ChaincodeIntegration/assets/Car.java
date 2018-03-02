package ChaincodeIntegration.assets;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class Car implements Asset {
    private AssetMetadata assetMetadata;

    /**
     *
     * @param assetMetadata
     */
    public Car(CarMetadata assetMetadata) {
        this.assetMetadata = assetMetadata;
    }

    @Override
    public void setAssetMetadata(final AssetMetadata assetMetadata) {
        this.assetMetadata = assetMetadata;
    }

    @Override
    public String getId() {
        return Hashing.sha256().hashBytes(this.assetMetadata.serialize()).toString();
    }

    @Override
    public AssetMetadata getAssetMetadata() {
        return this.assetMetadata;
    }

    @Override
    public String[] getArgs() {
        int size = getAssetMetadata().getArgs().length+1;
        String[] args = new String[size];
        args[0] = getId();
        System.arraycopy(getAssetMetadata().getArgs(),0, args,1,size-1);
        return args;
    }
}
