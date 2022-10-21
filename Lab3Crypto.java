import java.util.stream.Stream;

class main {

    public enum CipherMode {
        ECB,
        CBC,
        CFB,
        OFB,
        CTR
    };

    public interface EncryptionMethod{
        // can be tweaked to set up an inverse "decrypt" function on the same interface,
        // once we're ready for that
        public long encrypt(long input, long key);

        public long decrypt(long input, long key);
    }

    public class BitRotationMethod implements EncryptionMethod{

        public BitRotationMethod(){}
        
        public long encrypt(long plaintext, long key){
            long convertedInput = rotateBits(plaintext);
            
            return convertedInput ^ key;
        }

        public long decrypt(long cyphertext, long key){
            long convertedInput = rotateLeft(cyphertext);

            return convertedInput ^ key;
        }

        private long rotateBits(long input){
            String keyString = Long.toBinaryString(input);
            keyString = "0".repeat(35 - keyString.length()).concat(keyString);
            keyString = keyString.substring(32).concat(keyString.substring(0, 32));
            return Long.valueOf(keyString, 2);
        }

        private long rotateLeft(long input){
            String keyString = Long.toBinaryString(input);
            keyString = "0".repeat(35 - keyString.length()).concat(keyString);
            keyString = keyString.substring(3, 35).concat(keyString.substring(0, 3));
            return Long.valueOf(keyString, 2);
        }
    }



    

    public interface BlockMode {
        public long encryptBlock(long input);
        
        // Add in a decryptBlock function as well
    }

    public class ECBMode implements BlockMode {
        long key;
        EncryptionMethod em;

        public ECBMode(long key, EncryptionMethod em){
            this.key = key;
            this.em = em;
        }

        public long encryptBlock(long input){
            return em.encrypt(input, key);
        }
    }

    public class CBCMode implements BlockMode {
        long previousResult;
        long key;
        EncryptionMethod em;

        public CBCMode(long initializationVector, long key, EncryptionMethod em){
            previousResult = initializationVector;
            this.key = key;
            this.em = em;
        }

        public long encryptBlock(long input){
            previousResult = em.encrypt(input ^ previousResult, key);
            return previousResult;
        }
    }

    public class CFBMode implements BlockMode {
        long previousResult;
        long key;
        EncryptionMethod em;

        public CFBMode(long initializationVector, long key, EncryptionMethod em){
            previousResult = initializationVector;
            this.key = key;
            this.em = em;
        }

        public long encryptBlock(long input){
            previousResult = input ^ em.encrypt(previousResult, key);
            return previousResult;
        }
    }

    public class OFBMode implements BlockMode {
        long lastBlock;
        long key;
        EncryptionMethod em;
        
        public OFBMode (long initializationVector, long key, EncryptionMethod em){
            lastBlock = initializationVector;
            this.key = key;
            this.em = em;
        }

        public long nextBlock(){
            lastBlock = em.encrypt(lastBlock, key);
            return lastBlock;
        }

        public long encryptBlock(long input){
            return nextBlock() ^ input;
        }
    }

    public class CTRMode implements BlockMode {
        long counter;
        long key;
        EncryptionMethod em;

        public CTRMode(long nonce, long key, EncryptionMethod em){
            counter = nonce * 65336; //65336 == 2^16, to add zeroes in binary
            this.key = key;
            this.em = em;
        }

        public long nextBlock() {
            long output = em.encrypt(counter, key);
            counter++;
            return output;
        }

        public long encryptBlock(long input){
            return nextBlock() ^ input;
        }
    }

    public static void main(String args[]) {
        
    }

    /*
    public long encrypt(String cyphertext, String key){
        return encrypt(cyphertext, parseASCII(key));
    }

    public long encrypt(String cyphertext, long key){
        long convertedInput = rotateBits(parseASCII(cyphertext));
        
        return convertedInput ^ key;
    }
    */

    private long parseASCII(String input){
        return Long.valueOf(Stream.of(input.toCharArray())
            .flatMap(c -> Stream.of((int) c))
            .flatMap(c -> Stream.of(Number.toBinaryString(c)))
            .flatMap(c -> Stream.of("0".repeat(7 - c.length()).concat(c)))
            .reduce("", (a,b) -> a.concat(b)), 2);
    }

    /*
    public long electronicCodeBookEncryption(long plaintext, long previousResult, long key){
        return encrypt(plaintext, key);
    }

    public long cipherBlockChainingEncryption(long plaintext, long previousResult, long key){
        return encrypt(plaintext ^ previousResult, key);
    }

    public long cipherFeedbackEncryption(long plaintext, long previousResult, long key){
        return plaintext ^ encrypt(previousResult, key);
    }

    public long outputFeedbackMode(long plaintext, OFBGenerator gen){
        return plaintext ^ gen.nextBlock();
    }

    public long counterMode(long plaintext, CTRGenerator gen){
        return plaintext ^ gen.nextBlock();
    }
    */



}