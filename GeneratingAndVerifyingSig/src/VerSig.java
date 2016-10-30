import java.io.*;
import java.security.*;
import java.security.spec.*;
 
class VerSig {
 
    public static void main(String[] args) {
 
        /* Verify a DSA signature */
 
        if (args.length != 4) {
            System.out.println("Usage: VerSig alias password signaturefile datafile");
            }
        else try{
        	String alias = args[0];
        	String pw = args[1];
        	char[] password = pw.toCharArray();
        	String signaturefile = args[2];
        	String datafile = args[3];
        	
            /* import encoded public key */
        	KeyStore ks = KeyStore.getInstance("JKS");
    		FileInputStream ksfis = new FileInputStream(args[0]);
    		BufferedInputStream ksbufin = new BufferedInputStream(ksfis);
    		ks.load(ksbufin, password);
    		KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
    		PublicKey pubKey = keyFactory.generatePublic((KeySpec) ks.getCertificate(alias).getPublicKey());
 
           // X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(ks.getCertificate(alias).getPublicKey());
            
            //KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            //KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
            //PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
            
            
            
            
            
            /* input the signature bytes */
            FileInputStream sigfis = new FileInputStream(args[2]);
            byte[] sigToVerify = new byte[sigfis.available()]; 
            sigfis.read(sigToVerify );
 
            sigfis.close();
 
            /* create a Signature object and initialize it with the public key */
            //Signature sig = Signature.getInstance("SHA1WITHDSA", "SUN");
            Signature sig = Signature.getInstance("SHA256WITHRSA", "SunRsaSign");
            sig.initVerify(pubKey);
 
            /* Update and verify the data */
 
            FileInputStream datafis = new FileInputStream(args[3]);
            BufferedInputStream bufin = new BufferedInputStream(datafis);
 
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                sig.update(buffer, 0, len);
                };
 
            bufin.close();
 
 
            boolean verifies = sig.verify(sigToVerify);
 
            System.out.println("signature verifies: " + verifies);
 
 
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
};
 
    }
 
}
