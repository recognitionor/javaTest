public class Main {
    public static int findFirstDigitIndex(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return i;
            }
        }
        return -1; // 숫자가 없을 경우 -1 반환
    }

    static boolean chkVersion(String verCondition) {
        boolean needShow;
        int mVerCode = 11;
        if (verCondition.equals("x")) {
            needShow = true;
        } else {
            int findFirstDigitIndex = findFirstDigitIndex(verCondition);
            String conditionStr = verCondition.substring(0, findFirstDigitIndex);
            String versionStr = verCondition.substring(findFirstDigitIndex(verCondition));
            try {
                switch (conditionStr) {
                    case "<=": {
                        needShow = mVerCode <= Float.parseFloat(versionStr);
                        break;
                    }
                    case ">=": {
                        needShow = mVerCode >= Float.parseFloat(versionStr);
                        break;
                    }
                    default: {
                        needShow = mVerCode == Float.parseFloat(versionStr);
                    }
                }
            }catch (Exception e) {
                needShow = false;
            }
        }
        return needShow;
    }

    public static void main(String[] args) {
        System.out.println(chkVersion("x"));
        System.out.println(chkVersion("=1.2"));
        System.out.println(chkVersion(">=1.4"));
        System.out.println(chkVersion("<=5.1"));
    }
}