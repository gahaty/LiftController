/*
-- LEFELÉ GYŰJTŐ FELVONÓVEZÉRLŐ LOGIKA --

Ez a kisebb program egy 10 emeletes ház felvonó vezérlését szemlélteti.
A logika bemutatja, hogy milyen sorrendben kerülnek kiszolgálásra a liftből
és/vagy a hívótablóról az egy időben kapott parancsok. A személyek által
megadott jelzés egy gyűjtőtárolóba (SET) kerül, majd az irányvizsgálatot
követően további SET-ekbe szelektálódik. A továbbiakban a felvonó haladási
iránya fogja meghatározni a külső illetve belső kérések kiszolgálási sorrendjét.

Tetszőleges mennyiségű hívás és küldés kezdeményezhető.
A helyes működés szerint: 0 - 10 közötti számokat adjunk meg !

pl.:

liftAllas = 3; // Lift itt tartózkodik
hivo(2);    // 2. emeleten történik hívás
kuld(7);    // 7. liftben a 7-es gombot nyomták meg
*/


package felvonoController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class felvonoController {

	static ArrayList<Integer> liftHivo = new ArrayList<>();
	static Set<Integer> kuldL = new LinkedHashSet<>();
	static TreeSet<Integer> kuldT = new TreeSet<>();
	static TreeSet<Integer> kuldGyujt = new TreeSet<>();
	static Set<Integer> hivL = new LinkedHashSet<>();
	static TreeSet<Integer> hivT = new TreeSet<>();
	static TreeSet<Integer> hivGyujt = new TreeSet<>();
	static TreeSet<Integer> hivCsokk = (TreeSet<Integer>) hivT.descendingSet();
	static TreeSet<Integer> kiszolgal = new TreeSet<>();
	static Iterator<Integer> itrKuld = kuldL.iterator();
	static Iterator<Integer> itrHiv = hivL.iterator();

	static Integer iKuldTutolso = -1;
	static Integer iHivTutolso = -1;

	static Integer kiszolgalH = -1;
	static Integer kiszolgalK = -1;

	static Integer minSzint = null;
	static Integer maxSzint = null;

	static boolean utolsoSzint = false;
	static boolean elsoSzint = false;

	static boolean menetFel = false;
	static boolean menetLe = false;
	static boolean varakozik = false;

	static boolean gyujtBeszamolVezLe = true;
	static boolean gyujtBeszamolVezFel = true;

	static boolean beszamolHivo = false;
	static boolean beszamolLift = false;

	static int liftAllas = 0;

	public static void info() {

		System.out.println("LIFT: " + liftAllas);
		System.out.println("KÜLD(L) " + kuldL);
		System.out.println("KÜLD(T) " + kuldT);
		// System.out.println("KÜLD(GY " + kuldGyujt);
		System.out.println("HÍVÓ(L) " + hivL);
		// System.out.println("HÍVÓ(GY)" + hivGyujt);
		// System.out.println("HÍVÓ(T) " + hivT);
		System.out.println("HÍVÓ(TR) " + hivCsokk);
		System.out.println("KISZOLGAL: " + kiszolgal);
		System.out.println();
	}

	public static void vezerlesLe() {

		// Lefelé küldT beválogat
		if (kuldL.contains(-1)) {
			kuldL.clear();
		}
		if (hivL.contains(-1)) {
			hivL.clear();
		}

		if (gyujtBeszamolVezLe == true) {

			if (!kuldL.isEmpty()) {
				itrKuld = kuldL.iterator();
				while (itrKuld.hasNext()) {
					iKuldTutolso = itrKuld.next();
					if (iKuldTutolso < liftAllas) {
						hivT.add(iKuldTutolso);
						itrKuld.remove();
					}
				}
			}
			// Lefelé hivT beválogat

			if (!hivL.isEmpty()) {
				itrHiv = hivL.iterator();
				while (itrHiv.hasNext()) {
					iHivTutolso = itrHiv.next();
					if (iHivTutolso < liftAllas) {
						hivT.add(iHivTutolso);
						itrHiv.remove();
					}
				}
				if (!kuldL.isEmpty()) {

					itrKuld = kuldL.iterator();
					while (itrKuld.hasNext()) {
						iKuldTutolso = itrKuld.next();
						kuldGyujt.add(iKuldTutolso);
					}
					if (!hivL.isEmpty() && !kuldL.isEmpty()) {

						itrHiv = hivL.iterator();
						while (itrHiv.hasNext()) {
							iHivTutolso = itrHiv.next();
							hivGyujt.add(iHivTutolso);
						}
						if (hivGyujt.last() >= kuldGyujt.last()) {
							kuldL.add(hivGyujt.last());
							hivL.remove(hivGyujt.last());
							hivGyujt.clear();
							kuldGyujt.clear();
						}
					} else {
						kuldGyujt.clear();
					}
				}
				if (!hivL.isEmpty() && kuldL.isEmpty()) {
					itrHiv = hivL.iterator();
					while (itrHiv.hasNext()) {
						iHivTutolso = itrHiv.next();
						hivGyujt.add(iHivTutolso);
					}
					kuldL.add(hivGyujt.last());
					hivL.remove(hivGyujt.last());
					hivGyujt.clear();
				}
			}
			gyujtBeszamolVezLe = false;
		}

		kiszolgalH = hivCsokk.first();
		minSzint = hivCsokk.last();

		if (kiszolgal.isEmpty()) {
			kiszolgal.add(kiszolgalH);
		}
	}

	public static void vezerlesFel() {
		// iK = liftAllas;
		if (kuldL.contains(-1)) {
			kuldL.clear();
		}
		if (hivL.contains(-1)) {
			hivL.clear();
		}

		if (gyujtBeszamolVezFel == true) {

			if (!kuldL.isEmpty()) {

				itrKuld = kuldL.iterator();
				while (itrKuld.hasNext()) {
					iKuldTutolso = itrKuld.next();
					if (iKuldTutolso > liftAllas) {
						kuldT.add(iKuldTutolso);
						itrKuld.remove();
					}
				}
			}

			if (!hivL.isEmpty()) {

				if (!kuldT.isEmpty()) {
					itrHiv = hivL.iterator();
					while (itrHiv.hasNext()) {
						iHivTutolso = itrHiv.next();
						hivGyujt.add(iHivTutolso);
					}
					if (hivGyujt.last() >= kuldT.last()) {
						kuldT.add(hivGyujt.last());
						hivL.remove(hivGyujt.last());
						hivGyujt.clear();
					} else {
						hivGyujt.clear();
					}
				}
				if (kuldT.isEmpty()) {
					itrHiv = hivL.iterator();
					while (itrHiv.hasNext()) {
						iHivTutolso = itrHiv.next();
						hivGyujt.add(iHivTutolso);
					}
					kuldT.add(hivGyujt.last());
					hivL.remove(hivGyujt.last());
					hivGyujt.clear();
				}
			}
			gyujtBeszamolVezFel = false;
		}

		kiszolgalK = kuldT.first();
		maxSzint = kuldT.last();

		if (kiszolgal.isEmpty()) {
			kiszolgal.add(kiszolgalK);
		}
	}

	public static void hivo(Integer szam) {

		if (szam != liftAllas || beszamolLift == true) {
			beszamolHivo = true;
		}

		if (liftAllas == szam && beszamolHivo == false) {
		} else if (liftAllas == szam && beszamolHivo == true) {
			hivL.add(szam);
			liftHivo.add(szam);
		} else {
			hivL.add(szam);
			liftHivo.add(szam);
		}
	}

	public static void lift(Integer szam) {

		if (szam != liftAllas || beszamolHivo == true) {
			beszamolLift = true;
		}

		if (liftAllas == szam && beszamolLift == false) {
		} else if (liftAllas == szam && beszamolLift == true) {
			kuldL.add(szam);
			liftHivo.add(szam);
		} else {
			kuldL.add(szam);
			liftHivo.add(szam);
		}
	}

	public static void menetIrányFel() throws InterruptedException {
		// küld vizsgálata
		vezerlesFel();
                utolsoSzint = liftAllas == maxSzint - 1;
		info();
		Thread.sleep(3000);
		while (menetFel != false) {
			if (utolsoSzint == false) {
				vezerlesFel();

				// felmegyünk a következő szintre
				liftAllas++;

				if (Objects.equals(liftAllas, kiszolgal.first())) {
					kuldT.remove(kiszolgalK);
					kiszolgal.remove(kiszolgal.pollFirst());
					kiszolgal.add(kuldT.first());
					System.out.println("VÁRAKOZIK");
					info();

					Thread.sleep(5000);
				} else {
					info();
					Thread.sleep(3000);
				}

				if (liftAllas == maxSzint - 1) {
					utolsoSzint = true;
				}

			} else {

				liftAllas++;

				if (!kuldL.isEmpty() || !hivL.isEmpty()) {
					kuldT.remove(kuldT.first());
					kiszolgal.remove(kiszolgal.pollFirst());

					itrHiv = hivGyujt.iterator();
					while (itrHiv.hasNext()) {
						iHivTutolso = itrHiv.next();
						hivT.add(iHivTutolso);
						itrHiv.remove();
					}

					menetLe = true;
					menetFel = false;
					System.out.println("VÁRAKOZIK");
					System.out.println("MENETIRÁNY: LE");
					gyujtBeszamolVezLe = true;
				} else {
					kuldT.remove(kuldT.first());
					kiszolgal.remove(kiszolgal.pollFirst());
					menetFel = false;
					System.out.println("MEGÁLL");
					info();
				}
			}
		}
	}

	public static void menetIrányLe() throws InterruptedException {

		vezerlesLe();
		info();
                elsoSzint = liftAllas == minSzint + 1;
		Thread.sleep(3000);
		while (menetLe != false) {
			if (elsoSzint == false) {
				vezerlesLe();
				// lemegyünk a következő szintre
				liftAllas--;

				if (Objects.equals(liftAllas, kiszolgal.first())) {
					hivCsokk.remove(hivCsokk.first());
					kiszolgal.remove(kiszolgal.pollFirst());
					kiszolgal.add(hivCsokk.first());
					System.out.println("VÁRAKOZIK");
					info();
					Thread.sleep(5000);

				} else {
					info();
					Thread.sleep(3000);
				}
				if (liftAllas == minSzint + 1) {
					elsoSzint = true;
				}
			} else { // utolsó szint
				liftAllas--;

				if (!kuldL.isEmpty() || !hivL.isEmpty()) {
					hivT.remove(hivT.first());
					kiszolgal.remove(kiszolgal.pollFirst());

					itrKuld = kuldGyujt.iterator();
					while (itrKuld.hasNext()) {
						iKuldTutolso = itrKuld.next();
						kuldT.add(iKuldTutolso);
						itrKuld.remove();
					}

					menetLe = false;
					menetFel = true;
					System.out.println("VÁRAKOZIK");
					System.out.println("MENETIRÁNY: FEL");
					gyujtBeszamolVezFel = true;

				} else {
					hivT.remove(hivT.first());
					kiszolgal.remove(kiszolgal.pollFirst());
					menetLe = false;
					System.out.println("MEGÁLL");
					info();
				}
			}
		}
	}

	public static void tarolasVizsgalat() {

		itrHiv = hivL.iterator();

		while (itrHiv.hasNext()) {
			iHivTutolso = itrHiv.next();
			hivL.add(iHivTutolso);
		}

		itrKuld = kuldL.iterator();

		while (itrKuld.hasNext()) {
			iKuldTutolso = itrKuld.next();
			kuldL.add(iKuldTutolso);
		}
		// --------------------------------------------

		// Lefelé küldT
		itrKuld = kuldL.iterator();

		while (itrKuld.hasNext()) {
			iKuldTutolso = itrKuld.next();
			if (iKuldTutolso < liftAllas) {
				hivT.add(iKuldTutolso);
				itrKuld.remove();
			}
		}
		// Lefelé hivT

		itrHiv = hivL.iterator();

		while (itrHiv.hasNext()) {
			iHivTutolso = itrHiv.next();
			if (iHivTutolso < liftAllas) {
				hivT.add(iHivTutolso);
				itrHiv.remove();
			}
		}
	}

	public static void indulj() throws InterruptedException {
//        info();
//        Thread.sleep(3000);
		int liftHivoInt = -1;

		// tarolasVizsgalat();
		if (!liftHivo.isEmpty()) {
			int i;
			for (i = 0; i < liftHivo.size(); i++) {
				// System.out.println(liftHivo.get(i));
				if (!Objects.equals(liftHivo.get(i), liftAllas)) {
					liftHivoInt = liftHivo.get(i);
					break;
				}
			}
		}
		if (liftHivo.isEmpty() || liftHivoInt == liftAllas || liftHivoInt == -1) {
			if (!kuldL.isEmpty()) {
				kuldL.clear();
			}
			if (!hivL.isEmpty()) {
				hivL.clear();
			}

			menetFel = false;
			menetLe = false;
			System.out.println("STOP");
		} else if (liftHivoInt > liftAllas) {
			menetFel = true;
			System.out.println("MENETIRÁNY: FEL");
		} else if (liftHivoInt < liftAllas) {
			menetLe = true;
			System.out.println("MENETIRÁNY: LE");
		}

		while (menetFel != false || menetLe != false) {
			if (menetFel == true) {

				menetIrányFel();
			} else if (menetLe == true) {

				menetIrányLe();
			} else {
				menetFel = false;
				menetLe = false;
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {

            
            
		liftAllas = 3;
		
                hivo(2);
                hivo(7);
                lift(10);
		lift(5);
                hivo(4);
                lift(8);
                hivo(0);             

		indulj();
		System.out.println();
	}
}
