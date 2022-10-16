class main {

    public enum CipherMode {
        ECB,
        CBC,
        CFB,
        OFB,
        CTR
    };

    public class OFBGenerator {
        long lastBlock;
        long key;
        
        public OFBGenerator (long initializationVector, long key){
            lastBlock = initializationVector;
            this.key = key;
        }

        public long nextBlock(){
            lastBlock = encrypt(lastBlock, key);
            return lastBlock;
        }
    }

    public class CTRGenerator {
        long counter;
        long key;

        public CTRGenerator(long nonce, long key){
            counter = nonce * 65336; //65336 == 2^16, to add zeroes in binary
            this.key = key;
        }

        public long nextBlock() {
            output = encrypt(counter, key);
            counter++;
            return output;
        }
    }

    public static void main(String args[]) {
        
    }

    public long encrypt(String cyphertext, String key){
        return encrypt(cyphertext, parseASCII(key));
    }

    public long encrypt(String cyphertext, long key){
        long convertedInput = rotateBits(parseASCII(cyphertext));
        
        return convertedInput ^ key;
    }

    public long encrypt(long cyphertext, long key){
        long convertedInput = rotateBits(cyphertext);
        
        return convertedInput ^ key;
    }

    public long rotateLeft(long key){
        String keyString = Long.toBinaryString(key);
        keyString = "0".repeat(35 - keyString.length()).concat(keyString);
        keyString = keyString.substring(3, 35).concat(keyString.substring(0, 3));
        return Long.valueOf(keyString, 2);
    }

    private long rotateBits(long key){
        String keyString = Long.toBinaryString(key);
        keyString = "0".repeat(35 - keyString.length()).concat(keyString);
        keyString = keyString.substring(32).concat(keyString.substring(0, 32));
        return Long.valueOf(keyString, 2);
    }

    private long parseASCII(String input){
        return Long.valueOf(Stream.of(input.toCharArray())
            .flatMap(c -> Stream.of((int) c))
            .flatMap(c -> Stream.of(Number.toBinaryString(c)))
            .flatMap(c -> Stream.of("0".repeat(7 - c.length()).concat(c)))
            .reduce("", (a,b) -> a.concat(b)), 2);
    }

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



}