package deu.cse.spring_webmail.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private static final String PATTERNS = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*^&+=])(?=\\S+$).{8,}$";

    public static String validatePassword(String password) {
        Pattern pattern = Pattern.compile(PATTERNS);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("입력된 비밀번호는 정해진 규칙에 위반하였습니다. 아래는 문제점입니다. \\n");
            if (password.length() < 8) {
                errorMessage.append("비밀번호는 최소 8자 이상이어야 합니다. \\n");
            }

            if (!password.matches(".*[0-9].*")) {
                errorMessage.append("비밀번호에는 숫자가 포함되어야 합니다. \\n");
            }

            if (!password.matches(".*[a-z].*")) {
                errorMessage.append("비밀번호에는 소문자가 포함되어야 합니다. \\n");
            }

            if (!password.matches(".*[A-Z].*")) {
                errorMessage.append("비밀번호에는 대문자가 포함되어야 합니다. \\n");
            }

            if (!password.matches(".*[@#$%^*&+=].*")) {
                errorMessage.append("비밀번호에는 특수문자가 포함되어야 합니다. \\n");
            }

            if (password.matches(".*\\s.*")) {
                errorMessage.append("비밀번호에는 공백문자가 포함되지 않아야 합니다. \\n");
            }

            return errorMessage.toString().trim();
        }

        return null;
    }
}
