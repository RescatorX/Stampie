package cz.kalina.stampie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    protected final static String defaultTag = "Vitakarta";
    protected String dbName = null;

    protected Context currentContext = null;

    public DatabaseHelper(Context context, String dbname) {

        super(context, dbname, null, 1);

        dbName = dbname;
        currentContext = context;

        //if (!checkDataBase()) ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        CreateAllTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        android.util.Log.w(defaultTag, "Upgrading database from version " + oldVersion + " to " + newVersion + " which will destroy all old data");

        DropAllTables(db);

        onCreate(db);
    }

    public Boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(dbName, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e){}

        if (checkDB != null) checkDB.close();

        return (checkDB != null);
    }

    public void DropAllTables(SQLiteDatabase db) {

        Log.i("Vitakarta", "Dropping database");

        db.execSQL("DROP TABLE IF EXISTS cs_chron_obt");
        db.execSQL("DROP TABLE IF EXISTS cs_subjekty_mapa");
        db.execSQL("DROP TABLE IF EXISTS cs_typ_akce");
        db.execSQL("DROP TABLE IF EXISTS cs_typ_memo");

        db.execSQL("DROP TABLE IF EXISTS cis_as_typ_pripadu");
        db.execSQL("DROP TABLE IF EXISTS poj_as_typ_pripadu");

        db.execSQL("DROP TABLE IF EXISTS sync");
        db.execSQL("DROP TABLE IF EXISTS user");

        //db.execSQL("DROP INDEX IF EXISTS pojlekar_niczniczd");
        db.execSQL("DROP INDEX IF EXISTS poj_nico");

        db.execSQL("DROP TABLE IF EXISTS cs_typ_konf");
        db.execSQL("DROP TABLE IF EXISTS cs_typ_zapisu");
        db.execSQL("DROP TABLE IF EXISTS cs_ur_souhlasu");
        db.execSQL("DROP TABLE IF EXISTS c_odbornost");

        db.execSQL("DROP TABLE IF EXISTS extdata");
        db.execSQL("DROP TABLE IF EXISTS pojkonf");

        db.execSQL("DROP TABLE IF EXISTS soubor");
        db.execSQL("DROP TABLE IF EXISTS pojmemo");
        db.execSQL("DROP TABLE IF EXISTS pojlekar");
        db.execSQL("DROP TABLE IF EXISTS pojdet");
        db.execSQL("DROP TABLE IF EXISTS pojprev");
        db.execSQL("DROP TABLE IF EXISTS pojnab");
        db.execSQL("DROP TABLE IF EXISTS pojasist");
        db.execSQL("DROP TABLE IF EXISTS poj");
    }

    public void CreateAllTables(SQLiteDatabase db) {

        Log.i("Vitakarta", "Create database");

        db.execSQL("CREATE TABLE IF NOT EXISTS poj 		( id_poj				INTEGER PRIMARY KEY AUTOINCREMENT," +
                " nico					INTEGER ," +
                " rodcis				TEXT	," +
                " prijmeni				TEXT	," +
                " jmeno					TEXT	," +
                " titul_pred			TEXT	," +
                " titul_za				TEXT	," +
                " datum_vzniku_poj		TEXT	," +
                " datum_konce_poj		TEXT	," +
                " vek					INTEGER	," +
                " datum_umrti			TEXT	," +
                " pohlavi				TEXT	," +
                " prz_as                INTEGER )");

        db.execSQL("CREATE TABLE IF NOT EXISTS pojasist ( id_pojasist			INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_poj				INTEGER	NOT NULL," +
                " pripad    			TEXT	," +
                " typ    				TEXT	," +
                " nahlaseni             TEXT	," +
                " stav  				TEXT	," +
                " dat_obj               TEXT	," +
                " zz            		TEXT	)" );

        db.execSQL("CREATE TABLE IF NOT EXISTS pojprev ( id_pojprev				INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_poj				INTEGER	NOT NULL," +
                " odbornost				TEXT	," +
                " datum_posl            TEXT	," +
                " datum_dalsi           TEXT	)" );

        db.execSQL("CREATE TABLE IF NOT EXISTS pojnab ( id_pojnab				INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_poj				INTEGER	NOT NULL," +
                " typ    				TEXT	," +
                " podtyp                TEXT	," +
                " popis  				TEXT	," +
                " poradi                INTEGER ," +
                " url            		TEXT	)" );

        db.execSQL("CREATE TABLE IF NOT EXISTS	pojdet 	( id_pojdet				INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_poj				INTEGER	NOT NULL," +
                " typ_radku				INTEGER	NOT NULL," +
                " datum					TEXT	," +
                " datum_do				TEXT	," +
                " kod_vykonu			TEXT	," +
                " nazev_vykonu			TEXT	," +
                " pocet					NUMERIC	," +
                " celkem_kc				NUMERIC	," +
                " nicz					INTEGER	," +
                " nicz_nazev			TEXT	," +
                " nicz_typ				TEXT	," +
                " nicz_typ_popis		TEXT	," +
                " typ_pece				TEXT	," +
                " odbornost_hlavickova	TEXT	," +
                " odbornost_nazev		TEXT	," +
                " diagnoza				TEXT	," +
                " typ_davky				TEXT	," +
                " druh_dokladu			TEXT	," +
                " zapocteno				TEXT	," +
                " nicz_i				INTEGER	," +
                " nicz_i_nazev			TEXT	," +
                " nicz_i_typ			TEXT	," +
                " nicz_i_typ_popis		TEXT	," +
                " popis					TEXT	," +
                " skuchem				TEXT	," +
                " skuchem_popis			TEXT	," +
                " sluz_ID				TEXT	)");

        db.execSQL("CREATE TABLE IF NOT EXISTS pojlekar ( id_pojlekar			INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_poj				INTEGER	NOT NULL," +
                " nicz					INTEGER	," +
                " niczd					INTEGER	," +
                " prijmeni				TEXT	," +
                " jmeno					TEXT	," +
                " titpre				TEXT	," +
                " titza					TEXT	," +
                " odbornost				TEXT	," +
                " odbornost_nazev		TEXT	," +
                " uroven_souhlasu		INTEGER	," +
                " uroven_souhlasu_text	TEXT	," +
                " telefon				TEXT	," +
                " mobil 				TEXT	," +
                " email					TEXT	)");

        //db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS pojlekar_niczniczd on pojlekar(nicz, niczd)");

        db.execSQL("CREATE TABLE IF NOT EXISTS	pojmemo ( id_pojmemo			INTEGER PRIMARY KEY," +
                " id_poj				INTEGER	NOT NULL," +
                " operace				TEXT	," +
                " id_pojmemo_j			INTEGER	," +
                " kdo_zapsal_typ		TEXT	," +
                " kdo_zapsal_id			INTEGER	," +
                " kdo_zapsal_nazev		TEXT	," +
                " typ					TEXT	," +
                " text1					TEXT	," +
                " text2					TEXT	," +
                " text3					TEXT	," +
                " text4					TEXT	," +
                " cislo1                INTEGER ," +
                " cislo2                INTEGER ," +
                " zapsano				TEXT	," +
                " platod				TEXT	," +
                " stav_alertu			TEXT	," +
                " platdo				TEXT	)");

        db.execSQL("CREATE TABLE IF NOT EXISTS	soubor 	( id_soubor				INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_pojmemo			INTEGER	NOT NULL," +
                " id_pojmemo_j			INTEGER	," +
                " rodcis				TEXT    ," +
                " par_key				TEXT	NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS	pojkonf ( id_pojkonf			INTEGER PRIMARY KEY," +
                " id_poj				INTEGER	," +
                " typ					TEXT	," +
                " text1					TEXT	," +
                " text2					TEXT	," +
                " text3					TEXT	," +
                " text4					TEXT	," +
                " zapsano				TEXT	)");

        db.execSQL("CREATE TABLE IF NOT EXISTS	extdata ( id_extdata			INTEGER PRIMARY KEY," +
                " typ					TEXT	," +
                " text1					TEXT	," +
                " text2					TEXT	," +
                " text3					TEXT	," +
                " text4					TEXT	," +
                " zapsano				TEXT	)");

        db.execSQL("CREATE TABLE IF NOT EXISTS	c_odbornost 	( kododb	TEXT PRIMARY KEY," +
                " nazodb	TEXT	)");

        db.execSQL("CREATE TABLE IF NOT EXISTS	cs_ur_souhlasu	( kod	INTEGER PRIMARY KEY," +
                " popis	TEXT	)");

        db.execSQL("CREATE TABLE IF NOT EXISTS	cs_typ_zapisu	( kod	INTEGER PRIMARY KEY," +
                " popis	TEXT	)");

        db.execSQL("CREATE TABLE IF NOT EXISTS	user    ( id_user				INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_poj				INTEGER	," +
                " rodcis				TEXT    ," +
                " nazev					TEXT	NOT NULL," +
                " ident					TEXT	NOT NULL," +
                " sign                  TEXT    ," +
                " auth_sts              INTEGER )");

        db.execSQL("CREATE TABLE IF NOT EXISTS	sync    ( id_sync				INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_user				INTEGER	NOT NULL," +
                " typ					INTEGER	NOT NULL," +
                " stav					INTEGER	NOT NULL," +
                " result				INTEGER ," +
                " datum_start			TEXT	NOT NULL," +
                " datum_konec			TEXT	)");

        db.execSQL("CREATE TABLE IF NOT EXISTS poj_as_typ_pripadu (id_poj_as_typ_pripadu  INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_poj          INTEGER NOT NULL ," +
                " typ_pripadu     INTEGER          ," +
                " narok           TEXT             ," +
                " pocet           INTEGER          )" );

        db.execSQL("CREATE TABLE IF NOT EXISTS cis_as_typ_pripadu (id_cis_as_typ_pripadu  INTEGER PRIMARY KEY AUTOINCREMENT," +
                " kod             INTEGER NOT NULL ," +
                " text            TEXT             ," +
                " popis           TEXT             ," +
                " platnost_od     TEXT             ," +
                " platnost_do     TEXT             )" );

        db.execSQL("CREATE TABLE IF NOT EXISTS cs_chron_obt (id_cis INTEGER PRIMARY KEY AUTOINCREMENT," +
                " kod             TEXT NOT NULL ," +
                " nazev           TEXT             ," +
                " platnost_od     TEXT             ," +
                " platnost_do     TEXT             )" );

        db.execSQL("CREATE TABLE IF NOT EXISTS cs_subjekty_mapa (id_cis INTEGER PRIMARY KEY AUTOINCREMENT," +
                " kod             TEXT NOT NULL ," +
                " nazev           TEXT             ," +
                " popis           TEXT             ," +
                " platnost_od     TEXT             ," +
                " platnost_do     TEXT             )" );

        db.execSQL("CREATE TABLE IF NOT EXISTS cs_typ_akce (id_cis INTEGER PRIMARY KEY AUTOINCREMENT," +
                " kod             INTEGER NOT NULL ," +
                " nazev           TEXT             ," +
                " platnost_od     TEXT             ," +
                " platnost_do     TEXT             )" );

        db.execSQL("CREATE TABLE IF NOT EXISTS cs_typ_memo (id_cis INTEGER PRIMARY KEY AUTOINCREMENT," +
                " kod             TEXT NOT NULL ," +
                " nazev           TEXT             ," +
                " popis           TEXT             ," +
                " platnost_od     TEXT             ," +
                " platnost_do     TEXT             )" );


        db.execSQL("INSERT INTO pojkonf (typ, text1, text2) VALUES ('13', '844111000', 'asozp@ozp.cz')");	// asist.sluzba
        db.execSQL("INSERT INTO pojkonf (typ, text1, text2) VALUES ('14', '261105555', 'ozp@ozp.cz')");		// OZP (ostra je 261 105 555, ozp@ozp.cz)

        // prozatimni plneni ciselniku pro AS
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992009,'Dotaz na lékaře (základní)','Máte obecný medicínský dotaz týkající se oblasti interního či praktického lékařství? Napište nám a lékař Vám odpoví.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992021,'Virtuální lékárna','Potřebujete léky z volného prodeje, zdravot. prostředky či vitamíny a doplňky stravy? Napište nám, dodáme je na určenou adresu. Koordinátor asistenční služby Vám při objednávce sdělí inkasní údaje za tyto léky, dopravu za Vás uhradíme my – AS GOLD.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992007,'Ostatní dotazy','Hledáte informace o Evropském zdravotním průkazu (EHIC) a o možnostech jeho dodatečného vystavení, o cestovním pojištění, příp. o možnostech jeho sjednání na pobočce OZP či prostřednictvím webových stránek, atd.? Napište nám.','2013-02-01',null)");
        //	db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992008,'Newsletter','Newsletter','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992011,'Objednání k lékaři','Potřebujete objednat k lékaři? Napište nám a my Vás objednáme k jednomu z více než 800 lékařů všech specializací po celé ČR. Ozvěte se nám i v případě potřeby změny nebo zrušení termínu nebo i s Vaším názorem na péči daného lékaře.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992012,'Objednání dopravní zdravotní služby','Objednání dopravní zdravotní služby','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992013,'Léková poradna','Nenalezli jste potřebné informace o lécích, léčivech a jejich případných interakcích v naší poradně na webu OZP? Napište nám a odborník na farmacii Vám poradí.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992014,'Právně medicínská poradna','Nenalezli jste odpověď na své dotazy v naší \"Právně medicínské poradně\" na webu OZP? Napište nám, co Vás zajímá a odborník na danou problematiku Vám odpoví.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992002,'Vyhledání lékaře či pohotovosti','Hledáte nejbližšího vhodného poskytovatele zdravotních služeb, příp. informace o rychlé záchranné službě? Napište nám.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992010,'Dotaz na lékaře či zdravotníka (speciální)','Máte dotaz týkající se nejen Vašeho zdraví, ale i dalších odborných témat v oblasti medicíny? Napište nám a lékař expert nebo zkušený koordinátor s medicínskou praxí Vám odpoví.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992003,'Nedostupnost/kvalita zdravotních služeb','Je pro Vás nedostupná zdravotní péče, která je zákonem a smluvně garantována? Chcete reklamovat úroveň poskytnutých zdravotních služeb smluvních poskytovatelů OZP? Napište nám.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992005,'Zdravotní prevence (info)','Zajímá Vás finančně efektivní využití aktuálně platných preventivních programů? Napište nám.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992022,'Zlatá prevence','Jako klient asistenční služby GOLD můžete čerpat preventivní aktivitu v podobě Nutričního poradenství či Poradenství proti bolestem zad. V případě zájmu o tuto službu nám napište. (1x v průběhu platnosti karty GOLD)','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992006,'Zdravotní prevence (objednání)','Chcete objednat do sítě smluvních partnerů na preventivní prohlídku některého z našich programů? Napište nám.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992018,'Druhý názor (neakutní)','Chcete znát další názor na neakutní medicínský případ, jemuž předcházelo doporučení či navržené řešení jiného lékaře? Napište nám a lékař Vám odpoví – AS SILVER/GOLD.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992019,'Druhý názor (akutní)','Chcete znát další názor na akutní medicínský případ (např. pro klienty, kteří jsou momentálně hospitalizováni), jemuž předcházelo doporučení či návrh řešení jiného lékaře? – AS GOLD.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992004,'Hlášení změn, užitečné kontakty','Chcete nahlásit změnu v osobních či kontaktních údajích nebo jen hledáte informaci o kontaktních údajích OZP? Naformulujte svůj požadavek/dotaz a my Vám nejdéle následující pracovní den pošleme odpověď.','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992015,'Navigace ve Vitakartě','Potřebujete poradit jak efektivně pracovat s VITAKARTOU? Máte pro nás nějaké podněty? Napište nám.','2013-02-01',null)");
        //	db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992020,'Virtuální prohlídka (zdr. dotazník)','Virtuální prohlídka (zdr. dotazník)','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992016,'Stříbrná prevence','Máte zájem o balíček vitamínů v hodnotě 300 Kč na jehož čerpání máte nárok? Napište nám a my Vám vitamíny dodáme na kontaktní adresu. (1x v průběhu platnosti karty SILVER/GOLD)','2013-02-01',null)");
        db.execSQL("INSERT INTO cis_as_typ_pripadu (kod,text,popis,platnost_od,platnost_do) values (854992017,'Návrat z nemocnice','Chcete být po návratu z nemocnice informováni, jak vhodně pokračovat s léčbou (rehabilitace, jaké léky užívat atd.)? Dejte nám vědět, kdy do nemocnice nastupujete. Využít tuto službu můžete sami aktivně i po návratu z nemocnice – AS SILVER/GOLD.','2013-02-01',null)");

    }
}

