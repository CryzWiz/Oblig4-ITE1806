import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
 
class GenSig {
	public static String keystorage = "keystore";
    public static void main(String[] args) {
    	final char[] password= args[1].toCharArray();
        /* Generate a DSA signature */
 
        if (args.length != 2) {
            System.out.println("Usage: GenSig nameOfFileToSign password");
            }
        else try{
 
            /* Generate a key pair */
        	
            //KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        	KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "SunRsaSign");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
 
            keyGen.initialize(1024, random);
 
            PrivateKey pvt = getPrivate("TheVault", password);
            PublicKey pub = getPublic("TheVault", password);
 
 
            /* Create a Signature object and initialize it with the private key */
            
            //Signature dsa = Signature.getInstance("SHA1withDSA", "SUN"); 
            Signature dsa = Signature.getInstance("SHA256WITHRSA", "SunRsaSign");
            
            dsa.initSign(pvt);
 
            /* Update and sign the data */
 
            FileInputStream fis = new FileInputStream(args[0]);
            BufferedInputStream bufin = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                dsa.update(buffer, 0, len);
                };
 
            bufin.close();
 
            /* Now that all the data to be signed has been read in, 
                    generate a signature for it */
 
            byte[] realSig = dsa.sign();
 
         
            /* Save the signature in a file */
            FileOutputStream sigfos = new FileOutputStream("Allans-Signatur");
            sigfos.write(realSig);
 
            sigfos.close();
 
 
            /* Save the public key in a file */
            byte[] key = pub.getEncoded();
            FileOutputStream keyfos = new FileOutputStream("Allans-PubKey");
            keyfos.write(key);
 
            keyfos.close();
 
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
 
    }

	static PublicKey getPublic(String alias, char[] password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance("JKS");
		FileInputStream ksfis = new FileInputStream(keystorage);
		BufferedInputStream ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, password);
		return ks.getCertificate(alias).getPublicKey();
	}

	private static PrivateKey getPrivate(String alias, char[] password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		KeyStore ks = KeyStore.getInstance("JKS");
		FileInputStream ksfis = new FileInputStream(keystorage);
		BufferedInputStream ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, password);
		return (PrivateKey) ks.getKey(alias, password);
	};
 
}

