import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.*;
 
class VerSig {
	public static String keystorage = "keystore";
    public static void main(String[] args) {
 
        /* Verify a RSA signature */
        if (args.length != 4) {
            System.out.println("Usage: VerSig alias password signaturefile datafile");
            }
        else try{
        	String pw = args[1];
        	char[] password = pw.toCharArray();
        	String signaturefile = args[2];
        	String datafile = args[3];
        	
        	/* import encoded public key - Using GenSigs method for public key
        	 * Since this is for just me, I can have it just like this.
        	 * If not you'll need a public method for this VerSig and supply 
        	 * the password for you key storage
        	 */
            PublicKey pubKey = GenSig.getPublic(args[0], password);
            
            /* input the signature bytes */
            FileInputStream sigfis = new FileInputStream(signaturefile);
            byte[] sigToVerify = new byte[sigfis.available()]; 
            sigfis.read(sigToVerify );
            sigfis.close();
            
            /* create a Signature object and initialize it with the public key */
            //Signature sig = Signature.getInstance("SHA1WITHDSA", "SUN");
            Signature sig = Signature.getInstance("SHA256WITHRSA", "SunRsaSign");
            sig.initVerify(pubKey);
 
            /* Update and verify the data */
            FileInputStream datafis = new FileInputStream(datafile);
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
