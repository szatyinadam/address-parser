package hu.novaservices;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

class LanguageSettings {
    private final Pattern[][] tokenexps;
    private String[] tokenfields;
    private final String[][][] dictionaries;
    private final String name;
    private final int[] charweights;

    public LanguageSettings(String name, String[] tokenfields, String[][][] dictionaries, String[][] tokenexps, int[] charweights) {
        this.tokenfields = new String[0];
        if (tokenfields != null) {
            List<String> tokenfieldsl = new ArrayList<String>();
            for (String tokenfield : tokenfields) {
                String token = tokenfield;
                if (token != null) {
                    token = token.trim();
                    if (!token.isEmpty())
                        tokenfieldsl.add(token);
                }
            }
            this.tokenfields = tokenfieldsl.toArray(this.tokenfields);
        }

        if (tokenexps == null)
            this.tokenexps = new Pattern[this.tokenfields.length][0];
        else {
            this.tokenexps = new Pattern[this.tokenfields.length][];

            for (int i = 0; i < this.tokenfields.length; i++) {
                List<Pattern> exps = new ArrayList<>();
                if (tokenexps.length > i && tokenexps[i] != null) {
                    for (int j = 0; j < tokenexps[i].length; j++) {
                        String exp = tokenexps[i][j];
                        if (exp != null) {
                            exp = exp.trim();
                            if (!exp.isEmpty()) {
                                if (i == 0)
                                    exp = "\\A(" + exp + "(?:\\Z|\\s+))";
                                else if (i == this.tokenfields.length - 1)
                                    exp = "(?:\\A|\\s+)(" + exp + "\\Z)";
                                else
                                    exp = "(?:\\A|\\s+)(" + exp + "(?:\\Z|\\s+))";

                                exps.add(Pattern.compile(exp, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
                            }
                        }
                    }
                }
                this.tokenexps[i] = new Pattern[0];
                this.tokenexps[i] = exps.toArray(this.tokenexps[i]);
            }
        }

        if (dictionaries == null)
            this.dictionaries = new String[this.tokenfields.length][0][2];
        else {
            this.dictionaries = new String[this.tokenfields.length][][];

            for (int i = 0; i < this.tokenfields.length; i++) {
                List<String[]> dictelems = new ArrayList<>();
                if (dictionaries.length > i && dictionaries[i] != null) {
                    for (int j = 0; j < dictionaries[i].length; j++) {
                        String[] dictelem = dictionaries[i][j];
                        if (dictelem != null && dictelem.length > 0) {
                            String dictkey = dictelem[0];
                            if (dictkey != null) {
                                dictkey = dictkey.trim();
                                if (!dictkey.isEmpty()) {
                                    String dictvalue = null;
                                    if (dictelem.length > 1) {
                                        dictvalue = dictelem[1];
                                    }

                                    if (dictvalue != null) {
                                        dictvalue = dictvalue.trim();
                                    } else
                                        dictvalue = "";

                                    dictelem = new String[]{dictkey, dictvalue};
                                    dictelems.add(dictelem);
                                }
                            }
                        }
                    }
                }
                this.dictionaries[i] = new String[0][];
                this.dictionaries[i] = dictelems.toArray(this.dictionaries[i]);
            }
        }

        this.charweights = new int[this.tokenfields.length];
        if (charweights != null) {
            for (int i = 0; i < this.tokenfields.length && i < charweights.length; i++) {
                this.charweights[i] = charweights[i];
            }
        }

        if (name == null)
            this.name = "";
        else
            this.name = name.trim();
    }


    public int getTokenCount() {
        return tokenfields.length;
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
        return tokenfields[tokenNum];
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

    public String getName() {
        return name;
    }

    public int whichField(String fieldName) {
        for (int i = 0; i < tokenfields.length; i++)
            if (tokenfields[i].equalsIgnoreCase(fieldName))
                return i;
        return -1;
    }
}
