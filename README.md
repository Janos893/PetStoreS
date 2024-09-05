PetStoreS

Az 1. feladat a petDetailTest osztályban található
Észrevett hibák:
Negatív tesztet érvénytelen paraméterrel készítettem el, viszont az API HTTP 200 code-dal válaszol, amit szerintem 400 Bad Request-tel kellene inkább.
Megvizsgáltam a kötelező mezőket is. Az API szerint a name és a photoUrls mezők kötelezőek, de ezek hiányában is HTTP 200 érkezik.
Éppen ezért az 1.c feladatot (kötelező mezők vizsgálata), mindegyik mezőre elkészítettem, egyébként is érdekes volt a feladat, így gondoltam gyakorlok egy kicsit.
A petDetailTest osztály Test1 metódusa olykor 404-es hibát dob, szerintem itt az OpenApi-val lehet gond.
Az 1.b.ii feladat miatt (500 milisecond alatt volt a válaszidő) folyamatosan hibát fog jelezni a teszt, rendszerint 1500 ms a válaszidő. Szerintem ennek problémának a kiküszöbölése egy Pageable jó megoldás lenne, hogy adott esetben ne az összes adat érkezzen meg, hanem csak 10 például. 

A 2. feladatot a PetLifeCycle osztályban hoztam létre. Mivel folyamat tesztet kellett csinálni és például a delete függ a create-től, ezért ki kellett alakítanom egy minimális dependenciát.
Érdekesség: Nem biztos, hogy fontos, de a HYDRAIMAGE final static String 404-et dob.

A 3. feladat nálam üres volt. Nem jelent meg a pdf-ben a feladat szövege.

A 4. feladat a Listener-ben van.

Külön programkönyvtárat nem húztam be.

2 nagyon fontos dolog:
Kérem az /src/test/resource/log4j.xml konfigurációt nézzék át, mert egy rosszakarójuk trágár szöveget írt a PatternLayout tag pattern attribútumába. Szerintem jobb ha tudnak róla. Magamnál ezt töröltem.

Az OpenApi /pet/findByStatus végponttal nagyon vigyázzanak, mert (szerintem) orosz hackerek lapátolják be az adatokat és ennek a végpontnak a lekérésekor vírussal teli url címek lehetnek megadva.
Olyan nagy gond nincs ezzel, viszont céges gépeken inkább ne nyissák meg ezeket a linkeket.
