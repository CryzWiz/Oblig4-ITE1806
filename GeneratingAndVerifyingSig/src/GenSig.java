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
            /* Get the private key from the vault 
             * 
             */
            PrivateKey pvt = getPrivate("TheVault", password);
            /* Get the public key from the vault
             * 
             */
            PublicKey pub = getPublic("TheVault", password);
  
            /* Create a Signature object and initialize it with the private key 
             */
            Signature rsa = Signature.getInstance("SHA256WITHRSA", "SunRsaSign");         
            rsa.initSign(pvt);
 
            /* Update and sign the data */
            FileInputStream fis = new FileInputStream(args[0]);
            BufferedInputStream bufin = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                rsa.update(buffer, 0, len);
                };
            bufin.close();
 
            /* Now that all the data to be signed has been read in, 
             * generate a signature for it 
             * */
            byte[] realSig = rsa.sign();
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
    /* Get the public key - shared method with VerSig.
     * 
     */
	public static PublicKey getPublic(String alias, char[] password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance("JKS");
		FileInputStream ksfis = new FileInputStream(keystorage);
		BufferedInputStream ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, password);
		return ks.getCertificate(alias).getPublicKey();
	}
	/* Get the private Key
	 * 
	 */
	private static PrivateKey getPrivate(String alias, char[] password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		KeyStore ks = KeyStore.getInstance("JKS");
		FileInputStream ksfis = new FileInputStream(keystorage);
		BufferedInputStream ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, password);
		return (PrivateKey) ks.getKey(alias, password);
	};
 
}

