package reference.classes;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class KeyMap {
	private static Map<String, Integer> keyMap = getKeyMap();

	public static Map<String, Integer> getKeyMap() {
		if (keyMap == null) {
			Map<String, Integer> keyMap = new HashMap<>();
			keyMap.put("A", 0);
			keyMap.put("AA", 1);
			keyMap.put("AAPL", 2);
			keyMap.put("ABC", 3);
			keyMap.put("ABT", 4);
			keyMap.put("ACE", 5);
			keyMap.put("ACS", 6);
			keyMap.put("ADBE", 7);
			keyMap.put("ADI", 8);
			keyMap.put("ADM", 9);
			keyMap.put("ADP", 10);
			keyMap.put("ADSK", 11);
			keyMap.put("AEE", 12);
			keyMap.put("AEP", 13);
			keyMap.put("AES", 14);
			keyMap.put("AET", 15);
			keyMap.put("AFL", 16);
			keyMap.put("AGN", 17);
			keyMap.put("AIG", 18);
			keyMap.put("AIV", 19);
			keyMap.put("AIZ", 20);
			keyMap.put("AKAM", 21);
			keyMap.put("AKS", 22);
			keyMap.put("ALL", 23);
			keyMap.put("ALTR", 24);
			keyMap.put("AMAT", 25);
			keyMap.put("AMD", 26);
			keyMap.put("AMGN", 27);
			keyMap.put("AMP", 28);
			keyMap.put("AMT", 29);
			keyMap.put("AMZN", 30);
			keyMap.put("AN", 31);
			keyMap.put("ANF", 32);
			keyMap.put("AOC", 33);
			keyMap.put("AON", 34);
			keyMap.put("APA", 35);
			keyMap.put("APC", 36);
			keyMap.put("APD", 37);
			keyMap.put("APH", 38);
			keyMap.put("APOL", 39);
			keyMap.put("ARG", 40);
			keyMap.put("ATI", 41);
			keyMap.put("AVB", 42);
			keyMap.put("AVP", 43);
			keyMap.put("AVY", 44);
			keyMap.put("AXP", 45);
			keyMap.put("AYE", 46);
			keyMap.put("AZO", 47);
			keyMap.put("BA", 48);
			keyMap.put("BAC", 49);
			keyMap.put("BAX", 50);
			keyMap.put("BBBY", 51);
			keyMap.put("BBT", 52);
			keyMap.put("BBY", 53);
			keyMap.put("BCR", 54);
			keyMap.put("BDK", 55);
			keyMap.put("BDX", 56);
			keyMap.put("BEN", 57);
			keyMap.put("BF.B", 58);
			keyMap.put("BHI", 59);
			keyMap.put("BIG", 60);
			keyMap.put("BIIB", 61);
			keyMap.put("BJS", 62);
			keyMap.put("BK", 63);
			keyMap.put("BLL", 64);
			keyMap.put("BMC", 65);
			keyMap.put("BMS", 66);
			keyMap.put("BMY", 67);
			keyMap.put("BNI", 68);
			keyMap.put("BRCM", 69);
			keyMap.put("BRK.B", 70);
			keyMap.put("BSX", 71);
			keyMap.put("BTU", 72);
			keyMap.put("BXP", 73);
			keyMap.put("C", 74);
			keyMap.put("CA", 75);
			keyMap.put("CAG", 76);
			keyMap.put("CAH", 77);
			keyMap.put("CAM", 78);
			keyMap.put("CAT", 79);
			keyMap.put("CB", 80);
			keyMap.put("CBE", 81);
			keyMap.put("CBG", 82);
			keyMap.put("CBS", 83);
			keyMap.put("CCE", 84);
			keyMap.put("CCL", 85);
			keyMap.put("CEG", 86);
			keyMap.put("CELG", 87);
			keyMap.put("CEPH", 88);
			keyMap.put("CERN", 89);
			keyMap.put("CF", 90);
			keyMap.put("CFN", 91);
			keyMap.put("CHK", 92);
			keyMap.put("CHRW", 93);
			keyMap.put("CI", 94);
			keyMap.put("CIEN", 95);
			keyMap.put("CINF", 96);
			keyMap.put("CL", 97);
			keyMap.put("CLF", 98);
			keyMap.put("CLX", 99);
			keyMap.put("CMA", 100);
			keyMap.put("CMCSA", 101);
			keyMap.put("CME", 102);
			keyMap.put("CMI", 103);
			keyMap.put("CMS", 104);
			keyMap.put("CNP", 105);
			keyMap.put("CNX", 106);
			keyMap.put("COF", 107);
			keyMap.put("COG", 108);
			keyMap.put("COH", 109);
			keyMap.put("COL", 110);
			keyMap.put("COP", 111);
			keyMap.put("COST", 112);
			keyMap.put("CPB", 113);
			keyMap.put("CPWR", 114);
			keyMap.put("CRM", 115);
			keyMap.put("CSC", 116);
			keyMap.put("CSCO", 117);
			keyMap.put("CSX", 118);
			keyMap.put("CTAS", 119);
			keyMap.put("CTL", 120);
			keyMap.put("CTSH", 121);
			keyMap.put("CTXS", 122);
			keyMap.put("CVG", 123);
			keyMap.put("CVH", 124);
			keyMap.put("CVS", 125);
			keyMap.put("CVX", 126);
			keyMap.put("D", 127);
			keyMap.put("DD", 128);
			keyMap.put("DE", 129);
			keyMap.put("DELL", 130);
			keyMap.put("DF", 131);
			keyMap.put("DFS", 132);
			keyMap.put("DGX", 133);
			keyMap.put("DHI", 134);
			keyMap.put("DHR", 135);
			keyMap.put("DIS", 136);
			keyMap.put("DISCA", 137);
			keyMap.put("DNB", 138);
			keyMap.put("DNR", 139);
			keyMap.put("DO", 140);
			keyMap.put("DOV", 141);
			keyMap.put("DOW", 142);
			keyMap.put("DPS", 143);
			keyMap.put("DRI", 144);
			keyMap.put("DTE", 145);
			keyMap.put("DTV", 146);
			keyMap.put("DUK", 147);
			keyMap.put("DV", 148);
			keyMap.put("DVA", 149);
			keyMap.put("DVN", 150);
			keyMap.put("DYN", 151);
			keyMap.put("EBAY", 152);
			keyMap.put("ECL", 153);
			keyMap.put("ED", 154);
			keyMap.put("EFX", 155);
			keyMap.put("EIX", 156);
			keyMap.put("EK", 157);
			keyMap.put("EL", 158);
			keyMap.put("EMC", 159);
			keyMap.put("EMN", 160);
			keyMap.put("EMR", 161);
			keyMap.put("EOG", 162);
			keyMap.put("EP", 163);
			keyMap.put("EQR", 164);
			keyMap.put("EQT", 165);
			keyMap.put("ERTS", 166);
			keyMap.put("ESRX", 167);
			keyMap.put("ESV", 168);
			keyMap.put("ETFC", 169);
			keyMap.put("ETFCD", 170);
			keyMap.put("ETN", 171);
			keyMap.put("ETR", 172);
			keyMap.put("EXC", 173);
			keyMap.put("EXPD", 174);
			keyMap.put("EXPE", 175);
			keyMap.put("F", 176);
			keyMap.put("FAST", 177);
			keyMap.put("FCX", 178);
			keyMap.put("FDO", 179);
			keyMap.put("FDX", 180);
			keyMap.put("FE", 181);
			keyMap.put("FHN", 182);
			keyMap.put("FII", 183);
			keyMap.put("FIS", 184);
			keyMap.put("FISV", 185);
			keyMap.put("FITB", 186);
			keyMap.put("FLIR", 187);
			keyMap.put("FLR", 188);
			keyMap.put("FLS", 189);
			keyMap.put("FMC", 190);
			keyMap.put("FO", 191);
			keyMap.put("FPL", 192);
			keyMap.put("FRX", 193);
			keyMap.put("FSLR", 194);
			keyMap.put("FTI", 195);
			keyMap.put("FTR", 196);
			keyMap.put("GAS", 197);
			keyMap.put("GCI", 198);
			keyMap.put("GD", 199);
			keyMap.put("GE", 200);
			keyMap.put("GENZ", 201);
			keyMap.put("GILD", 202);
			keyMap.put("GIS", 203);
			keyMap.put("GLW", 204);
			keyMap.put("GME", 205);
			keyMap.put("GNW", 206);
			keyMap.put("GOOG", 207);
			keyMap.put("GPC", 208);
			keyMap.put("GPS", 209);
			keyMap.put("GR", 210);
			keyMap.put("GS", 211);
			keyMap.put("GT", 212);
			keyMap.put("GWW", 213);
			keyMap.put("HAL", 214);
			keyMap.put("HAR", 215);
			keyMap.put("HAS", 216);
			keyMap.put("HBAN", 217);
			keyMap.put("HCBK", 218);
			keyMap.put("HCN", 219);
			keyMap.put("HCP", 220);
			keyMap.put("HD", 221);
			keyMap.put("HES", 222);
			keyMap.put("HIG", 223);
			keyMap.put("HNZ", 224);
			keyMap.put("HOG", 225);
			keyMap.put("HON", 226);
			keyMap.put("HOT", 227);
			keyMap.put("HP", 228);
			keyMap.put("HPQ", 229);
			keyMap.put("HRB", 230);
			keyMap.put("HRL", 231);
			keyMap.put("HRS", 232);
			keyMap.put("HSP", 233);
			keyMap.put("HST", 234);
			keyMap.put("HSY", 235);
			keyMap.put("HUM", 236);
			keyMap.put("IBM", 237);
			keyMap.put("ICE", 238);
			keyMap.put("IFF", 239);
			keyMap.put("IGT", 240);
			keyMap.put("INTC", 241);
			keyMap.put("INTU", 242);
			keyMap.put("IP", 243);
			keyMap.put("IPG", 244);
			keyMap.put("IRM", 245);
			keyMap.put("ISRG", 246);
			keyMap.put("ITT", 247);
			keyMap.put("ITW", 248);
			keyMap.put("IVZ", 249);
			keyMap.put("JAVA", 250);
			keyMap.put("JBL", 251);
			keyMap.put("JCI", 252);
			keyMap.put("JCP", 253);
			keyMap.put("JDSU", 254);
			keyMap.put("JEC", 255);
			keyMap.put("JNJ", 256);
			keyMap.put("JNPR", 257);
			keyMap.put("JNS", 258);
			keyMap.put("JPM", 259);
			keyMap.put("JWN", 260);
			keyMap.put("K", 261);
			keyMap.put("KBH", 262);
			keyMap.put("KEY", 263);
			keyMap.put("KFT", 264);
			keyMap.put("KG", 265);
			keyMap.put("KIM", 266);
			keyMap.put("KLAC", 267);
			keyMap.put("KMB", 268);
			keyMap.put("KMX", 269);
			keyMap.put("KO", 270);
			keyMap.put("KR", 271);
			keyMap.put("KSS", 272);
			keyMap.put("L", 273);
			keyMap.put("LEG", 274);
			keyMap.put("LEN", 275);
			keyMap.put("LH", 276);
			keyMap.put("LIFE", 277);
			keyMap.put("LLL", 278);
			keyMap.put("LLTC", 279);
			keyMap.put("LLY", 280);
			keyMap.put("LM", 281);
			keyMap.put("LMT", 282);
			keyMap.put("LNC", 283);
			keyMap.put("LO", 284);
			keyMap.put("LOW", 285);
			keyMap.put("LSI", 286);
			keyMap.put("LTD", 287);
			keyMap.put("LUK", 288);
			keyMap.put("LUV", 289);
			keyMap.put("LXK", 290);
			keyMap.put("M", 291);
			keyMap.put("MA", 292);
			keyMap.put("MAR", 293);
			keyMap.put("MAS", 294);
			keyMap.put("MAT", 295);
			keyMap.put("MBI", 296);
			keyMap.put("MCD", 297);
			keyMap.put("MCHP", 298);
			keyMap.put("MCK", 299);
			keyMap.put("MCO", 300);
			keyMap.put("MDP", 301);
			keyMap.put("MDT", 302);
			keyMap.put("MEE", 303);
			keyMap.put("MET", 304);
			keyMap.put("MFE", 305);
			keyMap.put("MHP", 306);
			keyMap.put("MHS", 307);
			keyMap.put("MI", 308);
			keyMap.put("MIL", 309);
			keyMap.put("MJN", 310);
			keyMap.put("MKC", 311);
			keyMap.put("MMC", 312);
			keyMap.put("MMM", 313);
			keyMap.put("MO", 314);
			keyMap.put("MOLX", 315);
			keyMap.put("MON", 316);
			keyMap.put("MOT", 317);
			keyMap.put("MRK", 318);
			keyMap.put("MRO", 319);
			keyMap.put("MS", 320);
			keyMap.put("MSFT", 321);
			keyMap.put("MTB", 322);
			keyMap.put("MTW", 323);
			keyMap.put("MU", 324);
			keyMap.put("MUR", 325);
			keyMap.put("MWV", 326);
			keyMap.put("MWW", 327);
			keyMap.put("MYL", 328);
			keyMap.put("NBL", 329);
			keyMap.put("NBR", 330);
			keyMap.put("NDAQ", 331);
			keyMap.put("NEE", 332);
			keyMap.put("NEM", 333);
			keyMap.put("NI", 334);
			keyMap.put("NKE", 335);
			keyMap.put("NOC", 336);
			keyMap.put("NOV", 337);
			keyMap.put("NOVL", 338);
			keyMap.put("NRG", 339);
			keyMap.put("NSC", 340);
			keyMap.put("NSM", 341);
			keyMap.put("NTAP", 342);
			keyMap.put("NTRS", 343);
			keyMap.put("NU", 344);
			keyMap.put("NUE", 345);
			keyMap.put("NVDA", 346);
			keyMap.put("NVLS", 347);
			keyMap.put("NWL", 348);
			keyMap.put("NWSA", 349);
			keyMap.put("NYT", 350);
			keyMap.put("NYX", 351);
			keyMap.put("ODP", 352);
			keyMap.put("OI", 353);
			keyMap.put("OKE", 354);
			keyMap.put("OMC", 355);
			keyMap.put("ORCL", 356);
			keyMap.put("ORLY", 357);
			keyMap.put("OXY", 358);
			keyMap.put("PAYX", 359);
			keyMap.put("PBCT", 360);
			keyMap.put("PBG", 361);
			keyMap.put("PBI", 362);
			keyMap.put("PCAR", 363);
			keyMap.put("PCG", 364);
			keyMap.put("PCL", 365);
			keyMap.put("PCLN", 366);
			keyMap.put("PCP", 367);
			keyMap.put("PCS", 368);
			keyMap.put("PDCO", 369);
			keyMap.put("PEG", 370);
			keyMap.put("PEP", 371);
			keyMap.put("PFE", 372);
			keyMap.put("PFG", 373);
			keyMap.put("PG", 374);
			keyMap.put("PGN", 375);
			keyMap.put("PGR", 376);
			keyMap.put("PH", 377);
			keyMap.put("PHM", 378);
			keyMap.put("PKI", 379);
			keyMap.put("PLD", 380);
			keyMap.put("PLL", 381);
			keyMap.put("PM", 382);
			keyMap.put("PNC", 383);
			keyMap.put("PNW", 384);
			keyMap.put("POM", 385);
			keyMap.put("PPG", 386);
			keyMap.put("PPL", 387);
			keyMap.put("PRU", 388);
			keyMap.put("PSA", 389);
			keyMap.put("PTV", 390);
			keyMap.put("PWR", 391);
			keyMap.put("PX", 392);
			keyMap.put("PXD", 393);
			keyMap.put("Q", 394);
			keyMap.put("QCOM", 395);
			keyMap.put("QEP", 396);
			keyMap.put("QLGC", 397);
			keyMap.put("R", 398);
			keyMap.put("RAI", 399);
			keyMap.put("RDC", 400);
			keyMap.put("RF", 401);
			keyMap.put("RHI", 402);
			keyMap.put("RHT", 403);
			keyMap.put("RL", 404);
			keyMap.put("ROK", 405);
			keyMap.put("ROP", 406);
			keyMap.put("ROST", 407);
			keyMap.put("RRC", 408);
			keyMap.put("RRD", 409);
			keyMap.put("RSG", 410);
			keyMap.put("RSH", 411);
			keyMap.put("RTN", 412);
			keyMap.put("RX", 413);
			keyMap.put("S", 414);
			keyMap.put("SAI", 415);
			keyMap.put("SBUX", 416);
			keyMap.put("SCG", 417);
			keyMap.put("SCHW", 418);
			keyMap.put("SE", 419);
			keyMap.put("SEE", 420);
			keyMap.put("SGP", 421);
			keyMap.put("SHLD", 422);
			keyMap.put("SHW", 423);
			keyMap.put("SIAL", 424);
			keyMap.put("SII", 425);
			keyMap.put("SJM", 426);
			keyMap.put("SLB", 427);
			keyMap.put("SLE", 428);
			keyMap.put("SLM", 429);
			keyMap.put("SNA", 430);
			keyMap.put("SNDK", 431);
			keyMap.put("SNI", 432);
			keyMap.put("SO", 433);
			keyMap.put("SPG", 434);
			keyMap.put("SPLS", 435);
			keyMap.put("SRCL", 436);
			keyMap.put("SRE", 437);
			keyMap.put("STI", 438);
			keyMap.put("STJ", 439);
			keyMap.put("STR", 440);
			keyMap.put("STT", 441);
			keyMap.put("STZ", 442);
			keyMap.put("SUN", 443);
			keyMap.put("SVU", 444);
			keyMap.put("SWK", 445);
			keyMap.put("SWN", 446);
			keyMap.put("SWY", 447);
			keyMap.put("SYK", 448);
			keyMap.put("SYMC", 449);
			keyMap.put("SYY", 450);
			keyMap.put("T", 451);
			keyMap.put("TAP", 452);
			keyMap.put("TDC", 453);
			keyMap.put("TE", 454);
			keyMap.put("TEG", 455);
			keyMap.put("TER", 456);
			keyMap.put("TGT", 457);
			keyMap.put("THC", 458);
			keyMap.put("TIE", 459);
			keyMap.put("TIF", 460);
			keyMap.put("TJX", 461);
			keyMap.put("TLAB", 462);
			keyMap.put("TMK", 463);
			keyMap.put("TMO", 464);
			keyMap.put("TROW", 465);
			keyMap.put("TRV", 466);
			keyMap.put("TSN", 467);
			keyMap.put("TSO", 468);
			keyMap.put("TSS", 469);
			keyMap.put("TWC", 470);
			keyMap.put("TWX", 471);
			keyMap.put("TXN", 472);
			keyMap.put("TXT", 473);
			keyMap.put("UNH", 474);
			keyMap.put("UNM", 475);
			keyMap.put("UNP", 476);
			keyMap.put("UPS", 477);
			keyMap.put("URBN", 478);
			keyMap.put("USB", 479);
			keyMap.put("UTX", 480);
			keyMap.put("V", 481);
			keyMap.put("VAR", 482);
			keyMap.put("VFC", 483);
			keyMap.put("VIA", 484);
			keyMap.put("VIA.B", 485);
			keyMap.put("VLO", 486);
			keyMap.put("VMC", 487);
			keyMap.put("VNO", 488);
			keyMap.put("VRSN", 489);
			keyMap.put("VTR", 490);
			keyMap.put("VZ", 491);
			keyMap.put("WAG", 492);
			keyMap.put("WAT", 493);
			keyMap.put("WDC", 494);
			keyMap.put("WEC", 495);
			keyMap.put("WFC", 496);
			keyMap.put("WFMI", 497);
			keyMap.put("WFR", 498);
			keyMap.put("WHR", 499);
			keyMap.put("WIN", 500);
			keyMap.put("WLP", 501);
			keyMap.put("WM", 502);
			keyMap.put("WMB", 503);
			keyMap.put("WMT", 504);
			keyMap.put("WPI", 505);
			keyMap.put("WPO", 506);
			keyMap.put("WU", 507);
			keyMap.put("WY", 508);
			keyMap.put("WYE", 509);
			keyMap.put("WYN", 510);
			keyMap.put("WYNN", 511);
			keyMap.put("X", 512);
			keyMap.put("XEL", 513);
			keyMap.put("XL", 514);
			keyMap.put("XLNX", 515);
			keyMap.put("XOM", 516);
			keyMap.put("XRAY", 517);
			keyMap.put("XRX", 518);
			keyMap.put("XTO", 519);
			keyMap.put("YHOO", 520);
			keyMap.put("YUM", 521);
			keyMap.put("ZION", 522);
			keyMap.put("ZMH", 523);
			System.out.println(keyMap.size());
			return keyMap;
		}
		return keyMap;
	}
}
