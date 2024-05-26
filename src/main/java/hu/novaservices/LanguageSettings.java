package hu.novaservices;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        return Arrays.stream(charweights, 0, (Math.max(this.tokenfields.size(), charweights.length)))
                .toArray();
    }

    private String[][][] loadDictionaries(String[][][] dictionaries) {
        return IntStream.range(0, this.tokenfields.size())
                .mapToObj(i -> {
                            if (dictionaries != null && dictionaries.length > i && dictionaries[i] != null) {
                                return Arrays.stream(dictionaries[i])
                                        .filter(this::isValidDictElement)
                                        .map(dictelem -> new String[]{dictelem[0].trim(), getDictvalue(dictelem)})
                                        .toArray(String[][]::new);
                            } else {
                                return new String[0][];
                            }
                        }
                )
                .toArray(String[][][]::new);
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
        return IntStream.range(0, this.tokenfields.size())
                .mapToObj(i -> {
                            if (tokenexps != null && tokenexps.length > i && tokenexps[i] != null) {
                                return Arrays.stream(tokenexps[i])
                                        .filter(Objects::nonNull)
                                        .filter(exp -> !exp.trim().isEmpty())
                                        .map(exp -> buildPatternForExpression(exp, this.tokenfields.size() - 1, i))
                                        .map(pattern -> Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))
                                        .toArray(Pattern[]::new);
                            } else {
                                return new Pattern[0];
                            }
                        }
                )
                .toArray(Pattern[][]::new);
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
        return tokenNum >= getTokenCount() ? 0 : tokenexps[tokenNum].length;
    }

    public Pattern getTokenExp(int tokenNum, int tokenExpNum) {
        return tokenExpNum >= getTokenExpCount(tokenNum) ? null : tokenexps[tokenNum][tokenExpNum];
    }

    public int getCharWeight(int tokenNum) {
        return tokenNum >= getTokenCount() ? 0 : charweights[tokenNum];
    }

    public int whichField(String fieldName) {
        for (int i = 0; i < tokenfields.size(); i++)
            if (tokenfields.get(i).equalsIgnoreCase(fieldName))
                return i;
        return -1;
    }
}
