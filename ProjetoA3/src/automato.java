import java.util.Stack;

public class automato {
    public static void main(String[] args) {
        // Todos as formulas dentro do exemplo devem ser aceitas
        String exemplo = "(24 / 1) * 9 + 3; 10 - 4; (1 * 3 + 4 / 5 - 2) +100 ; 10 - 4 + 12; (4 + 4) / 4; 5+5;";

        // String usada para testar erros
        //String exemplo = "(1+1) + (); 4+4+a; 10/2+; (2+7-1; 2++2; 22; a";

        // Retira todos os espacos em branco
        String espaco = exemplo.replaceAll(" ", "").trim();

        // Divide as string pelos ";" e coloca em um array
        String[] exemploSeparado = espaco.split(";");

        // for para checar o se a string passa por todos os metodos de checagem
        for (int i = 0; i < exemploSeparado.length; i++) {

            if (automatoN(exemploSeparado[i]) == true) {
                if (validaParenteses(exemploSeparado[i]) == true) {
                    // System.out.println("Parenteses balanceados");
                    if (entreParentese(exemploSeparado[i]) == true) {
                        System.out.println(eval(exemploSeparado[i]));
                    }
                } else {
                    System.out.println("Parentese não balanceados");
                }
            }
            // System.out.println(validaParenteses(exemplo));
            System.out.println("");
        }

    }

    // Cria o automato para checar o texto
    public static Boolean automatoN(String texto) {
        Boolean resultado = false;
        String regraNumero = "\\d*"; // impede numeros
        String regraSinal = "[*+-/]"; // permite sinais
        String regraGrande = "^(?!.*([*+-/]){2})([0-9._\\-\\+\\*\\/\\(\\)]*)$";// impede dois sinais
        String regraLetra = "[a-zA-Z]"; // impede letra
        StringBuilder estadoAtual = new StringBuilder();

        for (int i = 0; i < texto.length(); i++) {
            // unico é a entrado do texto transfomada em char
            char unico = texto.charAt(i);
            // solo é unico mas em formato String para usar os metodos
            String solo = String.valueOf(unico);
            // estadoTotalString é o estadoAtual em forma de string
            String estadoTotalString = estadoAtual.toString();

            if (solo.matches(regraLetra)) {
                resultado = false;
                System.out.println("letras nao sao perimitidas");
                break;
            }

            if (solo.equals("(") || solo.equals(")")) {
                estadoAtual.append(solo);
            }

            // regra para aceitar numeros
            if (solo.matches(regraNumero)) {
                estadoAtual.append(solo);
                resultado = true;

                // regra que so se aplicam no fim do loop
                if (i == texto.length() - 1 && estadoTotalString.matches("\\d*")) {
                    resultado = false;
                    System.out.println("equalcoes nao podem contar apenas numeros");
                    break;
                }
            }

            // regra para aceitar sinais
            if (solo.matches(regraSinal)) {
                estadoAtual.append(solo);
                resultado = true;

                // Regra que só se aplica ao fim do texto se o ultimo digito for um sinal
                if (i == texto.length() - 1 && solo.matches(regraSinal)) {
                    resultado = false;
                    System.out.println("equacoes nao podem terminar ou comecar com sinal");
                    break;

                }

            }
            if (!estadoTotalString.matches(regraGrande)) {
                resultado = false;
                System.out.println("Nao e possivel ter dois sinais seguidos");
                break;
            }

            // printa que char esta sendo avalidado no momento
            // System.out.println(estadoAtual.toString());
        }
        return resultado;
    }

    // Cria um funcao que valida os parenteses de uma exprecao
    public static Boolean validaParenteses(String expression) {
        int i, length;
        char ch;
        Boolean resultado = false;

        Stack<Character> stack = new Stack<Character>();
        length = expression.length();

        for (i = 0; i < length; i++) {
            ch = expression.charAt(i);
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                if (stack.isEmpty() || stack.pop() != '(') {
                    return resultado;
                }
            }

        }
        if (stack.isEmpty()) {
            return resultado = true;
        }
        return resultado = false;
    }

    // funcao que verifica os dados dentro do parenteses
    public static Boolean entreParentese(String texto) {
        String regra1 = "^(?!.*\\(\\d*\\))([0-9._\\-\\+\\*\\/\\(\\) ]*)$";
        String regra2 = "^(?!.*\\(([*+-/])\\))([0-9._\\-\\+\\*\\/\\(\\) ]*)$";
        String regra3 = "^(?!.*\\d*([*+-/])\\))([0-9._\\-\\+\\*\\/\\(\\) ]*)$";
        Boolean resultado = false;

        if (texto.matches("()")) {
            resultado = false;
            System.out.println("Não é permitdo parenteses vazios");
            return resultado;
        }

        if (texto.matches(regra1)) {
            if (texto.matches(regra2)) {
                if (texto.matches(regra3)) {
                    // System.out.println("Nao há problema com a String");
                    resultado = true;
                    return resultado;
                }
            } else {
                System.out.println("há algum problema com a String");
                return resultado;
            }
        } else {
            System.out.println("há algum problema com a String");
            return resultado;
        }
        return resultado;

    }

    // Parte do codigo usado para transformar a string em double
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ')
                    nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length())
                    throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+'))
                        x += parseTerm(); // addition
                    else if (eat('-'))
                        x -= parseTerm(); // subtraction
                    else
                        return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*'))
                        x *= parseFactor(); // multiplication
                    else if (eat('/'))
                        x /= parseFactor(); // division
                    else
                        return x;
                }
            }

            double parseFactor() {
                if (eat('+'))
                    return +parseFactor(); // unary plus
                if (eat('-'))
                    return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')'))
                        throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.')
                        nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));}
                else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^'))
                    x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

}

