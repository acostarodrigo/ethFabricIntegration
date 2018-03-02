package ChaincodeIntegration.assets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Base64;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

/**
 * assets.Asset type we are deploying
 */
public class CarMetadata implements AssetMetadata{
    private String make;
    private String model;
    private String color;
    private String owner;

    public CarMetadata(String make, String model, String color, String owner) {
        this.make = make;
        this.model = model;
        this.color = color;
        this.owner = owner;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String[] getArgs(){
        return new String[]{make,model,color,owner};
    }

    @Override
    public byte[] serialize()  {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream( baos );
            oos.writeObject(this);
            oos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            return this.toString().getBytes();
        }
    }
}