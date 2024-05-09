package hu.novaservices;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressHelper {
    private static final String DEFAULT_LANGUAGE = "hu";
    private static final Pattern whiteSpacePattern = Pattern.compile("\\S+\\s+(\\S+)");
    private static final Pattern reverseWhiteSpacePattern = Pattern.compile("(\\S+)\\s+\\S+");

    private static class LangSettings {
        private Pattern[][] tokenexps;
        private String[] tokenfields;
        private String[][][] dictionaries;
        private String name;
        private int charweights[];

        public LangSettings(String name, String[] tokenfields, String[][][] dictionaries, String[][] tokenexps, int[] charweights) {
            this.tokenfields = new String[0];
            if (tokenfields != null) {
                List<String> tokenfieldsl = new ArrayList<String>();
                for (int i = 0; i < tokenfields.length; i++) {
                    String token = tokenfields[i];
                    if (token != null) {
                        token = token.trim();
                        if (token.length() > 0)
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
                    List<Pattern> exps = new ArrayList<Pattern>();
                    if (tokenexps.length > i && tokenexps[i] != null) {
                        for (int j = 0; j < tokenexps[i].length; j++) {
                            String exp = tokenexps[i][j];
                            if (exp != null) {
                                exp = exp.trim();
                                if (exp.length() > 0) {
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
                    List<String[]> dictelems = new ArrayList<String[]>();
                    if (dictionaries.length > i && dictionaries[i] != null) {
                        for (int j = 0; j < dictionaries[i].length; j++) {
                            String[] dictelem = dictionaries[i][j];
                            if (dictelem != null && dictelem.length > 0) {
                                String dictkey = dictelem[0];
                                if (dictkey != null) {
                                    dictkey = dictkey.trim();
                                    if (dictkey.length() > 0) {
                                        String dictvalue = null;
                                        if (dictelem.length > 1) {
                                            dictvalue = dictelem[1];
                                        }

                                        if (dictvalue != null) {
                                            dictvalue = dictvalue.trim();
                                        } else
                                            dictvalue = "";

                                        dictelem = new String[] { dictkey, dictvalue };
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

    private static LangSettings[] langSettings = new LangSettings[] { new LangSettings("hu", new String[] { "kozternev", "kozterjell", "hazszam", "epulet", "lepcsohaz", "emelet", "ajto" },
            new String[][][] { {}, { { "u", "utca" }, { "krt", "körút" }, { "t", "tér" }, { "rkp", "rakpart" } }, {}, {}, {}, { { "fsz", "földszint" }, { "mfsz", "magasföldszint" } }, {} },

            new String[][] {
                    { "(.+)" },
                    { "(u)", "(krt)", "(t)", "(rkp)", "(körútja)", "(akna)", "(akna-alsó)", "(akna-felső)", "(alagút)", "(alsórakpart)", "(arborétum)", "(autóút)", "(barakképület)", "(barlang)",
                            "(bejáró)", "(bekötőút)", "(bánya)", "(bányatelep)", "(bástya)", "(bástyája)", "(csárda)", "(csónakházak)", "(domb)", "(dűlő)", "(dűlők)", "(dűlősor)", "(dűlőterület)",
                            "(dűlőút)", "(egyetemváros)", "(egyéb)", "(elágazás)", "(emlékút)", "(erdészház)", "(erdészlak)", "(erdő)", "(erdősor)", "(fasor)", "(fasora)", "(felső)", "(forduló)",
                            "(főmérnökség)", "(főtér)", "(főút)", "(föld)", "(gyár)", "(gyártelep)", "(gyárváros)", "(gyümölcsös)", "(gát)", "(gátsor)", "(gátőrház)", "(határsor)", "(határút)",
                            "(hegy)", "(hegyhát)", "(hegyhát dűlő)", "(hegyhát)", "(köz)", "(hrsz)", "(hrsz.)", "(ház)", "(hídfő)", "(iskola)", "(játszótér)", "(kapu)", "(kastély)", "(kert)",
                            "(kertsor)", "(kerület)", "(kilátó)", "(kioszk)", "(kocsiszín)", "(kolónia)", "(korzó)", "(kultúrpark)", "(kunyhó)", "(kör)", "(körtér)", "(körvasútsor)", "(körzet)",
                            "(körönd)", "(körút)", "(köz)", "(kút)", "(kültelek)", "(lakóház)", "(lakónegyed)", "(lakópark)", "(lakótelep)", "(lejtő)", "(lejáró)", "(liget)", "(lépcső)", "(major)",
                            "(malom)", "(menedékház)", "(munkásszálló)", "(mélyút)", "(műút)", "(oldal)", "(orom)", "(park)", "(parkja)", "(parkoló)", "(part)", "(pavilon)", "(piac)", "(pihenő)",
                            "(pince)", "(pincesor)", "(postafiók)", "(puszta)", "(pálya)", "(pályaudvar)", "(rakpart)", "(repülőtér)", "(rész)", "(rét)", "(sarok)", "(sor)", "(sora)", "(sportpálya)",
                            "(sporttelep)", "(stadion)", "(strandfürdő)", "(sugárút)", "(szer)", "(sziget)", "(szivattyútelep)", "(szállás)", "(szállások)", "(szél)", "(szőlő)", "(szőlőhegy)",
                            "(szőlők)", "(sánc)", "(sávház)", "(sétány)", "(tag)", "(tanya)", "(tanyák)", "(telep)", "(temető)", "(tere)", "(tető)", "(turistaház)", "(téli kikötő)", "(tér)",
                            "(tömb)", "(udvar)", "(utak)", "(utca)", "(utcája)", "(vadaskert)", "(vadászház)", "(vasúti megálló)", "(vasúti őrház)", "(vasútsor)", "(vasútállomás)", "(vezetőút)",
                            "(villasor)", "(vágóhíd)", "(vár)", "(várköz)", "(város)", "(vízmű)", "(völgy)", "(zsilip)", "(zug)", "(állat és növ.kert)", "(állomás)", "(árnyék)", "(árok)", "(átjáró)",
                            "(őrház)", "(őrházak)", "(őrházlak)", "(út)", "(útja)", "(útőrház)", "(üdülő)", "(üdülő-part)", "(üdülő-sor)", "(üdülő-telep)" },
                    { "(\\d+\\s*(?:\\s*-\\s*\\d+||/[a-z]+)?)" }, { "(\\S+)\\s*(?:é|ép|épület)?" }, { "(\\S+)\\s*(?:l|lh|lp|lph|lépcsőház)" },
                    { "(fsz)", "(földszint)", "(mfsz)", "(magasföldszint)", "(\\d+)\\s*(?:em|emelet)?" }, { "(\\d+(/[a-z])*)\\s*(?:ajtó)?" } }, new int[] { 1, 1000, 100, 5, 2, 10, 20 }) };

    private static class PartialResult {
        private int left, right, contentleft, contentright;

        public PartialResult(int left, int right, int contentleft, int contentright) {
            this.left = left;
            this.right = right;
            this.contentleft = contentleft;
            this.contentright = contentright;
        }

        public int getContentleft() {
            return contentleft;
        }

        public int getContentright() {
            return contentright;
        }

        public int getLeft() {
            return left;
        }

        public int getRight() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || o.getClass() != PartialResult.class)
                return false;
            PartialResult other = (PartialResult) o;
            return (left == other.left && right == other.right && contentleft == other.contentleft && contentright == other.contentright);
        }
    }

    public static String getFieldValue(String address, String langcode, String field) {

        if (langSettings == null)
            return "";
        if (langcode == null || langcode.trim().length() == 0)
            langcode = DEFAULT_LANGUAGE;

        LangSettings lang = null;
        for (int i = 0; i < langSettings.length; i++)
            if (langSettings[i].name.equalsIgnoreCase(langcode)) {
                lang = langSettings[i];
                break;
            }

        if (lang == null)
            return "";
        if (lang.whichField(field) == -1)
            return "";

        if (address == null || address.trim().length() == 0)
            address = "";

        address = address.replaceAll("\\.", " ");
        address = address.replaceAll(",", " ");
        address = address.replaceAll(";", " ");
        address = address.trim();
        Matcher nsm = whiteSpacePattern.matcher(address);
        Matcher rsm = reverseWhiteSpacePattern.matcher(new StringBuffer(address).reverse().toString());

        @SuppressWarnings("unchecked")
        List<PartialResult>[] partialResults = new List[lang.getTokenCount()];

        for (int i = 0; i < lang.getTokenCount(); i++) {
            partialResults[i] = new ArrayList<PartialResult>();
            for (int j = 0; j < lang.getTokenExpCount(i); j++)
                partialResults[i].addAll(getPartialResults(address, nsm, rsm, lang.getTokenExp(i, j), i == 0, i == (lang.getTokenCount() - 1)));
        }

        int maxweight = 0;
        String[] result = null;
        List<String[]> results = getPossibleResults(partialResults, address.length(), address);
        for (int i = 0; i < results.size(); i++) {
            int weight = 0;
            for (int j = 0; j < lang.getTokenCount(); j++) {
                String restoken = results.get(i)[j];
                for (int k = 0; k < restoken.length(); k++) {
                    if (restoken.substring(k, k + 1).matches("\\S"))
                        weight += lang.getCharWeight(j);
                }
            }
            if (weight > maxweight) {
                maxweight = weight;
                result = results.get(i);
            }
        }

        if (result == null)
            return "";

        for (int j = 0; j < lang.getTokenCount(); j++) {
            result[j] = lang.getDictionaryValue(j, result[j]);
        }

        return result[lang.whichField(field)];
    }

    private static List<PartialResult> getPartialResults(String input, Matcher nsm, Matcher rsm, Pattern exp, boolean firstelem, boolean lastelem) {
        List<PartialResult> ret = new ArrayList<PartialResult>();
        int currpos = 0;
        while (currpos < input.length()) {

            int nextpos = input.length();
            if (!firstelem && nsm.find(currpos)) {
                nextpos = nsm.start(1);
            }

            int currend = input.length();
            while (currend > currpos) {
                String inp = input.substring(currpos, currend);
                Matcher m = exp.matcher(inp);

                if (m.find()) {
                    if (currend == input.length() && (m.start(1) + currpos) >= nextpos) {
                        nextpos = input.length();
                        if (nsm.find(m.start(1) + currpos)) {
                            nextpos = nsm.start(1);
                        }

                    }
                    PartialResult pr = new PartialResult(m.start(1) + currpos, m.end(1) + currpos, m.start(2) + currpos, m.end(2) + currpos);
                    if (!ret.contains(pr)) {
                        ret.add(pr);
                    }
                } else
                    break;

                if (!lastelem && rsm.find(input.length() - currend)) {
                    currend = input.length() - rsm.end(1);
                } else
                    currend = 0;
            }
            currpos = nextpos;
        }

        return ret;
    }

    private static List<String[]> getPossibleResults(List<PartialResult>[] partialResults, int currend, String input) {
        List<String[]> ret = new ArrayList<String[]>();
        int lastpr = partialResults.length - 1;

        if (partialResults.length > 0) {
            @SuppressWarnings("unchecked")
            List<PartialResult>[] remaining = new List[lastpr];
            for (int i = 0; i < lastpr; i++)
                remaining[i] = partialResults[i];

            for (int i = 0; i < partialResults[lastpr].size(); i++) {
                PartialResult res = partialResults[lastpr].get(i);
                if (res.getRight() == currend) {
                    List<String[]> subret = getPossibleResults(remaining, res.getLeft(), input);
                    for (int j = 0; j < subret.size(); j++) {
                        String[] possres = new String[partialResults.length];
                        for (int k = 0; k < lastpr; k++)
                            possres[k] = subret.get(j)[k];
                        possres[lastpr] = input.substring(res.getContentleft(), res.getContentright());

                        ret.add(possres);
                    }
                }
            }
            List<String[]> subret = getPossibleResults(remaining, currend, input);
            for (int j = 0; j < subret.size(); j++) {
                String[] possres = new String[partialResults.length];
                for (int k = 0; k < lastpr; k++)
                    possres[k] = subret.get(j)[k];
                possres[lastpr] = "";
                ret.add(possres);
            }
        } else if (currend == 0)
            ret.add(new String[0]);

        return ret;
    }

}
