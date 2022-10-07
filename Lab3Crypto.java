class main {
    public static void main(String args[]) {
        //System.out.println(Byte.toString((byte) (6 ^ 2)));
        System.out.println(Long.toBinaryString(35));
        System.out.println(String.format("%035", Long.toBinaryString(35)));
    }

    public long encrypt(String cyphertext, long key){
        long convertedKey = rotateBits(key);
        //return stream(key.to)
        return null;
    }

    private long rotateBits(long key){
        String keyString = Long.toBinaryString(key);
        keyString = "0".repeat(35 - keyString.length()).concat(keyString);
        keyString = keyString.substring(32).concat(keyString.substring(0, 32));
        return Long.valueOf(keyString, 2);
    }

    private long parseASCI(String input){
        return Long.valueOf(Stream.of(input.toCharArray())
            .flatMap(c -> Stream.of((int) c))
            .flatMap(c -> Stream.of(Number.toBinaryString(c)))
            .flatMap(c -> Stream.of("0".repeat(5 - c.length()).concat(c)))
            .reduce("", (a,b) -> a.concat(b)), 2);
    }
}