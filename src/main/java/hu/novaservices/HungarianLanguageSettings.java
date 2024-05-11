package hu.novaservices;

import java.util.Arrays;

public class HungarianLanguageSettings {
    public static LanguageSettings[] langSettings = new LanguageSettings[]{
            new LanguageSettings(
                    "hu",
                    Arrays.asList("kozternev", "kozterjell", "hazszam", "epulet", "lepcsohaz", "emelet", "ajto"),
                    new String[][][]
                            {
                                    {}, {{"u", "utca"}, {"krt", "körút"}, {"t", "tér"}, {"rkp", "rakpart"}}, {}, {}, {}, {{"fsz", "földszint"}, {"mfsz", "magasföldszint"}}, {}
                            },

                    new String[][]{
                            /*kozternev*/      {"(.+)"},
                            /*kozterjell*/     {"(u)", "(krt)", "(t)", "(rkp)", "(körútja)", "(akna)", "(akna-alsó)", "(akna-felső)", "(alagút)", "(alsórakpart)", "(arborétum)", "(autóút)", "(barakképület)", "(barlang)",
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
                                                       "(őrház)", "(őrházak)", "(őrházlak)", "(út)", "(útja)", "(útőrház)", "(üdülő)", "(üdülő-part)", "(üdülő-sor)", "(üdülő-telep)"},
                            /*hazszam*/         {"(\\d+\\s*(?:\\s*-\\s*\\d+||/[a-z]+)?)"},
                            /*epulet*/          {"(\\S+)\\s*(?:é|ép|épület)?"},
                            /*lepcsohaz*/       {"(\\S+)\\s*(?:l|lh|lp|lph|lépcsőház)"},
                            /*emelet*/          {"(fsz)", "(földszint)", "(mfsz)", "(magasföldszint)", "(\\d+)\\s*(?:em|emelet)?"},
                            /*ajto*/            {"(\\d+(/[a-z])*)\\s*(?:ajtó)?"}
                    },
                    new int[]{1, 1000, 100, 5, 2, 10, 20})};
}
