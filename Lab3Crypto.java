class main {
    public static void main(String args[]){
        System.out.println()
    }

    public long encrypt(String cyphertext, String key){
        return encrypt(cyphertext, parseASCII(key));
    }

    public long encrypt(String cyphertext, long key){
        long convertedInput = rotateBits(parseASCII(cyphertext));
        
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
            .flatMap(c -> Stream.of("0".repeat(5 - c.length()).concat(c)))
            .reduce("", (a,b) -> a.concat(b)), 2);
    }


}