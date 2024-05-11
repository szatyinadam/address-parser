package hu.novaservices;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class LanguageSettings {
    private final Pattern[][] tokenexps;
    private final List<String> tokenfields;
    private final String[][][] dictionaries;
    @Getter
    private final String languageCode;
    private final int[] charweights;

    public LanguageSettings(String languageCode, List<String> tokenFields, String[][][] dictionaries, String[][] tokenexps, int[] charweights) {
        this.tokenfields = initializeTokenFields(tokenFields);
        this.tokenexps = loadTokenExpressions(tokenexps);
        this.dictionaries = loadDictionaries(dictionaries);
        this.charweights = loadCharacterWeights(charweights);
        this.languageCode = (languageCode == null) ? "" : languageCode.trim();
    }

    private int[] loadCharacterWeights(int[] charweights) {
        int[] characterWeights = new int[this.tokenfields.size()];
        if (charweights != null) {
            for (int i = 0; i < this.tokenfields.size() && i < charweights.length; i++) {
                characterWeights[i] = charweights[i];
            }
        }
        return characterWeights;
    }

    private String[][][] loadDictionaries(String[][][] dictionaries) {
        String[][][] dictionariesResult = new String[this.tokenfields.size()][][];

        for (int i = 0; i < this.tokenfields.size(); i++) {
            List<String[]> dictelems = new ArrayList<>();
            if (dictionaries != null && dictionaries.length > i && dictionaries[i] != null) {
                for (String[] dictelem : dictionaries[i]) {
                    if (isValidDictElement(dictelem)) {
                        String dictkey = dictelem[0].trim();
                        String dictvalue = getDictvalue(dictelem);

                        dictelems.add(new String[]{dictkey, dictvalue});
                    }
                }
            }
            dictionariesResult[i] = dictelems.toArray(new String[0][]);
        }
        return dictionaries;
    }

    private String getDictvalue(String[] dictelem) {
        return (dictelem.length > 1 && dictelem[1] != null) ? dictelem[1].trim() : "";
    }

    private boolean isValidDictElement(String[] dictelem) {
        return (dictelem != null && dictelem.length > 0 && dictelem[0] != null && !dictelem[0].trim().isEmpty());
    }

    private List<String> initializeTokenFields(List<String> tokenFields) {
        return tokenFields.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private Pattern[][] loadTokenExpressions(String[][] tokenexps) {
        Pattern[][] tokenExpressions = new Pattern[this.tokenfields.size()][];

        for (int i = 0; i < this.tokenfields.size(); i++) {
            List<Pattern> exps = new ArrayList<>();
            if (tokenexps != null && tokenexps.length > i && tokenexps[i] != null) {
                for (String exp : tokenexps[i]) {
                    if (exp != null && !exp.trim().isEmpty()) {
                        String pattern = buildPatternForExpression(exp, this.tokenfields.size() - 1, i);
                        exps.add(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
                    }
                }
            }
            tokenExpressions[i] = exps.toArray(new Pattern[0]);
        }
        return tokenExpressions;
    }

    private String buildPatternForExpression(String exp, int indexOfTheDoor, int actualFieldIndex) {
        if (actualFieldIndex == 0)
            return "\\A(" + exp + "(?:\\Z|\\s+))";
        else if (actualFieldIndex == indexOfTheDoor)
            return "(?:\\A|\\s+)(" + exp + "\\Z)";
        else
            return "(?:\\A|\\s+)(" + exp + "(?:\\Z|\\s+))";
    }


    public int getTokenCount() {
        return tokenfields.size();
    }

    /*
     * private int getDictionaryItemCount(int tokenNum) {
     * if(tokenNum>=getTokenCount())return 0; else return
     * dictionaries[tokenNum].length; }
     */

    public String getDictionaryValue(int tokenNum, String key) {
        if (key == null)
            return key;
        if (tokenNum >= getTokenCount())
            return key;

        key = key.trim();

        for (int i = 0; i < dictionaries[tokenNum].length; i++) {
            if (dictionaries[tokenNum][i][0].equalsIgnoreCase(key))
                return dictionaries[tokenNum][i][1];
        }

        return key;
    }

    public String getFieldName(int tokenNum) {
        if (tokenNum >= getTokenCount())
            return null;
        return tokenfields.get(tokenNum);
    }

    public int getTokenExpCount(int tokenNum) {
        if (tokenNum >= getTokenCount())
            return 0;
        else
            return tokenexps[tokenNum].length;
    }

    public Pattern getTokenExp(int tokenNum, int tokenExpNum) {
        if (tokenExpNum >= getTokenExpCount(tokenNum))
            return null;
        else
            return tokenexps[tokenNum][tokenExpNum];
    }

    public int getCharWeight(int tokenNum) {
        if (tokenNum >= getTokenCount())
            return 0;
        else
            return charweights[tokenNum];
    }

    public int whichField(String fieldName) {
        for (int i = 0; i < tokenfields.size(); i++)
            if (tokenfields.get(i).equalsIgnoreCase(fieldName))
                return i;
        return -1;
    }
}
