package hu.novaservices;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressParser {
    private static final String DEFAULT_LANGUAGE = "hu";
    private static final Pattern whiteSpacePattern = Pattern.compile("\\S+\\s+(\\S+)");
    private static final Pattern reverseWhiteSpacePattern = Pattern.compile("(\\S+)\\s+\\S+");
    private final LanguageSettings[] langSettings;
    private final String address;
    private final String languageCode;

    public AddressParser(LanguageSettings[] languageSettings, String address) {
        this(languageSettings, address, DEFAULT_LANGUAGE);
    }

    public AddressParser(LanguageSettings[] languageSettings, String address, String languageCode) {
        if (Objects.requireNonNull(languageSettings).length == 0
                || Objects.requireNonNull(address).trim().isEmpty()
                || Objects.requireNonNull(languageCode).trim().isEmpty()) {
            throw new IllegalArgumentException("Language settings, code and address can not be empty");
        }

        this.langSettings = languageSettings;
        this.address = address.replaceAll("\\.", " ")
                .replaceAll(",", " ")
                .replaceAll(";", " ")
                .trim();
        this.languageCode = languageCode;
    }

    public String getFieldValue(String field) {
        LanguageSettings lang = null;
        for (LanguageSettings langSetting : langSettings)
            if (langSetting.getLanguageCode().equalsIgnoreCase(this.languageCode)) {
                lang = langSetting;
                break;
            }

        if (lang == null)
            return "";
        if (lang.whichField(field) == -1)
            return "";

        Matcher nsm = whiteSpacePattern.matcher(address);
        Matcher rsm = reverseWhiteSpacePattern.matcher(new StringBuffer(address).reverse().toString());

        @SuppressWarnings("unchecked")
        List<PartialResult>[] partialResults = new List[lang.getTokenCount()];

        for (int i = 0; i < lang.getTokenCount(); i++) {
            partialResults[i] = new ArrayList<>();
            for (int j = 0; j < lang.getTokenExpCount(i); j++)
                partialResults[i].addAll(getPartialResults(address, nsm, rsm, lang.getTokenExp(i, j), i == 0, i == (lang.getTokenCount() - 1)));
        }

        int maxweight = 0;
        String[] result = null;
        List<String[]> results = getPossibleResults(partialResults, address.length(), address);
        for (String[] strings : results) {
            int weight = 0;
            for (int j = 0; j < lang.getTokenCount(); j++) {
                String restoken = strings[j];
                for (int k = 0; k < restoken.length(); k++) {
                    if (restoken.substring(k, k + 1).matches("\\S"))
                        weight += lang.getCharWeight(j);
                }
            }
            if (weight > maxweight) {
                maxweight = weight;
                result = strings;
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
        List<PartialResult> ret = new ArrayList<>();
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
        List<String[]> ret = new ArrayList<>();
        int lastpr = partialResults.length - 1;

        if (partialResults.length > 0) {
            @SuppressWarnings("unchecked")
            List<PartialResult>[] remaining = new List[lastpr];
            System.arraycopy(partialResults, 0, remaining, 0, lastpr);

            for (int i = 0; i < partialResults[lastpr].size(); i++) {
                PartialResult res = partialResults[lastpr].get(i);
                if (res.getRight() == currend) {
                    List<String[]> subret = getPossibleResults(remaining, res.getLeft(), input);
                    for (String[] strings : subret) {
                        String[] possres = new String[partialResults.length];
                        System.arraycopy(strings, 0, possres, 0, lastpr);
                        possres[lastpr] = input.substring(res.getContentLeft(), res.getContentRight());

                        ret.add(possres);
                    }
                }
            }
            List<String[]> subret = getPossibleResults(remaining, currend, input);
            for (String[] strings : subret) {
                String[] possres = new String[partialResults.length];
                System.arraycopy(strings, 0, possres, 0, lastpr);
                possres[lastpr] = "";
                ret.add(possres);
            }
        } else if (currend == 0)
            ret.add(new String[0]);

        return ret;
    }

}
