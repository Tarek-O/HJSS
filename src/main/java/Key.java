import java.util.ArrayList;
import java.util.Random;

public class Key {

    private final int keyLength = 8;
    private ArrayList<String> listOfPreviousKeys;
    public Key() {
        listOfPreviousKeys = new ArrayList<String>();
    }

    public String generateUniqueKey(){
        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder(keyLength);
        for(int i = 0; i < keyLength; i++) {
            if(random.nextInt(2) == 0){
                stringBuilder.append(random.nextInt(10));
            }else {
                stringBuilder.append((char)(random.nextInt(26) + 'A'));
            }
        }

        if(listOfPreviousKeys.contains(stringBuilder.toString())){
            return generateUniqueKey();
        }
        listOfPreviousKeys.add(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public void setListOfPreviousKeys(ArrayList<String> listOfPreviousKeys){
        this.listOfPreviousKeys = listOfPreviousKeys;
    }
}
